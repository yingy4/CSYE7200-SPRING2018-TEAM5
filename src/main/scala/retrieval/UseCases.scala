package retrieval

import ingest.Functions.Item
import ingest.{Functions, SearchConsole}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Seconds, StreamingContext}
import scala.collection.mutable.ListBuffer
import scala.util.Random

/**
  * Created by Team5 on 4/14/2018.
  *
  *
  */

object UseCases {
  //set Console
  //set whether CACHE to .txt
  val cacheColors = true
  val cacheBrands = true
  val cachePrices = true

  //set spark context
  val sc = new SparkContext("local[*]", "PopularElements")
  val ssc = new StreamingContext(sc, Seconds(1))

  //get data from running APP and reading .txt
  def Top_K_Colors(k: Int, buf: ListBuffer[Item]): List[(String, Int)] = {
    val colorsLower = getLocalColors(buf, cacheColors)//a list of color unsorted(what we get from running App one time)
    val color2RDD = sc.textFile("color.txt")
    val colorRDD =  Functions.sortResultDecending(Functions.seperate(color2RDD, ",")).
      filter{case(_, count ) => count > 10}//a RDD of sorted color tuples filtered out less than and equal to 10
    val colorSeq = Functions.sortResultDescending(colorsLower).
      filter{case(_, count ) => count > 1}//a seq of sorted color tuples filtered out less than and equal to 1
    val colorCombined =  colorRDD.collect().toList ++ colorSeq.toList
    val colorResult =  Functions.mergeAndSort(colorCombined)
    colorResult.take(k)
  }

  def Top_K_Brands(k: Int, buf: ListBuffer[Item]): List[(String, Int)] = {
    val brandsUpper = getLocalBrands(buf, cacheBrands)
    val brand2RDD = sc.textFile("brand.txt")
    val brandRDD = Functions.sortResultDecending(Functions.seperate(brand2RDD, ",")).
      filter{case(_, count ) => count > 10}
    val brandSeq = Functions.sortResultDescending(brandsUpper).
      filter{case(_, count ) => count > 1}
    val brandCombined =  brandRDD.collect().toList ++ brandSeq.toList
    val brandResult =  Functions.mergeAndSort(brandCombined)
    brandResult.take(k)
  }

  def Top_k_p(buf: ListBuffer[Item]): List[(String,Int)] = {
    val pricesString = getLocalPricesString(buf, cachePrices)
    val price2RDD = sc.textFile("price.txt")
    val priceRDD =  Functions.seperate(price2RDD, ",")
    val priceSeq = pricesString
    val priceCombined = priceRDD.collect().toList ++ priceSeq
    val priceDouble = priceCombined.map(w => Functions.safeStringToDouble(w)).flatten
    vectorizedListOfDouble(priceDouble)
  }

  /**
    * KNN: k-means using original data points as initial cluster centers
    * description: unsupervised learning algorithm(no need to tag). iteratively train the model until loss less than a threshold. automatically cluster all data points to k clusters
    * step:
    * 1.randomly choose K points in data set as starting cluster c[k] where 1<=k<=K
    * 2.for every data[i] in data_all, calculate distance between data[i] and every cluster c[k](k from 1 to K), K*data_all.size times computation in total
    *   assign data[i] to its nearest cluster c[k], denoted as (c[k], data[i])
    * 3.for all data assigned to same c[k], sum and average those data to get the new value for c[k]: (c[k], data[i]) groupBy key and reduce (sum&average value)
    * 4.if stop condition meets, stop; otherwise using newly generated clusters in step 3 to repeat step 2 and step 3
    */

