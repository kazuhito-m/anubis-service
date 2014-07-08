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

    def createTree(parts: List[String], parentCell: Cell): Cell = {
      if (!parts.isEmpty) {
        // 引数に指定されたCellの子どもを文字列で検索し、あればそれを、なければ新しく追加しつつ取得。
        val firstValue: String = parts.head
        val node = parentCell.children
        val hit = if (node.contains(firstValue)) node(firstValue) else Cell(firstValue, mutable.LinkedHashMap[String, Cell]())
        node(firstValue) = hit // 既にあった場合は「取得してセットし直す」という気色悪い挙動… TODO 余裕できたら治す。
        // List側を一つ前に進め、Cell側は子どもの代へと移行し、再帰
        createTree(parts.tail, hit)
      }
      parentCell
    }

    // Cell構造作成処理
    val root = Cell("root", mutable.LinkedHashMap[String, Cell]())
    values.foreach { createTree(_, root) }

    root
  }

  /**
   * Cell型オブジェクトのツリー内から「末端となるCellオブジェクトがいくつあるか」を返す。
   * (HTMLのRowspanを求めることを視野に入れたメソッド)
   *
   * 再帰的にツリーの中を泳いでいき、末端Cellを見つけた場合"1"と評価し、それを合算していく。
   *
   * @param rootCell 検査対象のCellツリーオブジェクト。
   * @return 末端となるCellがいくつぶら下がっているか。
   */
  def analyzeEndCellCount(rootCell: Cell) : Int = {
    val map = rootCell.children
    if (map.isEmpty) 1 else map.values.foldLeft(0){ (count,item) => count + analyzeEndCellCount(item) }
  }

  def createTableDetail(cell:Cell):String = {
    val map = cell.children
    val trTags = if (map.isEmpty) "</tr>\n<tr>\n" else ""

    val rowSpanCount = analyzeEndCellCount(cell)
    val rs = if (rowSpanCount > 1) " rowspan=\"" + rowSpanCount + "\"" else ""
    val tdTag = if (cell.value == "root") "" else  "  <td" + rs + ">" + cell.value + "</td>\n"

    tdTag + trTags + map.values.foldLeft(""){ (html,item) => html + createTableDetail(item) }

  }

  def makeHtmlByAbstructDatas(rootCell:Cell):String = {
    """<html>
        <body>
          <table border="1">
            <tr>
""" + createTableDetail(rootCell) + """
            </tr>
          </table>
        </body>
      </html>"""
  }

}

/**
 * 抽象データを表すケースクラス。
 * @param value データの値(文字列)。
 * @param children 表中、右側にぶら下がるデータ。グルーピングされているなら複数ぶら下がる。
 */
case class Cell(value: String, children: mutable.LinkedHashMap[String, Cell])