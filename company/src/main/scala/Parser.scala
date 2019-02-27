import play.api.libs.json.Json
import scala.io.Source

class Parser(dir: String) {
    def parseJson(file: String): Unit = {
        val fileContents = Source.fromFile(file).getLines.mkString
        val parsed = Json.parse(fileContents)
        println(parsed)
    }
    def parseCSV(file: String): Unit = {
        println("Index, Girth, Height, Volume")
        val bufferedSource = Source.fromFile("example.csv")
        for (line <- bufferedSource.getLines) {
            val cols = line.split(",").map(_.trim)
            println(s"${cols(0)} | ${cols(1)} | ${cols(2)} | ${cols(3)}")
        }
        bufferedSource.close
    }
}
