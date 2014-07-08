package com.github.kazuhito_m.anubisservice

import org.specs2.mutable._
import scala.collection.mutable
import scala.xml.XML
import scala.xml.Utility

class ConverterSpec extends Specification {

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

  val resultlHtml = <html>
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

  // Tests

  "コンバーターでファイルを読む" should {
    "ファイル指定で読み込みリストに格納" in {
      val actual = Converter.loadFileToList(testCsvFilePath)

      actual.size must equalTo(35)

    }
  }

  "リストの特殊なソート" should {
    "文字列リストで一行目以外を昇順ソート" in {
      val base = List("999_最初の行", "003", "001", "004", "002")
      val actual = Converter.specialSort(base)

      actual(0) must equalTo("999_最初の行")
      actual(4) must equalTo("004")
    }
  }

  "CSVテキストのリストを分解" should {
    "カンマ区切り文字列のリストからリストのリストへ" in {
      val base = List("11,12,13", "21,22,23", "31,32,33")
      val expected = List(
        List("11", "12", "13")
        , List("21", "22", "23")
        , List("31", "32", "33")
      )

      val actual = Converter.csvListToValueList(base)

      actual must equalTo(expected)
    }
  }

  "値の行列から抽象データ型へ変換" should {
    "グルーピングをしたツリー作成" in {

      // 空のCellを材料に、Mapを投影する。
      val expected = createBaseTreeForTest()

      //  Test対象の実行。
      val actual = Converter.valueListToAbstractDatas(csvTextList)

      // オブジェクトのツリー構造を比較(==の比較能力に依存)
      actual must equalTo(expected)

    }
  }

  "抽象データ型からHTMLに変換する" should {
    "Cell型オブジェク卜の内部ツリーからHTMLのテキストへ" in {

      // HTMLの元となるデータ
      val baseTree = createBaseTreeForTest()

      // 確認用のHTML(ScalaのXMLオブジェクト)
      val expected = Utility.trim(resultlHtml) // XMLオブジェクトはそのまま比較すると空白などにも敏感に比較するので整え

      // テスト対象を実行。
      val actualString = Converter.makeHtmlByAbstractDatas(baseTree)

      // 結果をXMLにコンバート
      val actual = Utility.trim(XML.loadString(actualString))

      // 結果確認
      actual must equalTo(expected)

    }

    "Cell型のツリー上から「自分から繋がる末端データはいくつあるか」を調べる" in {
      // 元となるツリーデータを取得
      val baseTree = createBaseTreeForTest()

      // Test対象の実行
      val actual = Converter.analyzeEndCellCount(baseTree)

      actual must equalTo(6)

    }

    "Cell型のツリー上から「自分から繋がる末端データ」を途中からでも出せる" in {
      // 元となるツリーデータを取得
      val baseTree = createBaseTreeForTest()

      def enRouteItemTest(key: String, expected: Int) = {

        val enRouteTree = baseTree.children(key) // 途中からのツリー

        // Test対象の実行
        val actual = Converter.analyzeEndCellCount(enRouteTree)

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
      val actual = Converter.getLastCell(baseTree)

      actual.value must equalTo("last")

    }

  }

  "CSVをHTMLにコンバートする" should {
    "CSVファイルを読み込みHTMLのテキストへと変換し内容を精査" in {
      // 予め「確認用正解ファイル」を読み込んでおく
      val expected = Utility.trim(XML.loadFile(testHtmlFileForExpectedPath))
      // 検証対象を実行
      val result = Converter.convertCsvToHtml(testCsvFilePath)
      // 結果の文字列データを比較可能なXMLオブジェクトに変換
      val actual = Utility.trim(XML.loadString(result))
      // 結果確認
      actual must equalTo(expected)
    }
  }

}
