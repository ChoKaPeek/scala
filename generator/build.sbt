scalaVersion := "2.11.12"

name := "generator"
organization := "com.us"
version := "1.0"

libraryDependencies ++= Seq("com.typesafe.play" %% "play-json" % "2.7.3",
                      			"org.apache.spark" %% "spark-core" % "2.4.0", // spark
                            "com.datastax.spark" %% "spark-cassandra-connector-unshaded" % "2.4.1") // spark-cassandra