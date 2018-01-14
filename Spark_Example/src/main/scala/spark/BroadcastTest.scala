package spark

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.Set
import scala.io.Source

/**
  * Created by Grant on 2018/1/3/003.
  * 不可执行
  */
object BroadcastTest extends App{
  if (args.length < 1) {
    System.err.println("Usage: <local-dict> <hadoop-file>")
    System.exit(1)
  }

  // 加载词典，并且设置为 "广播变量"
  val sources = Source.fromFile(args(0))
  val dict = Set[String]()

  for (line <- sources.getLines()) {
    dict += line
  }

  val conf = new SparkConf().setAppName("AccumulatorTest")
  conf.setMaster("local")
  conf.set("executor-memory", "1G")
  conf.set("total-executor-cores", "2")
  val sc = new SparkContext(conf)
  val broadcastVar = sc.broadcast(dict)

  // 加载 hadoop file，判断是否包含特定的词典
  val sentences = sc.textFile(args(1)).flatMap(x => x.split("\\."))

  val rdd = sentences.filter(x => {
    val words = x.split("\\s+")
    words.exists(x =>
      broadcastVar.value.contains(x))
  })

  rdd.collect.foreach(println)

  sc.stop()
}
