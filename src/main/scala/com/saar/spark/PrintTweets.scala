

package com.saar.spark

import java.sql.DriverManager

import Utilities._
import org.apache.spark.streaming.rabbitmq.RabbitMQUtils
import org.apache.spark.streaming.{Seconds, StreamingContext, rabbitmq}

/** Simple application to listen to a stream of Tweets and print them out */
object PrintTweets {

  /** Our main function where the action happens */
  def main(args: Array[String]) {
    setUpLoggingSl4J()

    // Set up a Spark streaming context named "PrintTweets" that runs locally using
    // all CPU cores and one-second batches of data
    val ssc = new StreamingContext("local[*]", "PrintTweets", Seconds(1))

    val messages = RabbitMQUtils.createStream[String](ssc,
      Map("queueName" -> "SaarQueueNew3",
      "exchangeName" -> "tut.fanout",
//      "password" -> "nice",
      "password" -> "guest",
//      "userName" -> "nice"
      "userName" -> "guest"
    ));

    messages.foreachRDD {
      rdd =>
        if(!rdd.isEmpty()){
          rdd.foreachPartition {
            part =>
//              val conn= DriverManager.getConnection(String.format("jdbc:sqlserver://%s:%s;databaseName=%s;integratedSecurity=false", "localhost", 1433.toString, "Data-Lake"), "sa", "sa")
              val conn = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-S42L7FK\\MSSQLSERVER01;databaseName=Data-Lake", "sa", "sa")
              val batchInsert = conn.prepareStatement("INSERT INTO dt (value) VALUES (?) ")

              for (body <- part) {
                batchInsert.setString(1, body)
                batchInsert.addBatch()
                batchInsert.clearParameters()
              }

              println("lines inserted: " + batchInsert.executeBatch().sum)
              conn.close()
          }
        }
    }
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

    ssc.start()
    ssc.awaitTermination()
  }
}