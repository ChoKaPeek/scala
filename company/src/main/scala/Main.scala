import parser.Parser

object Main extends App {
  println("Hello, World!")
  val parser = new Parser("tests")
  parser.parse()
}
