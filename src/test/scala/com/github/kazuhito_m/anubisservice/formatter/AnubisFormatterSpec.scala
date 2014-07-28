package com.github.kazuhito_m.anubisservice.formatter

import java.util.LinkedHashMap
import org.ho.yaml.exception.ObjectCreationException
import org.ho.yaml.Yaml
import org.scalatest.junit.JUnitSuite
import org.junit.Assert._
import org.junit.Test
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.HashMap
import com.github.kazuhito_m.anubisservice.test.TestDataHelper._


class AnubisFormatterSpec extends JUnitSuite {

  // Test target
  val sut = new AnubisFormatter()

  // Tests

  @Test
  def 抽象データ型を独自形式テキストに変換() {

      // HTMLの元となるデータ1
      val baseTree = createBaseTreeForTest()

      val actual = sut.format(baseTree)

      // 結果確認
      assert(actual === """0:'root'
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
    4:'last'""")

  }

}
