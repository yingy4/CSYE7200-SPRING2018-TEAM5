package ingest


import org.apache.spark.rdd.RDD

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global



object  Functions {
  var count = 0
  case class Item(Color: String, Brand: String, Price: String, URL: String)

  def transfer(item: Item): Map[String, String] = item match {
    case Item(c, b, p, u) => Map("Color" -> c, "Brand" -> b, "Price" -> p, "URL" -> u)//TODO:add more attributes
  }

  def urlToItem(url: String): Seq[Item] = {
    import scala.xml.XML
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

  def futureToFutureTry(f: Future[Seq[Item]]): Future[Try[Seq[Item]]] = {
    val result = f.map(Success(_)).recover {
      case e: Exception => println(e);Failure(e)
    }
    result
  }

  def futureProcess(buf: ListBuffer[Item], urls: Seq[String]): Unit = {
    if (buf == null) {
      println("ListBuffer is not defined!")
      return
    }

    val listOfFuture = for(url <- urls) yield {
      Future(urlToItem(url))
    }
    val listOfFutureTrys = listOfFuture.map(futureToFutureTry(_))
    val futureListOfTrys = Future.sequence(listOfFutureTrys)
    val futureListOfSuccesses = futureListOfTrys.map(_.filter(_.isSuccess))
    futureListOfSuccesses onComplete {
      case Success(x) => val y = for(s <- x) yield {
          s match {
            case Success(l) => for (t <- l) buf += t
            case Failure(e) => println(e)
          }
        }
        count += 1
        println(count + "/82 complished")
      case _ => println("Whole FutureListFails")
    }
  }

  def noneFutureProcess(buf: ListBuffer[Item],urls: Seq[String]): Unit = {
    val listOfItemTrys = for(url <- urls) yield Try(urlToItem(url))
    for(t <- listOfItemTrys) t match {
      case Success(s) => for (l <- s) buf += l
      case Failure(e) => println(e)
    }

  }
  //given a List of Maps and a keyword, return a List of values marked by the keyword
  def itemToAttribute(se: Seq[Item], keyword: String): Seq[String] = {
    //traverse the Map and find the pair that has the keyword matched
    @tailrec def inner(m: Map[String, String]): Option[String] = m.toList match {
      case Nil => None
      case h :: t => h match {
        case (a, b) if a == keyword => if (b != "") Some(b) else None
        case _ => inner(t.toMap)
      }
    }
    //turn items in the List into Maps of the List
    def attributeOfItem(se: Seq[Item]): Seq[Map[String, String]] = {
      val seq = for (i <- se) yield transfer(i)
      seq
    }
    val seq = for (map <- attributeOfItem(se)) yield inner(map)
    seq.flatten
  }

  def sortResultDescending(list: List[String]): Seq[(String, Int)] = {
    list.groupBy(w => w).mapValues(_.size).toSeq.sortWith(_._2 > _._2)
  }

  def sortResultDecending(rdd: RDD[String]): RDD[(String, Int)] = {
    rdd.map(String => (String, 1)).reduceByKey(_+_, 1).map(item => item.swap).sortByKey(false, 1).map(item => item.swap)
  }

  def safeStringToDouble(str: String): Option[Double] = try {
    Some(str.toDouble)
  } catch {
    case e: NumberFormatException => None
  }

  def seperate(lines: RDD[String],separator: String) = {
    lines.flatMap(_.split(separator))
  }

  def mergeAndSort(list: List[(String, Int)]): List[(String, Int)] = {
    list.groupBy(_._1).map( kv => (kv._1, kv._2.map( _._2).sum ) ).toSeq.sortWith(_._2 > _._2).toList
  }

  def writeFile(fileName: String, list: List[String]) = {
    import java.io._
    val file = fileName + ".txt"
    val writer = new BufferedWriter(new FileWriter(file, true))
    for(x <- list){
      writer.append(x + ",")
    }
    writer.close()
  }
}
