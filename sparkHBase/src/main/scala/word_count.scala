
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

import org.apache.commons.io.FileUtils
import org.apache.commons.cli.Options
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs._
import org.apache.hadoop.io.compress.GzipCodec

object word_count {
  def main(args: Array[String]) {
    // create Spark context with Spark configuration
    // val sc = new SparkContext(new SparkConf().setAppName("Spark Count"))
    val conf = new SparkConf().setMaster("local[*]").setAppName("Spark Word Count")
    val sc = new SparkContext(conf)

    // get threshold
    val threshold = args(1).toInt

    // read in text file and split each document into words
    val tokenized = sc.textFile(args(0)).flatMap(_.split(" "))

    // count the occurrence of each word
    val wordCounts = tokenized.map((_, 1)).reduceByKey(_ + _)

    // filter out words with fewer than threshold occurrences
    val filtered = wordCounts.filter(_._2 >= threshold)

    // count characters
    val charCounts = filtered.flatMap(_._1.toCharArray).map((_, 1)).reduceByKey(_ + _)

    System.out.println(charCounts.collect().mkString(", "))

    // testing HDFS connectivity
    val hdfs = org.apache.hadoop.fs.FileSystem.get(new Configuration())

    val hdfsFilePath = "/user/arcadia"
    val files = hdfs.listFiles(new Path(hdfsFilePath), true)
    while (files.hasNext) {
      val file = files.next()
      println(file.getPath.toString)
    }

  }
}
