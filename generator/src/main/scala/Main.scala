import tools._
import tools.Spark._

object Main extends App {
    val population_path = "../data/population"
    val objects = Parser.serialize(population_path)
    Parser.serialize(population_path).map {
        obj => Storage.write(obj)
    }
    
    val default = "../data/logs"
    val path2 = scala.io.StdIn.readLine(s"Please enter the path to json logs ($default)")
    val stream = Parser.parse(path2, default)
    stream.map {
        elt => elt.map {
            json => Post.sendToBase(json)
        } 
    }

    spark.stop
}
