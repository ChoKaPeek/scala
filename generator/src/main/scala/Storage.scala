import com.datastax.spark.connector._
import org.apache.spark.sql.cassandra._
import org.apache.spark.sql._
import Drone._
import Spark._

class Storage {
    def write (sequence: Seq[Drone], table: String): Unit = {
        val rdd = sc.parallelize(sequence)
        rdd.saveToCassandra("test", table,
            SomeColumns("id", "speed", "altitude", "latitude", "longitude", "datetime", "temperature", "battery"))
    }
    
    def read (table: String): Array[Drone] = {
        sc.cassandraTable[Drone]("test", table).collect
    }
}
