import Drone._

object Main extends App {
    println("Welcome, please enter the path to given files")
    val path = scala.io.StdIn.readLine()
    val parser = new Parser(path)
    val storage = new Storage()
    val list = parser.parse()
    list.map {
      elt => storage.write(elt, "drone")
    }
}