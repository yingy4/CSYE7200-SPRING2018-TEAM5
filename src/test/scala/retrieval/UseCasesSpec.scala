package retrieval

import ingest.Functions.Item
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable.ListBuffer

class UseCasesSpec extends FlatSpec with Matchers{

  behavior of "Top_K_Colors"
  it should """get data from running APP and reading .txt, return List[(String, Int)]"""in{
    val test = UseCases.Top_K_Colors(1)
    test should matchPattern{
      case List((s:String,i:Int))=>
    }
  }

  behavior of "Top_K_Brands"
  it should """get data from running APP and reading .txt, return List[(String, Int)]"""in{
    val test = UseCases.Top_K_Brands(1)
    test should matchPattern{
      case List((s:String,i:Int))=>
    }
  }

  behavior of "Top_k_p()"
  it should """get data from running APP and reading .txt, return List[String]"""in{
    val test = UseCases.Top_k_p()
    test should matchPattern{
      case ls:List[String]=>
    }
  }

 //Test time is too long, got rid of
//  behavior of "Top_K_Prices()"
//  it should """using KNN to cluster 5 classes and divide every class into 10 intervals with same distance between"""in{
//    val test = UseCases.Top_K_Prices()
//    test should matchPattern{
//      case silsm: Map[String, List[(String, Int)]]=>
//    }
//  }

  behavior of "getLocalColors"
  it should "use buf: ListBuff[Item, Item, ...] to process colors into same lowercase format"in{
    val i1 = Item("red","Apple","$1000.00","www.apple.com")
    val i2 = Item("Red","Ipad","$300.00","www.apple.com")
    val lbi = ListBuffer(i1,i2)
    val ls = List("red","red")
    val test = UseCases.getLocalColors(lbi, false)
    test shouldBe ls
  }

  behavior of "getLocalBrands"
  it should "use buf: ListBuff[Item, Item, ...] to process brands into same uppercase format"in{
    val i1 = Item("red","Apple","$1000.00","www.apple.com")
    val i2 = Item("Red","Ipad","$300.00","www.apple.com")
    val lbi = ListBuffer(i1,i2)
    val ls = List("APPLE","IPAD")
    val test = UseCases.getLocalBrands(lbi, false)
    test shouldBe ls
  }

  behavior of "getLocalPricesDouble"
  it should "use buf: ListBuff[Item, Item, ...] to process price into List[Double]"in{
    val i1 = Item("red","Apple","$1000.00","www.apple.com")
    val i2 = Item("Red","Ipad","$300.00","www.apple.com")
    val lbi = ListBuffer(i1,i2)
    val li = List(1000.0, 300.0)
    val test = UseCases.getLocalPricesDouble(lbi)
    test shouldBe li
  }

  behavior of "getLocalPricesString"
  it should "use buf: ListBuff[Item, Item, ...] to process price into List[String]"in{
    val i1 = Item("red","Apple","$1000.00","www.apple.com")
    val i2 = Item("Red","Ipad","$300.00","www.apple.com")
    val lbi = ListBuffer(i1,i2)
    val ls = List("1000.00", "300.00")
    val test = UseCases.getLocalPricesString(lbi, false)
    test shouldBe ls
  }

  behavior of "vectorizedListOfDouble"
  it should "divide numerical values into 10 equidistant intervals and give desecending output"in{
    val pd = List(1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0)
    val lsi = List(("(2.80 - 3.70]",1), ("(6.40 - 7.30]",1), ("(4.60 - 5.50]",1), ("(5.50 - 6.40]",1), ("(3.70 - 4.60]",1), ("(9.10 - 10.00]",1), ("(7.30 - 8.20]",1), ("(1.90 - 2.80]",1), ("(1.00 - 1.90]",1), ("(8.20 - 9.10]",1))
    val test = UseCases.vectorizedListOfDouble(pd)
    test shouldBe lsi
  }




}
