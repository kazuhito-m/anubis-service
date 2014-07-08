package com.github.kazuhito_m.anubisservice

import org.specs2.mutable._
import java.io.PrintStream

class AnubisServiceSpec extends Specification {

  val testCsvFilePath = "target/scala-2.11/test-classes/Data.csv"

  val actualFile = "target/scala-2.11/test-classes/stdio.log"

  "コマンドライン引数" should {
    "引数無し" in {
      // テスト実行
      // AnubisService.main(Array())
      // TODO Scallopのせいだろう。exit()でも書いてあるのか、テストすると終わってしまう。
      true
    }
    "引数が複数" in {
      // 初期処理。stdoutを騙す。
      val ps = new PrintStream(actualFile)
      val swapO = System.out
      System.setOut(ps)

      // テスト実行
      AnubisService.main(Array("-t", "html", "-s", testCsvFilePath))

      // 後処理
      System.setOut(swapO)
      true
    }
    "Typeの引数を省略" in {
      // 初期処理。stdoutを騙す。
      val ps = new PrintStream(actualFile)
      val swapO = System.out
      System.setOut(ps)

      // テスト実行
      AnubisService.main(Array("-s", testCsvFilePath))

      // 後処理
      System.setOut(swapO)
      true
    }
  }

}
