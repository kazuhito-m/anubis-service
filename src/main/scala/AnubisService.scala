package com.github.kazuhito_m.anubisservice

import org.rogach.scallop._

class AnubisServiceConf(arguments: Seq[String]) extends ScallopConf(arguments) {
  version("AnubisService 0.0.1 (c) 2014 Kazuhito Miura")
  banner("うるせ〜ｗ")
  footer("\n for all other information, see [url]")

  val convertType = opt[String]("convertType", default = Some("html"), short = 't')
  val source = opt[String]("source", short = 's')
}


object AnubisService {
  def main(args: Array[String]){
    val conf = new AnubisServiceConf(args)

    println(conf.convertType)
    println(conf.source)
  }
}

