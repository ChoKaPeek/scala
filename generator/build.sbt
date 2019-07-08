scalaVersion := "2.11.12"

name := "generator"
organization := "com.us"
version := "1.0"

libraryDependencies ++= Seq("org.apache.spark" %% "spark-core" % "2.4.1", // spark
                            "org.apache.spark" %% "spark-sql" % "2.4.1", // spark-sql, for cassandra + dataframes
                            "org.apache.spark" % "spark-streaming_2.11" % "2.4.1", // kafka
                            "org.apache.spark" % "spark-sql-kafka-0-10_2.11" % "2.4.1",
                            "com.datastax.spark" %% "spark-cassandra-connector" % "2.4.0") // spark-cassandra