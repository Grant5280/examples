package spark

import java.io.File

import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source

/**
  * Created by Grant on 2018/1/3/003.
  */
object FilesAndArchivesTest extends App{
  // 注意用户采用的是 --files localtest.txt#appSees.txt 形式进行提交的，参数也需要带一个 appSees.txt，这样我们知道
  if (args.length < 3) {
    System.err.println("Usage: <files> <archives> <words>" + "\n" +
      "files - (string) such as story.txt#st" + "\n" +
      "archives - (string) such as techtc300.zip#z" + "\n" +
      "words - (string) such as 'young,Englewood' etc")
    System.exit(1)
  }
  val conf = new SparkConf().setAppName("FilesAndArchivesTest")
  conf.setMaster("local")
  conf.set("executor-memory", "1G")
  conf.set("total-executor-cores", "2")
  val sc = new SparkContext(conf)

  val files = args(0)
  val archives = args(1)
  val words = args(2)

  // 找出 files 中包含 words 中单词的所有句子
  // 找出 archives 中包含 words 中单词的所有句子，以及打印出其文件名称
  println(s"files: ${files}, archives: ${archives}, words: ${words}")

  val r = sc.parallelize(words.split( """,""").toList).persist()

  r.collect().foreach(println)

  // 处理 files
  r.mapPartitions(p => {
    // 加载 files，注意 --files filename#sortname, 可以直接使用 sortname
    val lines = Source.fromFile(files).getLines()
    val words = p.toList // 这个非常重要，不能直接使用 p.exists(xxx), 因为 p 是一个迭代器，注意迭代器的一些 trick

    // 对于包含 p 中单词的，会进行打印
    val flines = lines.filter(x => {
      words.exists(x.indexOf(_) >= 0)
    })

    // 注意，这里的 flines 是一个迭代器，千万不要调用类似 foreach 的接口
    flines
  }).distinct().collect().foreach(x => println(s"files match -- ${x}"))

  // 处理 archives, 可以将一些文件压缩为一个 zip, tar.gz 等等
  r.mapPartitions(p => {
    //val dir = new File(archives + File.separator + "conf")
    val dir = new File(archives)
    val fileNameAndLines =
      for (f <- dir.listFiles()) yield {
        val fileName = f.getName
        val lines = Source.fromFile(f.getCanonicalPath).getLines()
        (fileName, lines)
      }

    val tmp = fileNameAndLines.flatMap({ case (fileName, lines) => for (line <- lines) yield (fileName, line) })
    val words = p.toList

    val flines = tmp.filter({
      case (fileName, line) => words.exists(line.indexOf(_) >= 0)
    })

    flines.toIterator
  }).distinct().collect().foreach(x => println(s"archives match -- ${x._1}/${x._2}"))

  sc.stop()
}
