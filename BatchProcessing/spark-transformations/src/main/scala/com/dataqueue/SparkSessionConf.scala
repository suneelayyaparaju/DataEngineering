package com.dataqueue

import com.typesafe.config.Config
import org.apache.spark.sql.SparkSession

/**
  * Singleton class to create single spark session for the application.
  *
  * @author Suneel Ayyaparaju
  */
object SparkSessionConf {
  var spark: SparkSession = _

  /**
    * creates spark session depending on the
    *
    * @param config configuration obtained from properties file.
    *
    */
  def createSparkSession(config: Config): Unit = {
    if (spark == null) {
      spark = SparkSession.
        builder()
        .appName(config.getString("appName"))
        .master(config.getString("master"))
        .getOrCreate()
      spark.sparkContext.setLogLevel(config.getString("logLevel"))
    }
  }

  /**
    * @return spark session
    */
  def getSparkSession: SparkSession = spark

}