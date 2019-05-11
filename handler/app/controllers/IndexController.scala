package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.mvc._
import play.api.libs.json._

import play.api.libs.functional.syntax._

import org.apache.spark._
import org.apache.spark._

case class Drone(id: Int, speed: Float, altitude: Float, latitude: Double,
                 longitude: Double, datetime: String, temperature: Float,
                 battery: Float)

class IndexController @Inject()(val controllerComponents: ControllerComponents)
  extends BaseController {
    var fixme: List[Drone] = List();
    val spark = SparkSession.builder()
                            .master("local")
                            .appName("Company")
                            .config("spark.cassandra.connection.host", "localhost")
                            .getOrCreate()

    def index() = Action {
        val data: DataFrame = spark.read
                                   .cassandraFormat("POST_LINE", "keyspace")
                                   .options(ReadConf.SplitSizeInMBParam.option(32))
                                   .load()

        val rows: String = data.select("message").collect().map(_.getString(0)).mkString("\n")
        Ok(rows)
    }

    def list() = Action {
      val user = Map("Undef" -> "N/A")
      val posts = fixme
      Ok(views.html.hello("Welcome to DroneTech", user, posts))
    }

    implicit object DroneReads extends Reads[Drone] {
        def reads(json: JsValue) = JsSuccess(Drone(  // Has to be a JsResult, should be a case with the possibility of a JsFailure
            (json \ "id").as[Int],
            (json \ "speed").as[Float],
            (json \ "altitude").as[Float],
            (json \ "latitude").as[Double],
            (json \ "longitude").as[Double],
            (json \ "datetime").as[String],
            (json \ "temperature").as[Float],
            (json \ "battery").as[Float]
        ))
    }

    def msg = Action { request =>
        request.body.asJson.map { json =>
        json.validate[Drone].map { 
          Drone => fixme = Drone :: fixme;
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
