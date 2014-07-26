package com.github.kazuhito_m.anubisservice.formatter

import java.io.File
import com.github.kazuhito_m.anubisservice.commons.Cell

/**
 * Created by kazuhito on 14/07/26.
 */
class HtmlFormatter extends BaseFormatter {

  def format(data: Cell) : String = {
    makeHtmlByAbstractDatas(data)
  }
  /**
   * Cell型オブジェクトのツリー内から「末端となるCellオブジェクトがいくつあるか」を返す。
   * (HTMLのRowspanを求めることを視野に入れたメソッド)
   *
   * 再帰的にツリーの中を泳いでいき、末端Cellを見つけた場合"1"と評価し、それを合算していく。
   *
   * @param rootCell 検査対象のCellツリーオブジェクト。
   * @return 末端となるCellがいくつぶら下がっているか。
   */
  def analyzeEndCellCount(rootCell: Cell) : Int = {
    val map = rootCell.children
    if (map.isEmpty) 1 else map.values.foldLeft(0){ (count,item) => count + analyzeEndCellCount(item) }
  }

  def getLastCell(cell: Cell) : Cell = cell.children.values.foldLeft(cell){ (last,cur) => getLastCell(cur) }

  def createTableDetail(cell:Cell , lastCell:Cell):String = {
    // </tr><tr> の判定と作成(最後の値の時は書かない)
    val map = cell.children
    val trTags = if (map.isEmpty && cell != lastCell) "</tr>\n<tr>\n" else ""
    // <td>タグのrowspan作成
    val rowSpanCount = analyzeEndCellCount(cell)
    val rs = if (rowSpanCount > 1) " rowspan=\"" + rowSpanCount + "\"" else ""
    // 値部作成（一段目は無視)
    val tdTag = if (cell.value == "root") "" else  "  <td" + rs + ">" + cell.value + "</td>\n"
    // 組立とreturn
    tdTag + trTags + map.values.foldLeft(""){ (html,item) => html + createTableDetail(item,lastCell) }
  }

  def makeHtmlByAbstractDatas(rootCell:Cell):String =
    """<html>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <body>
          <table border="1">
            <tr>
    """ + createTableDetail(rootCell,getLastCell(rootCell)) + """</tr>
          </table>
        </body>
      </html>"""


}
