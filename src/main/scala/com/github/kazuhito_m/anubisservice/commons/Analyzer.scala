package com.github.kazuhito_m.anubisservice.commons

import scala.Predef._
import com.github.kazuhito_m.anubisservice.converter.{BaseConverter, CsvToHtmlConverter}


/**
 * Created by kazuhito on 14/07/07.
 */
object Analyzer {

  /**
   * メインメソッド。
   * 引数に渡されたCSVファイルパスから読み込み、HTMLの文字列へと変換し返す。
   * @param convertType 変換の種類を示す文字列。
   * @param source 変換対象となる元情報(ファイルパス,URL,etc...)。
   * @return 変換結果の文字列表現。
   */
  def convert(convertType: String, source: String): String = selectConverter(convertType).convert(source)

  // コンバートのタイプから、適したコンバータを返す。
  def selectConverter(convertType: String): BaseConverter = {
    convertType match {
      case "xxx" => new BaseConverter {
        def convert(source: String): String = ""
      } // ダミー
      case _ => new CsvToHtmlConverter() // デフォルトは”ｈｔｍｌ”指定とみなす。
    }
  }

}