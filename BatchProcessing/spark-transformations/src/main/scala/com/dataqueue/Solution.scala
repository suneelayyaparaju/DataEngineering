package com.dataqueue

import com.typesafe.config.Config
import org.apache.spark.sql.expressions.Window
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
    import spark.implicits._
    val jsonData = spark.read.json(config.getString("jsonFilesPath"))

    // *** 1. Find the average time per session(sessionid is the defining field for every session) when "appnameenc"=1 and "appnameenc"=2 ***
    var session = jsonData.filter("appnameenc==1 OR appnameenc==2" ).select("calc_userid","sessionid","appnameenc","timestamp")
    val sessionIdDesc = Window.partitionBy("sessionid").orderBy(col("timestamp").desc)
    val timestampDiff = max("timestamp").over(sessionIdDesc) - col("timestamp")
    session = session.withColumn("session_duration", timestampDiff)
    val averageTimePerSession = session.groupBy("sessionid").avg("session_duration")
    averageTimePerSession.show(100,false)

    // *** 2. Count of "calc_userid" for each "region". ignore "-" and nulls ***
    val calcUserIdCountPerRegion = jsonData.where(col("region").isNotNull.and(col("region").notEqual("-"))).groupBy("region").count()
    calcUserIdCountPerRegion.show(100,false)

    // *** 3. Consider "eventlaenc" =126 or 107 as defining actions. Calculate first and second defining action for each "calc_userid" and also find the count of those actions. ***
    //calc_userid	first_action	first_action_count	second_action	  second_action_count
    //    1	           126	          4	                 107	            1
    //    2	           107	          8
    //    3	           107	          2	                 126	            6

    var firstAndSecondActions = jsonData.filter("eventlaenc==126 OR eventlaenc==107").select("calc_userid","eventlaenc","timestamp")
    firstAndSecondActions = firstAndSecondActions.withColumn("first_action", when($"eventlaenc"===126,126))
                            .withColumn("second_action", when($"eventlaenc"===107,107)).drop("eventlaenc")
    val calcUserIdWindowSpec = Window.partitionBy("calc_userid")
    firstAndSecondActions = firstAndSecondActions.withColumn("first_action_count", count("first_action").over(calcUserIdWindowSpec))
      .withColumn("second_action_count", count("second_action").over(calcUserIdWindowSpec)).orderBy("timestamp").drop("timestamp")
    val reorderedCols = Seq("calc_userid", "first_action", "first_action_count", "second_action", "second_action_count")
    firstAndSecondActions = firstAndSecondActions.select(reorderedCols.head, reorderedCols.tail: _*)
    firstAndSecondActions.show(100,false)
  }
}
