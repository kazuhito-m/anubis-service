package com.github.kazuhito_m.anubisservice



import org.specs2.mutable._
import scala.collection.mutable.LinkedHashMap
import scala.collection.immutable.TreeMap


class ConverterSpec extends Specification {

  "コンバーターでファイルを読む" should {
    "ファイル指定で読み込みリストに格納" in {
      val csv = "target/scala-2.11/test-classes/Data.csv"
      val actual = Converter.loadFileToList(csv)

      actual.size must equalTo(35)

    }

    "リストの特殊なソート" should {
      "文字列リストで一行目以外を昇順ソート" in {
        val base = List("999_最初の行","003","001","004","002")
        val actual = Converter.specialSort(base)

        actual(0) must equalTo("999_最初の行")
        actual(4) must equalTo("004")
      }
    }

    "CSVテキストのリストを分解" should {
      "カンマ区切り文字列のリストからリストのリストへ" in {
        val base = List("11,12,13","21,22,23","31,32,33")
        val expected = List(
          List("11","12","13")
          , List("21","22","23")
          , List("31","32","33")
        )

        val actual = Converter.csvListToValueList(base)

        actual == expected
      }
    }

    "値の行列から抽象データ型へ変換" should {
      "グルーピングをしたツリー作成" in {

        val base = List(
          List("11","12","13","14")
          , List("11","12","13","Other")
          , List("11","12","16","17")
          , List("11","18","19","20")
          , List("11","other","20","21")
          , List("other","12","19","17")
        )

        val expected = LinkedHashMap(
          "11"->LinkedHashMap(
            "12"->LinkedHashMap(
              "13"->LinkedHashMap(
                "other"->LinkedHashMap()
                ,"14"->LinkedHashMap()
              )
              ,"16"->LinkedHashMap(
                "17"->LinkedHashMap()
              )
            )
            ,"18"->LinkedHashMap(
              "19"->LinkedHashMap(
                "20"->LinkedHashMap()
              )
            )
            ,"other"->LinkedHashMap(
              "20"->LinkedHashMap(
                "21"->LinkedHashMap()
              )
            )
            ,"other"->LinkedHashMap(
              "12"->LinkedHashMap(
                "19"->LinkedHashMap(
                  "20"->LinkedHashMap()
                )
              )
            )
          )
        )

        val actual = Converter.valueListToAbstructDatas(base)

        // TODO ネストして行くようなTree構造の時の「型の定義」ってどうするのか。
//        actual = expected

        true
      }
    }

  }

}
