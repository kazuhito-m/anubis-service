package com.github.kazuhito_m.anubisservice

import scala.io.Source
import scala.Predef._
import scala.collection.immutable.TreeMap


/**
 * Created by kazuhito on 14/07/07.
 */
object Converter {



  def convertCsvToHtml(csvFile: String): String = {
    return csvFile
  }

  def loadFileToList(filePath:String): List[String] = Source.fromFile(filePath).getLines().toList

  def specialSort(strings: List[String]): List[String] = strings.head :: strings.tail.sorted

  def csvListToValueList(csvList:List[String]) : List[List[String]] = csvList.map(line => line.split(",|\n").toList)

  // TODO 型の宣言から見直し。
  def valueListToAbstructDatas(values : List[List[String]]) : TreeMap[String, Null] = {
    TreeMap("Test"->null)
  }

}

