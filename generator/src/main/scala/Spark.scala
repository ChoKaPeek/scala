import com.datastax.spark.connector._
import org.apache.spark.sql.cassandra._
import org.apache.spark.sql._
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.log4j.{ Logger, Level }
import org.apache.log4j.Level

object Spark {
    val conf = new SparkConf(true)
        .set("spark.cassandra.connection.host", "localhost")
        .set("spark.cassandra.auth.username", "cassandra")            
        .set("spark.cassandra.auth.password", "cassandra")
        .setAppName("Company")
        .setMaster("local[*]") // Run Spark locally with as many worker threads as possessed logical cores

    val sc = new SparkContext(conf)

    val spark = SparkSession.builder().getOrCreate()
    
    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.ERROR)
}