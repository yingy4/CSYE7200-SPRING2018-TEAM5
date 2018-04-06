package retrieval

import ingest.SearchConsole

class sbtTest {
  def main(args: Array[String]) {
    val buf = scala.collection.mutable.ListBuffer.empty[Any]
    SearchConsole.SEARCH_KEYWORDS = args(0)

    println(buf.toList)
  }
}
