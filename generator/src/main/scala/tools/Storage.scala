package tools

import com.datastax.spark.connector._
import org.apache.spark.sql.cassandra._
import org.apache.spark.sql._
import Models._
import Spark._

object Storage {
    def isLog(x: Row): Boolean = x.toSeq match {
        case Seq(id, brand) => false
        case _ => true
    }

    def write(df: DataFrame): Unit = {
        val table = if (df.limit(1).filter{isLog(_)}.count() == 0) "drone" else "log"
        
        df.write.cassandraFormat(table, "test")
            .mode(SaveMode.Append).save()
    }

    def writeStream(df: DataFrame): Unit = {
        df.writeStream.foreachBatch {
            (batchDF, _) => batchDF.write
                .cassandraFormat("log", "test")
                .mode(SaveMode.Append)
                .save()
        }.start()
    }

    def readDrone(): Array[Drone] = {
        spark.read.cassandraFormat("drone", "test")
            .load().as[Drone](droneEncoder).collect
    }

    def readLog(): Array[Log] = {
        readLogDF.as[Log](logEncoder).collect
    }

    def readLogDF(): DataFrame = {
        spark.read.cassandraFormat("log", "test")
            .load()
    }
}
