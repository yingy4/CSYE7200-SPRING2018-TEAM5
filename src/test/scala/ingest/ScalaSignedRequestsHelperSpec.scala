package ingest


import org.scalatest.{FlatSpec, Matchers}

class ScalaSignedRequestsHelperSpec extends FlatSpec with Matchers{

  behavior of "sign"
  it should "take a param hashmap and return a signed string"in{
    val m = scala.collection.mutable.Map("aws" -> "amazon")
    val ssrh = ScalaSignedRequestsHelper("aws","key","id")
    val test = ssrh.sign(m)
    test should matchPattern{
      case _:String =>
    }
  }

  behavior of "hmac"
  it should "take a string and return a string"in{
    val s = "test String"
    val ssrh = ScalaSignedRequestsHelper("aws","key","id")
    val test = ssrh.hmac(s)
    test should matchPattern{
      case _:String =>
    }
  }

  behavior of "timestamp"
  it should "generate a string"in{
    val ssrh = ScalaSignedRequestsHelper("aws","key","id")
    val test = ssrh.timestamp()
    test should matchPattern{
      case _:String =>
    }
  }

  behavior of "canonicalize"
  it should "take a param hashmap and return a canonicalized string"in{
    val m = scala.collection.SortedMap("aws" -> "amazon")
    val ssrh = ScalaSignedRequestsHelper("aws","key","id")
    val test = ssrh.canonicalize(m)
    test should matchPattern{
      case _:String =>
    }
  }

  behavior of "percentEncodeRfc3986"
  it should "take a string and return en encode string"in{
    val s = "test String"
    val ssrh = ScalaSignedRequestsHelper("aws","key","id")
    val test = ssrh.percentEncodeRfc3986(s)
    test should matchPattern{
      case _:String =>
    }
  }

}
