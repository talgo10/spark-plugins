package com.example.listener

import com.holdenkarau.spark.testing.SharedSparkContext
import org.scalatest.funsuite.AnyFunSuite

class RDDRecomputeListenerSuite extends AnyFunSuite with SharedSparkContext {

  test("detect recomputation for non-persisted RDD") {
    val listener = new RDDRecomputeListener
    sc.addSparkListener(listener)

    val rdd = sc.parallelize(1 to 10).map(_ + 1)
    rdd.count()
    rdd.collect()

    val recomputed = listener.recomputedRDDs
    assert(recomputed.contains(rdd.id))
    assert(recomputed(rdd.id) >= 2)
  }

  test("no recomputation warning for persisted RDD") {
    val listener = new RDDRecomputeListener
    sc.addSparkListener(listener)

    val rdd = sc.parallelize(1 to 10).map(_ + 1).persist()
    rdd.count()
    rdd.collect()

    assert(!listener.recomputedRDDs.contains(rdd.id))
  }

  test("usageMetrics counts recomputations") {
    val listener = new RDDRecomputeListener
    sc.addSparkListener(listener)

    val rdd = sc.parallelize(1 to 5).map(_ * 2)
    rdd.count()
    rdd.collect()
    rdd.reduce(_ + _)

    val metrics = listener.usageMetrics
    assert(metrics.contains(rdd.id))
    assert(metrics(rdd.id) >= 3)
    assert(listener.recomputedRDDs(rdd.id) >= 3)
  }

  test("detect recomputation through DataFrame API") {
    val listener = new RDDRecomputeListener
    sc.addSparkListener(listener)

    val spark = org.apache.spark.sql.SparkSession.builder()
      .config(sc.getConf)
      .getOrCreate()
    import spark.implicits._

    val baseRdd = sc.parallelize(1 to 4)
    val df = baseRdd.toDF("value")
    df.count()
    df.collect()

    assert(listener.recomputedRDDs.contains(baseRdd.id))
  }

  test("detect recomputation for range DataFrame without persist") {
    val listener = new RDDRecomputeListener
    sc.addSparkListener(listener)

    val spark = org.apache.spark.sql.SparkSession.builder()
      .config(sc.getConf)
      .getOrCreate()

    val df = spark.range(0, 5)
    df.count()
    df.collect()

    assert(listener.recomputedRDDs.nonEmpty)
  }
}
