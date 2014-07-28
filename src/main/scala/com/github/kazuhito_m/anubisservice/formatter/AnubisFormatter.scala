package com.github.kazuhito_m.anubisservice.formatter

import com.github.kazuhito_m.anubisservice.commons.Cell

/**
 * 抽象データ型を、その文字列表現である「AnubisText」形式へと変換するフォーマッタ。
 */
class AnubisFormatter extends BaseFormatter {

  def format(data: Cell): String = {
    makeSpecialText(data, 0)
  }

  /**
   * AnubisTextという「抽象データ型のテキスト表現」用独自テキストを作成する。
   *
   * Cell型の文字列表現として、
   * (depth分の空白) + depth値：'値'
   * を一行とした文字列表現をツリー型の内容で順次展開していく。
   * Cell型はツリー構造のため、再帰的に自身関数を呼ぶ。
   *
   * @param cell 出力対象となる抽象データ型オブジェクト。
   * @param depth ネストした再帰呼出しが「現在幾つ目か？」を表すDepth値。
   * @return 展開したAnubisText形式文字列。
   */
  def makeSpecialText(cell: Cell, depth: Int): String = cell.children.values.foldLeft(s"${(" " * depth)}$depth:'${cell.value}'") {
    (text, child) => s"$text\n${makeSpecialText(child, depth + 1)}"
  }

}
