# Spark Plugins

This repository contains an example Spark plugin written in Scala and built with sbt.

The plugin implements the `SparkPlugin` interface to demonstrate driver and executor lifecycle hooks.

## Testing

Run the unit tests with:

```bash
sbt test
```

## Building

Run the following command to build the plugin JAR:

```bash
sbt package
```

The resulting artifact will be found under `target/scala-2.12/`.

## Usage

Include the plugin JAR on Spark's classpath and specify the plugin via `spark.plugins`:

```bash
spark-submit \
  --conf "spark.plugins=com.example.plugin.ExampleSparkPlugin" \
  --class your.MainClass your-app.jar
```

When enabled, the plugin prints simple log messages during initialization and shutdown on both the driver and executors.
