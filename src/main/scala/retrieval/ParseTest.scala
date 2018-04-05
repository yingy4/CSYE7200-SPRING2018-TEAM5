package retrieval

import ingest.SearchConsole



object ParseTest extends App {
  SearchConsole.SEARCH_KEYWORDS = "Trouser"
  SearchConsole.ATTRIBUTE = "Color"

  val buf = scala.collection.mutable.ListBuffer.empty[Any]
  SearchConsole.searchYYY(buf)
  Thread.sleep(1000)
  val wordCount = buf.toList.groupBy(w => w).mapValues(_.size)
  println(wordCount)

}
