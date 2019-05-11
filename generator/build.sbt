scalaVersion := "2.11.12"

name := "generator"
organization := "com.us"
version := "1.0"

libraryDependencies ++= Seq("org.apache.spark" %% "spark-core" % "2.4.1", // spark
                      			"org.apache.spark" %% "spark-sql" % "2.4.1", // spark-sql, for cassandra
                            "com.datastax.spark" %% "spark-cassandra-connector" % "2.4.0") // spark-cassandra