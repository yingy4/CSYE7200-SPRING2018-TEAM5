package ingest


import org.apache.spark.rdd.RDD
import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.XML
import java.io._


/**
  * Created by Team5 on 4/14/2018.
  * reference:
  *
  */

object  Functions {
  case class Item(color: String, brand: String, price: String, url: String)

  /**
    * parameters: an Item
    * description: case match an Item, Item(color, brand, price, url) => Map("Color"->color, "Brand"->brand, "Price"->price, "URL"->url)
    */

  def transfer(item: Item): Map[String, String] = item match {
    case Item(c, b, p, u) => Map("Color" -> c, "Brand" -> b, "Price" -> p, "URL" -> u)
  }

  /**
    * parameters: an URL
    * description: load URL to XML, extract features and wrapped into Items for the URL(at most 10 Items according to the response from API)
    */

  def urlToItem(url: String): Seq[Item] = {
    val xml = XML.load(url)
    val itemInXML = xml \\ "Item"
    val seqItem = for (x <- itemInXML) yield {
      val color = (x \\ "Color").text
      val brand = (x \\ "Brand").text
      val price = (x \\ "FormattedPrice").text
      val url = (x \\ "DetailPageURL").text
      val item = Item(color, brand, price, url)
      item
    }
    seqItem
  }

  /**
    * parameters: Future[Item, Item, ...]
    * description: turn to Future[Try[Item], Try[Item], ...]
    */

  def futureToFutureTry(f: Future[Seq[Item]]): Future[Try[Seq[Item]]] = {
    val result = f.map(Success(_)).recover {
      case e: Exception => println(e);Failure(e)
    }
    result
  }

  /**
    * parameters: ListBuffer[Item, Item, ...], Seq[URL, URL, ...]
    * description: parallelly extracting Items in URLS and add to buf
    */

  def futureProcess(buf: ListBuffer[Item], urls: Seq[String]): Unit = {
    //check if buf is null
//    if (buf == null) {
//      println("ListBuffer is not defined!")
//      return
//    }
    //for every URL in Seq[URLs], generate 10 Items using Future to wrap
    val listOfFuture = for(url <- urls) yield {
      Future(urlToItem(url))
    }
    //list of Future[Trys]
    val listOfFutureTrys = listOfFuture.map(futureToFutureTry(_))
    //flip to Future of list[Trys], and filter out Failure
    val futureListOfTrys = Future.sequence(listOfFutureTrys)
    val futureListOfSuccesses = futureListOfTrys.map(_.filter(_.isSuccess))
    //Future onComplete is a Try on [Try[Item, Item, ...], Try[Item, Item, ...], ...]
    futureListOfSuccesses onComplete {
      case Success(x) => // get ride of first Try, get [Try[Item, Item, ...], Try[Item, Item, ...], ...] as x
          for(s <- x) //get Try[Item, Item, ...] as s
          yield {
          s match {
            case Success(l) => for (t <- l) buf += t //get Success[Item, Item, ...] as l, add every Item to buf
            case Failure(e) => println(e) // java.io.IOException: Server returned HTTP response code: 503 for URL: ...
          }
        }
      case _ => println("Whole FutureListFails")
    }
  }

  //none parallel of the function upside
  def noneFutureProcess(buf: ListBuffer[Item],urls: Seq[String]): Unit = {
    val listOfItemTrys = for(url <- urls) yield Try(urlToItem(url))
    for(t <- listOfItemTrys) t match {
      case Success(s) => for (l <- s) buf += l
      case Failure(e) => println(e)
    }

  }

  /**
    * parameters: Seq[Item, Item, ...], keyword
    * description: extract feature according to the keyword in every Item in the list
    */

  def itemToAttribute(se: Seq[Item], keyword: String): Seq[String] = {
    //traverse the Map and find the pair that has the keyword matched
    @tailrec def inner(m: Map[String, String]): Option[String] = m.toList match {
      case Nil => None
      case h :: t => h match {
        case (a, b) if a == keyword => if (b != "") Some(b) else None
        case _ => inner(t.toMap)
      }
    }
    //for all Item, turn every one into a Map["Color"->color, "Brand"->brand, "Price"->price, "URL"->url]
    def attributeOfItem(se: Seq[Item]): Seq[Map[String, String]] = {
      val seq = for (i <- se) yield transfer(i)
      seq
    }
    //traverse a list of Map["Color"->color, "Brand"->brand, "Price"->price, "URL"->url], find the key that matches keyword and add the value to seq
    val seq = for (map <- attributeOfItem(se)) yield inner(map)
    seq.flatten // get ride of Option: Some(), or None
  }

  // word-count and sort descending
  def sortResultDescending(list: List[String]): Seq[(String, Int)] = {
    list.groupBy(w => w).mapValues(_.size).toSeq.sortWith( _._2 > _._2)
  }

  // on RDD
  def sortResultDecending(rdd: RDD[String]): RDD[(String, Int)] = {
    rdd.map(str => (str, 1)).reduceByKey(_+_, 1).map(item => item.swap).sortByKey(false, 1).map(item => item.swap)
  }

  //safely parse String to Double using Option to avoid not-formatted error
  def safeStringToDouble(str: String): Option[Double] = Some(str.toDouble)


  //separate RDD[String] by separator
  def seperate(lines: RDD[String],separator: String) = {
    lines.flatMap(_.split(separator))
  }

  //merge two (String, Int) by key and add Int values
  def mergeAndSort(list: List[(String, Int)]): List[(String, Int)] = {
    list.groupBy(_._1).map( kv => (kv._1, kv._2.map( _._2).sum ) ).toSeq.sortWith(_._2 > _._2).toList
  }

  //write list to .txt
  def writeFile(fileName: String, list: List[String]) = {
    val file = fileName + ".txt"
    val writer = new BufferedWriter(new FileWriter(file, true))
    for(x <- list){
      writer.append(x + ",")
    }
    writer.close()
  }
}