  //using KNN to cluster 5 classes and divide every class into 10 intervals with same distance between
//  def Top_K_Prices(buf: ListBuffer[Item]): Map[String, List[(String, Int)]] = {
//    val pricesString = getLocalPricesString(buf, CACHE_PRICES)
//    val price_RDD = sc.textFile("price.txt")
//    val priceRDD =  Functions.seperate(price_RDD, ",")
//    val priceSeq = sc.parallelize(pricesString)
//    val priceCombined =  priceRDD ++ priceSeq
//    val data = priceCombined.map(x=>Functions.safeStringToDouble(x)).flatMap(x => x)
//    def kNN_Spark(data: RDD[Double]): List[Double] = {
//      val K = 5 //number of clusters
//      val threshold = 50.0 // threshold to stop training
//
//      val clusterlist = scala.collection.mutable.ListBuffer.empty[Double]//contain cluster centers as c[k]
//      //generate k clusters randomly
//      val datasize = data.count().toInt
//      for(a <- 1 to K){
//        val rand = (new Random).nextInt(datasize-1)
//        clusterlist += data.zipWithIndex.map{case (k,v) => (v,k)}.lookup(rand)(0)
//      }
//
//      var gap = 0.0 // record difference between loss and previous loss
//      var clusterlist_update = clusterlist.toList// initiate start clusterlist_start
//      var loss_previous = 0.0
//      //training process
//      while(gap>threshold) {
//        var loss = 0.0 // record loss
//        val data_clustered // allocate data[i] to nearest cluster center c[k], return a tuple(c[k], data[i])
//        = data.map(data_i => {
//          var min = data.max
//          var index = datasize - 1
//          for(k <- 0 to clusterlist_update.size-1)
//            if((math.abs(data_i - clusterlist_update(k))<min)) // using abs loss
//            {min = math.abs(data_i - clusterlist_update(k))
//              index = k}
//          loss += min
//          (index+1, data_i)
//        })
//        gap = loss_previous - loss
//        loss_previous = loss
//        val clusterlist_generated = data_clustered.groupBy(_._1).map(kv => (kv._1, kv._2.map(_._2).sum/kv._2.size)).sortBy(_._1).map(_._2).collect().toList//sum and avg data_clustered to get new clusterlist
//        clusterlist_update = clusterlist_generated // substitute newly generated clusters to old ones
//      }
//      clusterlist_update
//    }
//
//    def vectorizedListOfDoubleRDD(priceDouble: RDD[Double]): List[(String, Int)] = {
//      val interval = (priceDouble.max - priceDouble.min)/10
//      val priceMax = priceDouble.max()
//      val priceResult = priceDouble.groupBy(price => price match {
//        case x if (priceMax - 1*interval)<x & x<=(priceMax) => "(%1.2f - %1.2f]".format(priceMax - 1*interval, priceMax)
//        case x if (priceMax - 2*interval)<x & x<=(priceMax - 1*interval) => "(%1.2f - %1.2f]".format(priceMax - 2*interval, priceMax - 1*interval)
//        case x if (priceMax - 3*interval)<x & x<=(priceMax - 2*interval) => "(%1.2f - %1.2f]".format(priceMax - 3*interval, priceMax - 2*interval)
//        case x if (priceMax - 4*interval)<x & x<=(priceMax - 3*interval) => "(%1.2f - %1.2f]".format(priceMax - 4*interval, priceMax - 3*interval)
//        case x if (priceMax - 5*interval)<x & x<=(priceMax - 4*interval) => "(%1.2f - %1.2f]".format(priceMax - 5*interval, priceMax - 4*interval)
//        case x if (priceMax - 6*interval)<x & x<=(priceMax - 5*interval) => "(%1.2f - %1.2f]".format(priceMax - 6*interval, priceMax - 5*interval)
//        case x if (priceMax - 7*interval)<x & x<=(priceMax - 6*interval) => "(%1.2f - %1.2f]".format(priceMax - 7*interval, priceMax - 6*interval)
//        case x if (priceMax - 8*interval)<x & x<=(priceMax - 7*interval) => "(%1.2f - %1.2f]".format(priceMax - 8*interval, priceMax - 7*interval)
//        case x if (priceMax - 9*interval)<x & x<=(priceMax - 8*interval) => "(%1.2f - %1.2f]".format(priceMax - 9*interval, priceMax - 8*interval)
//        case x if (priceMax - 10*interval)<=x & x<=(priceMax - 9*interval) => "(%1.2f - %1.2f]".format(priceMax - 10*interval, priceMax - 9*interval)
//      }).mapValues(_.size).collect().toSeq.sortWith(_._2 > _._2).toList //sort list result descending
//      priceResult
//    }
//
//    def vectorizedListOfDouble(priceDouble: List[Double]): List[(String, Int)] = {
//      val interval = (priceDouble.max - priceDouble.min)/10
//      val priceResult = priceDouble.groupBy(price => price match {
//        case x if (priceDouble.max - 1*interval)<x & x<=(priceDouble.max) => "(%1.2f - %1.2f]".format(priceDouble.max - 1*interval, priceDouble.max)
//        case x if (priceDouble.max - 2*interval)<x & x<=(priceDouble.max - 1*interval) => "(%1.2f - %1.2f]".format(priceDouble.max - 2*interval, priceDouble.max - 1*interval)
//        case x if (priceDouble.max - 3*interval)<x & x<=(priceDouble.max - 2*interval) => "(%1.2f - %1.2f]".format(priceDouble.max - 3*interval, priceDouble.max - 2*interval)
//        case x if (priceDouble.max - 4*interval)<x & x<=(priceDouble.max - 3*interval) => "(%1.2f - %1.2f]".format(priceDouble.max - 4*interval, priceDouble.max - 3*interval)
//        case x if (priceDouble.max - 5*interval)<x & x<=(priceDouble.max - 4*interval) => "(%1.2f - %1.2f]".format(priceDouble.max - 5*interval, priceDouble.max - 4*interval)
//        case x if (priceDouble.max - 6*interval)<x & x<=(priceDouble.max - 5*interval) => "(%1.2f - %1.2f]".format(priceDouble.max - 6*interval, priceDouble.max - 5*interval)
//        case x if (priceDouble.max - 7*interval)<x & x<=(priceDouble.max - 6*interval) => "(%1.2f - %1.2f]".format(priceDouble.max - 7*interval, priceDouble.max - 6*interval)
//        case x if (priceDouble.max - 8*interval)<x & x<=(priceDouble.max - 7*interval) => "(%1.2f - %1.2f]".format(priceDouble.max - 8*interval, priceDouble.max - 7*interval)
//        case x if (priceDouble.max - 9*interval)<x & x<=(priceDouble.max - 8*interval) => "(%1.2f - %1.2f]".format(priceDouble.max - 9*interval, priceDouble.max - 8*interval)
//        case x if (priceDouble.max - 10*interval)<=x & x<=(priceDouble.max - 9*interval) => "(%1.2f - %1.2f]".format(priceDouble.max - 10*interval, priceDouble.max - 9*interval)
//        case _ => "other"
//      }).mapValues(_.size).toSeq.sortWith(_._2 > _._2).toList //sort list result descending
//      priceResult
//    }
//
//    val clusterlist_spark = kNN_Spark(data)
//    val dataLocal = data.collect()
//    val datasize = dataLocal.size
//    val data_result = dataLocal.map(data_i => {
//      var min = data.max
//      var index = datasize - 1
//      for(k <- 0 to clusterlist_spark.size-1)
//        if((math.abs(data_i - clusterlist_spark(k))<min)) {
//          min = math.abs(data_i - clusterlist_spark(k))
//          index = k
//        }
//      (index+1, data_i)
//    }).groupBy(_._1).map(kv => (kv._1, kv._2.map(_._2).toList)).toSeq.sortWith(_._1 < _._1)
//    val result_vectorized = for(l <- data_result) yield l match {
//      case (a, b) => Some(clusterlist_spark(a-1), vectorizedListOfDouble(b))
//      case _ => None
//    }
//    val result = result_vectorized.flatten.sortWith(_._1>_._1)
//    var RL:Map[String,List[(String, Int)]]=
//      Map("Very High"->result(0)._2,
//        "High"->result(1)._2,
//        "Medium"->result(2)._2,
//        "Low"->result(3)._2,
//        "Very Cheap"->result(4)._2)
//    RL
//    val priceDouble = priceCombined.map(x=>Functions.safeStringToDouble(x)).flatten
//    vectorizedListOfDouble(priceDouble) // return 10 vectorized double classes
//  }


//  def getPricesResult(): List[(Double, List[(String, Int)])] = {
//    val clusterlist_spark = Top_K_Prices()
//    val datasize = data.count().toInt
//    val data_result = data.map(data_i => {
//      var min = data.max
//      var index = datasize - 1
//      for(k <- 0 to clusterlist_spark.size-1)
//        if((math.abs(data_i - clusterlist_spark(k))<min)) {
//          min = math.abs(data_i - clusterlist_spark(k))
//          index = k
//        }
//      (index+1, data_i)
//    }).groupBy(_._1).map(kv => (kv._1, kv._2.map(_._2).toList)).collect().toSeq.sortWith(_._1 < _._1)
//    val result_vectorized = for(l <- data_result) yield l match {
//      case (a, b) => Some(clusterlist_spark(a-1), vectorizedListOfDouble(b))
//      case _ => None
//    }
//    result_vectorized.flatten.sortWith(_._1>_._1).toList
//  }
  //if cache si true, store results in .txt

