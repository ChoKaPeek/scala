import tools._
import tools.Spark._
import org.apache.spark.sql.functions._
import spark.implicits._ // implicit encoder for case classes, uses spark object

object Main extends App {
    Parser.serializePopulation("../data/population").foreach {
        df => Storage.write(df)
    }
    
    val default = "../data/logs"
    val path2 = scala.io.StdIn.readLine(s"Please enter the path to logs ($default)")
    val list = Parser.parse(path2, default)
    val sinks = list.map {
        df => df.printSchema; Producer.writeStreamToKafka(df, "logs")
    }
    sinks.map {
        sink => sink.awaitTermination()
    }
    
    spark.stop()
}
