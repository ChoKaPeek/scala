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

class FailuresController @Inject()(val controllerComponents: ControllerComponents)
extends BaseController {
    val max_altitude = 100
    val max_temperature = 80
    val min_temperature = 1

    def list() = Action {
        val user = Map("Undef" -> "N/A")
        val df = Storage.readLogDF()
            .where($"speed" === 0)

        val data = List(df.where($"altitude" > max_altitude || $"altitude" <= 0)
                .as[Log](logEncoder).collect.toList, 
            df.where($"temperature" > max_temperature || $"temperature" <= min_temperature)
                .as[Log](logEncoder).collect.toList,
            df.where($"battery" === 0)
                .as[Log](logEncoder).collect.toList,
            df.where($"battery" =!= 0 && $"altitude" <= max_altitude
                    && $"altitude" > 0 && $"temperature" <= max_temperature
                    && $"temperature" > min_temperature)
                .as[Log](logEncoder).collect.toList)
        val data_count = data.map(elt => elt.length)
        Ok(views.html.failures("Failures by type", user, data, data_count))
    }
}
