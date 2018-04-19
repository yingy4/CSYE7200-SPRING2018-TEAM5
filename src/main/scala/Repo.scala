package retrieval

import ingest.Functions
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.util.Random

object testCode extends App {
  val sc = new SparkContext("local[*]", "PopularElements")
  val ssc = new StreamingContext(sc, Seconds(1))

  Top_K_PricesTest().foreach(println)
  def Top_K_PricesTest(): Map[String, List[(String, Int)]] = {
    val price_RDD = sc.textFile("price.txt")
    val priceRDD = Functions.seperate(price_RDD, ",")
    val priceCombined = priceRDD
    val data = priceCombined.map(x => Functions.safeStringToDouble(x)).flatMap(x => x)

    def kNN_Spark(data: RDD[Double]): List[Double] = {
      val K = 5 //number of clusters
      val threshold = 100.0 // threshold to stop training

      val clusterlist = scala.collection.mutable.ListBuffer.empty[Double]
      //contain cluster centers as c[k]
      //generate k clusters randomly
      val datasize = data.count().toInt
      for (a <- 1 to K) {
        val rand = (new Random).nextInt(datasize - 1)
        clusterlist += data.zipWithIndex.map { case (k, v) => (v, k) }.lookup(rand)(0)
      }
      var gap = 0.0 // record difference between loss and previous loss
      var clusterlist_update = clusterlist.toList
      // initiate start clusterlist_start
      var loss_previous = 0.0
      //training process
      while (gap/datasize > threshold) {
        var loss = 0.0 // record loss
        val data_clustered // allocate data[i] to nearest cluster center c[k], return a tuple(c[k], data[i])
        = data.map(data_i => {
          var min = data.max
          var index = datasize - 1
          for (k <- 0 to clusterlist_update.size - 1)
            if ((math.abs(data_i - clusterlist_update(k)) < min)) // using abs loss
            {
              min = math.abs(data_i - clusterlist_update(k))
              index = k
            }
          loss += min
          (index + 1, data_i)
        })
        gap = loss_previous - loss
        loss_previous = loss
        val clusterlist_generated = data_clustered.groupBy(_._1).map(kv => (kv._1, kv._2.map(_._2).sum / kv._2.size)).sortBy(_._1).map(_._2).collect().toList //sum and avg data_clustered to get new clusterlist
        clusterlist_update = clusterlist_generated // substitute newly generated clusters to old ones
      }
      clusterlist_update
    }

    def vectorizedListOfDouble(priceDouble: List[Double]): List[(String, Int)] = {
      val interval = (priceDouble.max - priceDouble.min)/10
      val priceResult = priceDouble.groupBy(price => price match {
        case x if (priceDouble.max - 1*interval)<x & x<=(priceDouble.max) => "(%1.2f - %1.2f]".format(priceDouble.max - 1*interval, priceDouble.max)
        case x if (priceDouble.max - 2*interval)<x & x<=(priceDouble.max - 1*interval) => "(%1.2f - %1.2f]".format(priceDouble.max - 2*interval, priceDouble.max - 1*interval)
        case x if (priceDouble.max - 3*interval)<x & x<=(priceDouble.max - 2*interval) => "(%1.2f - %1.2f]".format(priceDouble.max - 3*interval, priceDouble.max - 2*interval)
        case x if (priceDouble.max - 4*interval)<x & x<=(priceDouble.max - 3*interval) => "(%1.2f - %1.2f]".format(priceDouble.max - 4*interval, priceDouble.max - 3*interval)
        case x if (priceDouble.max - 5*interval)<x & x<=(priceDouble.max - 4*interval) => "(%1.2f - %1.2f]".format(priceDouble.max - 5*interval, priceDouble.max - 4*interval)
        case x if (priceDouble.max - 6*interval)<x & x<=(priceDouble.max - 5*interval) => "(%1.2f - %1.2f]".format(priceDouble.max - 6*interval, priceDouble.max - 5*interval)
        case x if (priceDouble.max - 7*interval)<x & x<=(priceDouble.max - 6*interval) => "(%1.2f - %1.2f]".format(priceDouble.max - 7*interval, priceDouble.max - 6*interval)
        case x if (priceDouble.max - 8*interval)<x & x<=(priceDouble.max - 7*interval) => "(%1.2f - %1.2f]".format(priceDouble.max - 8*interval, priceDouble.max - 7*interval)
        case x if (priceDouble.max - 9*interval)<x & x<=(priceDouble.max - 8*interval) => "(%1.2f - %1.2f]".format(priceDouble.max - 9*interval, priceDouble.max - 8*interval)
        case x if (priceDouble.max - 10*interval)<=x & x<=(priceDouble.max - 9*interval) => "(%1.2f - %1.2f]".format(priceDouble.max - 10*interval, priceDouble.max - 9*interval)
        case _ => "other"
      }).mapValues(_.size).toSeq.sortWith(_._2 > _._2).toList //sort list result descending
      priceResult
    }

    val clusterlist_spark = kNN_Spark(data)
    val dataLocal = data.collect()
    val datasize = dataLocal.size
    val data_result = dataLocal.map(data_i => {
      var min = data.max
      var index = datasize - 1
      for(k <- 0 to clusterlist_spark.size-1)
        if((math.abs(data_i - clusterlist_spark(k))<min)) {
          min = math.abs(data_i - clusterlist_spark(k))
          index = k
        }
      (index+1, data_i)
    }).groupBy(_._1).map(kv => (kv._1, kv._2.map(_._2).toList)).toSeq.sortWith(_._1 < _._1)
    val result_vectorized = for(l <- data_result) yield l match {
      case (a, b) => Some(clusterlist_spark(a-1), vectorizedListOfDouble(b))
      case _ => None
    }
    val result = result_vectorized.flatten.sortWith(_._1>_._1)
    var RL:Map[String,List[(String, Int)]]=
      Map("Very High"->result(0)._2,
        "High"->result(1)._2,
        "Medium"->result(2)._2,
        "Low"->result(3)._2,
        "Very Cheap"->result(4)._2)
    RL
  }
}
//  //store every single result into buf
//  val buf = scala.collection.mutable.ArrayBuffer.empty[Any]
//
//  val url = "http://webservices.amazon.com/onca/xml?AWSAccessKeyId=AKIAJVADVVC5WAOOAQHA&AssociateTag=scalaproject-20&ItemPage=1&Keywords=Trouser&Operation=ItemSearch&ResponseGroup=ItemAttributes&SearchIndex=All&Service=AWSECommerceService&Timestamp=2018-03-29T21%3A15%3A53Z&Signature=nuPYqjEmgc1nsGjF1xWVbhVSQVcejsY6tKgIu1C%2FV5U%3D"
//  //use this to generate url
//  val urlX = AmazonClient.generateUrl(1,3)
//  //code by Yichuan
//  val li =  AmazonClient.generateUrl(1,2)
//  Thread.sleep(1000)
//  println(li)
////  val items = urlToItem(urlX)
////  val colors = itemToAttribute(items, "Color")
////  val brands = itemToAttribute(items, "Brand")
////  val prices = itemToAttribute(items, "FormattedPrice")
////  val urls = List(urlX)
//
//  case class Item(Color: String, Brand: String, Price: String)
//
//  def aaa(urls: Seq[String]): Unit = {
//    val listOfFuture = for(url <- urls) yield Future(urlToItem(url))
//    val futureOfList = Future.sequence(listOfFuture)
//    futureOfList onComplete {
//      case Success(x) => val y = for(listOfItems <- x) yield {
//        itemToAttribute(listOfItems,"Color")}
//        for(s <- y.flatten) yield {
//          buf += s
//        }
//        println(y.flatten)
//
//        //TODO: intrigue another url-generating process
//    }
//  }
//
//  //give a URL, turn it into 10 List of items that the URL contains
//  def urlToItem(url: String): Seq[Item] = {
//    import scala.xml.XML
//    val xml = XML.load(url)
//    val itemInXML = xml \\ "Item"
//    val seqItem = for (x <- itemInXML) yield {
//      val color = (x \\ "Color").text
//      val brand = (x \\ "Brand").text
//      val price = (x \\ "FormattedPrice").text
//      val item = Item(color, brand, price)
//      item
//    }
//    seqItem
//  }
//
//  //TODO:Combine read-in URL and turn it to Map into one function (deal with Item construction)
//  def transfer(item: Item): Map[String, Any] = item match {
//    case Item(c, b, p) => Map("Color" -> c, "Brand" -> b, "Price" -> p)
//  }
//
//  //given a List of Maps and a keyword, return a List of values marked by the keyword
//  def itemToAttribute(se: Seq[Item], keyword: String): Seq[Any] = {
//    //traverse the Map and find the pair that has the keyword matched
//    @tailrec def inner(m: Map[String, Any]): Option[Any] = m.toList match {
//      case Nil => None
//      case h :: t => h match {
//        case (a, b) if a == keyword => if (b != "") Some(b) else None
//        case _ => inner(t.toMap)
//      }
//    }
//
//    //turn items in the List into Maps of the List
//    def attributeOfItem(se: Seq[Item]): Seq[Map[String, Any]] = {
//      val seq = for (i <- se) yield transfer(i)
//      seq
//    }
//    val seq = for (map <- attributeOfItem(se)) yield inner(map)
//    seq.flatten
//  }
//}
//
//
////  def timer[A](blockOfCode: => A) = {
////    val starttime=System.nanoTime              //系统纳米时间
////    val result=blockOfCode
////    val endtime=System.nanoTime
////    val delta=endtime-starttime
////    (result,delta/1000000d)
////  }
////
////
////val (result2,time2)=timer(UUU(url, url2, url3))
////println(time2)
////val (result3,time3)=timer(UUU2(url, url2, url3))
////println(time3)
////val (result1,time1)=timer(UUU(url, url2, url3))
////println(time1)
////val (result4,time4)=timer(UUU2(url, url2, url3))
////println(time4)
////val (result0,time0)=timer(UUU(url, url2, url3))
////println(time0)
////val (result00,time00)=timer(UUU(url, url2, url3))
////println(time00)
////val (result5,time5)=timer(UUU2(url, url2, url3))
////println(time5)
////
//////  val (result11,time11)=timer(UUU6(url, url2, url3, url4, url5, url6))
//////  println(time11)
//////  val (result13,time13)=timer(UUU62(url, url2, url3, url4, url5, url6))
//////  println(time13)
//////  val (result12,time12)=timer(UUU62(url, url2, url3, url4, url5, url6))
//////  println(time12)
////
////
////def UUU(a:String, b:String, c:String): Unit = {
////  import scala.xml.XML
////  import scala.concurrent.duration._
////  import scala.concurrent.ExecutionContext.Implicits.global
////  val co1 = Future(XML.load(a))
////  val co2 = Future(XML.load(b))
////  val co3 = Future(XML.load(c))
////  val f = for {
////  result1 <- co1
////  result2 <- co2
////  result3 <- co3
////} yield {result1.toList:::result2.toList:::result3.toList}
////  Await.result(f,450 millis)
////  println(f)
////}
////  def UUU2(a:String, b:String, c:String): Unit = {
////  import scala.xml.XML
////  val co1 = XML.load(a)
////  val co2 = XML.load(b)
////  val co3 = XML.load(c)
////  val f = for {
////  result1 <- co1
////  result2 <- co2
////  result3 <- co3
////} yield {result1.toList:::result2.toList:::result3.toList}
////  println(f)
////}
////
////  def UUU6(a:String, b:String, c:String, d:String, e: String, g: String): Unit = {
////  import scala.xml.XML
////  import scala.concurrent.duration._
////  import scala.concurrent.ExecutionContext.Implicits.global
////  val co1 = Future(XML.load(a))
////  val co2 = Future(XML.load(b))
////  val co3 = Future(XML.load(c))
////  val co4 = Future(XML.load(d))
////  val co5 = Future(XML.load(e))
////  val co6 = Future(XML.load(g))
////  val f = for {
////  result1 <- co1
////  result2 <- co2
////  result3 <- co3
////  result4 <- co4
////  result5 <- co5
////  result6 <- co6
////} yield {result1.toList:::result2.toList:::result3.toList:::result4.toList:::result5.toList:::result6.toList}
////  Await.result(f,1500 millis)
////  println(f)
////}
////  def UUU62(a:String, b:String, c:String, d:String, e: String, g: String): Unit = {
////  import scala.xml.XML
////  val co1 = XML.load(a)
////  val co2 = XML.load(b)
////  val co3 = XML.load(c)
////  val co4 = XML.load(d)
////  val co5 = XML.load(e)
////  val co6 = XML.load(g)
////  val f = for {
////  result1 <- co1
////  result2 <- co2
////  result3 <- co3
////  result4 <- co4
////  result5 <- co5
////  result6 <- co6
////} yield {result1.toList:::result2.toList:::result3.toList:::result4.toList:::result5.toList:::result6.toList}
////  println(f)
////}
//////  import scala.xml.XML
//////
//////  val xml = XML.load(url)
//////  val itemOriginal = (xml \\ "Item")
//////
//////  val seqItem = for (x <- itemOriginal) yield {
//////    val color = (x \\ "Color").text
//////    val brand = (x \\ "Brand").text
//////    val price = (x \\ "FormattedPrice").text
//////    val item = Item(color, brand, price)
//////    item
//////  }
//////
//////  val colorCollect1 = for (i <- seqItem) yield i match {
//////    case Item(a, b, c) if a != "" => a
//////    case _ => null
