package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Reads._

import play.api.libs.functional.syntax._

import tools.Models._
import tools.Spark._
import tools.Storage._

class IndexController @Inject()(val controllerComponents: ControllerComponents)
  extends BaseController {
    def index() = Action {
        val user = Map("Undef" -> "N/A")
        val data = readLog()

        val rows: String = data.select("message").collect().map(_.getString(0)).mkString("\n")
        Ok(views.html.hello("Welcome to DroneTech", user, rows))
    }

    def list() = Action {
        val user = Map("Undef" -> "N/A")
        val data = readLog()

        val rows: String = data.select("message").collect().map(_.getString(0)).mkString("\n")
        Ok(views.html.hello("Welcome to DroneTech", user, rows))
    }

    def msg() = Action { request =>
        request.body.asJson.map { json =>
        json.validate[Log].map { 
          log => writeLog(log);
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
