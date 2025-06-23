package com.example.plugin

import org.apache.spark.SparkContext
import org.apache.spark.api.plugin.{DriverPlugin, ExecutorPlugin, PluginContext, SparkPlugin}

/** Example Spark plugin demonstrating driver and executor lifecycle hooks. */
class ExampleSparkPlugin extends SparkPlugin {
  override def driverPlugin(): DriverPlugin = new ExampleDriverPlugin
  override def executorPlugin(): ExecutorPlugin = new ExampleExecutorPlugin
}

class ExampleDriverPlugin extends DriverPlugin {
  override def init(sc: SparkContext, ctx: PluginContext): java.util.Map[String, String] = {
    // initialization logic on the driver
    println("ExampleDriverPlugin initialized")
    java.util.Collections.emptyMap[String, String]
  }

  override def shutdown(): Unit = {
    // cleanup logic on the driver
    println("ExampleDriverPlugin shutdown")
  }
}

class ExampleExecutorPlugin extends ExecutorPlugin {
  override def init(ctx: PluginContext, extraConf: java.util.Map[String, String]): Unit = {
    // initialization logic on executors
    println("ExampleExecutorPlugin initialized")
  }

  override def shutdown(): Unit = {
    // cleanup logic on executors
    println("ExampleExecutorPlugin shutdown")
  }
}
