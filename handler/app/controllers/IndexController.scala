package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Reads._

import play.api.libs.functional.syntax._

import tools.Models._
import tools.Overrides._
import tools.Spark._
import tools.Storage._

class IndexController @Inject()(val controllerComponents: ControllerComponents)
  extends BaseController {
    def index() = Action {
        val user = Map("Undef" -> "N/A")
        Ok(views.html.hello("Welcome to DroneTech", user))
    }

    def list() = Action {
        val user = Map("Undef" -> "N/A")
        val data = readLog().toList
        Ok(views.html.hello("Welcome to DroneTech", user, data))
    }

    def msg() = Action { request =>
        request.body.asJson.map { json =>
        json.validate[Log].map { 
          log => writeLog(Seq(log));
                 Ok("Proper Json");
        }.recoverTotal{
          e =>  println("Detected an error");
                BadRequest("Detected error:");
        }
      }.getOrElse {
        println("Completely failed");
        BadRequest("Expecting Json data")
      }
    }
}
