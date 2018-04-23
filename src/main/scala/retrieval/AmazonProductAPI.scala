package retrieval

case class  AmazonClient(endpoint : String, awsAccessKeyId : String, awsSecretKey : String)
//user can create new client and switch Using to different clients
object Client {
  val Using = Clients.HouzeLiu
}

object Clients{
  val ACCESS_KEY_ID = "AKIAJVADVVC5WAOOAQHA"
  val SECRET_KEY = "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"
  val ENDPOINT = "webservices.amazon.com"
  val HouzeLiu = AmazonClient(ENDPOINT, ACCESS_KEY_ID, SECRET_KEY)
}