package spark

import java.io.File

import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source

/**
  * Created by Grant on 2018/1/4/004.
  */
object HDFSTest {
  private def printUsage(): Unit = {
    val usage: String =
      "Usage: localFile dfsDir\n" +
        "\n" +
        "localFile - (string) local file to use in test\n" +
        "dfsDir - (string) DFS directory for read/write tests\n"

    println(usage)
  }

  def runLocalWordCount(fileContents: List[String]): Int = {
    fileContents.flatMap(_.split("\\s+"))
      .filter(_.nonEmpty)
      .groupBy(w => w)
      .mapValues(_.size)
      .values
      .sum
  }

  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      printUsage()
      System.exit(1)
    }

    val SparkConf = new SparkConf().setAppName("HdfsFileTest")
    SparkConf.setMaster("local")
    SparkConf.set("executor-memory", "1G")
    SparkConf.set("total-executor-cores", "2")
    SparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").registerKryoClasses(Array(classOf[Student]))
    val sc = new SparkContext(SparkConf)

    val localFilePath = args(0)
    val dfsDirPath = args(1)
    val lineList = Source.fromFile(new File(localFilePath)).getLines().toList

    val localWordCount = runLocalWordCount(lineList)

    sc.parallelize(lineList).saveAsTextFile(dfsDirPath)

    val dfsWordCount = sc.textFile(dfsDirPath).
      flatMap(_.split("\\s+")).
      filter(_.nonEmpty).
      map(w => (w, 1)).
      countByKey().
      values.
      sum

    sc.stop()

    if (localWordCount == dfsWordCount) {
      println(s"Success! Local Word Count ($localWordCount) " +
        s"and DFS Word Count ($dfsWordCount) agree.")
    } else {
      println(s"Failure! Local Word Count ($localWordCount) " +
        s"and DFS Word Count ($dfsWordCount) disagree.")
    }
  }
}
