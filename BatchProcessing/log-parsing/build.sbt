name := "log-parsing"
version := "0.1"

scalaVersion := "2.11.8"
val sparkVersion = "2.3.0"
val log4jVersion = "1.2.17"
val typeSafeVersion = "1.3.3"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "log4j" % "log4j" % log4jVersion % "provided",
  "com.typesafe" % "config" % typeSafeVersion

)
//--------------------------------
//---- sbt-assembly settings -----
//--------------------------------

assemblyJarName := "log-parsing.jar"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

