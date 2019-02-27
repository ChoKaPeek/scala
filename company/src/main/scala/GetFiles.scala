import java.io.File

def listAllFiles(path: String) : List[File] = {
    val directory = new File(path)
    if (directory.exists && directory.isDirectory) {
        directory.listFiles.toList
    }
    else {
        List[File]()
    }
}

def listDirectories(path: String) : List[File] {
    listAllFiles(path).filter(_.isDirectory)
}

def listFiles(path: String) : List[File] = {
    listAllFiles(path).filter(_.isFiles)
}
