package com.github.kazuhito_m.anubisservice.parser

import org.specs2.mutable._
import com.github.kazuhito_m.anubisservice.test.TestDataHelper._

class CsvParserSpec extends Specification {

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
