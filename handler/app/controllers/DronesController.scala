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

class DronesController @Inject()(val controllerComponents: ControllerComponents)
extends BaseController {
    def list() = Action {
        val user = Map("Undef" -> "N/A")
        val data = Storage.readDroneDF()
            .orderBy($"id")
            .as[Drone](droneEncoder).collect.toList
        Ok(views.html.drones("Our list of drones", user, data))
    }

    def show(id: Long) = Action {
        val user = Map("Undef" -> "N/A")
        val data = Storage.readLogDF()
            .where($"id_drone" === id)
            .orderBy($"datetime")
            .as[Log](logEncoder).collect.toList
        Ok(views.html.drone("Drone " + id, user, data))
    }
}
