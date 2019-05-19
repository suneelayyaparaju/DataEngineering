package com.dataqueue
/**
  * Data Model for log record.
  *
  * @author Suneel Ayyaparaju
  */
case class LogRecord( host: String, timeStamp: String, url:String, httpCode:Int)
