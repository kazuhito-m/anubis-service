package com.github.kazuhito_m.anubisservice.parser

import org.specs2.mutable._
import scala.collection.mutable
import scala.xml.XML
import scala.xml.Utility
import com.github.kazuhito_m.anubisservice.converter.CsvToHtmlConverter
import com.github.kazuhito_m.anubisservice.commons.Cell

class CsvParserSpec extends Specification {

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
     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
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

   // Test target
   val sut = new CsvParser()

   // Tests

   "コンバーターでファイルを読む" should {
     "ファイル指定で読み込みリストに格納" in {
       val actual = sut.loadFileToList(testCsvFilePath)

       actual.size must equalTo(35)

     }
   }

   "リストの特殊なソート" should {
     "文字列リストで一行目以外を昇順ソート" in {
       val base = List("999_最初の行", "003", "001", "004", "002")
       val actual = sut.specialSort(base)

       actual(0) must equalTo("999_最初の行")
       actual(4) must equalTo("004")
     }
   }

   "値の行列から抽象データ型へ変換" should {
     "グルーピングをしたツリー作成" in {

       // 空のCellを材料に、Mapを投影する。
       val expected = createBaseTreeForTest()

       //  Test対象の実行。
       val actual = sut.valueListToAbstractData(csvTextList)

       // オブジェクトのツリー構造を比較(==の比較能力に依存)
       actual must equalTo(expected)

     }
   }

 }
