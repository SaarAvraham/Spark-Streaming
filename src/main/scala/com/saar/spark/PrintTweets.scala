

package com.saar.spark

import java.sql.DriverManager

import Utilities._
import com.rabbitmq.client.Channel
import com.rabbitmq.client.QueueingConsumer.Delivery
import org.apache.spark.streaming.rabbitmq.RabbitMQUtils
import org.apache.spark.streaming.scheduler.{StreamingListener, StreamingListenerBatchCompleted, StreamingListenerBatchStarted, StreamingListenerReceiverStarted}
import org.apache.spark.streaming.{Seconds, StreamingContext, rabbitmq}

import scala.util.{Failure, Success, Try}

/** Simple application to listen to a stream of Tweets and print them out */
object PrintTweets {

  /** Our main function where the action happens */
  def main(args: Array[String]) {
    setUpLoggingSl4J()

    // Set up a Spark streaming context named "PrintTweets" that runs locally using
    // all CPU cores and one-second batches of data
    val ssc = new StreamingContext("local[*]", "PrintTweets", Seconds(1))
    ssc.addStreamingListener(new StreamingListener {
      override def onBatchStarted(batchStarted: StreamingListenerBatchStarted): Unit = {
        println("## batch started!")
      }

      override def onBatchCompleted(batchCompleted: StreamingListenerBatchCompleted): Unit = {
        println("$$ batch completed!")
//        batchCompleted.batchInfo
//        batchCompleted.prod
      }
    })

    val messages = RabbitMQUtils.createStream[Message](ssc,
      Map("queueName" -> "SaarQueueNew3",
        "exchangeName" -> "tut.fanout",
        "password" -> "guest",
        "userName" -> "guest",
        "ackType" -> "auto"
      ), delivery => new Message(delivery, null: Channel));


    messages.foreachRDD {
      rdd =>
        if (!rdd.isEmpty()) {
          rdd.foreachPartition {
            part =>
              insertBatch(part)
            //              Try(insertBatch(part))
            //              match {
            //                case Success(data) =>
            //                  //Send ack if not set the auto ack property
            //                  consumer.sendBasicAck(delivery)
            //                case Failure(e) =>
            //                  //Send noack if not set the auto ack property
            //                  log.warn(s"failed to process message. Sending noack ...", e)
            //                  consumer.sendBasicNAck(delivery)
            //              }
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

  class Message(val delivery: Delivery, var channel: Channel) extends Serializable {
    def setChannel(channel1: Channel): Unit = {
      channel = channel1;
    }
  }

  private def insertBatch(part: Iterator[Message]): Unit = {
    val conn = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-S42L7FK\\MSSQLSERVER01;databaseName=Data-Lake", "sa1", "sa")
    val batchInsert = conn.prepareStatement("INSERT INTO dt (value) VALUES (?) ")
    var count = 0;

    for (message <- part) {
      batchInsert.setString(1, new Predef.String(message.delivery.getBody))
      batchInsert.addBatch()
      batchInsert.clearParameters()
      count = count + 1
    }

    val sum = batchInsert.executeBatch()
    println("lines inserted: " + sum)
    if (sum == count) {
      println("going to ack..")
      for (message <- part) {
        val channel = message.channel
        channel.basicAck(message.delivery.getEnvelope.getDeliveryTag, false)
      }
    }
  }
}