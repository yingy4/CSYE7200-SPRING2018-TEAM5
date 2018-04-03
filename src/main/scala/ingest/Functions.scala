package ingest

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Success


object  Functions {
  case class Item(Color: String, Brand: String, Price: String)

  def transfer(item: Item): Map[String, Any] = item match {
    case Item(c, b, p) => Map("Color" -> c, "Brand" -> b, "Price" -> p)//TODO:add more attributes
  }

  def urlToItem(url: String): Seq[Item] = {
    import scala.xml.XML
    val xml = XML.load(url)
    val itemInXML = xml \\ "Item"
    val seqItem = for (x <- itemInXML) yield {
      val color = (x \\ "Color").text
      val brand = (x \\ "Brand").text
      val price = (x \\ "FormattedPrice").text
      val item = Item(color, brand, price)
      item
    }
    seqItem
  }

  def futureProcess(buf: ListBuffer[Any], urls: Seq[String], keyword: String): Unit = {
    val listOfFuture = for(url <- urls) yield Future(urlToItem(url))
    val futureOfList = Future.sequence(listOfFuture)
    futureOfList onComplete {
      case Success(x) => val y = for(listOfItems <- x) yield {
        itemToAttribute(listOfItems,keyword)}
        for(s <- y.flatten) yield {
          buf += s
        }
      case _ =>
    }
    //TODO: Using to test: remember to delete!
  }

  def noneFutureProcess(urls: Seq[String], keyword: String): List[Any] = {
    val listOfItem = for(url <- urls) yield itemToAttribute(urlToItem(url),keyword)
    listOfItem.flatten.toList
  }
  //given a List of Maps and a keyword, return a List of values marked by the keyword
  def itemToAttribute(se: Seq[Item], keyword: String): Seq[Any] = {
    //traverse the Map and find the pair that has the keyword matched
    @tailrec def inner(m: Map[String, Any]): Option[Any] = m.toList match {
      case Nil => None
      case h :: t => h match {
        case (a, b) if a == keyword => if (b != "") Some(b) else None
        case _ => inner(t.toMap)
      }
    }
    //turn items in the List into Maps of the List
    def attributeOfItem(se: Seq[Item]): Seq[Map[String, Any]] = {
      val seq = for (i <- se) yield transfer(i)
      seq
    }
    val seq = for (map <- attributeOfItem(se)) yield inner(map)
    seq.flatten
  }
}
