# SparkHBaseIntegration  
Hbase integration with Spark. A sample project is given in Scala  
  
## Table of Contents  
## Starting Hbase	2  
## Change Ambari configuration on Hbase to enable phoenix	2  
## Change to phoenix dir	2  
## launch Phoenix shell	2  
## User directories for Spark and Hbase	2  
## Setting classPath for jars copied from HBase libraries	2  
## Integrating HBase with Saprk	2  
### By jar file transfer to automate integration	2  
### By setting ClassPath and HBase config to manually integrate	3  
## Testing using spark-shell for imports.	4  
## Testing for connection to HBase	4  
## Other methods for HBase and Spark Integration	4  
## Executing with jars and config file for Hbase in YARN mode
## executing HBase CMD in a script from CMD line.
## Retriving Data from HBase shell

  
```code
# Starting Hbase 
hbase shell

# Change Ambari configuration on Hbase to enable phoenix

# Change to phoenix dir
cd /usr/hdp/current/phoenix-client/bin

# launch Phoenix shell
./sqlline.py localhost

# User directories for Spark and Hbase
/usr/hdp/current/spark-client/lib
/usr/hdp/current/hbase-master/lib
/usr/hdp/current/hbase-client/lib

# Setting classPath for jars copied from HBase libraries
export SPARK_CLASSPATH=/usr/hdp/current/spark-client/lib/*

# Integrating HBase with Saprk

##By jar file transfer to automate integration
(Pass library jar files path to spark library) Transfer all the jar files from “/usr/hdp/current/hbase-master/lib” to “/usr/hdp/current/spark-client/lib”.
(Pass library jar files path to spark class path) edit the file “/usr/hdp/current/spark-client/conf/spark-env.sh” to include.
# Setting spark classpath for integration of HBase
export SPARK_CLASSPATH=/usr/hdp/current/spark-client/lib/*
(Pass HBase Zookepper configurations to spark) create a soft link in “/etc/spark/conf/hbase-site.xml” for “/etc/hbase/conf/hbase-site.xml” using.
ln -s /etc/hbase/conf/hbase-site.xml /etc/spark/conf/hbase-site.xml

## By setting ClassPath and HBase config to manually integrate
 (Pass library jar files path to spark class path) export all HBase jar library files using SPARK_CLASSPATH.
export SPARK_CLASSPATH=/usr/hdp/current/hbase-client/lib/hbase-annotations.jar:/usr/hdp/current/hbase-client/lib/hbase-client.jar:/usr/hdp/current/hbase-client/lib/hbase-common.jar:/usr/hdp/current/hbase-client/lib/hbase-examples.jar:/usr/hdp/current/hbase-client/lib/hbase-hadoop-compat.jar:/usr/hdp/current/hbase-client/lib/hbase-hadoop2-compat.jar:/usr/hdp/current/hbase-client/lib/hbase-it.jar:/usr/hdp/current/hbase-client/lib/hbase-prefix-tree.jar:/usr/hdp/current/hbase-client/lib/hbase-procedure.jar:/usr/hdp/current/hbase-client/lib/hbase-protocol.jar:/usr/hdp/current/hbase-client/lib/hbase-resource-bundle.jar:/usr/hdp/current/hbase-client/lib/hbase-rest.jar:/usr/hdp/current/hbase-client/lib/hbase-server.jar:/usr/hdp/current/hbase-client/lib/hbase-shell.jar:/usr/hdp/current/hbase-client/lib/hbase-thrift.jar:/usr/hdp/current/hbase-client/lib/zookeeper.jar:/usr/hdp/current/hbase-client/lib/phoenix-server.jar:etc..
 (Pass library jar files path to spark library) launch “spark-shell” using all the HBase library jar files.
spark-shell --master yarn --jars="\
/usr/hdp/current/hbase-client/lib/hbase-annotations.jar,\
/usr/hdp/current/hbase-client/lib/hbase-client.jar,\
/usr/hdp/current/hbase-client/lib/hbase-common.jar,\
/usr/hdp/current/hbase-client/lib/hbase-examples.jar,\
/usr/hdp/current/hbase-client/lib/hbase-hadoop-compat.jar,\
/usr/hdp/current/hbase-client/lib/hbase-hadoop2-compat.jar,\
/usr/hdp/current/hbase-client/lib/hbase-it.jar,\
/usr/hdp/current/hbase-client/lib/hbase-prefix-tree.jar,\
/usr/hdp/current/hbase-client/lib/hbase-procedure.jar,\
/usr/hdp/current/hbase-client/lib/hbase-protocol.jar,\
/usr/hdp/current/hbase-client/lib/hbase-resource-bundle.jar,\
/usr/hdp/current/hbase-client/lib/hbase-rest.jar,\
/usr/hdp/current/hbase-client/lib/hbase-server.jar,\
/usr/hdp/current/hbase-client/lib/hbase-shell.jar,\
/usr/hdp/current/hbase-client/lib/hbase-thrift.jar,\
/usr/hdp/current/hbase-client/lib/zookeeper.jar,\
/usr/hdp/current/hbase-client/lib/phoenix-server.jar,\
etc.."
(Pass HBase Zookepper configurations to spark) pass the HBase and Zookepper configuration in the program using HBase configuration.
val conf = HBaseConfiguration.create()
conf.addResource(new Path("file:///etc/hbase/conf/hbase-site.xml"))

# Testing using spark-shell for imports.
    import org.apache.spark._
    import org.apache.spark.rdd.NewHadoopRDD
    import org.apache.hadoop.hbase.{HBaseConfiguration, HTableDescriptor}
    import org.apache.hadoop.hbase.client.HBaseAdmin
    import org.apache.hadoop.hbase.mapreduce.TableInputFormat
    import org.apache.hadoop.fs.Path;
    import org.apache.hadoop.hbase.HColumnDescriptor
    import org.apache.hadoop.hbase.util.Bytes
    import org.apache.hadoop.hbase.client.Put;
    import org.apache.hadoop.hbase.client.HTable;

# Testing for connection to HBase
    import org.apache.hadoop.hbase.HBaseConfiguration
    val conf = HBaseConfiguration.create()
    val tableName="spark_table"
    import org.apache.hadoop.hbase.mapreduce.TableInputFormat
    conf.addResource(new Path("file:///etc/hbase/conf/hbase-site.xml"))
    conf.set(TableInputFormat.INPUT_TABLE,tableName)
    import org.apache.hadoop.hbase.client.HBaseAdmin
    val admin = new HBaseAdmin(conf)
    admin.isTableAvailable(tableName)

# Other methods for HBase and Spark Integration
SPARK_CLASSPATH was detected (set to '/usr/hdp/current/spark-client/lib/*').
This is deprecated in Spark 1.0+.

Please instead use:
 - ./spark-submit with --driver-class-path to augment the driver classpath
 - spark.executor.extraClassPath to augment the executor classpath
 
# Executing with jars and config file for Hbase in YARN mode
spark-submit --class "com.sparkHBase.sparkHBase" --master yarn --deploy-mode client --num-executors 120 --driver-memory 17g --executor-cores 3 --executor-memory 17g --jars /usr/hdp/current/hbase-client/lib/hbase-client.jar,/usr/hdp/current/hbase-client/lib/hbase-common.jar,/usr/hdp/current/hbase-client/lib/hbase-server.jar,/usr/hdp/current/hbase-client/lib/guava-12.0.1.jar,/usr/hdp/current/hbase-client/lib/hbase-protocol.jar,/usr/hdp/current/hbase-client/lib/htrace-core-3.1.0-incubating.jar --files /etc/hbase/conf/hbase-site.xml /home/arcadia/sparkhbase.jar>output.log 2>&1

# executing HBase CMD in a script from CMD line.
$./asteroids.sh | hbase shell -n
$ hbase shell ./asteroids.sh

# Retriving Data from HBase shell
t = get_table 'hbase:meta'
t.scan, {
limit 1
}
```
