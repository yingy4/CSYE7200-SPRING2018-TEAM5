package retrieval

import ingest.SearchConsole

class sbtTest {
  def main(args: Array[String]) {
    val buf = scala.collection.mutable.ListBuffer.empty[Any]
    SearchConsole.SEARCH_KEYWORDS = args(0)
    SearchConsole.ATTRIBUTE = args(1)
    SearchConsole.searchMultiple(buf, 1,2)

    val list2 = SearchConsole.searchSingle(1)
    val list3 = SearchConsole.searchSingle(1)
    println(list2)
    println(buf.toList)
  }
}
