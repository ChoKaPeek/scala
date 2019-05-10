import play.api.libs.json._
import scala.io.Source
import java.io.File

class Parser(dir: String) {
    
    case class Drone(id: Int, speed: Float, altitude: Float, latitude: Double, longitude: Double, datetime: String, temperature: Int)

    implicit object DroneReads extends Reads[Drone] {
        def reads(json: JsValue) = JsSuccess(Drone(  // Has to be a JsResult, should be a case with the possibility of a JsFailure
            (json \ "id").as[Int],
            (json \ "speed").as[Float],
            (json \ "altitude").as[Float],
            (json \ "latitude").as[Double],
            (json \ "longitude").as[Double],
            (json \ "datetime").as[String],
            (json \ "temperature").as[Int]
        ))
    }

    def parseJson(file: File): Unit = {
        val fileContents = Source.fromFile(file).getLines().toVector.map {
            line => (Json.parse(line.mkString))
        }

        println(fileContents)
    }

    def parseCSV(file: File): Unit = {
        val fileContents = Source.fromFile(file).getLines().drop(1).toVector.map {
            line => line.split(",").toVector.map(_.trim) match {
                case Vector(id, speed, altitude, latitude, longitude, datetime, temperature) => Drone(id.toInt, speed.toFloat, altitude.toFloat, latitude.toDouble, longitude.toDouble, datetime, temperature.toInt)
                case _ => println(s"WARNING UNKNOWN DATA FORMAT FOR LINE: $line")
                          None
            }
        }
     
        println(fileContents)
    }

    def parse() = {
      println("CSV FILES:")
      println(getListOfFiles(dir, List(".csv")))
      getListOfFiles(dir, List(".csv")).map(parseCSV(_))
      println("JSON FILES:")
      println(getListOfFiles(dir, List(".json")))
      getListOfFiles(dir, List(".json")).map(parseJson(_))
    }

    def getListOfFiles(path: String, extensions: List[String]): List[File] = {
      val files = listFilesR(path)
      files.filter{file => extensions.exists(file.getName.endsWith(_))}
    }

    def listAllFiles(path: String): List[File] = {
        val directory = new File(path)
        if (directory.exists && directory.isDirectory) {
            directory.listFiles.toList
        }
        else {
            List[File]()
        }
    }

    def listDirectories(path: String): List[File] = {
        listAllFiles(path).filter(_.isDirectory)
    }

    def listFiles(path: String): List[File] = {
        listAllFiles(path).filter{
          f => f.isFile && (f.getName.endsWith(".csv")
            || f.getName.endsWith(".json")) }
    }

    def listFilesR_aux(directories: List[File], files: List[File]) : List[File] = {
        directories match {
            case Nil => files
            case d::tail => listFilesR_aux(tail, files:::listFiles(d.getPath()))
        }
    }

    def listFilesR(path: String) : List[File] = {
        listFilesR_aux(listDirectories(path), listFiles(path))
    }
}
