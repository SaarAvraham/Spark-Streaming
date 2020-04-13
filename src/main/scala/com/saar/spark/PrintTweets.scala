

package com.saar.spark

import java.sql.DriverManager

import Utilities._
import org.apache.spark.streaming.rabbitmq.RabbitMQUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/** Simple application to listen to a stream of Tweets and print them out */
object PrintTweets {
 
  /** Our main function where the action happens */
  def main(args: Array[String]) {
    setUpLoggingSl4J()
    setupTwitter()
    
    // Set up a Spark streaming context named "PrintTweets" that runs locally using
    // all CPU cores and one-second batches of data
    val ssc = new StreamingContext("local[*]", "PrintTweets", Seconds(1))

    val messages = RabbitMQUtils.createStream[String](ssc,
      Map("queueName" -> "SaarNewQueue3",
      "exchangeName" -> "tut.fanout",
      "password" -> "nice",
      "userName" -> "nice"
    ));

    messages.foreachRDD{
      rdd =>
        rdd.foreachPartition{
          part =>
            for (message <- part)
                    {
                      println("!@# " + message)

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