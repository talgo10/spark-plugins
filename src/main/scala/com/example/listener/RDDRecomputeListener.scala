package com.example.listener

import org.apache.spark.scheduler.{SparkListener, SparkListenerStageCompleted, SparkListenerStageSubmitted, StageInfo}
import org.apache.spark.storage.StorageLevel

import scala.collection.mutable

/**
 * Spark listener that tracks RDD usage across stages/jobs and warns when an RDD
 * is computed multiple times without being persisted.
 */
class RDDRecomputeListener extends SparkListener {

  private val rddUsage = mutable.Map[Int, Int]()
  private val persisted = mutable.Set[Int]()

  override def onStageSubmitted(stageSubmitted: SparkListenerStageSubmitted): Unit = {
    updateStageRDDs(stageSubmitted.stageInfo)
  }

  override def onStageCompleted(stageCompleted: SparkListenerStageCompleted): Unit = {
    updateStageRDDs(stageCompleted.stageInfo)
  }

  private def updateStageRDDs(stageInfo: StageInfo): Unit = {
    for (info <- stageInfo.rddInfos) {
      val id = info.id
      val count = rddUsage.getOrElse(id, 0) + 1
      rddUsage.update(id, count)
      if (info.storageLevel != StorageLevel.NONE) {
        persisted.add(id)
      }
      if (count > 1 && !persisted.contains(id)) {
        // Emit a warning that this RDD has been recomputed.
        println(s"Warning: RDD '${info.name}' (ID: $id) has been recomputed $count times without persistence")
      }
    }
  }

  /**
   * Returns a map of RDD id to the number of times it was used across stages.
   */
  def usageMetrics: Map[Int, Int] = rddUsage.toMap

  /**
   * Returns RDD ids that were recomputed at least twice without persistence and
   * the number of times they were seen.
   */
  def recomputedRDDs: Map[Int, Int] = rddUsage.filter {
    case (id, count) => count > 1 && !persisted.contains(id)
  }.toMap
}
