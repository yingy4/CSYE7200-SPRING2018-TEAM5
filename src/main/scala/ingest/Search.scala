package ingest

import retrieval.Client

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object SearchConsole{
  private val ENDPOINT = Client.Using.endpoint
  private val ACCESS_KEY_ID = Client.Using.awsAccessKeyId
  private val SECRET_KEY = Client.Using.awsSecretKey
  var SEARCH_KEYWORDS = ""
  var ATTRIBUTE = ""
    //using Future to process multiple request
    def searchMultiple(buf: ListBuffer[Any], startPage: Int, endPage: Int): Unit = {
      val helper = ScalaSignedRequestsHelper (ENDPOINT, ACCESS_KEY_ID, SECRET_KEY)
      val requestList = new ListBuffer[String]()
      for (i <- startPage to endPage) {
        val map = new mutable.HashMap[String, String]
        map.put ("Service", "AWSECommerceService")
        map.put ("Operation", "ItemSearch")
        map.put ("AWSAccessKeyId", ACCESS_KEY_ID)
        map.put ("AssociateTag", "scalaproject-20")
        map.put ("SearchIndex", "All")
        map.put ("Keywords", SEARCH_KEYWORDS)
        map.put ("ResponseGroup", "Images,ItemAttributes")
        map.put ("ItemPage" , i.toString)
        requestList += helper.sign (map)
      }
      Functions.futureProcess(buf, requestList.toList, ATTRIBUTE)
    }
    //return one page
    def searchSingle(pageNumber: Int): List[Any] = {
      val helper = ScalaSignedRequestsHelper (ENDPOINT, ACCESS_KEY_ID, SECRET_KEY)
      val map = new mutable.HashMap[String, String]
      map.put ("Service", "AWSECommerceService")
      map.put ("Operation", "ItemSearch")
      map.put ("AWSAccessKeyId", ACCESS_KEY_ID)
      map.put ("AssociateTag", "scalaproject-20")
      map.put ("SearchIndex", "All")
      map.put ("Keywords", SEARCH_KEYWORDS)
      map.put ("ResponseGroup", "Images,ItemAttributes")
      map.put ("ItemPage" , pageNumber.toString)
      val requestList = new ListBuffer[String]()
      requestList += helper.sign (map)
      Functions.noneFutureProcess(requestList.toList, ATTRIBUTE)
    }

  private def searchXXX(buf: ListBuffer[Any], startPage: Int, endPage: Int, SEARCH_INDEX: String): Unit = {
    val helper = ScalaSignedRequestsHelper (ENDPOINT, ACCESS_KEY_ID, SECRET_KEY)
    val requestList = new ListBuffer[String]()
    for (i <- startPage to endPage) {
      val map = new mutable.HashMap[String, String]
      map.put ("Service", "AWSECommerceService")
      map.put ("Operation", "ItemSearch")
      map.put ("AWSAccessKeyId", ACCESS_KEY_ID)
      map.put ("AssociateTag", "scalaproject-20")
      map.put ("SearchIndex", SEARCH_INDEX)
      map.put ("Keywords", SEARCH_KEYWORDS)
      map.put ("ResponseGroup", "Images,ItemAttributes")
      map.put ("ItemPage" , i.toString)
      Thread.sleep(300)
      requestList += helper.sign (map)
    }
    println(111)
    Functions.futureProcess(buf, requestList.toList, ATTRIBUTE)
  }
  def searchYYY(buf: ListBuffer[Any]): Unit = {
//    searchXXX(buf,1,5,"Appliances")
//    searchXXX(buf,1,5,"ArtsAndCrafts")
//    searchXXX(buf,1,5,"Automotive")
//    searchXXX(buf,1,5,"Baby")
//    searchXXX(buf,1,5,"Beauty")
//    searchXXX(buf,1,5,"Blended")
//    searchXXX(buf,1,5,"Books")
//    searchXXX(buf,1,5,"Collectibles")
//    searchXXX(buf,1,5,"Electronics")
    searchXXX(buf,1,5,"Fashion")
    searchXXX(buf,6,10,"Fashion")
    searchXXX(buf,1,5,"FashionBaby")
    searchXXX(buf,6,10,"FashionBaby")
    searchXXX(buf,1,5,"FashionBoys")
    searchXXX(buf,6,10,"FashionBoys")
    searchXXX(buf,1,5,"FashionGirls")
    searchXXX(buf,6,10,"FashionGirls")
    searchXXX(buf,1,5,"FashionMen")
    searchXXX(buf,6,10,"FashionMen")
    searchXXX(buf,1,5,"FashionWomen")
    searchXXX(buf,6,10,"FashionWomen")
    //    searchXXX(buf,1,5,"GiftCards")
//    searchXXX(buf,1,5,"Grocery")
//    searchXXX(buf,1,5,"HealthPersonalCare")
//    searchXXX(buf,1,5,"HomeGarden")
//    searchXXX(buf,1,5,"Industrial")
//    searchXXX(buf,1,5,"KindleStore")
//    searchXXX(buf,1,5,"LawnAndGarden")
//    searchXXX(buf,1,5,"Luggage")
//    searchXXX(buf,1,5,"MP3Downloads")
//    searchXXX(buf,1,5,"Magazines")
//    searchXXX(buf,1,5,"Merchants")
//    searchXXX(buf,1,5,"MobileApps")
//    searchXXX(buf,1,5,"Movies")
//    searchXXX(buf,1,5,"Music")
//    searchXXX(buf,1,5,"MusicalInstruments")
//    searchXXX(buf,1,5,"OfficeProducts")
//    searchXXX(buf,1,5,"PCHardware")
//    searchXXX(buf,1,5,"PetSupplies")
//    searchXXX(buf,1,5,"Software")
//    searchXXX(buf,1,5,"SportingGoods")
//    searchXXX(buf,1,5,"Tools")
//    searchXXX(buf,1,5,"Toys")
//    searchXXX(buf,1,5,"UnboxVideo")
//    searchXXX(buf,1,5,"VideoGames")
//    searchXXX(buf,1,5,"Wine")
//    searchXXX(buf,1,5,"Wireless")
  }
}
