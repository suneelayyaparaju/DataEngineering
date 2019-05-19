package com.dataqueue

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.log4j.Logger

/**
  * Driver/Main entry point for the application.
  *
  * @author Suneel Ayyaparaju
  */
object Driver {
  val logger: Logger = Logger.getLogger(Driver.getClass)
  val config: Config = ConfigFactory.load()
  def main(args: Array[String]) {
    try {
      ParserConf.createSparkSession(config)
      LogParser.parseLogs()

    } catch {
      case ex: Exception => {
        throw new Exception(ex)
      }
    }
  }
}
