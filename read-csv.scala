object CSVDemo extends App {
    println("Month, Income, Expenses, Profit")
    val bufferedSource = io.Source.fromFile("/tmp/finance.csv")
    for (line <- bufferedSource.getLines) {
        val cols = line.split(",").map(_.trim)
        // do whatever you want with the columns here
        println(s"${cols(0)}|${cols(1)}|${cols(2)}|${cols(3)}")
    }
    bufferedSource.close
}
