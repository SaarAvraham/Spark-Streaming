//
//
//package com.saar.spark
//
//import java.sql.DriverManager
//
//import com.saar.spark.Utilities._
////import org.apache.spark.sql.SparkSession
////import org.apache.spark.sql.execution.datasources.jdbc.JdbcUtils
//import org.apache.spark.streaming.twitter.TwitterUtils
//import org.apache.spark.streaming.{Seconds, StreamingContext}
///** Simple application to listen to a stream of Tweets and print them out */
//object PrintTweetsDF {
//
//  /** Our main function where the action happens */
//  def main(args: Array[String]) {
//
//    // Configure Twitter credentials using twitter.txt
//    setupTwitter()
//
//    // Use new SparkSession interface in Spark 2.0
//    val spark = SparkSession
//      .builder
//      .appName("StructuredStreaming")
//      .master("local[*]")
//      .config("spark.sql.warehouse.dir", "file:///C:/temp") // Necessary to work around a Windows bug in Spark 2.0.0; omit if you're not on Windows.
//      .config("spark.sql.streaming.checkpointLocation", "file:///C:/checkpoint")
//      .getOrCreate()
//
//    setupLogging()
//
//    // Get rid of log spam (should be called after the context is set up)
//    setupLogging()
//
//    val ssc = new StreamingContext(spark.sparkContext,Seconds(1))
//    // Create a DStream from Twitter using our streaming context
//    val tweets = TwitterUtils.createStream(ssc, None)
//
//    tweets.foreachRDD{
//      rdd =>
//        rdd.foreachPartition { part =>
//          val conn= DriverManager.getConnection("jdbc:sqlserver://DESKTOP-S42L7FK\\MSSQLSERVER01;databaseName=d", "sa", "sa")
//          val batchInsert = conn.prepareStatement ("INSERT INTO dt (value) VALUES (?) ")
//
//          //        batchInsert.setString(1,"%s".format(rdd.id))
//          //        batchInsert.addBatch()
//          //        batchInsert.clearParameters()
//          for (row <- part)
//          {
//            batchInsert.setString(1,"%s".format(row.getText))
//            batchInsert.addBatch()
//            batchInsert.clearParameters()
//          }
//
//          println("lines inserted: "+ batchInsert.executeBatch().sum)
//        }
//    }
//
//    // Kick it all off
//    ssc.start()
//    ssc.awaitTermination()
//  }
//}