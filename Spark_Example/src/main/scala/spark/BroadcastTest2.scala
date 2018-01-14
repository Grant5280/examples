package spark

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Grant on 2018/1/3/003.
  */
// 我们定义一个 Student class，这里面包含了 Student 的信息，注意 case class 包含了序列化
// case class Student(val name: String, val card: String, val birthday: String)

// 这个 Student class 不是 case class，因此需要 spark register
class Student(val name: String, val card: String, val birthday: String)

object BroadcastTest2 extends App {
  // 使用 kryo classes 序列号，提高性能
  val conf = new SparkConf().setAppName("AccumulatorTest")
  conf.setMaster("local")
  conf.set("executor-memory", "1G")
  conf.set("total-executor-cores", "2")
  conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").registerKryoClasses(Array(classOf[Student]))
  val sc = new SparkContext(conf)
  val students = Map("qifeng.dai" -> new Student("qifeng.dai", "SA08011084", "02-22"), "yijing.liu" -> new Student("yijing.liu", "BA08011001", "01-05"))
  val broadcastVar = sc.broadcast(students)
  val newStudents = sc.parallelize(List("jessical", "matrix", "qifeng.dai")).map(x => broadcastVar.value.get(x) match {
    case None => (x, None)
    case Some(e) => (x, e.card + "," + e.birthday)
  })

  newStudents.collect.foreach(println)

  sc.stop()
}
