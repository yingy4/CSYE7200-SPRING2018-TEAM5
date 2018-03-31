package retrieval

import scala.xml.XML

object ParseTest extends App {
  val urlX = AmazonClient.generateUrl(1)

  val xml = XML.load(urlX)
  val itemOriginal = (xml \\ "Item")

  case class Item(Color: String, Brand: String, Price: String)

  val seqItem = for (x <- itemOriginal) yield {
    val color = (x \\ "Color").text
    val brand = (x \\ "Brand").text
    val price = (x \\ "FormattedPrice").text
    val item = Item(color, brand, price)
    item
  }

  val colorCollect1 = for (i <- seqItem) yield i match {
    case Item(a, b, c) if a != "" => a
    case _ => null
  }

  print(colorCollect1.toSeq)
}
