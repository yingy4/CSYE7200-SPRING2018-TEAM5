package retrieval







import scala.annotation.tailrec
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global


object ParseXML extends App {
  //store every single result into buf
  val buf = scala.collection.mutable.ArrayBuffer.empty[Any]

  val url = "http://webservices.amazon.com/onca/xml?AWSAccessKeyId=AKIAJVADVVC5WAOOAQHA&AssociateTag=scalaproject-20&ItemPage=1&Keywords=Trouser&Operation=ItemSearch&ResponseGroup=ItemAttributes&SearchIndex=All&Service=AWSECommerceService&Timestamp=2018-03-29T21%3A15%3A53Z&Signature=nuPYqjEmgc1nsGjF1xWVbhVSQVcejsY6tKgIu1C%2FV5U%3D"
  //use this to generate url
  val snippet = ScalaSnippet(AmazonClient.ACCESS_KEY_ID, AmazonClient.SECRET_KEY, AmazonClient.ENDPOINT)
  val urlX = snippet.generateUrl()
  //code by Yichuan
  def kkk(): Unit = {
    val l = new Array[String](10)
    for (i <- 0 to 9) {
      val url = snippet.generateUrl()
      l(i) = url
    }
    aaa(l)
  }
  kkk()
  Thread.sleep(1000)
  kkk()
  Thread.sleep(1000)
  println(buf)
//  val items = urlToItem(urlX)
//  val colors = itemToAttribute(items, "Color")
//  val brands = itemToAttribute(items, "Brand")
//  val prices = itemToAttribute(items, "FormattedPrice")
//  val urls = List(urlX)

  case class Item(Color: String, Brand: String, Price: String)
  def aaa(urls: Seq[String]): Unit = {
    val listOfFuture = for(url <- urls) yield Future(urlToItem(url))
    val futureOfList = Future.sequence(listOfFuture)
    futureOfList onComplete {
      case Success(x) => val y = for(listOfItems <- x) yield {
        itemToAttribute(listOfItems,"Color")}
        for(s <- y.flatten) yield {
          buf += s
        }
        println(y.flatten)

        //TODO: intrigue another url-generating process
    }
  }

  //give a URL, turn it into 10 List of items that the URL contains
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

  //TODO:Combine read-in URL and turn it to Map into one function (deal with Item construction)
  def transfer(item: Item): Map[String, Any] = item match {
    case Item(c, b, p) => Map("Color" -> c, "Brand" -> b, "Price" -> p)
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


//  def timer[A](blockOfCode: => A) = {
//    val starttime=System.nanoTime              //系统纳米时间
//    val result=blockOfCode
//    val endtime=System.nanoTime
//    val delta=endtime-starttime
//    (result,delta/1000000d)
//  }
//
//
//val (result2,time2)=timer(UUU(url, url2, url3))
//println(time2)
//val (result3,time3)=timer(UUU2(url, url2, url3))
//println(time3)
//val (result1,time1)=timer(UUU(url, url2, url3))
//println(time1)
//val (result4,time4)=timer(UUU2(url, url2, url3))
//println(time4)
//val (result0,time0)=timer(UUU(url, url2, url3))
//println(time0)
//val (result00,time00)=timer(UUU(url, url2, url3))
//println(time00)
//val (result5,time5)=timer(UUU2(url, url2, url3))
//println(time5)
//
////  val (result11,time11)=timer(UUU6(url, url2, url3, url4, url5, url6))
////  println(time11)
////  val (result13,time13)=timer(UUU62(url, url2, url3, url4, url5, url6))
////  println(time13)
////  val (result12,time12)=timer(UUU62(url, url2, url3, url4, url5, url6))
////  println(time12)
//
//
//def UUU(a:String, b:String, c:String): Unit = {
//  import scala.xml.XML
//  import scala.concurrent.duration._
//  import scala.concurrent.ExecutionContext.Implicits.global
//  val co1 = Future(XML.load(a))
//  val co2 = Future(XML.load(b))
//  val co3 = Future(XML.load(c))
//  val f = for {
//  result1 <- co1
//  result2 <- co2
//  result3 <- co3
//} yield {result1.toList:::result2.toList:::result3.toList}
//  Await.result(f,450 millis)
//  println(f)
//}
//  def UUU2(a:String, b:String, c:String): Unit = {
//  import scala.xml.XML
//  val co1 = XML.load(a)
//  val co2 = XML.load(b)
//  val co3 = XML.load(c)
//  val f = for {
//  result1 <- co1
//  result2 <- co2
//  result3 <- co3
//} yield {result1.toList:::result2.toList:::result3.toList}
//  println(f)
//}
//
//  def UUU6(a:String, b:String, c:String, d:String, e: String, g: String): Unit = {
//  import scala.xml.XML
//  import scala.concurrent.duration._
//  import scala.concurrent.ExecutionContext.Implicits.global
//  val co1 = Future(XML.load(a))
//  val co2 = Future(XML.load(b))
//  val co3 = Future(XML.load(c))
//  val co4 = Future(XML.load(d))
//  val co5 = Future(XML.load(e))
//  val co6 = Future(XML.load(g))
//  val f = for {
//  result1 <- co1
//  result2 <- co2
//  result3 <- co3
//  result4 <- co4
//  result5 <- co5
//  result6 <- co6
//} yield {result1.toList:::result2.toList:::result3.toList:::result4.toList:::result5.toList:::result6.toList}
//  Await.result(f,1500 millis)
//  println(f)
//}
//  def UUU62(a:String, b:String, c:String, d:String, e: String, g: String): Unit = {
//  import scala.xml.XML
//  val co1 = XML.load(a)
//  val co2 = XML.load(b)
//  val co3 = XML.load(c)
//  val co4 = XML.load(d)
//  val co5 = XML.load(e)
//  val co6 = XML.load(g)
//  val f = for {
//  result1 <- co1
//  result2 <- co2
//  result3 <- co3
//  result4 <- co4
//  result5 <- co5
//  result6 <- co6
//} yield {result1.toList:::result2.toList:::result3.toList:::result4.toList:::result5.toList:::result6.toList}
//  println(f)
//}
////  import scala.xml.XML
////
////  val xml = XML.load(url)
////  val itemOriginal = (xml \\ "Item")
////
////  val seqItem = for (x <- itemOriginal) yield {
////    val color = (x \\ "Color").text
////    val brand = (x \\ "Brand").text
////    val price = (x \\ "FormattedPrice").text
////    val item = Item(color, brand, price)
////    item
////  }
////
////  val colorCollect1 = for (i <- seqItem) yield i match {
////    case Item(a, b, c) if a != "" => a
////    case _ => null