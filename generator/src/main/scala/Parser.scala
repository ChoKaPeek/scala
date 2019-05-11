import scala.io.Source
import java.io.File
import java.util.ArrayList
import Models._
import collection.JavaConversions._
import Spark._
import org.apache.spark.sql.types._
import spark.implicits._ // implicit encoder for case classes

object Parser {

    def parseDroneJson(file: File): Seq[Drone] = {
        val df = spark.read.json(file.getPath()) // may need to close fd
            .select($"id".cast(IntegerType), $"brand")
        collection.Seq() ++ df.as[Drone].collect
    }

    def parseLogJson(file: File): Seq[Log] = {
        val df = spark.read.json(file.getPath()) // may need to close fd
            .select($"id".cast(IntegerType), $"id_drone".cast(IntegerType),
                    $"speed".cast(FloatType), $"altitude".cast(FloatType), $"latitude".cast(DoubleType),
                    $"longitude".cast(DoubleType), $"datetime", $"temperature".cast(FloatType),
                    $"battery".cast(FloatType))
        collection.Seq() ++ df.as[Log].collect
    }

    def parseDroneCSV(file: File): Seq[Drone] = {
        val bufferedSource = Source.fromFile(file)
        val fileContents = bufferedSource.getLines().drop(1).map {
            line => line.split(",").toVector.map(_.trim) match {
                case Vector(id, brand) => Drone(id.toInt, brand)
                // TODO Handle others cases
            }
        }
  
        // conversion to seq
        collection.Seq() ++ fileContents
    }

    def parseLogCSV(file: File): Seq[Log] = {
        val bufferedSource = Source.fromFile(file)
        val fileContents = bufferedSource.getLines().drop(1).map {
            line => line.split(",").toVector.map(_.trim) match {
                case Vector(id, id_drone, speed, altitude, latitude, longitude, datetime, temperature, battery) => Log(id.toInt, id_drone.toInt, speed.toFloat, altitude.toFloat, latitude.toDouble, longitude.toDouble, datetime, temperature.toFloat, battery.toFloat)
                // TODO Handle others cases
            }
        }
  
        // conversion to seq
        collection.Seq() ++ fileContents
    }

    def parseJson(file: File): Seq[String] = {
        val df = spark.read.json(file.getPath()) // may need to close fd
        df.map(row => row.mkString).collect
    }

    def serializeLog(dir: String): List[Seq[Log]] = {
        val new_dir = dir + "/log"
        val list_seq_csv = getListOfFiles(new_dir, List(".csv")).map(parseLogCSV(_))
        val list_seq_json = getListOfFiles(new_dir, List(".json")).map(parseLogJson(_))
        list_seq_csv ++ list_seq_json
    }

    def serializeDrone(dir: String): List[Seq[Drone]] = {
        val new_dir = dir + "/drone"
        val list_seq_csv = getListOfFiles(new_dir, List(".csv")).map(parseDroneCSV(_))
        val list_seq_json = getListOfFiles(new_dir, List(".json")).map(parseDroneJson(_))
        list_seq_csv ++ list_seq_json
    }

    def parse(dir: String): List[Seq[String]] = {
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
