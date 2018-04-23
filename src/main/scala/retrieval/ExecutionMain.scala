package retrieval

import java.io.{BufferedWriter, FileWriter}

import ingest.Functions.Item
import ingest.SearchConsole
import retrieval.UseCases.{Top_K_Brands, Top_K_Colors, Top_k_p}
import org.apache.log4j.Logger
import org.apache.log4j.Level

object ExecutionMain {

  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  val buf = scala.collection.mutable.ListBuffer.empty[Item]
  SearchConsole.searchAllCategoriesLinear(buf, "socks")

  Top_K_Colors(10, buf).foreach(println)
  Top_K_Brands(10, buf).foreach(println)
  //  print(Top_k_p())//normal prices in String format

  //k-means deal with prices
  Top_k_p(buf).foreach(println)


  val file4 = "item.txt"
  val writer4 = new BufferedWriter(new FileWriter(file4, true))
  for (x <- buf) {
    writer4.append(x + "\n")
  }
  writer4.close()

  def main(args: Array[String]) {

    if (args.size == 0) println("Please refer to README.md in our repo for input parameter.") else {
      val keyword = if (args.size > 1) args.tail.mkString(" ") else ""

      //      args(0) match {
      //        case "hashtags" => Usecases.popularHashTags(keyword)
      //        case "map" => Usecases.popularLocations(keyword)
      //        case "weather" => runWeather(keyword)
      //        case "stock" => runStock(keyword)
      //        case _ => println("Invalid input. Please refer to README.md in our repo for input parameter.")
      //      }
      //    }
    }
  }
}


