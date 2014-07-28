package com.github.kazuhito_m.anubisservice

import org.rogach.scallop._
import com.github.kazuhito_m.anubisservice.commons.Analyzer


class AnubisServiceConf(arguments: Seq[String]) extends ScallopConf(arguments) {
  version("AnubisService 0.0.1 (c) 2014 Kazuhito Miura")
  banner("うるせ〜ｗ")
  footer("\n for all other information, see [url]")

  val convertType = opt[String]("convertType", default = Some("html"), short = 't')
  val source = opt[String]("source", short = 's', required = true)
}


object AnubisService {
  def main(args: Array[String]) {
    val conf = new AnubisServiceConf(args)
    val result = Analyzer.convert(conf.convertType.apply(), conf.source.apply())
    System.out.println(result)
  }
}

