package tools

import play.api.libs.json._
import play.api.libs.json.Reads._

import Models._

object Overrides {
    implicit object LogReads extends Reads[Log] {
        def reads(json: JsValue) = JsSuccess(Log(  // Has to be a JsResult, should be a case with the possibility of a JsFailure
            (json \ "id").as[Int],
            (json \ "id_drone").as[Int],
            (json \ "speed").as[Float],
            (json \ "altitude").as[Float],
            (json \ "latitude").as[Double],
            (json \ "longitude").as[Double],
            (json \ "datetime").as[String],
            (json \ "temperature").as[Float],
            (json \ "battery").as[Float]
        ))
    }
}