package retrieval

import ingest.{Functions, SearchConsole}
import ingest.Functions.Item
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Seconds, StreamingContext}
import retrieval.ParseTest.color_RDD

object test2 extends  App {
//  import java.io._
//
//  val file = "whatever.txt"
//  val writer = new BufferedWriter(new FileWriter(file))
//  val i1 = Item("ad", "awd", "123")
//  val i2 = Item("gd", "qwe", "324")
//  List(i1+"\n",i2+"\n").foreach(writer.write)
//  writer.close()
//
//  val writer2 = new BufferedWriter(new FileWriter(file, true))
//  List("on"+",","ji"+",","hi"+",").foreach(writer2.write)
//  writer2.close()
//
//  val PersonRegex = "Item\\((.*),(.*),(.*)\\)".r
//
//  object PersonString {
//    def unapply(str: String): Option[Item] = str match {
//      case PersonRegex(color, brand, price) => Some(Item(color, brand, price))
//      case _ => None
//    }
//  }
//
//  val t = PersonString.unapply("Item(asd,gd,34)") match {
//    case Some(s) => s.Color
//    case _ =>
//  }
//  println(t)
//
//  val sc = new SparkContext("local[*]", "PopularElements")
//  val ssc = new StreamingContext(sc, Seconds(1))
//  val item_RDD = sc.textFile("whatever.txt")
//  val itemOp = Functions.seperate(item_RDD, "\n").map(x => PersonString.unapply(x))
//  itemOp.collect().flatten.foreach(println)

  val buf = scala.collection.mutable.ListBuffer.empty[Item]
  SearchConsole.SEARCH_KEYWORDS = "Trouser"
  SearchConsole.RESPONSE_TIME_MILLI = 100
  SearchConsole.ASYN = true
  SearchConsole.searchAllCategoriesLinear(buf)

  buf.toList.foreach(println)
}
