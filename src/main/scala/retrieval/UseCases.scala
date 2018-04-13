package retrieval

import ingest.Functions.Item
import ingest.{Functions, SearchConsole}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable.ListBuffer

object UseCases {
  //set Console
  SearchConsole.SEARCH_KEYWORDS = "Trouser"
  SearchConsole.RESPONSE_TIME_MILLI = 100
  SearchConsole.ASYN = true
  //set CACHE
  var CACHE_COLORS = true
  var CACHE_BRANDS = true
  var CACHE_PRICES = true

  val buf = scala.collection.mutable.ListBuffer.empty[Item]
  SearchConsole.searchAllCategoriesLinear(buf)
  //set spark context
  val sc = new SparkContext("local[*]", "PopularElements")
  val ssc = new StreamingContext(sc, Seconds(1))

  def Top_K_Colors(k: Int): List[(String, Int)] = {
    val colorsLower = getLocalColors(buf, CACHE_COLORS)//a list of color unsorted
    val color_RDD = sc.textFile("color.txt")
    val colorRDD =  Functions.sortResultDecending(Functions.seperate(color_RDD, ",")).
      filter(_._2 > 10)//a RDD of sorted color tuples filtered
    val colorSeq = Functions.sortResultDescending(colorsLower).
      filter(_._2 > 1)//a seq of sorted color tuples
    val colorCombined =  colorRDD.collect().toList ++ colorSeq.toList
    val colorResult =  Functions.mergeAndSort(colorCombined)
    colorResult.take(k)
  }

  def Top_K_Brands(k: Int): List[(String, Int)] = {
    val brandsUpper = getLocalBrands(buf, CACHE_BRANDS)
    val brand_RDD = sc.textFile("brand.txt")
    val brandRDD = Functions.sortResultDecending(Functions.seperate(brand_RDD, ",")).
      filter(_._2 > 10)
    val brandSeq = Functions.sortResultDescending(brandsUpper).
      filter(_._2 > 1)
    val brandCombined =  brandRDD.collect().toList ++ brandSeq.toList
    val brandResult =  Functions.mergeAndSort(brandCombined)
    brandResult.take(k)
  }

  def Top_10_Prices(): List[(String, Int)] = {
    val pricesString = getLocalPricesString(buf, CACHE_PRICES)
    val price_RDD = sc.textFile("price.txt")
    val priceRDD =  Functions.seperate(price_RDD, ",")
    val priceSeq = sc.parallelize(pricesString)
    val priceCombined =  priceRDD ++ priceSeq

    def vectorizedListOfDoubleRDD(priceDouble: RDD[Double]): List[(String, Int)] = {
      val interval = (priceDouble.max - priceDouble.min)/10
      val priceMax = priceDouble.max()
      val priceResult = priceDouble.groupBy(price => price match {
        case x if (priceMax - 1*interval)<x & x<=(priceMax) => "(%1.2f - %1.2f]".format(priceMax - 1*interval, priceMax)
        case x if (priceMax - 2*interval)<x & x<=(priceMax - 1*interval) => "(%1.2f - %1.2f]".format(priceMax - 2*interval, priceMax - 1*interval)
        case x if (priceMax - 3*interval)<x & x<=(priceMax - 2*interval) => "(%1.2f - %1.2f]".format(priceMax - 3*interval, priceMax - 2*interval)
        case x if (priceMax - 4*interval)<x & x<=(priceMax - 3*interval) => "(%1.2f - %1.2f]".format(priceMax - 4*interval, priceMax - 3*interval)
        case x if (priceMax - 5*interval)<x & x<=(priceMax - 4*interval) => "(%1.2f - %1.2f]".format(priceMax - 5*interval, priceMax - 4*interval)
        case x if (priceMax - 6*interval)<x & x<=(priceMax - 5*interval) => "(%1.2f - %1.2f]".format(priceMax - 6*interval, priceMax - 5*interval)
        case x if (priceMax - 7*interval)<x & x<=(priceMax - 6*interval) => "(%1.2f - %1.2f]".format(priceMax - 7*interval, priceMax - 6*interval)
        case x if (priceMax - 8*interval)<x & x<=(priceMax - 7*interval) => "(%1.2f - %1.2f]".format(priceMax - 8*interval, priceMax - 7*interval)
        case x if (priceMax - 9*interval)<x & x<=(priceMax - 8*interval) => "(%1.2f - %1.2f]".format(priceMax - 9*interval, priceMax - 8*interval)
        case x if (priceMax - 10*interval)<=x & x<=(priceMax - 9*interval) => "(%1.2f - %1.2f]".format(priceMax - 10*interval, priceMax - 9*interval)
      }).mapValues(_.size).collect().toSeq.sortWith(_._2 > _._2).toList //sort list result descending
      priceResult
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
      }).mapValues(_.size).toSeq.sortWith(_._2 > _._2).toList //sort list result descending
      priceResult
    }



      val priceDoubleRDD = priceCombined.map(x=>Functions.safeStringToDouble(x)).flatMap(x => x)
      vectorizedListOfDoubleRDD(priceDoubleRDD)
//    val priceDouble = priceCombined.map(x=>Functions.safeStringToDouble(x)).flatten
//    vectorizedListOfDouble(priceDouble) // return 10 vectorized double classes
  }

  //if cache si true, store results in .txt
  def getLocalColors(buf: ListBuffer[Item], cache: Boolean): List[String] = {
    val colors = Functions.itemToAttribute(buf, "Color")
    val letterParser = "[ A-Za-z]".r
    val colorsLower = for(s <- colors.toList) yield  {
      letterParser.findAllMatchIn(s).toList.mkString.toLowerCase.trim
    }
    if (cache) Functions.writeFile("color", colorsLower)
    colorsLower
  }

  def getLocalBrands(buf: ListBuffer[Item], cache: Boolean): List[String] = {
    val brands = Functions.itemToAttribute(buf, "Brand")
    val brandsUpper = for(s <- brands.toList) yield s.toUpperCase.trim
    if (cache) Functions.writeFile("brand", brandsUpper)
    brandsUpper
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
}
