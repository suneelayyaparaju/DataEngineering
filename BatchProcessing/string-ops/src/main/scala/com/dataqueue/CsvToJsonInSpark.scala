package com.dataqueue

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Dataset, SparkSession}
/**
  * class to convert Csv string to Json string using Spark with scala.
  *
  * @author Suneel Ayyaparaju
  */
object CsvToJsonInSpark {
  val logger: Logger = Logger.getLogger(getClass)
  var config: Config = ConfigFactory.load()
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)
  AppConf.createSparkSession(config)
  val spark: SparkSession = AppConf.getSparkSession

  def main(args: Array[String]) {
    try {
      val employeeJsonDS = convertCsvToJsonString(config.getString("csvFilePath"))
      employeeJsonDS.show(false)
      //close SparkSession
      spark.close()
    } catch {
      case ex: Exception => {
        logger.error(ex.getMessage)
        throw new Exception(ex)
      }
    }
  }
  def convertCsvToJsonString(path: String): Dataset[String] ={
    val employeeDS = spark.read.option("header", true).csv(config.getString("csvFilePath"))
    employeeDS.toJSON
  }
}
