package tools

import java.util.Properties
import org.apache.kafka.clients.producer._
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

object Producer {
    def writeStreamToKafka(df: DataFrame, topic: String) = {
        df.select(col("id") as "key", to_json(struct(col("*"))) as "value")
            .selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
            .writeStream
            .format("kafka")
            .option("kafka.bootstrap.servers", "localhost:9092")
            .option("topic", topic)
            .start()
    }
}