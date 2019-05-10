import org.apache.spark.*
import org.apache.spark.*

class DB {
    private val spark = SparkSession.builder()
                                    .master("local")
                                    .appName("Company")
                                    .config("spark.cassandra.connection.host", "localhost")
                                    .getOrCreate()

    def post (s: String): Unit = {
        val newPosts = Seq(s)
        val newPostsRDD = spark.sparkContext.parallelize(newPosts)
        val newPostsDF = spark.createDataFrame(newPostsRDD)

        newPostsDF.write
                  .mode(SaveMode.Append)
                  .cassandraFormat("POST_LINE", "keyspace")
                  .save()
    }
    
    def get (): String = {
        val data: DataFrame = spark.read
                                    .cassandraFormat("POST_LINE", "keyspace")
                                    .options(ReadConf.SplitSizeInMBParam.option(32))
                                    .load()

        val rows: String = data.select("message").collect().map(_.getString(0)).mkString("\n")
        rows
    }
}
