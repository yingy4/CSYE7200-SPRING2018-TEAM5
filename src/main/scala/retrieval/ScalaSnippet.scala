package retrieval

import java.util

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
object  AmazonClient {

  val ACCESS_KEY_ID = "AKIAJVADVVC5WAOOAQHA"
  val SECRET_KEY = "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"
  val ENDPOINT = "webservices.amazon.com"
  //return a list of urls given start and end page
  def generateUrl(startPage: Int, endPage: Int): List[String] = {
    val helper = ScalaSignedRequestsHelper (ENDPOINT, ACCESS_KEY_ID, SECRET_KEY)

    val requestList = new ListBuffer[String]()
    for (i <- startPage to endPage) {
      val map = new mutable.HashMap[String, String]
      map.put ("Service", "AWSECommerceService")
      map.put ("Operation", "ItemSearch")
      map.put ("AWSAccessKeyId", ACCESS_KEY_ID)
      map.put ("AssociateTag", "scalaproject-20")
      map.put ("SearchIndex", "All")
      map.put ("Keywords", "Boots")
      map.put ("ResponseGroup", "Images,ItemAttributes")
      map.put ("ItemPage" , i.toString)
      requestList += helper.sign (map)
    }
    requestList.toList
  }
  //return one page
  def generateUrl(pageNumber: Int): String = {
    val helper = ScalaSignedRequestsHelper (ENDPOINT, ACCESS_KEY_ID, SECRET_KEY)
    val map = new mutable.HashMap[String, String]
    map.put ("Service", "AWSECommerceService")
    map.put ("Operation", "ItemSearch")
    map.put ("AWSAccessKeyId", ACCESS_KEY_ID)
    map.put ("AssociateTag", "scalaproject-20")
    map.put ("SearchIndex", "All")
    map.put ("Keywords", "Boots")
    map.put ("ResponseGroup", "Images,ItemAttributes")
    map.put ("ItemPage" , pageNumber.toString)
    helper.sign (map)
  }



}
//TODO: put ParseXML some part of attribute process to this object so that Client can adjust search input


