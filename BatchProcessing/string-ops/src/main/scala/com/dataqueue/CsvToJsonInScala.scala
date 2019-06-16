package com.dataqueue

import java.io.File

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.csv.{CsvMapper, CsvSchema}
import com.typesafe.config.{Config, ConfigFactory}

/**
  * class to convert Csv string to Json string using scala.
  *
  * @author Suneel Ayyaparaju
  */
object CsvToJsonInScala {
  var config: Config = ConfigFactory.load()

  def main(args: Array[String]) {
    println("### Output Json: ###")
    println(csvFileToJson(config.getString("csvFilePath")))
  }

  def csvFileToJson(filePath: String): String = {
    val inputCsvFile = new File(filePath)

    // if the csv has header, use setUseHeader(true)
    val csvSchema = CsvSchema.builder().setUseHeader(true).build()
    val csvMapper = new CsvMapper()

    // java.util.Map[String, String] identifies they key values type in JSON
    val result = csvMapper
      .readerFor(classOf[java.util.Map[String, String]])
      .`with`(csvSchema)
      .readValues(inputCsvFile)
      .readAll()

    val mapper = new ObjectMapper()

    // json return value
    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result)
  }

}