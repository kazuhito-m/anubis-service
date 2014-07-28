package com.github.kazuhito_m.anubisservice.converter

import com.github.kazuhito_m.anubisservice.test.TestDataHelper._
import org.specs2.mutable._
import scala.xml.XML
import scala.xml.Utility

class CsvToHtmlConverterSpec extends Specification {

  // Test target
  val sut = new CsvToHtmlConverter

  // Tests

  "CSVをHTMLにコンバートする" should {
    "CSVファイルを読み込みHTMLのテキストへと変換し内容を精査" in {
      // 予め「確認用正解ファイル」を読み込んでおく
      val expected = Utility.trim(XML.loadFile(testHtmlFileForExpectedPath))
      // 検証対象を実行
      val result = sut.convert(testCsvFilePath)
      // 結果の文字列データを比較可能なXMLオブジェクトに変換
      val actual = Utility.trim(XML.loadString(result))
      // 結果確認
      actual must equalTo(expected)
    }
  }

}
