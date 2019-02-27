package parser

import play.api.libs.json.Json
import scala.io.Source
import java.io.File

class Parser(dir: String) {
    def parseJson(file: String): Unit = {
        val fileContents = Source.fromFile(file).getLines.mkString
        val parsed = Json.parse(fileContents)
        println(parsed)
    }
    def parseCSV(file: String): Unit = {
        println("Index, Girth, Height, Volume")
        val bufferedSource = Source.fromFile("example.csv")
        for (line <- bufferedSource.getLines) {
            val cols = line.split(",").map(_.trim)
            println(s"${cols(0)} | ${cols(1)} | ${cols(2)} | ${cols(3)}")
        }
        bufferedSource.close
    }

    def parse() = {
      println(listAllFiles(dir))
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

    def listFiles(path: String): List[String] = {
        listAllFiles(path).filter{
          f => f.isFile && (f.getName.endsWith(".csv")
            || f.getName.endsWith(".json")) }.map(_.getAbsolutePath)
    }
}
