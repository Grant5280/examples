package spark

import java.sql.{DriverManager, ResultSet}

import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Grant on 2018/1/4/004.
  */
object LoadByJDBC {
  def createConnection() = {
    Class.forName("com.mysql.jdbc.Driver").newInstance();
    DriverManager.getConnection("jdbc:mysql://172.19.1.76/bma_local?", "root", "123456")
  }

  def extractValues(r: ResultSet) = {
    (r.getString(5), r.getDate(3))
  }

  def main(args: Array[String]): Unit = {
    val SparkConf = new SparkConf().setAppName("LoadByJDBC")
    SparkConf.setMaster("local")
    SparkConf.set("executor-memory", "1G")
    SparkConf.set("total-executor-cores", "2")
    SparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").registerKryoClasses(Array(classOf[Student]))
    val sc = new SparkContext(SparkConf)

    // JdbcRDD 的第二个参数是 sql，比较有趣: 该 query 必须包含两个 ? 占位符，用于对结果进行 partition.
    // 最后一个参数是 mapRow: 这个函数是完成从 ResultSet 到单个 row 的转换，只需要调用 getInt, getString, 等等; 默认 function 是将一个 ResultSet 转化为 Object array.
    val data = new JdbcRDD(sc,
      createConnection, "SELECT * FROM auth_user WHERE ? <= id AND ID <= ?",
      lowerBound = 1, upperBound = 100, numPartitions = 3, mapRow = extractValues)

    println(data.collect().toList)

    sc.stop()
  }
}
