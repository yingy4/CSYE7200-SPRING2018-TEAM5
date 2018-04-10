package retrieval

import ingest.Functions.Item
import ingest.{Functions, SearchConsole}



object ParseTest extends App {
  val buf = scala.collection.mutable.ListBuffer.empty[Item]
  SearchConsole.SEARCH_KEYWORDS = "Trouser"
  SearchConsole.RESPONSE_TIME_MILLI = 1000
  SearchConsole.ASYN = true
  SearchConsole.searchAllCategoriesLinear(buf)


  //  SearchConsole.searchMultiple(buf, 1,2 )
  //  SearchConsole.searchWWW(buf)



  val colorsLower = UseCases.getColors(buf)

  val brandsUpper = UseCases.getBrands(buf)

  val pricesDouble = UseCases.getPrices(buf)
//TODO: When the list grows too big, we might need Map-Reduce to process it; generating new items and added to buf while reading buf using RDD
  println(Functions.sortResultAscending(colorsLower))
  println(Functions.sortResultAscending(brandsUpper))
  println(pricesDouble)
  println(buf.toList.size)

}
