import tools._
import tools.Spark._

object Main extends App {
    val path1 = "../data/population"
    val drones = Parser.serializeDrone(path1)
    drones.map { drone => Storage.writeDrone(drone) }
    val logs = Parser.serializeLog(path1)
    logs.map { log => Storage.writeLog(log) }

    val default = "../data/logs"
    val path2 = scala.io.StdIn.readLine(s"Please enter the path to json logs ($default)")
    val stream = Parser.parse(path2, default)
    stream.map { elt => elt.map { json => Post.sendToBase(json) } }

    spark.stop
}
