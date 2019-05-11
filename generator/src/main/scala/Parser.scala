import play.api.libs.json._
import scala.io.Source
import java.io.File
import java.util.ArrayList

/* Added for sending to handler */
import org.apache.commons._
import org.apache.http._
import org.apache.http.client._
import org.apache.http.client.methods.HttpPost
import org.apache.http.message.BasicNameValuePair
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.entity.StringEntity

class Parser(dir: String) {
    
  case class Drone(id: Int, speed: Float, altitude: Float, latitude: Double,
                   longitude: Double, datetime: String, temperature: Float,
                   battery: Float)


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

    def sendToBase(json_string: String) = {
      val url = "http://localhost:9000/msg";
      val post = new HttpPost(url)
      val client = new DefaultHttpClient
      println(json_string)
      post.setHeader("content-type", "application/json")
      post.setEntity(new StringEntity(json_string))
      val response = client.execute(post)
      //println("--- HEADERS ---")
      //response.getAllHeaders.foreach(arg => println(arg))
    }


    def parseJson(file: File): Unit = {
        println("PARSING JSON")
        val bufferedSource = Source.fromFile(file)
        val fileContents = bufferedSource.getLines().toVector.map {
            line => print("Processing: ");
                    println(line);
                    sendToBase(line)
        }
        bufferedSource.close()
        println(fileContents)
    }

    def parseCSV(file: File): Unit = {
        val bufferedSource = Source.fromFile(file)
        val fileContents = bufferedSource.getLines().drop(1).toVector.map {
            line => line.split(",").toVector.map(_.trim) match {
                case Vector(id, speed, altitude, latitude, longitude, datetime, temperature, battery) => Drone(id.toInt, speed.toFloat, altitude.toFloat, latitude.toDouble, longitude.toDouble, datetime, temperature.toFloat, battery.toInt)
                case _ => println(s"WARNING UNKNOWN DATA FORMAT FOR LINE: $line")
                          None
            }
        }
        bufferedSource.close()
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
