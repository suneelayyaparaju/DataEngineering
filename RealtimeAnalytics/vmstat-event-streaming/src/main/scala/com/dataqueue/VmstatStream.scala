package com.dataqueue

import com.typesafe.config.Config
import kafka.serializer.StringDecoder
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}


class VmstatStream {
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)
  val logger: Logger = Logger.getLogger(getClass)

  def triggerVmstatEvent(config: Config): Unit = {
    val (brokerList, topics) = (config.getString("kafkaBrokerList"), config.getString("kafkaTopic"))
    val spark = SparkSessionConf.getSparkSession
    val ssc = new StreamingContext(spark.sparkContext, Seconds(5))
    import spark.implicits._

    val topicMap = topics.split(",").toSet
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokerList)
    val lines = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicMap).map(_._2)
    lines.foreachRDD { rdd =>
      //rdd.foreach(println)
      val vmstatDf = rdd.filter(line => !line.contains("memory"))
        .filter(line => !line.contains("buff")).map(line => line.split("[\\s]+"))
        .map(c => Vmstat(c(1), c(2), c(3), c(4), c(5), c(6), c(7), c(8), c(9), c(10), c(11), c(12), c(13), c(14), c(15), c(16))).toDF()
      vmstatDf.show(5)
      vmstatDf.write.format("orc").mode(SaveMode.Append).saveAsTable(config.getString("hiveTable"))
    }
    ssc.start()
    ssc.awaitTermination()
  }
}
