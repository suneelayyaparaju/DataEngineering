package com.dataqueue
/**
  * This class contains String manipulation operations using scala.
  *
  * @author Suneel Ayyaparaju
  */
object StringManipulation {
  def main(args: Array[String]) {
    println("### First Output: ###")
    println(manipulateFirstLastCharComb("123456"))
    println("### Second Output: ###")
    println(manipulateFirstLastCharComb("12345"))
  }

  def manipulateFirstLastCharComb(inputStr: String): String = {
    val halfLength = inputStr.length / 2
    var outputStr = ""
    var temp = inputStr
    for (i <- 0 until halfLength) {
      outputStr += temp.charAt(0) + temp.last.toString
      temp = temp.replaceAll("^.|.$", "")
      if (temp.length == 1) outputStr += temp
    }
    outputStr
  }
}
