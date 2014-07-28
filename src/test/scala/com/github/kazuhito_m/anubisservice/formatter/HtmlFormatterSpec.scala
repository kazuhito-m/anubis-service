package com.github.kazuhito_m.anubisservice.formatter

import com.github.kazuhito_m.anubisservice.test.TestDataHelper._
import org.specs2.mutable._
import scala.xml.XML
import scala.xml.Utility

class HtmlFormatterSpec extends Specification {

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
