object Main extends App {
  println("Welcome, please enter the path to given files")
  val path = scala.io.StdIn.readLine()
  val parser = new Parser(path)
  parser.parse()
}