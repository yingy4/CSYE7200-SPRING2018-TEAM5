package retrieval

import ingest.Functions.Item
import ingest.{Functions}

import scala.collection.mutable.ListBuffer

object UseCases {

  def getColors(buf: ListBuffer[Item]): List[String] = {
    val colors = Functions.itemToAttribute(buf, "Color")
    val letterParser = "[ A-Za-z]".r
    val colorsLower = for(s <- colors.toList) yield  {
      letterParser.findAllIn(s).toList.mkString.toLowerCase.trim
    }
    colorsLower
  }

  def getBrands(buf: ListBuffer[Item]): List[String] = {
    val brands = Functions.itemToAttribute(buf, "Brand")
    val brandsUpper = for(s <- brands.toList) yield s.toUpperCase.trim
    brandsUpper
  }

  def getPrices(buf: ListBuffer[Item]): List[Double] = {
    val prices = Functions.itemToAttribute(buf, "Price")
    val pricesDouble = for (s <- prices.toList) yield {
      Functions.safeStringToDouble(s.replaceAll("[${*}]", "").trim)
    }
    pricesDouble.flatten
  }
}
