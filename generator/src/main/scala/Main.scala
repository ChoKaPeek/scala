import tools._
import tools.Spark._

object Main extends App {
    //val path1 = "../data/population"
    //val drones = Parser.serializeDrone(path1)
    //drones.map { drone => Storage.writeDrone(drone) }
    //val logs = Parser.serializeLog(path1)
    //logs.map { log => Storage.writeLog(log) }

    println("Please enter the path to json logs (../data/logs)")
    val path2 = scala.io.StdIn.readLine()
    val stream = Parser.parse(path2)
    stream.map { elt => elt.map { json => Post.sendToBase(json) } }

    spark.stop
}