package com.github.kazuhito_m.anubisservice.formatter

import org.specs2.mutable._
import scala.collection.mutable
import scala.xml.XML
import scala.xml.Utility
import com.github.kazuhito_m.anubisservice.converter.CsvToHtmlConverter
import com.github.kazuhito_m.anubisservice.commons.Cell

class HtmlFormatterSpec extends Specification {

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
   val sut = new HtmlFormatter

   // Tests


  "抽象データ型からHTMLに変換する" should {
    "Cell型オブジェク卜の内部ツリーからHTMLのテキストへ" in {

      // HTMLの元となるデータ
      val baseTree = createBaseTreeForTest()

      // 確認用のHTML(ScalaのXMLオブジェクト)
      val expected = Utility.trim(resultHtml) // XMLオブジェクトはそのまま比較すると空白などにも敏感に比較するので整え

      // テスト対象を実行。
      val actualString = sut.makeHtmlByAbstractDatas(baseTree)

      // 結果をXMLにコンバート
      val actual = Utility.trim(XML.loadString(actualString))

      // 結果確認
      actual must equalTo(expected)

    }

    "Cell型のツリー上から「自分から繋がる末端データはいくつあるか」を調べる" in {
      // 元となるツリーデータを取得
      val baseTree = createBaseTreeForTest()

      // Test対象の実行
      val actual = sut.analyzeEndCellCount(baseTree)

      actual must equalTo(6)

    }

    "Cell型のツリー上から「自分から繋がる末端データ」を途中からでも出せる" in {
      // 元となるツリーデータを取得
      val baseTree = createBaseTreeForTest()

      def enRouteItemTest(key: String, expected: Int) = {

        val enRouteTree = baseTree.children(key) // 途中からのツリー

        // Test対象の実行
        val actual = sut.analyzeEndCellCount(enRouteTree)

        actual must equalTo(expected)

      }

      // パラメトリックテストっぽい何か。
      // TODO もうちょっと習熟したら綺麗な方法に書き換えたい
      enRouteItemTest("11", 5)
      enRouteItemTest("other", 1)

    }

    "Cell型のツリー上から「最後のもの」を取得する" in {
      // 元となるツリーデータを取得
      val baseTree = createBaseTreeForTest()

      // Test対象の実行
      val actual = sut.getLastCell(baseTree)

      actual.value must equalTo("last")

    }

  }

 }
