package com.dataqueue

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.log4j.Logger
import org.scalatest.FunSuite

class CsvToJsonInScalaTest extends FunSuite {
  val logger: Logger = Logger.getLogger(getClass)
  var config: Config = ConfigFactory.load()

  test("Test a scala function to receive string and return it as Json string.") {
    val expected =
      """[ {
        |  "Company" : "Ganit",
        |  "CEO" : "Shiva",
        |  "Greetings" : "Welcome to the team"
        |}, {
        |  "Company" : "Google",
        |  "CEO" : "Sundar",
        |  "Greetings" : "Welcome to the search"
        |} ]""".stripMargin
    println("### Expected Output: ###")
    println(expected)
    val actual = CsvToJsonInScala.csvFileToJson(config.getString("csvFilePath"))
    println("### Actual Output: ###")
    println(actual)
    assert(expected === actual)


  }
}
