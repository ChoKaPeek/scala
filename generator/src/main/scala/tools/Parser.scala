package tools

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

    def parseJson(file: File): Seq[Any] = {
        val df = spark.read.json(file.getPath()) // may need to close fd

        df.as[Log]
         match .select($"id".cast(IntegerType), $"id_drone".cast(IntegerType),
                    $"speed".cast(FloatType), $"altitude".cast(FloatType), $"latitude".cast(DoubleType),
                    $"longitude".cast(DoubleType), $"datetime", $"temperature".cast(FloatType),
                    $"battery".cast(FloatType))
        collection.Seq() ++ df.as[Log].collect
    }

    def parseCSV(file: File): Seq[Any] = {
        val bufferedSource = Source.fromFile(file)
        val fileContents = bufferedSource.getLines().drop(1).map {
            line => line.split(",").toVector.map(_.trim) match {
                case Vector(id, id_drone, speed, altitude, latitude, longitude, datetime, temperature, battery) => Log(id.toInt, id_drone.toInt, speed.toFloat, altitude.toFloat, latitude.toDouble, longitude.toDouble, datetime, temperature.toFloat, battery.toFloat)
                case Vector(id, brand) => Drone(id.toInt, brand)
                // TODO Handle others cases
            }
        }

        // conversion to seq
        collection.Seq() ++ fileContents
    }

    def parseJson(file: File): List[String] = {
        Source.fromFile(file).getLines().toList
    }

    def serializePopulation(dir: String): List[Seq[Any]] = {
        val list_seq_csv = getFiles(dir, List(".csv")).map(parseCSV(_))
        val list_seq_json = getFiles(dir, List(".json")).map(parseJson(_))
        list_seq_csv ++ list_seq_json
    }
    def serializeLog(dir: String): List[Seq[Log]] = {
        val new_dir = dir + "/log"
        val list_seq_csv = getFiles(new_dir, List(".csv")).map(parseLogCSV(_))
        val list_seq_json = getFiles(new_dir, List(".json")).map(parseLogJson(_))
        list_seq_csv ++ list_seq_json
    }

    def serializeDrone(dir: String): List[Seq[Drone]] = {
        val new_dir = dir + "/drone"
        val list_seq_csv = getFiles(new_dir, List(".csv")).map(parseDroneCSV(_))
        val list_seq_json = getFiles(new_dir, List(".json")).map(parseDroneJson(_))
        list_seq_csv ++ list_seq_json
    }

    def parse(dir: String, default: String = "../data/logs"): List[Seq[String]] = dir match {
        case "" => getFiles(default, List(".json")).map(parseJson(_))
        case any => getFiles(any, List(".json")).map(parseJson(_))
    }

    def getFiles(path: String, extensions: List[String]): List[File] = {
        listFiles(new File(path)).filter{file => extensions.exists(file.getName.endsWith(_))}
    }

    def listFiles(file: File): List[File] = {
        if (file.exists) {
            if (file.isDirectory) {
                return listFilesR(file.listFiles.toList)
            }
            return List[File](file)
        }
        return Nil
    }

    def listFilesR(files: List[File]): List[File] = files match {
        case Nil => Nil
        case f :: tail => if (f.isDirectory) {
            listFilesR(f.listFiles.toList) ::: listFilesR(tail)
        }
        else {
            f :: listFilesR(tail)
        }
    }
}
