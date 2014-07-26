package com.github.kazuhito_m.anubisservice.converter

import com.github.kazuhito_m.anubisservice.parser.CsvParser
import com.github.kazuhito_m.anubisservice.formatter.HtmlFormatter

/**
 * Created by kazuhito on 14/07/26.
 */
class CsvToHtmlConverter extends BaseConverter {

  /**
   * メインメソッド。
   * 引数に渡されたCSVファイルパスから読み込み、HTMLの文字列へと変換し返す。
   * @param csvFile 変換対象となるファイルパス。
   * @return 変換結果のHTML文字列。
   */
  def convert(csvFile: String): String = {
    val data = (new CsvParser()).perse(csvFile)
    (new HtmlFormatter()).format(data)
  }

}
