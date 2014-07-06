package com.github.kazuhito_m.anubisservice

import scala.io.Source
import scala.Predef._
import scala.collection.mutable


/**
 * Created by kazuhito on 14/07/07.
 */
object Converter {

  def convertCsvToHtml(csvFile: String): String = {
    return csvFile
  }

  def loadFileToList(filePath: String): List[String] = Source.fromFile(filePath).getLines().toList

  def specialSort(strings: List[String]): List[String] = strings.head :: strings.tail.sorted

  def csvListToValueList(csvList: List[String]): List[List[String]] = csvList.map(line => line.split(",|\n").toList)

  def valueListToAbstructDatas(values: List[List[String]]): Cell = {

    def createTree(v1: List[String], parentCell: Cell): Cell = {
      if (!v1.isEmpty) {
        // 引数に指定されたCellの子どもを文字列で検索し、あればそれを、なければ新しく追加しつつ取得。
        val v: String = v1.head
        val children = parentCell.children
        val currentCell = if (children.contains(v)) children(v) else Cell(v, mutable.LinkedHashMap[String, Cell]())
        children(v) = currentCell // 既にあった場合は「取得してセットし直す」という気色悪い挙動… TODO 余裕できたら治す。
        // List側を一つ前に進め、Cell側は子どもの代へと移行し、再帰
        createTree(v1.tail, currentCell)
      }
      return parentCell
    }

    // Cell構造作成処理
    val root = Cell("root", mutable.LinkedHashMap[String, Cell]())
    values.foreach {
      v =>
        createTree(v, root)
    }

    return root
  }

}

case class Cell(value: String, children: mutable.LinkedHashMap[String, Cell])