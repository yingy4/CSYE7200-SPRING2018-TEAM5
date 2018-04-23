package ingest

import scala.collection.SortedMap
import scala.collection.mutable.Map
import scala.util.Try
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Mac


/**
  * Cited
  * reference:
  *
  */

case class ScalaSignedRequestsHelper(endpoint : String, awsAccessKeyId : String, awsSecretKey : String){
  val utf8CharSet = "UTF-8"
  val hmacSha256Algo = "HmacSHA256"
  val requestURI = "/onca/xml"
  val requestMethod = "GET"

  val secretyKeyBytes = awsSecretKey.getBytes(utf8CharSet)
  val secretKeySpec = new SecretKeySpec(secretyKeyBytes, hmacSha256Algo)
  val mac = Mac.getInstance(hmacSha256Algo)
  mac.init(secretKeySpec)

  def sign(params : Map[String, String]) : String = {
    params.put("Timestamp", timestamp())
    val sortedParamMap = SortedMap[String, String]() ++ params
    val canonicalQS = canonicalize(sortedParamMap)
    val toSign = requestMethod + "\n" + endpoint + "\n" + requestURI +"\n" + canonicalQS
    val hmac1 = hmac(toSign)
    val sig = percentEncodeRfc3986(hmac1)
    val url = "http://" + endpoint + requestURI + "?" + canonicalQS + "&Signature=" + sig
    url
  }

  def hmac(stringToSign : String) : String = {
    val data = stringToSign.getBytes()
    val rawHmac = mac.doFinal(data)
    val encoder = new org.apache.commons.codec.binary.Base64()
    val signature = new String(encoder.encode(rawHmac))
    signature
  }

  def timestamp() : String = {
    val calendar = java.util.Calendar.getInstance()
    val dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("GMT"))
    val timestamp = dateFormat.format(calendar.getTime())
    timestamp
  }

  def canonicalize(sortedMap : SortedMap[String, String]): String ={
    def encode(map: SortedMap[String, String]):String = {
      val buffer = new StringBuffer()
      for(kv <- sortedMap){
        buffer.append(percentEncodeRfc3986(kv._1))
        buffer.append("=")
        buffer.append(percentEncodeRfc3986(kv._2))
        buffer.append("&")
      }
      buffer.deleteCharAt(buffer.length() - 1)
      buffer.toString
    }
    sortedMap.isEmpty match {
      case true =>""
      case _ => encode(sortedMap)
    }
  }

  def percentEncodeRfc3986(s :String) : String = {
    def encoder(s :String): Try[String] = Try(java.net.URLEncoder.encode(s, utf8CharSet).replace("+", "%20").replace("*", "%2A").replace("%7E", "~"))
    val out = encoder(s).getOrElse(s)
    out
  }
}

