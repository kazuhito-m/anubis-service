package com.github.kazuhito_m.anubisservice.converter

import scala.Predef._

/**
 * ポリモフィズム用のコンバーターの既定トレイト。
 */
trait BaseConverter {
  def convert(source: String) : String
}
