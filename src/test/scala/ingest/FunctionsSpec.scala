package ingest


import ingest.Functions.Item
import org.apache.spark
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Future
import scala.util.{Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

class FunctionsSpec extends FlatSpec with Matchers{
  behavior of "transfer"
  it should """map an Item(red, Apple, $1000, www.apple.com) to a Map("Color"->"red", "Brand"->"Apple", "Price"->"$1000", "URL"->"www.apple.com")""" in{
    val c = "red"
    val b = "Apple"
    val p = "$1000"
    val u = "www.apple.com"
    val item = Item(c,b,p,u)
    val imap = Map("Color" -> c, "Brand" -> b, "Price" -> p, "URL" -> u)
    val test = Functions.transfer(item)
    test shouldBe imap
  }

  //The urlToItem is unable to test because the url will exipre in 15 minutes
//  behavior of "urlToItem"
//  it should """load an url to item""" in{
//    val url="<ItemSearchResponse xmlns=\"http://webservices.amazon.com/AWSECommerceService/2011-08-01\"><HTTPHeaders><Header Name=\"UserAgent\" Value=\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36\"/></HTTPHeaders><Item><Color>red</Color><Brand>Apple</Brand><FormattedPrice>$1000</FormattedPrice><DetailPageURL>www.apple.com</DetailPageURL></Item></ItemSearchResponse>"
//    val c = "red"
//    val b = "Apple"
//    val p = "$1000"
//    val u = "www.apple.com"
//    val item = Item(c,b,p,u)
//    val items = Seq(item)
//    val test = Functions.urlToItem(url)
//    test shouldBe items
//  }

  behavior of "futureToFutureTry"
  it should """turn Future[Item, Item, ...] into Future[Try[Item], Try[Item], ...]"""in{
    val c = "red"
    val b = "Apple"
    val p = "$1000"
    val u = "www.apple.com"
    val item = Item(c,b,p,u)
    val items = Seq(item)
    val itemts = Try(items)
    val itemfs = Future(items)
    val itemfts = Future(itemts)
    val test = Functions.futureToFutureTry(itemfs)
    test should matchPattern{
      case itemfts=>
    }
  }

  behavior of "itemToAttribute"
  it should """extract feature according to the keyword in every Item in the list""" in{
    val c = "red"
    val cc = "black"
    val b = "Apple"
    val p = "$1000"
    val u = "www.apple.com"
    val item1 = Item(c,b,p,u)
    val item2 = Item(cc,b,p,u)
    val keyword = "Color"
    val items = Seq(item1, item2)
    val ks = Seq("red", "black")
    val test = Functions.itemToAttribute(items, keyword)
    test shouldBe ks
  }

  behavior of "sortResultDescending"
  it should """word-count and sort descending on List[String]"""in{
    val wl = List("Red","Black","Red","Green","Brown","Red","Black")
    val ssi = Seq(("Red",3),("Black",2),("Brown",1),("Green",1))
    val test = Functions.sortResultDescending(wl)
    test shouldBe ssi
  }

  behavior of "sortResultDescending on RDD"
  it should """word-count and sort descending on RDD[String]"""in{
    val sc = new spark.SparkContext("local[*]", "PopularElements")
    val wl = sc.parallelize(Seq("Red","Black","Red","Green","Brown","Red","Black"))
    val rddsi = sc.parallelize(Seq(("Red",3),("Black",2),("Brown",1),("Green",1)))
    val test = Functions.sortResultDecending(wl).collect()
    test shouldBe rddsi.collect()
  }

  behavior of "safeStringToDouble"
  it should """safely parse String to Double using Option to avoid not-formatted error"""in{
    val num = "123.4"
    val test = Functions.safeStringToDouble(num)
    test should matchPattern{
      case Some(123.4) =>
    }
  }

  behavior of "mergeAndSort"
  it should """merge two (String, Int) by key, add Int values and sort"""in{
    val l1 = List(("Red",2),("Black",1),("Brown",1),("Black",1),("Red",1))
    val l1s = List(("Red",3), ("Black",2), ("Brown",1))
    val test = Functions.mergeAndSort(l1)
    test shouldBe l1s
  }
}
