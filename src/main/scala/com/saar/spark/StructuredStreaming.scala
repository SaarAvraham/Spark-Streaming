//package com.saar.spark
//
//import java.text.SimpleDateFormat
//import java.util.Locale
//import java.util.regex.{Matcher, Pattern}
//import org.apache.spark.sql.SparkSession
//import org.apache.spark.sql.execution.datasources.jdbc.JDBCOptions
//import com.saar.spark.Utilities._
//import org.apache.spark.sql._
//import org.apache.spark.sql.functions._
//import org.apache.spark.sql.streaming.{OutputMode, Trigger}
//
//object StructuredStreaming {
//   def main(args: Array[String]) {
//      // Use new SparkSession interface in Spark 2.0
//      val spark = SparkSession
//        .builder
//        .appName("StructuredStreaming")
//        .master("local[*]")
//        .config("spark.sql.warehouse.dir", "file:///C:/temp") // Necessary to work around a Windows bug in Spark 2.0.0; omit if you're not on Windows.
//        .config("spark.sql.streaming.checkpointLocation", "file:///C:/checkpoint")
//        .getOrCreate()
//
//      setupLogging()
//
//      val df = spark.readStream
//        .format("rate")
//        .option("numPartitions", "5")
//        .option("rowsPerSecond", "100")
//        .load()
//
//      val lines = df.select("value")
//      lines.printSchema()
//      println()
//
//      // write result
////      val query = lines.writeStream
////        .outputMode("append")
////        .format("streaming-jdbc")
////        .outputMode(OutputMode.Append)
////        .option(JDBCOptions.JDBC_URL, "jdbc:sqlserver://DESKTOP-S42L7FK\\MSSQLSERVER01;databaseName=d")
////        .option(JDBCOptions.JDBC_TABLE_NAME, "d")
////        .option(JDBCOptions.JDBC_DRIVER_CLASS, "com.microsoft.sqlserver.jdbc.SQLServerDriver")
////        .option(JDBCOptions.JDBC_BATCH_INSERT_SIZE, "5")
////        .option("user", "sa")
////        .option("password", "sa")
////        .trigger(Trigger.ProcessingTime("10 seconds"))
////        .start()
////
////      query.awaitTermination()
//   }
//}