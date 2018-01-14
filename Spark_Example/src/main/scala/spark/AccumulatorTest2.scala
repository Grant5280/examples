package spark

import org.apache.spark.util.AccumulatorV2
import org.apache.spark.{AccumulatorParam, SparkConf, SparkContext}

import scala.collection.mutable
import scala.collection.mutable.Map
import scala.util.Random

/**
  * Created by Grant on 2018/1/2/002.
  */
class MapAccumulator extends AccumulatorV2[(String,String),mutable.Map[String, String]] {
  private  val mapAccumulator = mutable.Map[String,String]()
  def add(keyAndValue:((String,String))): Unit ={
    val key = keyAndValue._1
    val value = keyAndValue._2
    if (!mapAccumulator.contains(key))
      mapAccumulator += key->value
    else if(mapAccumulator.get(key).get!=value) {
      mapAccumulator += key->(mapAccumulator.get(key).get+"||"+value)
    }
  }
  def isZero: Boolean = {
    mapAccumulator.isEmpty
  }
  def copy(): AccumulatorV2[((String,String)),mutable.Map[String, String]] ={
    val newMapAccumulator = new  MapAccumulator()
    mapAccumulator.foreach(x=>newMapAccumulator.add(x))
    newMapAccumulator
  }
  def value: mutable.Map[String,String] = {
    mapAccumulator
  }
  def merge(other:AccumulatorV2[((String,String)),mutable.Map[String, String]]) = other match
  {
    case map:MapAccumulator => {
      other.value.foreach(x =>
        if (!this.value.contains(x._1))
          this.add(x)
        else
          x._2.split("\\|\\|").foreach(
            y => {
              if (!this.value.get(x._1).get.split("\\|\\|").contains(y))
                this.add(x._1, y)
            }
          )
      )
    }
    case _  =>
      throw new UnsupportedOperationException(
        s"Cannot merge ${this.getClass.getName} with ${other.getClass.getName}")
  }
  def reset(): Unit ={
    mapAccumulator.clear()
  }
}

object AccumulatorTest2 extends App{
  val conf = new SparkConf().setAppName("AccumulatorTest")
  conf.setMaster("local")
  conf.set("executor-memory", "1G")
  conf.set("total-executor-cores", "2")
  val sc = new SparkContext(conf)
  val accmap = new MapAccumulator
  sc.register(accmap,"MapAccumulator")
  sc.parallelize(1 to 1000000, 10).foreach(i => {
    val r = Random.nextInt(10000)
    if (5000 < r && r <= 5010) {
      accmap.add(r.toString,"1")
    }
  })

  println(s"accmap: ${accmap.value}")
}
