package ingest

import scala.collection.mutable
import scala.collection.mutable.{Map, TreeMap}
import scala.util.Try

case class ScalaSignedRequestsHelper(endpoint : String, awsAccessKeyId : String, awsSecretKey : String){
  val UTF8_CHARSET = "UTF-8"
  val HMAC_SHA256_ALGORITHM = "HmacSHA256"
  val REQUEST_URI = "/onca/xml"
  val REQUEST_METHOD = "GET"

  val secretyKeyBytes = awsSecretKey.getBytes(UTF8_CHARSET)
  import javax.crypto.spec.SecretKeySpec
  val secretKeySpec = new SecretKeySpec(secretyKeyBytes, HMAC_SHA256_ALGORITHM)
  import javax.crypto.Mac
  val mac = Mac.getInstance(HMAC_SHA256_ALGORITHM)
  mac.init(secretKeySpec)

  def sign(params : Map[String, String]) : String = {
    params.put("Timestamp", timestamp())
    val sortedParamMap = TreeMap[String, String]() ++ params
    val canonicalQS = canonicalize(sortedParamMap)
    val toSign = REQUEST_METHOD + "\n" + endpoint + "\n" + REQUEST_URI +"\n" + canonicalQS
    val hmac1 = hmac(toSign)
    val sig = percentEncodeRfc3986(hmac1)
    val url = "http://" + endpoint + REQUEST_URI + "?" + canonicalQS + "&Signature=" + sig
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

  def canonicalize(sortedMap : mutable.SortedMap[String, String]): String ={
    def encode(map: mutable.SortedMap[String, String]):String = {
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
    def encoder(s :String): Try[String] = Try(java.net.URLEncoder.encode(s, UTF8_CHARSET).replace("+", "%20").replace("*", "%2A").replace("%7E", "~"))
    val out = encoder(s).getOrElse(s)
    out
  }
}

