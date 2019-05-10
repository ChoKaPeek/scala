import play.api.libs.json._
import scala.io.Source
import java.io.File

class Parser(dir: String) {

    case class Drone(name: String, url: String)

    implicit object DroneReads extends Reads[Drone] {
        def reads(json: JsValue) = JsSuccess(Drone(  // Has to be a JsResult, should be a case with the possibility of a JsFailure
            (json \ "name").as[String],
            (json \ "url").as[String]
        ))
    }

    def parseJson(file: File): Unit = {
        val fileContents = Source.fromFile(file).getLines.map
            { line => (Json.parse(line.mkString)) }

        println(fileContents)
    }

    def parseCSV(file: File): Unit = {
        println("Index, Girth, Height, Volume")
        val bufferedSource = Source.fromFile(file)
        for (line <- bufferedSource.getLines) {
            val cols = line.split(",").map(_.trim)
            println(s"${cols(0)} | ${cols(1)} | ${cols(2)} | ${cols(3)}")
        }
        bufferedSource.close
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
