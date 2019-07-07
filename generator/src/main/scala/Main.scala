import tools._
import tools.Spark._
import org.apache.spark.sql.functions._
import spark.implicits._ // implicit encoder for case classes, uses spark object

object Main extends App {
    Parser.serializePopulation("../data/population").foreach {
        df => Storage.write(df)
    }
    
    val default = "../data/logs"
    val path2 = scala.io.StdIn.readLine(s"Please enter the path to json logs ($default)")
    val list = Parser.parse(path2, default)
    list.map {
        df => println(df); df.select(to_json(struct(df.col("*")))).as[String].foreachPartition {
            Producer.writeIterToKafka(_)
        }
    }

    val ret = scala.io.StdIn.readChar()
    spark.stop
}
