package controllers

import javax.inject._
import play.api.mvc._

class IndexController @Inject()(val controllerComponents: ControllerComponents)
  extends BaseController {
    def index() = Action {
      Ok("Hello, World!")
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
}
