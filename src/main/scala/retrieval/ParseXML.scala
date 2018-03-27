package retrieval

import scala.annotation.tailrec
import scala.util.Try

object ParseXML extends App {

  val url = "http://webservices.amazon.com/onca/xml?AWSAccessKeyId=AKIAJVADVVC5WAOOAQHA&AssociateTag=scalaproject-20&ItemPage=3&Keywords=Trouser&Operation=ItemSearch&ResponseGroup=ItemAttributes&SearchIndex=All&Service=AWSECommerceService&Timestamp=2018-03-27T02%3A33%3A07Z&Signature=sedwj7hzhddzLrdc75XbrMsX4Cc3W40IfaybcJLbcHI%3D"


//  import scala.xml.XML
//
//  val xml = XML.load(url)
//  val itemOriginal = (xml \\ "Item")
//
//  val seqItem = for (x <- itemOriginal) yield {
//    val color = (x \\ "Color").text
//    val brand = (x \\ "Brand").text
//    val price = (x \\ "FormattedPrice").text
//    val item = Item(color, brand, price)
//    item
//  }
//
//  val colorCollect1 = for (i <- seqItem) yield i match {
//    case Item(a, b, c) if a != "" => a
//    case _ => null
//
  val se =itemToAttribute(urlToItem(url),"Color")
  println(se)

  case class Item(Color: String, Brand: String, Price: String)
  //give a URL, turn it into 10 List of items that the URL contains
  def urlToItem(url: String): Seq[Item] = {
    import scala.xml.XML
    val xml = XML.load(url)
    val itemInXML = xml\\"Item"
    val seqItem = for (x <- itemInXML) yield {
      val color = (x \\ "Color").text
      val brand = (x \\ "Brand").text
      val price = (x \\ "FormattedPrice").text
      val item = Item(color, brand, price)
      item
    }
    seqItem
  }

  def transfer(item: Item): Map[String, Any] = item match {
    case Item(c,b,p) => Map("Color"->c, "Brand"->b, "Price"->p)
  }
  //given a List of Maps and a keyword, return a List of values marked by the keyword
  def itemToAttribute(se: Seq[Item], keyword: String): Seq[Any] ={
    //traverse the Map and find the pair that has the keyword matched
    @tailrec def inner(m: Map[String, Any]): Option[Any] = m.toList match {
      case Nil => None
      case h::t => h match {
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
//      case List(s, v, p)  => s match {
//        case (a,b) if a == keyword => if (b != "") Some(b) else None
//        case _ => v match {
//          case (a,b) if a == keyword => if (b != "") Some(b) else None
//          case _ => p match {
//            case (a,b) if a == keyword => if (b != "") Some(b) else None
//            case _ => None
//          }
//        }
//      }
//      case _ => None
//  }
    seq.flatten
  }
}
