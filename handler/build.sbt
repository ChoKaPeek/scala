scalaVersion := "2.11.12"

name := "handler"
organization := "com.us"
version := "1.0"

libraryDependencies ++= Seq("com.typesafe.play" %% "play" % "2.7.0",
                            "com.typesafe.play" %% "play-ws" % "2.7.0",
                            "org.typelevel" %% "cats-core" % "1.4.0",
                            "org.apache.spark" %% "spark-core" % "2.4.1",
                            "org.apache.spark" %% "spark-sql" % "2.4.1",
                            "com.datastax.spark" %% "spark-cassandra-connector" % "2.4.0") // cassandra
                            
dependencyOverrides ++= Seq("com.fasterxml.jackson.core" % "jackson-databind" % "2.6.7.2")
// Enable Play
lazy val root = (project in file(".")).enablePlugins(PlayScala)
libraryDependencies += guice
