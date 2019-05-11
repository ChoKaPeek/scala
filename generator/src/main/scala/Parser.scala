import scala.io.Source
import java.io.File
import java.util.ArrayList
import Drone._
import collection.JavaConversions._
import Spark._
import org.apache.spark.sql.types._
import spark.implicits._ // implicit encoder for case classes

class Parser(dir: String) {

    def parseJson(file: File): Seq[Drone] = {
        val df = spark.read.json(file.getPath()) // may need to close fd
            .select($"id".cast(IntegerType),
                    $"speed".cast(FloatType), $"altitude".cast(FloatType), $"latitude".cast(DoubleType),
                    $"longitude".cast(DoubleType), $"datetime", $"temperature".cast(FloatType),
                    $"battery".cast(FloatType))
        collection.Seq() ++ df.as[Drone].collect
    }

    def parseCSV(file: File): Seq[Drone] = {
        val bufferedSource = Source.fromFile(file)
        val fileContents = bufferedSource.getLines().drop(1).map {
            line => line.split(",").toVector.map(_.trim) match {
                case Vector(id, speed, altitude, latitude, longitude, datetime, temperature, battery) => Drone(id.toInt, speed.toFloat, altitude.toFloat, latitude.toDouble, longitude.toDouble, datetime, temperature.toFloat, battery.toFloat)
                // TODO Handle others cases
            }
        }
  
        // conversion to seq
        collection.Seq() ++ fileContents
    }

    def parse(): List[Seq[Drone]] = {
        println("CSV FILES:")
        println(getListOfFiles(dir, List(".csv")))
        val list_seq_csv = getListOfFiles(dir, List(".csv")).map(parseCSV(_))
        println("JSON FILES:")
        println(getListOfFiles(dir, List(".json")))
        val list_seq_json = getListOfFiles(dir, List(".json")).map(parseJson(_))

        list_seq_csv ++ list_seq_json
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
