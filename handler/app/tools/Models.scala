package tools

object Models {
    case class Drone(id: Int, brand: String)

    case class Log(id: Int, id_drone: Int, speed: Float, altitude: Float,
                   latitude: Double, longitude: Double, datetime: String,
                   temperature: Float, battery: Float)
}
