package com.dataqueue

import com.typesafe.config.Config

import scala.util.matching.Regex
/**
  * This class holds business logic for Log parsing and Analysis.
  *
  * @author Suneel Ayyaparaju
  */
object LogParser {
  val PATTERN: Regex = """^(\S+) (\S+) (\S+) \[([\w:/]+\s[+\-]\d{4})\] "(\S+) (\S+)(.*)" (\d{3}) (\S+)""".r

  def parseLogs(config: Config): Unit = {
    val spark = ParserConf.getSparkSession
    import spark.sqlContext.implicits._
    val logFile = spark.sparkContext.textFile(config.getString("logFilePath"))
    val accessLog = logFile.map(parseLogLine)
    val accessDf = accessLog.toDF()
    accessDf.printSchema
    accessDf.createOrReplaceTempView("nasalog")
    val output = spark.sql("select * from nasalog")
    output.createOrReplaceTempView("nasa_log")

    spark.sql("cache TABLE nasa_log")

    // **Q1: Write spark code to find out top 10 requested URLs along with count of number of times they have been requested (This information will help company to find out most popular pages and how frequently they are accessed)**
    val top10ReqUrls = spark.sql("select url,count(*) as req_cnt from nasa_log where upper(url) like '%HTML%' group by url order by req_cnt desc LIMIT 10")
    top10ReqUrls.show(10, false)
    //Write to CSV file
    DSCsvGenerator.generateCsvFromDataset(config.getString("top10ReqUrlsCsvPath"), true, true, top10ReqUrls, config)

    // **Q2: Write spark code to find out top 5 hosts / IP making the request along with count (This information will help company to find out locations where website is popular or to figure out potential DDoS attacks)**
    val top5Hosts = spark.sql("select host,count(*) as req_cnt from nasa_log group by host order by req_cnt desc LIMIT 5")
    top5Hosts.show(5,false)
    DSCsvGenerator.generateCsvFromDataset(config.getString("top5HostsCsvPath"), true, true, top5Hosts, config)

    // **Q3: Write spark code to find out top 5 time frame for high traffic (which day of the week or hour of the day receives peak traffic, this information will help company to manage resources for handling peak traffic load)**
    val top5TimeFramesHighTraffic = spark.sql("select substr(timeStamp,1,14) as timeFrame,count(*) as req_cnt from nasa_log group by substr(timeStamp,1,14) order by req_cnt desc LIMIT 5")
    top5TimeFramesHighTraffic.show(5,false)
    DSCsvGenerator.generateCsvFromDataset(config.getString("top5TimeFramesHighTrafficCsvPath"), true, true, top5TimeFramesHighTraffic, config)

    // **Q4: Write spark code to find out 5 time frames of least traffic (which day of the week or hour of the day receives least traffic, this information will help company to do production deployment in that time frame so that less number of users will be affected if some thing goes wrong during deployment)**
    val top5TimeFramesLeastTraffic = spark.sql("select substr(timeStamp,1,14) as timeFrame,count(*) as req_cnt from nasa_log group by substr(timeStamp,1,14) order by req_cnt  LIMIT 5")
    top5TimeFramesLeastTraffic.show(5,false)
    DSCsvGenerator.generateCsvFromDataset(config.getString("top5TimeFramesLeastTrafficCsvPath"), true, true, top5TimeFramesLeastTraffic, config)

    // **Q5: Write spark code to find out unique HTTP codes returned by the server along with count (this information is helpful for devops team to find out how many requests are failing so that appropriate action can be taken to fix the issue)**
    val uniqueHTTPCodes = spark.sql("select httpCode,count(*) as req_cnt from nasa_log group by httpCode ")
    uniqueHTTPCodes.show(5,false)
    DSCsvGenerator.generateCsvFromDataset(config.getString("uniqueHTTPCodesCsvPath"), true, true, uniqueHTTPCodes, config)

  }


  def parseLogLine(log: String): LogRecord = {
    val res = PATTERN.findFirstMatchIn(log)
    if (res.isEmpty) {
      println("Rejected Log Line: " + log)
      LogRecord("Empty", "", "",  -1 )
    } else {
      val m = res.get
      LogRecord(m.group(1), m.group(4), m.group(6), m.group(8).toInt)
    }
  }

}
