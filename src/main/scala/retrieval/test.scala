package retrieval
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.log4j.Logger
import org.apache.log4j.Level
import ingest.Functions

import scala.util.Random

object WordCount extends App {
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  val sc = new SparkContext("local[*]", "PopularElements")
  //read in price.txt and return a list of price ranges
  val price_RDD = sc.textFile("price.txt")
  val priceRDD = Functions.seperate(price_RDD,",")
  val priceDouble = priceRDD.map(x=>Functions.safeStringToDouble(x)).flatMap(x => x)
//TODO: implement a k-means using Spark
  //1.  本地选择k个初始中心点。c[]存入文件clusterlist；
  val data = priceDouble
  val clusterlist = scala.collection.mutable.ListBuffer.empty[Double]
  //generate 10 clusters randomly
  val datasize = data.count().toInt
  for(a <- 1 to 10){
    val rand = (new Random).nextInt(datasize-1)
    clusterlist += data.zipWithIndex.map{case (k,v) => (v,k)}.lookup(rand)(0)
  }
  //clusterlist[k] represents c[k]
  //for every data[i], compare with c[0], c[1], ..., c[9], choose least one and mark data[i] with it

  //newData: (c, data) c is the class (1 to 10)
  def reduceByKey[K,V](collection: Traversable[Tuple2[K, V]])(implicit num: Numeric[V]) = {
    import num._
    collection
      .groupBy(_._1)
      .map { case (group: K, traversable) => traversable.reduce{(a,b) => (a._1, a._2 + b._2)} }
  }

  var n = 0.0
  var startlist = clusterlist.toList
  var loss_previous = 0.0
  while(n>10) {
    var loss = 0.0
    val newDataX = data.map(data_i => {
      var min = data.max
      var index = datasize - 1
      for(k <- 0 to startlist.size-1)
        if((math.abs(data_i - startlist(k))<min)) {
          min = math.abs(data_i - startlist(k))
          index = k
        }
      loss += min
      (index+1, data_i)
    })
    n = loss_previous - loss
    println(n)
    loss_previous = loss
    val update_clusterlist = newDataX.groupBy(_._1).map(kv => (kv._1, kv._2.map(_._2).sum/kv._2.size)).sortBy(_._1).map(_._2)
    startlist = update_clusterlist.collect().toList
  }

  val newDataT = data.map(data_i => {
    var min = data.max
    var index = datasize - 1
    for(k <- 0 to startlist.toList.size-1)
      if((math.abs(data_i - startlist(k))<min)) {
        min = math.abs(data_i - startlist(k))
        index = k
      }
    (index+1, data_i)
  })

  newDataT.groupBy(_._1).map(kv => (kv._1, kv._2.map(_._2).toList)).foreach(println)




















//  2.  启动mapreduce过程，将文件clusterlist分发到各个节点。输入为存在dfs上的data,输出为dfs的dfs_clusterlist；
//  3.  map过程：输入data[k1,..,k2]。对于data[k], 与c[0]…c[n-1]比较，假定与c[i]差值最少，就标记为i类。输出i,data[k]。i为key,data[k]为value；
//  4.  reduce过程：由于类别为key，则同一类别的所有data会输入同reduce并且紧邻。这样我们可以重新计算c[i]={ 所有标记为i类的data[j]之和}/标记为i类的个数。将结果输出到res。
//  5.  本地抓取dfs_clusterlist。dfs_clusterlist与原有的clusterlist比较。若变化小于给定阈值，则算法结束；反之，则用dfs_clusterlist替换clusterlist,跳转到(2)。

}