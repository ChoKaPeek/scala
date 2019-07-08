scalaVersion := "2.11.12"

name := "handler"
organization := "com.us"
version := "1.0"

libraryDependencies ++= Seq("com.typesafe.play" %% "play" % "2.7.0",
                            "com.typesafe.play" %% "play-ws" % "2.7.0",
                            "org.typelevel" %% "cats-core" % "1.4.0",
                            "org.apache.spark" %% "spark-core" % "2.4.1",
                            "org.apache.spark" %% "spark-sql" % "2.4.1",
                            "org.apache.kafka" %% "kafka" % "2.3.0",
                            "org.apache.spark" % "spark-streaming_2.11" % "2.4.1", // kafka
                            "org.apache.spark" % "spark-sql-kafka-0-10_2.11" % "2.4.1",
                            "com.datastax.spark" %% "spark-cassandra-connector" % "2.4.0") // cassandra
       
//dependencyOverrides ++= Seq("com.fasterxml.jackson.core" % "jackson-databind" % "2.6.7",
//                            "com.fasterxml.jackson.core" % "jackson-annotations" % "2.6.7",
//                            "com.fasterxml.jackson.core" % "jackson-core" % "2.6.7")
// Enable Play
lazy val root = (project in file(".")).enablePlugins(PlayScala)
libraryDependencies += guice
