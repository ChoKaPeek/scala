import play.api.libs.json.Json
import scala.io.Source

def parseJson(file: String):Void = {
    val fileContents = Source.fromFile(file).getLines.mkString
    val parsed = Json.parse(fileContents)
    println(parsed)
}
