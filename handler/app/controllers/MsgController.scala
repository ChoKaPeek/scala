package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

//case class Drone(id: Int, name: String, country: String)

class MsgController @Inject()(val controllerComponents: ControllerComponents)
  extends BaseController {
    def msg = Action {request =>
      implicit val droneReads = Json.reads[Drone]
      val json = request.body.asJson.get
      val drone = json.as[Drone]
      println(drone)
      Ok
    }
}
