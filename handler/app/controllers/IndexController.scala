package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Reads._

import play.api.libs.functional.syntax._

import tools.Models._
import tools.Overrides._
import tools.Spark._
import tools._

import spark.implicits._

class IndexController @Inject()(val controllerComponents: ControllerComponents)
extends BaseController {
    def index() = Action {
        val user = Map("Undef" -> "N/A")
        val data = Storage.readLog().toList
        Ok(views.html.hello("Welcome to DroneTech", user, data))
    }

    def list() = Action {
        val user = Map("Undef" -> "N/A")
        val data = Storage.readLog().toList
        Ok(views.html.hello("Welcome to DroneTech", user, data))
    }

    def drones() = Action {
        val user = Map("Undef" -> "N/A")
        val data = Storage.readDrone().toList
        Ok(views.html.drones("Our list of drones", user, data))
    }

    // http method
    def msg() = Action(parse.json) {
        request => request.body.validate[Log].map { 
            log => Storage.write(Seq(log).toDF); Ok("Proper Json")
        }.recoverTotal {
            e => println("Detected an error"); BadRequest("Detected error:")
        }
    }

    def update() = Action {
        Consumer.consumeFromKafka("logs")
        val user = Map("Undef" -> "N/A")
        val data = Storage.readLog().toList
        Ok(views.html.hello("Welcome to DroneTech", user, data))
    }
  
  
}
