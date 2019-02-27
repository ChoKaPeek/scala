object CSVDemo extends App {
    println("Index, Girth, Height, Volume")
    val bufferedSource = io.Source.fromFile("example.csv")
    for (line <- bufferedSource.getLines) {
        val cols = line.split(",").map(_.trim)
        println(s"${cols(0)} | ${cols(1)} | ${cols(2)} | ${cols(3)}")
    }
    bufferedSource.close
}
