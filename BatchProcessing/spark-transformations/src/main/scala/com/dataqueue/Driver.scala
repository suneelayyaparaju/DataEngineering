package com.dataqueue

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.log4j.Logger

/**
  * Driver/Main entry point for the application.
  *
  * @author Suneel Ayyaparaju
  */
object Driver {
  val logger: Logger = Logger.getLogger(Driver.getClass)
  var config: Config = ConfigFactory.load()
  def main(args: Array[String]) {
    try {
      SparkSessionConf.createSparkSession(config)
      Solution.writeMRFunctions(config)

    } catch {
      case ex: Exception => {
        throw new Exception(ex)
      }
    }
  }
}
