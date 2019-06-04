package com.dataqueue

import com.typesafe.config.Config
import org.apache.spark.sql.functions._

/**
  * This class holds business logic for MapReduce functions.
  *
  * @author Suneel Ayyaparaju
  */
object Solution {
  val LEFT_OUTER = "left_outer"

  def writeMRFunctions(config: Config): Unit = {
    val spark = SparkSessionConf.getSparkSession

    val jsonData = spark.read.json(config.getString("jsonFilesPath"))

    // *** 1. Find the average time per session(sessionid is the defining field for every session) when "appnameenc"=1 and "appnameenc"=2 ***
    val averageTimePerSession = jsonData.filter("appnameenc==1 OR appnameenc==2" ).groupBy("sessionid").avg("timestamp")
    averageTimePerSession.show(100,false)

    // *** 2. Count of "calc_userid" for each "region". ignore "-" and nulls ***
    val calcUserIdCountPerRegion = jsonData.where(col("region").isNotNull.and(col("region").notEqual("-"))).groupBy("region").count()
    calcUserIdCountPerRegion.show(100,false)

    // *** 3. Consider "eventlaenc" =126 or 107 as defining actions. Calculate first and second defining action for each "calc_userid" and also find the count of those actions. ***
    //val firstAndSecondActions = jsonData.filter("eventlaenc==126 OR eventlaenc==107").groupBy("calc_userid","eventlaenc").count()
    val firstAction = jsonData.filter("eventlaenc==126").groupBy(col("calc_userid"),col("eventlaenc").alias("first_action")).agg(count("calc_userid").as("first_action_count"))
    val secondAction = jsonData.filter("eventlaenc==107").groupBy(col("calc_userid").alias("calc_userid_second"),col("eventlaenc").alias("second_action")).agg(count("calc_userid").as("second_action_count"))
    val firstAndSecondAction = firstAction.join(secondAction, firstAction.col("calc_userid").equalTo(secondAction.col("calc_userid_second")), LEFT_OUTER).drop("calc_userid_second")
    firstAndSecondAction.show(false)
  }
}
