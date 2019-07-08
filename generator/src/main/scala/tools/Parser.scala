package tools

import scala.io.Source
import java.io.File
import java.util.ArrayList
import Models._
import collection.JavaConversions._
import Spark._
import org.apache.spark.sql.types._
import org.apache.spark.sql.DataFrame
import spark.implicits._ // implicit encoder for case classes, uses spark object

object Parser {
    def parseJson(file: File): DataFrame = {
        spark.read.json(file.getPath()).toDF()
    }

    def parseCSV(file: File): DataFrame = {
        spark.read
            .format("csv")
            .option("sep", ",")
            .option("inferSchema", "true")
            .option("header", "true")
            .option("ignoreLeadingWhiteSpace", "true")
            .option("quote", "\"")
            .option("escape", "\"")
            .load(file.getPath())
    }

    def streamJson(file: File): DataFrame = {
        spark.readStream
            .format("json")
            .schema(logSchema)
            .load(file.getParent())
    }

    def streamCSV(file: File): DataFrame = {
        spark.readStream
            .format("csv")
            .option("sep", ",")
            .option("header", "true")
            .option("ignoreLeadingWhiteSpace", "true")
            .option("quote", "\"")
            .option("escape", "\"")
            .schema(logSchema)
            .load(file.getParent())
    }

    def serializePopulation(dir: String): List[DataFrame] = {
        val list_seq_csv = getFiles(dir, List(".csv")).map(parseCSV(_))
        list_seq_csv ++ getFiles(dir, List(".json")).map(parseJson(_))
    }

    def parse(dir: String, default: String): List[DataFrame] = {
        val str = if (dir == "") default else dir
        val list_seq_csv = getFiles(str, List(".json")).map(streamJson(_))
        list_seq_csv ++ getFiles(str, List(".csv")).map(streamCSV(_))
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
        } else {
            f :: listFilesR(tail)
        }
    }
}
