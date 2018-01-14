package spark

import org.apache.spark.{AccumulatorParam, SparkConf, SparkContext}

import scala.collection.mutable.Map
import scala.util.Random
/**
  * Created by Grant on 2018/1/2/002.
  */
object AccumulatorTest extends App{
  val conf = new SparkConf().setAppName("AccumulatorTest")
  conf.setMaster("local")
  conf.set("executor-memory", "1G")
  conf.set("total-executor-cores", "2")
  val sc = new SparkContext(conf)
  val accum = sc.longAccumulator("My Accumulator")
  sc.parallelize(1 to 1000000, 10).foreach(i => {
    val r = Random.nextInt(10000)
    if (5000 < r && r <= 5050) {
      accum.add(1L)
    }
  })

  println(s"accum: ${accum.value}, ")
}
