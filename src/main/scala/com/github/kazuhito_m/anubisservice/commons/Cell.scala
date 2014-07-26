package com.github.kazuhito_m.anubisservice.commons

import scala.collection.mutable

/**
 * 抽象データを表すケースクラス。
 * @param value データの値(文字列)。
 * @param children 表中、右側にぶら下がるデータ。グルーピングされているなら複数ぶら下がる。
 */
case class Cell(value: String, children: mutable.LinkedHashMap[String, Cell])