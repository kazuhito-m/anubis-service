package com.github.kazuhito_m.anubisservice

import org.rogach.scallop._

class AnubisServiceConf(arguments: Seq[String]) extends ScallopConf(arguments) {
  version("AnubisService 0.0.1 (c) 2014 Kazuhito Miura")
  banner("""Usage: AnubisService [OPTION].... [foo|bar] [OPTION].... [foo|bar]
            |AnubisService is......[describe the app here]
            |""".stripMargin)
  footer("\n for all other information, see [url]")

  val option1 = opt[Int]("option1")
}


object AnubisService {
  def main(args: Array[String]){
    val conf = new AnubisServiceConf(args)

    println(conf.option1)
  }
}

