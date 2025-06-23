package com.example.plugin

import com.holdenkarau.spark.testing.SharedSparkContext
import org.apache.spark.SparkConf
import org.scalatest.funsuite.AnyFunSuite

/** Unit tests for ExampleSparkPlugin using spark-testing-base */
class ExampleSparkPluginSuite extends AnyFunSuite with SharedSparkContext {
  override def conf: SparkConf = {
    super.conf
      .setAppName("plugin-test")
      .set("spark.plugins", "com.example.plugin.ExampleSparkPlugin")
  }

  test("driverPlugin and executorPlugin return expected instances") {
    val plugin = new ExampleSparkPlugin
    assert(plugin.driverPlugin().isInstanceOf[ExampleDriverPlugin])
    assert(plugin.executorPlugin().isInstanceOf[ExampleExecutorPlugin])
  }

  test("SparkContext starts with ExampleSparkPlugin") {
    assert(sc.isStopped === false)
  }
}
