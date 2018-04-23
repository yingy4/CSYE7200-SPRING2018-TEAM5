package retrieval

case class  AmazonClient(endpoint : String, awsAccessKeyId : String, awsSecretKey : String)
//user can create new client and switch Using to different clients
object Client {
  val Using = Clients.houzeLiu
}

object Clients{
  val accessKeyID = "AKIAJVADVVC5WAOOAQHA"
  val secretKey = "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"
  val endPoint = "webservices.amazon.com"
  val houzeLiu = AmazonClient(endPoint, accessKeyID, secretKey)
}