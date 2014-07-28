package com.github.kazuhito_m.anubisservice.test

import com.github.kazuhito_m.anubisservice.commons.Cell
import scala.collection.mutable

/**
 * Created by kazuhito on 14/07/28.
 */
object TestDataHelper {

  // Test Datas

  val testCsvFilePath = "target/scala-2.11/test-classes/Data.csv"

  val testHtmlFileForExpectedPath = "target/scala-2.11/test-classes/Data.html"


  val csvTextList = List(
    List("11", "12", "13", "14")
    , List("11", "12", "13", "other")
    , List("11", "12", "16", "17")
    , List("11", "18", "19", "20")
    , List("11", "other", "19", "21")
    , List("other", "12", "19", "last")
  )

  val treeTextMap = Map(
    "11" -> Map(
      "12" -> Map(
        "13" -> Map(
          "14" -> Map()
          , "other" -> Map()
        )
        , "16" -> Map(
          "17" -> Map()
        )
      )
      , "18" -> Map(
        "19" -> Map(
          "20" -> Map()
        )
      )
      , "other" -> Map(
        "19" -> Map(
          "21" -> Map()
        )
      )
    )
    , "other" -> Map(
      "12" -> Map(
        "19" -> Map(
          "last" -> Map()
        )
      )
    )
  )

  val resultHtml = <html>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <body>
      <table border="1">
        <tr>
          <td rowspan="5">11</td>
          <td rowspan="3">12</td>
          <td rowspan="2">13</td>
          <td>14</td>
        </tr>
        <tr>
          <td>other</td>
        </tr>
        <tr>
          <td>16</td>
          <td>17</td>
        </tr>
        <tr>
          <td>18</td>
          <td>19</td>
          <td>20</td>
        </tr>
        <tr>
          <td>other</td>
          <td>19</td>
          <td>21</td>
        </tr>
        <tr>
          <td>other</td>
          <td>12</td>
          <td>19</td>
          <td>last</td>
        </tr>
      </table>
    </body>
  </html>

  val resultAnubisText = """0:'root'
 1:'11'
  2:'12'
   3:'13'
    4:'14'
    4:'other'
   3:'16'
    4:'17'
  2:'18'
   3:'19'
    4:'20'
  2:'other'
   3:'19'
    4:'21'
 1:'other'
  2:'12'
   3:'19'
    4:'last'"""

  // Utility Method for Test

  // Mapの構造を、Cell型に投影する。
  def convertMapToCells(lhMap: Map[String, Any], targetCell: Cell): Cell = {
    // Map内を全て回す。
    lhMap.foreach {
      case (key: String, value: Map[String, Any]) =>
        // Mapに要素があれば、Cellを作り、大本のCellに追加する。
        val newCell = Cell(key, new mutable.LinkedHashMap[String, Cell])
        targetCell.children += (key -> newCell)
        // 下に要素があるようであれば、再帰で呼び出す。
        convertMapToCells(value, newCell)
    }
    targetCell
  }

  // RootになるCell型オブジェクトを作成するエイリアス。
  def createRootCell() = Cell("root", new mutable.LinkedHashMap[String, Cell])

  // Test用のCell型ツリーオブジェクトを取得するエイリアス。
  def createBaseTreeForTest() = convertMapToCells(treeTextMap, createRootCell())

}
