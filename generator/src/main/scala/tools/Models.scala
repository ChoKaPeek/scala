package tools

import org.apache.spark.sql._
import Spark._

import spark.implicits._

object Models {
    val logEncoder = Encoders.product[Log]
    val droneEncoder = Encoders.product[Drone]

    val logSchema = implicitly[Encoder[Log]].schema
    val droneSchema = implicitly[Encoder[Drone]].schema

    sealed trait Model {
        def id: Int
    }
    
    case class Drone
    (
        id: Int,
        brand: String
    ) extends Model

    case class Log
    (
        id: Int,
        id_drone: Int,
        speed: Float,
        altitude: Double,
        latitude: Double,
        longitude: Double,
        datetime: String,
        temperature: Float,
        battery: Float
    ) extends Model
}
