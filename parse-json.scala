import play.api.libs.json._
import play.api.libs.json._
 
val rawJson = """{"hello": "world", "age": 42}"""
rawJson: String = {"hello": "world", "age": 42}
 
Json.parse(rawJson)
res0: play.api.libs.json.JsValue = {"hello":"world","age":42}