  /**
    * parameters: buf: ListBuff[Item, Item, ...], cache is to write to .txt or not
    * description: process Strings into same format
    */

  def getLocalColors(buf: ListBuffer[Item], cache: Boolean): List[String] = {
    val colors = Functions.itemToAttribute(buf, "Color")
    val letterParser = "[ A-Za-z]".r
    val colorsLower = for(s <- colors.toList) yield  {
      letterParser.findAllMatchIn(s).toList.mkString.toLowerCase.trim
    }
    if (cache) Functions.writeFile("color", colorsLower)
    colorsLower //something like (black, white, pink, ...)
  }

  def getLocalBrands(buf: ListBuffer[Item], cache: Boolean): List[String] = {
    val brands = Functions.itemToAttribute(buf, "Brand")
    val brandsUpper = for(s <- brands.toList) yield s.toUpperCase.trim
    if (cache) Functions.writeFile("brand", brandsUpper)
    brandsUpper // something like (LV, GUCCI, GIVENCHY, ...)
  }

  def getLocalPricesDouble(buf: ListBuffer[Item]): List[Double] = {
    val prices = Functions.itemToAttribute(buf, "Price")
    val pricesDouble = for (s <- prices.toList) yield {
      Functions.safeStringToDouble(s.replaceAll("[${*}]", "").trim)
    }
    pricesDouble.flatten
  }

  def getLocalPricesString(buf: ListBuffer[Item], cache: Boolean): List[String] = {
    val prices = Functions.itemToAttribute(buf, "Price")
    val pricesString = for (s <- prices.toList) yield {
      s.replaceAll("[${*}]", "").trim
    }
    if (cache) Functions.writeFile("price", pricesString)
    pricesString
  }

  //divide numerical values into 10 equidistant intervals
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
    }).mapValues(_.size).toSeq.sortWith(_._2 > _._2).toList //sort list result descending
    priceResult
  }
}
