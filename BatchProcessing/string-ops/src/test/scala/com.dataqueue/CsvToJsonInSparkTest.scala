package com.dataqueue

import com.holdenkarau.spark.testing.DatasetSuiteBase
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.log4j.{Level, Logger}
import org.scalatest.FunSuite

class CsvToJsonInSparkTest extends FunSuite with DatasetSuiteBase {
  val logger: Logger = Logger.getLogger(getClass)
  var config: Config = ConfigFactory.load()
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  test("Test a Spark/Scala function to receive string and return it as Json string.") {
    import spark.implicits._

    val expected = spark.sparkContext.parallelize(
      """{"Company":"Ganit","CEO":"Shiva","Greetings":"Welcome to the team"}""" ::
        """{"Company":"Google","CEO":"Sundar","Greetings":"Welcome to the search"}""" :: Nil).toDS()
    println("### Expected Output: ###")
    expected.show(false)
    val actual = CsvToJsonInSpark.convertCsvToJsonString(config.getString("csvFilePath"))
    println("### Actual Output: ###")
    actual.show(false)
    assertDatasetEquals(expected, actual) // equal
  }
}
