package retrieval

import scala.xml.XML

object ParseXML extends App {

    //val url = "http://webservices.amazon.com/onca/xml?AWSAccessKeyId=AKIAJVADVVC5WAOOAQHA&AssociateTag=scalaproject-20&ItemPage=3&Keywords=Trouser&Operation=ItemSearch&ResponseGroup=ItemAttributes&SearchIndex=All&Service=AWSECommerceService&Timestamp=2018-03-26T18%3A51%3A54Z&Signature=SYL9COvz9KLkIB5ep%2FpyA2Oql1mjYeTqPMfXxVauuyY%3D"
   // val snippet = new JavaCodeSnippet()

    val snippet = ScalaSnippet(AmazonClient.ACCESS_KEY_ID, AmazonClient.SECRET_KEY, AmazonClient.ENDPOINT)

    val url = snippet.generateUrl()
    val xml = XML.load(url)
    val itemOriginal = (xml \\ "Item")

    val seqItem = for (x <- itemOriginal) yield {
      val color = (x\\"Color").text
      val brand = (x\\"Brand").text
      val price = (x\\"FormattedPrice").text
      val item = Item(color, brand, price)
      item
    }

    val colorCollect = for(i <- seqItem) yield i match{
      case Item(a,b,c) if a != "" => a
      case _ => null
    }

    println(colorCollect.flatten(Option[String]))
}

case class Item(Color: String, Brand: String, Price: String)
