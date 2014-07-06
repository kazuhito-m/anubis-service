package com.github.kazuhito_m.anubisservice

import org.specs2.mutable._

class AnubisServiceSpec extends Specification {

  "コマンドライン引数" should {
    "引数無し" in {
      AnubisService.main(Array())
      true
    }
    "引数が複数" in {
      AnubisService.main(Array("-t", "html" ,"-s","filename"))
      true
    }
    "Typeの引数を省略" in {
      AnubisService.main(Array("-s","filename"))
      true
    }
  }

}
