package com.github.kazuhito_m.anubisservice

import org.specs2.mutable._

class AnubisServiceSpec extends Specification {

  "The 'Hello world' string" should {
    "contain 11 characters" in {
      "Hello world" must have size (11)
    }
    "start with 'Hello'" in {
      "Hello world" must startWith("Hello")
    }
    "end with 'world'" in {
      "Hello world" must endWith("world")
    }
  }

  "コマンドライン引数" should {
    "引数無し" in {
      AnubisService.main(Array())
      true
    }
    "引数が複数" in {
      AnubisService.main(Array("-t", "html" ,"-s","filename"))
      true
    }
  }

}
