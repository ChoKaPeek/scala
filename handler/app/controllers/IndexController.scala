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
        val data = readLog().toList
        Ok(views.html.hello("Welcome to DroneTech", user, data))
    }

    def list() = Action {
        val user = Map("Undef" -> "N/A")
        val data = readLog().toList
        Ok(views.html.hello("Welcome to DroneTech", user, data))
    }

    def msg() = Action { request =>
            spark.read.json(Seq(request.body).toDS).collect.toVector match {
                case Vector(id, id_drone, speed, altitude, latitude, longitude, datetime, temperature, battery) => writeLog(Seq(Log(id.toInt, id_drone.toInt, speed.toFloat, altitude.toFloat, latitude.toDouble, longitude.toDouble, datetime, temperature.toFloat, battery.toFloat)));
                                                                                                                  Ok("Proper Json")
                case _ => println("Detected an error");
                          BadRequest("Detected error:")
            }
        }.getOrElse {
            println("Completely failed");
            BadRequest("Expecting Json data")
        }
    }
}
