package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Drone(id: Int, name: String, country: String)

class IndexController @Inject()(val controllerComponents: ControllerComponents)
  extends BaseController {
    var fixme: List[Drone] = List();
    def index() = Action {
      Ok(fixme.toString())
    }

    def hello() = Action {
      //val user = Map("username" -> "login_x")
      val user = Map("Undef" -> "N/A")
      val posts = List(
        Map(
          "author" -> "login_x",
          "body" -> "Getting started with Play"
          ),
        Map(
          "author" -> "ing1",
          "body" -> "Getting started with C"
          )
        )
      Ok(views.html.hello("Welcome to DroneTech", user, posts))
    }

    def msg() = Action {request =>
        implicit val droneReads = Json.reads[Drone]
        val json = request.body.asJson.get
        val drone = json.as[Drone]
        fixme = drone :: fixme;
        println(drone)
        Ok
    }
}
