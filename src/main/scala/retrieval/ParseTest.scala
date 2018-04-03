package retrieval

import ingest.SearchConsole


object ParseTest extends App {
  SearchConsole.SEARCH_KEYWORDS = "Trouser"
  SearchConsole.ATTRIBUTE = "Color"

  val buf = scala.collection.mutable.ListBuffer.empty[Any]
  SearchConsole.searchMultiple(buf, 1,2)
  SearchConsole.searchMultiple(buf, 3,4)
  val list2 = SearchConsole.searchSingle(1)
  val list3 = SearchConsole.searchSingle(1)
  val wordCount = buf.toList.groupBy(w => w).mapValues(_.size)
  println(wordCount)

}
