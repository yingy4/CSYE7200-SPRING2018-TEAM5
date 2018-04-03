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
}
