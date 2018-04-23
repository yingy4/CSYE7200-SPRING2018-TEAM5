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
    def searchMultiple(buf: ListBuffer[Item], startPage: Int, endPage: Int, searchKeywords: String): Unit = {
      val helper = ScalaSignedRequestsHelper (ENDPOINT, ACCESS_KEY_ID, SECRET_KEY)
      val requestList = new ListBuffer[String]()
      for (i <- startPage to endPage) {
        val map = new mutable.HashMap[String, String]
        map.put ("Service", "AWSECommerceService")
        map.put ("Operation", "ItemSearch")
        map.put ("AWSAccessKeyId", ACCESS_KEY_ID)
        map.put ("AssociateTag", "scalaproject-20")
        map.put ("SearchIndex", "All")
        map.put ("Keywords", searchKeywords)
        map.put ("ResponseGroup", "Images,ItemAttributes")
        map.put ("ItemPage" , i.toString)
        requestList += helper.sign (map)
      }
      Functions.futureProcess(buf, requestList.toList)
      Thread.sleep(RESPONSE_TIME_MILLI)
    }
    //return one page
    def searchSingle(buf: ListBuffer[Item], pageNumber: Int, searchKeywords: String): Unit = {
      val helper = ScalaSignedRequestsHelper (ENDPOINT, ACCESS_KEY_ID, SECRET_KEY)
      val map = new mutable.HashMap[String, String]
      map.put ("Service", "AWSECommerceService")
      map.put ("Operation", "ItemSearch")
      map.put ("AWSAccessKeyId", ACCESS_KEY_ID)
      map.put ("AssociateTag", "scalaproject-20")
      map.put ("SearchIndex", "All")
      map.put ("Keywords", searchKeywords)
      map.put ("ResponseGroup", "Images,ItemAttributes")
      map.put ("ItemPage" , pageNumber.toString)
      val requestList = new ListBuffer[String]()
      requestList += helper.sign (map)
      Functions.noneFutureProcess(buf, requestList.toList)
    }

  //same. you can assign certain range of pages to search
   def searchMultiple(buf: ListBuffer[Item], startPage: Int, endPage: Int, searchKeyword: String, flag: Boolean,  searchKeywordsIdx: String): Unit = {
    val helper = ScalaSignedRequestsHelper (ENDPOINT, ACCESS_KEY_ID, SECRET_KEY)
    val requestList = new ListBuffer[String]()
    for (i <- startPage to endPage) {
      val map = new mutable.HashMap[String, String]
      map.put ("Service", "AWSECommerceService")
      map.put ("Operation", "ItemSearch")
      map.put ("AWSAccessKeyId", ACCESS_KEY_ID)
      map.put ("AssociateTag", "scalaproject-20")
      map.put ("SearchIndex", searchKeywordsIdx)
      map.put ("Keywords", searchKeyword)
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
  def searchAllCategoriesLinear(buf: ListBuffer[Item], searchKeywords: String, flag: Boolean = ASYN): Unit = {
    searchMultiple(buf,1,5,"Fashion",flag,searchKeywords)
    searchMultiple(buf,6,10,"Fashion",flag,searchKeywords)
    searchMultiple(buf,1,5,"FashionBaby",flag,searchKeywords)
    searchMultiple(buf,6,10,"FashionBaby",flag,searchKeywords)
    searchMultiple(buf,1,5,"FashionBoys",flag,searchKeywords)
    searchMultiple(buf,6,10,"FashionBoys",flag,searchKeywords)
    searchMultiple(buf,1,5,"FashionGirls",flag,searchKeywords)
    searchMultiple(buf,6,10,"FashionGirls",flag,searchKeywords)
    searchMultiple(buf,1,5,"FashionMen",flag,searchKeywords)
    searchMultiple(buf,6,10,"FashionMen",flag,searchKeywords)
    searchMultiple(buf,1,5,"FashionWomen",flag,searchKeywords)
    searchMultiple(buf,6,10,"FashionWomen",flag,searchKeywords)
    searchMultiple(buf,1,5,"Appliances",flag,searchKeywords)
    searchMultiple(buf,6,10,"Appliances",flag,searchKeywords)
    searchMultiple(buf,1,5,"ArtsAndCrafts",flag,searchKeywords)
    searchMultiple(buf,6,10,"ArtsAndCrafts",flag,searchKeywords)
    searchMultiple(buf,1,5,"Automotive",flag,searchKeywords)
    searchMultiple(buf,6,10,"Automotive",flag,searchKeywords)
    searchMultiple(buf,1,5,"Baby",flag,searchKeywords)
    searchMultiple(buf,6,10,"Baby",flag,searchKeywords)
    searchMultiple(buf,1,5,"Beauty",flag,searchKeywords)
    searchMultiple(buf,6,10,"Beauty",flag,searchKeywords)
    searchMultiple(buf,1,5,"Blended",flag,searchKeywords)
    searchMultiple(buf,6,10,"Blended",flag,searchKeywords)
    searchMultiple(buf,1,5,"Books",flag,searchKeywords)
    searchMultiple(buf,6,10,"Books",flag,searchKeywords)
    searchMultiple(buf,1,5,"Collectibles",flag,searchKeywords)
    searchMultiple(buf,6,10,"Collectibles",flag,searchKeywords)
    searchMultiple(buf,1,5,"Electronics",flag,searchKeywords)
    searchMultiple(buf,6,10,"Electronics",flag,searchKeywords)
    searchMultiple(buf,1,5,"GiftCards",flag,searchKeywords)
    searchMultiple(buf,6,10,"GiftCards",flag,searchKeywords)
    searchMultiple(buf,1,5,"Grocery",flag,searchKeywords)
    searchMultiple(buf,6,10,"Grocery",flag,searchKeywords)
    searchMultiple(buf,1,5,"HealthPersonalCare",flag,searchKeywords)
    searchMultiple(buf,6,10,"HealthPersonalCare",flag,searchKeywords)
    searchMultiple(buf,1,5,"HomeGarden",flag,searchKeywords)
    searchMultiple(buf,6,10,"HomeGarden",flag,searchKeywords)
    searchMultiple(buf,1,5,"Industrial",flag,searchKeywords)
    searchMultiple(buf,6,10,"Industrial",flag,searchKeywords)
    searchMultiple(buf,1,5,"KindleStore",flag,searchKeywords)
    searchMultiple(buf,6,10,"KindleStore",flag,searchKeywords)
    searchMultiple(buf,1,5,"LawnAndGarden",flag,searchKeywords)
    searchMultiple(buf,6,10,"LawnAndGarden",flag,searchKeywords)
    searchMultiple(buf,1,5,"Luggage",flag,searchKeywords)
    searchMultiple(buf,6,10,"Luggage",flag,searchKeywords)
    searchMultiple(buf,1,5,"MP3Downloads",flag,searchKeywords)
    searchMultiple(buf,6,10,"MP3Downloads",flag,searchKeywords)
    searchMultiple(buf,1,5,"Magazines",flag,searchKeywords)
    searchMultiple(buf,6,10,"Magazines",flag,searchKeywords)
    searchMultiple(buf,1,5,"Merchants",flag,searchKeywords)
    searchMultiple(buf,6,10,"Merchants",flag,searchKeywords)
    searchMultiple(buf,1,5,"MobileApps",flag,searchKeywords)
    searchMultiple(buf,6,10,"MobileApps",flag,searchKeywords)
    searchMultiple(buf,1,5,"Movies",flag,searchKeywords)
    searchMultiple(buf,6,10,"Movies",flag,searchKeywords)
    searchMultiple(buf,1,5,"Music",flag,searchKeywords)
    searchMultiple(buf,6,10,"Music",flag,searchKeywords)
    searchMultiple(buf,1,5,"MusicalInstruments",flag,searchKeywords)
    searchMultiple(buf,6,10,"MusicalInstruments",flag,searchKeywords)
    searchMultiple(buf,1,5,"OfficeProducts",flag,searchKeywords)
    searchMultiple(buf,6,10,"OfficeProducts",flag,searchKeywords)
    searchMultiple(buf,1,5,"PCHardware",flag,searchKeywords)
    searchMultiple(buf,6,10,"PCHardware",flag,searchKeywords)
    searchMultiple(buf,1,5,"PetSupplies",flag,searchKeywords)
    searchMultiple(buf,6,10,"PetSupplies",flag,searchKeywords)
    searchMultiple(buf,1,5,"Software",flag,searchKeywords)
    searchMultiple(buf,6,10,"Software",flag,searchKeywords)
    searchMultiple(buf,1,5,"SportingGoods",flag,searchKeywords)
    searchMultiple(buf,6,10,"SportingGoods",flag,searchKeywords)
    searchMultiple(buf,1,5,"Tools",flag,searchKeywords)
    searchMultiple(buf,6,10,"Tools",flag,searchKeywords)
    searchMultiple(buf,1,5,"Toys",flag,searchKeywords)
    searchMultiple(buf,6,10,"Toys",flag,searchKeywords)
    searchMultiple(buf,1,5,"UnboxVideo",flag,searchKeywords)
    searchMultiple(buf,6,10,"UnboxVideo",flag,searchKeywords)
    searchMultiple(buf,1,5,"VideoGames",flag,searchKeywords)
    searchMultiple(buf,6,10,"VideoGames",flag,searchKeywords)
    searchMultiple(buf,1,5,"Wine",flag,searchKeywords)
    searchMultiple(buf,6,10,"Wine",flag,searchKeywords)
    searchMultiple(buf,1,5,"Wireless",flag,searchKeywords)
    searchMultiple(buf,6,10,"Wireless",flag,searchKeywords)
  }
}
