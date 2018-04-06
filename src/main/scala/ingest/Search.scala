package ingest

import ingest.Functions.Item
import retrieval.Client

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object SearchConsole{
  private val ENDPOINT = Client.Using.endpoint
  private val ACCESS_KEY_ID = Client.Using.awsAccessKeyId
  private val SECRET_KEY = Client.Using.awsSecretKey
  var SEARCH_KEYWORDS = ""
  var RESPONSE_TIME_MILLI = 0
  var ASYN = true
    //using Future to process multiple request
    def searchMultiple(buf: ListBuffer[Item], startPage: Int, endPage: Int): Unit = {
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
      Functions.futureProcess(buf, requestList.toList)
      Thread.sleep(RESPONSE_TIME_MILLI)
    }
    //return one page
    def searchSingle(buf: ListBuffer[Item], pageNumber: Int): Unit = {
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
      Functions.noneFutureProcess(buf, requestList.toList)
    }

  private def searchMultiple(buf: ListBuffer[Item], startPage: Int, endPage: Int, SEARCH_INDEX: String, flag: Boolean): Unit = {
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
      requestList += helper.sign (map)
    }

    if (flag) {
      println("FutureProcess starts!")
      Functions.futureProcess(buf, requestList.toList)
      Thread.sleep(RESPONSE_TIME_MILLI)
    }

    else if (!flag) {
      Functions.noneFutureProcess(buf, requestList.toList)
    }
  }

  def searchAllCategoriesLinear(buf: ListBuffer[Item], flag: Boolean = ASYN): Unit = {
    searchMultiple(buf,1,5,"Fashion",flag)
    searchMultiple(buf,6,10,"Fashion",flag)
    searchMultiple(buf,1,5,"FashionBaby",flag)
    searchMultiple(buf,6,10,"FashionBaby",flag)
    searchMultiple(buf,1,5,"FashionBoys",flag)
    searchMultiple(buf,6,10,"FashionBoys",flag)
    searchMultiple(buf,1,5,"FashionGirls",flag)
    searchMultiple(buf,6,10,"FashionGirls",flag)
    searchMultiple(buf,1,5,"FashionMen",flag)
    searchMultiple(buf,6,10,"FashionMen",flag)
    searchMultiple(buf,1,5,"FashionWomen",flag)
    searchMultiple(buf,6,10,"FashionWomen",flag)
    searchMultiple(buf,1,5,"Appliances",flag)
    searchMultiple(buf,6,10,"Appliances",flag)
    searchMultiple(buf,1,5,"ArtsAndCrafts",flag)
    searchMultiple(buf,6,10,"ArtsAndCrafts",flag)
    searchMultiple(buf,1,5,"Automotive",flag)
    searchMultiple(buf,6,10,"Automotive",flag)
    searchMultiple(buf,1,5,"Baby",flag)
    searchMultiple(buf,6,10,"Baby",flag)
    searchMultiple(buf,1,5,"Beauty",flag)
    searchMultiple(buf,6,10,"Beauty",flag)
    searchMultiple(buf,1,5,"Blended",flag)
    searchMultiple(buf,6,10,"Blended",flag)
    searchMultiple(buf,1,5,"Books",flag)
    searchMultiple(buf,6,10,"Books",flag)
    searchMultiple(buf,1,5,"Collectibles",flag)
    searchMultiple(buf,6,10,"Collectibles",flag)
    searchMultiple(buf,1,5,"Electronics",flag)
    searchMultiple(buf,6,10,"Electronics",flag)
    searchMultiple(buf,1,5,"GiftCards",flag)
    searchMultiple(buf,6,10,"GiftCards",flag)
    searchMultiple(buf,1,5,"Grocery",flag)
    searchMultiple(buf,6,10,"Grocery",flag)
    searchMultiple(buf,1,5,"HealthPersonalCare",flag)
    searchMultiple(buf,6,10,"HealthPersonalCare",flag)
    searchMultiple(buf,1,5,"HomeGarden",flag)
    searchMultiple(buf,6,10,"HomeGarden",flag)
    searchMultiple(buf,1,5,"Industrial",flag)
    searchMultiple(buf,6,10,"Industrial",flag)
    searchMultiple(buf,1,5,"KindleStore",flag)
    searchMultiple(buf,6,10,"KindleStore",flag)
    searchMultiple(buf,1,5,"LawnAndGarden",flag)
    searchMultiple(buf,6,10,"LawnAndGarden",flag)
    searchMultiple(buf,1,5,"Luggage",flag)
    searchMultiple(buf,6,10,"Luggage",flag)
    searchMultiple(buf,1,5,"MP3Downloads",flag)
    searchMultiple(buf,6,10,"MP3Downloads",flag)
    searchMultiple(buf,1,5,"Magazines",flag)
    searchMultiple(buf,6,10,"Magazines",flag)
    searchMultiple(buf,1,5,"Merchants",flag)
    searchMultiple(buf,6,10,"Merchants",flag)
    searchMultiple(buf,1,5,"MobileApps",flag)
    searchMultiple(buf,6,10,"MobileApps",flag)
    searchMultiple(buf,1,5,"Movies",flag)
    searchMultiple(buf,6,10,"Movies",flag)
    searchMultiple(buf,1,5,"Music",flag)
    searchMultiple(buf,6,10,"Music",flag)
    searchMultiple(buf,1,5,"MusicalInstruments",flag)
    searchMultiple(buf,6,10,"MusicalInstruments",flag)
    searchMultiple(buf,1,5,"OfficeProducts",flag)
    searchMultiple(buf,6,10,"OfficeProducts",flag)
    searchMultiple(buf,1,5,"PCHardware",flag)
    searchMultiple(buf,6,10,"PCHardware",flag)
    searchMultiple(buf,1,5,"PetSupplies",flag)
    searchMultiple(buf,6,10,"PetSupplies",flag)
    searchMultiple(buf,1,5,"Software",flag)
    searchMultiple(buf,6,10,"Software",flag)
    searchMultiple(buf,1,5,"SportingGoods",flag)
    searchMultiple(buf,6,10,"SportingGoods",flag)
    searchMultiple(buf,1,5,"Tools",flag)
    searchMultiple(buf,6,10,"Tools",flag)
    searchMultiple(buf,1,5,"Toys",flag)
    searchMultiple(buf,6,10,"Toys",flag)
    searchMultiple(buf,1,5,"UnboxVideo",flag)
    searchMultiple(buf,6,10,"UnboxVideo",flag)
    searchMultiple(buf,1,5,"VideoGames",flag)
    searchMultiple(buf,6,10,"VideoGames",flag)
    searchMultiple(buf,1,5,"Wine",flag)
    searchMultiple(buf,6,10,"Wine",flag)
    searchMultiple(buf,1,5,"Wireless",flag)
    searchMultiple(buf,6,10,"Wireless",flag)
  }
}
