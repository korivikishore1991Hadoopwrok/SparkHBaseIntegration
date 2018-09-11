package com.sparkHBase

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.{HColumnDescriptor, HTableDescriptor}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.client.{HBaseAdmin, HTable, Put}

import org.apache.commons.io.FileUtils
import org.apache.commons.cli.Options
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs._
import org.apache.hadoop.io.compress.GzipCodec

object sparkHBase {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]"
    ).setAppName("Spark HBase Test")
    //val conf = new SparkConf().setAppName("Spark HBase Test")
    val sc = new SparkContext(conf)
    val hBaseconf = HBaseConfiguration.create()
    /*hBaseconf.addResource(new Path(
      "file:///etc/hbase/conf/hbase-site.xml"))*/

    // Checking for Table existences
    val tableName="spark_table"
    hBaseconf.set(TableInputFormat.INPUT_TABLE,tableName)
    val admin = new HBaseAdmin(hBaseconf)
    println("test result for table existence is %s".format(
      admin.isTableAvailable(tableName)))

    // Creation of Table
    val tableDesc = new HTableDescriptor(tableName)
    tableDesc.addFamily(new HColumnDescriptor(Bytes.toBytes("cf")))
    admin.createTable(tableDesc)

    // Writing the data using inline methods
    val table = new HTable(hBaseconf, tableName)
    var row = new Put(Bytes.toBytes("dummy"))
    row.add(Bytes.toBytes("cf"), Bytes.toBytes("content"),
      Bytes.toBytes("Test data"))
    table.put(row)
    table.flushCommits()

    // Writing the data using textfile
    val filesRDD = sc.wholeTextFiles("/user/demo")
    filesRDD.foreachPartition { iter =>
      val hbaseConf = HBaseConfiguration.create()
      val table = new HTable(hbaseConf, tableName)
      iter.foreach { x =>
        var p = new Put(Bytes.toBytes(x._1))
        p.add(Bytes.toBytes("cf"),Bytes.toBytes("content"),
          Bytes.toBytes(x._2))
        table.put(p)
      }}

    // Reading the Data
    val hBaseRDD = sc.newAPIHadoopRDD(hBaseconf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])
    println("Count of the number of rows found was %s".format(
      hBaseRDD.count()))
    val resultRDD = hBaseRDD.map(tuple => tuple._2)
    val keyValueRDD = resultRDD.map(result =>
      (Bytes.toString(result.getRow()).split(" ")(0),
        Bytes.toString(result.value)))
    println(keyValueRDD.collect().toList)
  }
}
