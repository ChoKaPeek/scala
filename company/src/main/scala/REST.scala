package controllers

import play.api
import play.api.mvc._


object Application extends Controller {
  def getName = Action {
    Ok("Jim")
  }
}
