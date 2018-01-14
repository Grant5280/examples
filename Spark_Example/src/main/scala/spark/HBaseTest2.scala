package spark

import org.apache.hadoop.hbase.client.HBaseAdmin
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.{HBaseConfiguration, HTableDescriptor, TableName}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Grant on 2018/1/3/003.
  */
object HBaseTest2 extends App {
  if (args.length < 1) {
    System.err.println("Usage: <table_name>")
    System.exit(1)
  }

  // please ensure HBASE_CONF_DIR is on classpath of spark driver
  // e.g: set it through spark.driver.extraClassPath property
  // in spark-defaults.conf or through --driver-class-path
  // command line option of spark-submit
  val conf = HBaseConfiguration.create()
  conf.set("hbase.zookeeper.property.clientPort", "2181")
  conf.set("hbase.zookeeper.quorum", "node1,node2,node3")
  conf.set("hbase.master", "node1:60000")
  conf.set(TableInputFormat.INPUT_TABLE, args(0))

  val SparkConf = new SparkConf().setAppName("AccumulatorTest")
  SparkConf.setMaster("local")
  SparkConf.set("executor-memory", "1G")
  SparkConf.set("total-executor-cores", "2")
  SparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").registerKryoClasses(Array(classOf[Student]))
  val sc = new SparkContext(SparkConf)

  // Initialize hBase table if necessary
  val admin = new HBaseAdmin(conf)
  if (!admin.isTableAvailable(args(0))) {
    val tableDesc = new HTableDescriptor(TableName.valueOf(args(0)))
    admin.createTable(tableDesc)
  }

  val hBaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat],
    classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
    classOf[org.apache.hadoop.hbase.client.Result])

  hBaseRDD.take(10).foreach(x => println(s"${x._1.toString}\t${x._2.toString}"))
  hBaseRDD.count()

  sc.stop()
  admin.close()
}
