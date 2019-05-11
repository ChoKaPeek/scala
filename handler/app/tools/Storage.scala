package tools

import com.datastax.spark.connector._
import org.apache.spark.sql.cassandra._
import org.apache.spark.sql._
import Models._
import Spark._

object Storage {
    def writeDrone(sequence: Seq[Drone]): Unit = {
        val rdd = sc.parallelize(sequence)
        rdd.saveToCassandra("test", "drone",
            SomeColumns("id", "brand"))
    }
    def writeLog(sequence: Seq[Log]): Unit = {
        val rdd = sc.parallelize(sequence)
        rdd.saveToCassandra("test", "log",
            SomeColumns("id", "id_drone", "speed", "altitude", "latitude", "longitude", "datetime", "temperature", "battery"))
    }
    
    def readDrone(): Array[Drone] = {
        sc.cassandraTable[Drone]("test", "drone").collect
    }

    def readLog(): Array[Log] = {
        sc.cassandraTable[Log]("test", "log").collect
    }
}
