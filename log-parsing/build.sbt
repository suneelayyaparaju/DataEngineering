name := "log-parsing"
version := "0.1"

scalaVersion := "2.11.12"
val sparkVersion = "2.4.3"
val typeSafeVersion = "1.3.3"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "com.typesafe" % "config" % typeSafeVersion % "provided"
)
//--------------------------------
//---- sbt-assembly settings -----
//--------------------------------

assemblyJarName := "log-parsing.jar"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

