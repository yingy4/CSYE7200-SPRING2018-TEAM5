package ingest

import ingest.Functions.Item
import retrieval.{Client, ExecutionMain}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by Team5 on 4/14/2018.
  * reference:
  *
  */

object SearchConsole{
  private val ENDPOINT = Client.Using.endpoint
  private val ACCESS_KEY_ID = Client.Using.awsAccessKeyId
  private val SECRET_KEY = Client.Using.awsSecretKey
  //don't need to set here below any value!(as long as you set them in main)
//  var SEARCH_KEYWORDS = ""
  val RESPONSE_TIME_MILLI = 3000
  val ASYN = true
    //using Future to process multiple request
    def searchMultiple(buf: ListBuffer[Item], startPage: Int, endPage: Int, SEARCH_KEYWORDS: String): Unit = {
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
    def searchSingle(buf: ListBuffer[Item], pageNumber: Int, SEARCH_KEYWORDS: String): Unit = {
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

  //same. you can assign certain range of pages to search
   def searchMultiple(buf: ListBuffer[Item], startPage: Int, endPage: Int, SEARCH_INDEX: String, flag: Boolean,  SEARCH_KEYWORDS: String): Unit = {
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
    //decide whether to parallel
    if (flag) {
      println("FutureProcess starts!")
      Functions.futureProcess(buf, requestList.toList)
      Thread.sleep(RESPONSE_TIME_MILLI)
    }

    else if (!flag) {
      Functions.noneFutureProcess(buf, requestList.toList)
    }
  }

  //return all results! 82 in total, every 5 urls wrapped in a Future(5*10=50 Items), 82*50=4100 Items in total theoretically!
  def searchAllCategoriesLinear(buf: ListBuffer[Item], SEARCH_KEYWORDS: String, flag: Boolean = ASYN): Unit = {
    searchMultiple(buf,1,5,"Fashion",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"Fashion",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"FashionBaby",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"FashionBaby",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"FashionBoys",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"FashionBoys",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"FashionGirls",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"FashionGirls",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"FashionMen",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"FashionMen",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"FashionWomen",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"FashionWomen",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"Appliances",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"Appliances",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"ArtsAndCrafts",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"ArtsAndCrafts",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"Automotive",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"Automotive",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"Baby",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"Baby",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"Beauty",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"Beauty",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"Blended",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"Blended",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"Books",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"Books",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"Collectibles",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"Collectibles",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"Electronics",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"Electronics",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"GiftCards",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"GiftCards",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"Grocery",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"Grocery",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"HealthPersonalCare",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"HealthPersonalCare",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"HomeGarden",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"HomeGarden",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"Industrial",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"Industrial",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"KindleStore",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"KindleStore",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"LawnAndGarden",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"LawnAndGarden",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"Luggage",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"Luggage",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"MP3Downloads",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"MP3Downloads",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"Magazines",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"Magazines",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"Merchants",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"Merchants",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"MobileApps",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"MobileApps",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"Movies",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"Movies",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"Music",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"Music",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"MusicalInstruments",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"MusicalInstruments",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"OfficeProducts",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"OfficeProducts",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"PCHardware",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"PCHardware",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"PetSupplies",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"PetSupplies",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"Software",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"Software",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"SportingGoods",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"SportingGoods",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"Tools",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"Tools",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"Toys",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"Toys",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"UnboxVideo",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"UnboxVideo",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"VideoGames",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"VideoGames",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"Wine",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"Wine",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,1,5,"Wireless",flag,SEARCH_KEYWORDS)
    searchMultiple(buf,6,10,"Wireless",flag,SEARCH_KEYWORDS)
  }
}
