package com.github.kazuhito_m.anubisservice

import scala.io.Source
import scala.Predef._
import scala.collection.mutable

/**
 * 抽象データを表すケースクラス。
 * @param value データの値(文字列)。
 * @param children 表中、右側にぶら下がるデータ。グルーピングされているなら複数ぶら下がる。
 */
case class Cell(value: String, children: mutable.LinkedHashMap[String, Cell])

/**
 * ポリモフィズム用のコンバーターの既定トレイト。
 */
trait BaseConverter {
  def convert(source: String) : String
}


/**
 * Created by kazuhito on 14/07/07.
 */
object Converter {

  /**
   * メインメソッド。
   * 引数に渡されたCSVファイルパスから読み込み、HTMLの文字列へと変換し返す。
   * @param convertType 変換の種類を示す文字列。
   * @param source 変換対象となる元情報(ファイルパス,URL,etc...)。
   * @return 変換結果の文字列表現。
   */
  def convert(convertType: String, source: String): String = selectConverter(convertType).convert(source)

  // コンバートのタイプから、適したコンバータを返す。
  def selectConverter(convertType: String) = {
    convertType match {
      case "xxx" => new BaseConverter { def convert(source: String): String = "" }  // ダミー
      case _ => new CsvToHtmlConverter()  // デフォルトは”ｈｔｍｌ”指定とみなす。
    }
  }

}