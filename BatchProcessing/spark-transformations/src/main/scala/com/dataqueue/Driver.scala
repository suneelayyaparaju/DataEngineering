package com.dataqueue

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.log4j.Logger
import org.apache.log4j.Level

/**
  * Driver/Main entry point for the application.
  *
  * @author Suneel Ayyaparaju
  */
object Driver {
  val logger: Logger = Logger.getLogger(Driver.getClass)
  var config: Config = ConfigFactory.load()
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)
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
