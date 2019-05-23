package com.dataqueue

import com.typesafe.config.Config
import org.apache.spark.sql.{DataFrame, Dataset, Row}

/**
  * This class is responsible for generating a csv file from Datasets.
  *
  * @author Suneel Ayyaparaju
  */
object DSCsvGenerator {
  /**
    * write Dataset into a csv file
    *
    * @param path        : path where csv file needs to be saved.
    * @param hasHeaders  : if headers need to be added in the csv file.
    * @param inferSchema : Is inferschema
    * @param df          : dataset from which csv file needs to be created.
    */
  def generateCsvFromDataset(path: String, hasHeaders: Boolean, inferSchema: Boolean, df: DataFrame, config: Config): Unit = {
    if(config.getBoolean("isCoalesce")){
      df.coalesce(config.getInt("coalesce_partitions")).write.option("header", String.valueOf(hasHeaders))
        .option("inferSchema", String.valueOf(inferSchema)).option("quote", "\"").option("escape", "\"").csv(path)
    }else{
      df.write.option("header", String.valueOf(hasHeaders)).option("inferSchema", String.valueOf(inferSchema))
        .option("quote", "\"").option("escape", "\"").csv(path)
    }

  }

}
