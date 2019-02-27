import play.api.libs.json._
import scala.io.Source

val fileContents = Source.fromFile(filename).getLines.mkString
Json.parse(rawJson)
//res0: play.api.libs.json.JsValue = RESULT_HERE
