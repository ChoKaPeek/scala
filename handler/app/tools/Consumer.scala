package tools

import java.util
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Properties
import scala.collection.JavaConverters._
import Spark._
import Models._
import org.apache.spark.sql.functions._

import spark.implicits._

object Consumer {
    def consumeOnce(topic: String) = {
        val dfs = spark
            .read
            .format("kafka")
            .option("kafka.bootstrap.servers", "localhost:9092")
            .option("subscribe", topic)
            .load()
        val df = dfs.selectExpr("CAST(value AS STRING)")
            .select(from_json($"value", logSchema) as "data")
            .select("data.*")
        Storage.write(df)
    }

    def consumeFromKafka(topic: String) = {
        val dfs = spark
            .readStream
            .format("kafka")
            .option("kafka.bootstrap.servers", "localhost:9092")
            .option("subscribe", topic)
            .load()
        val df = dfs.selectExpr("CAST(value AS STRING)")
            .select(from_json($"value", logSchema) as "data")
            .select("data.*")
        Storage.writeStream(df)
    }
}