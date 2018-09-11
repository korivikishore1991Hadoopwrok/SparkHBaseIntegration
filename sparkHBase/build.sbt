name := "sparkHBase"

version := "0.1"

scalaVersion := "2.10.5"

val sparkVersion = "1.6.1"

val hBaseVersion = "1.1.2"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-hive" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "org.apache.hbase" % "hbase-server" % hBaseVersion,
  "org.apache.hbase" % "hbase-client" % hBaseVersion,
  "org.apache.hbase" % "hbase-common" % hBaseVersion,
  "org.apache.hadoop" % "hadoop-common" % "2.7.1",
  "org.apache.hadoop" % "hadoop-hdfs" % "2.7.1"
)