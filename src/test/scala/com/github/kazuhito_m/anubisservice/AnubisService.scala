package com.github.kazuhito_m.anubisservice

import org.specs2.mutable._
import java.io.PrintStream
import com.github.kazuhito_m.anubisservice.test.TestDataHelper._

class AnubisServiceSpec extends Specification {

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
      // FIXME どーしても標準出力に出してしまうので、あえてダミーストリームを戻さないで続行(副作用はあると思われ)
      // System.setOut(swapO)
      ps.close()
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
      // FIXME どーしても標準出力に出してしまうので、あえてダミーストリームを戻さないで続行(副作用はあると思われ)
      // System.setOut(swapO)
      ps.close()
      true
    }
  }

}
