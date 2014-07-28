package com.github.kazuhito_m.anubisservice.parser

import com.github.kazuhito_m.anubisservice.commons.Cell

/**
 * Created by kazuhito on 14/07/26.
 */
trait BaseParser {
  def perse(source: String): Cell
}
