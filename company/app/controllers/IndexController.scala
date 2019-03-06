package controllers

import javax.inject._
import play.api.mvc._


class IndexController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
    def index() = Action {
      Ok("Hello, World!")
    }
}
