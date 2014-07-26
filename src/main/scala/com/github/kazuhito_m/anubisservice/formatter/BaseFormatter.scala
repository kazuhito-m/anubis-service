package com.github.kazuhito_m.anubisservice.formatter

import java.io.File
import com.github.kazuhito_m.anubisservice.commons.Cell

/**
 * Created by kazuhito on 14/07/26.
 */
trait BaseFormatter {
  def format(data: Cell) : String
}
