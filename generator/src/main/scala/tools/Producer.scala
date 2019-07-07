package tools

import java.util.Properties
import org.apache.kafka.clients.producer._

class Producer {
    def writeIterToKafka(iter: Iterator[String], key: String = null) = {
        val props = new Properties()
        props.put("bootstrap.servers", "localhost:9092")
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        val producer = new KafkaProducer[String, String](props)

        iter.foreach(str => writeToKafka("logs", producer, key, str))
        
        producer.close()
    }

    def writeToKafka(topic: String, producer: KafkaProducer, key: String, value: String): Unit = {
        val record = new ProducerRecord[String, String](topic, key, value)
        producer.send(record)
    }
}