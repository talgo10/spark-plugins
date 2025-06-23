name := "spark-plugins"

version := "0.1.0"

scalaVersion := "2.12.18"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.5.0" % "provided",
  "org.apache.spark" %% "spark-sql" % "3.5.0" % "provided",
  "org.scalatest" %% "scalatest" % "3.2.18" % Test,
  "com.holdenkarau" %% "spark-testing-base" % "3.5.0_1.5.3" % Test
)

Test / fork := true
Test / javaOptions ++= Seq(
  "--add-exports=java.base/sun.nio.ch=ALL-UNNAMED",
  "--add-opens=java.base/java.io=ALL-UNNAMED",
  "--add-opens=java.base/java.lang=ALL-UNNAMED",
  "--add-opens=java.base/java.nio=ALL-UNNAMED",
  "--add-opens=java.base/java.util=ALL-UNNAMED"
)
Test / classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.ScalaLibrary
