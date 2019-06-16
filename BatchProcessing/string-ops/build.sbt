name := "string-ops"
version := "0.1"

scalaVersion := "2.12.8"
val sparkVersion = "2.4.3"
val typeSafeVersion = "1.3.4"
val sparkTestingBase = "2.4.3_0.12.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "com.typesafe" % "config" % typeSafeVersion,
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "com.holdenkarau" %% "spark-testing-base" % sparkTestingBase % Test,
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.9",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-csv" % "2.9.9"
)

dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.7"

parallelExecution in Test := false

//--------------------------------
//---- sbt-assembly settings -----
//--------------------------------

assemblyJarName := "string-ops.jar"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}