package retrieval

import java.util

import scala.collection.mutable

case class ScalaSnippet(ACCESS_KEY_ID : String, SECRET_KEY : String, ENDPOINT : String){
  def generateUrl(): String = {
    val helper = ScalaSignedRequestsHelper(ENDPOINT,ACCESS_KEY_ID, SECRET_KEY)
    val map = new mutable.HashMap[String, String]
    map.put("Service", "AWSECommerceService")
    map.put("Operation", "ItemSearch")
    map.put("AWSAccessKeyId", ACCESS_KEY_ID)
    map.put("AssociateTag", "scalaproject-20")
    map.put("SearchIndex", "All")
    map.put("Keywords", "Boots")
    map.put("ResponseGroup", "Images,ItemAttributes")
    val requestUrl = helper.sign(map)
    requestUrl
  }
}

object ScalaSnippet {
  //def apply(ACCESS_KEY_ID: String, SECRET_KEY: String, ENDPOINT: String): ScalaSnippet = new ScalaSnippet(ACCESS_KEY_ID, SECRET_KEY, ENDPOINT)
}

