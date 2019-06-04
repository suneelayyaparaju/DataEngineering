name := "spark-transformations"
version := "0.1"

scalaVersion := "2.11.12"
val sparkVersion = "2.4.3"
val typeSafeVersion = "1.3.4"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "com.typesafe" % "config" % typeSafeVersion

)
//--------------------------------
//---- sbt-assembly settings -----
//--------------------------------

assemblyJarName := "spark-transformations.jar"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

