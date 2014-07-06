package com.github.kazuhito_m.anubisservice



import org.specs2.mutable._
import scala.collection.mutable.LinkedHashMap
import scala.collection.mutable


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
          , List("11","12","13","other")
          , List("11","12","16","17")
          , List("11","18","19","20")
          , List("11","other","20","21")
          , List("other","12","19","17")
        )


        val expectedSeed = Map(
          "11"->Map(
            "12"->Map(
              "13"->Map(
                "14"->Map()
                ,        "other"->Map()
              )
              ,"16"->Map(
                "17"->Map()
              )
            )
            ,"18"->Map(
              "19"->Map(
                "20"->Map()
              )
            )
            ,"other"->Map(
              "20"->Map(
                "21"->Map()
              )
            )
          )
          ,"other"->Map(
            "12"->Map(
              "19"->Map(
                "17"->Map()
              )
            )
          )
        )

        // 上記のMapの構造を、Cell型に投影する。
        def convertMapToCells(lhMap:Map[String,Any],targetCell:Cell):Cell = {
          // Map内を全て回す。
          lhMap.foreach{case (key:String, value:Map[String,Any]) =>
            // Mapに要素があれば、Cellを作り、大本のCellに追加する。
            val newCell = Cell(key , new mutable.LinkedHashMap[String,Cell])
            targetCell.children += (key -> newCell)
            // 下に要素があるようであれば、再帰で呼び出す。
            convertMapToCells(value, newCell)
          }
          return targetCell
        }
        
        // 逆変換、CellをMapへ投影
        def convertCellsToMap(srcCells:Cell):Map[String,Any] = {
          var r = Map[String,Any]()
          srcCells.children.values.foreach { cell:Cell =>
            r = r.updated(cell.value , convertCellsToMap(cell))
          }
          return r
        }
       

        // 空のCellを材料に、Mapを投影する。
        val expected = convertMapToCells(expectedSeed,Cell("root",new mutable.LinkedHashMap[String,Cell]))

        //  Test対象の実行。
        val result = Converter.valueListToAbstructDatas(base)


        // 確認物を同じ土俵に上げるため、再度ピュアMapにコンバート
        val actual = convertCellsToMap(result)

        // TODO ネストして行くようなTree構造の時の「型の定義」ってどうするのか。
        actual == expectedSeed

      }
    }

  }

}
