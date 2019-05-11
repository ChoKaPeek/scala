scalaVersion := "2.12.7"

name := "company"
organization := "com.epita"
version := "1.0"

libraryDependencies ++= Seq("com.typesafe.play" %% "play" % "2.7.0",
                            "com.typesafe.play" %% "play-ws" % "2.7.0",
                            "org.typelevel" %% "cats-core" % "1.4.0",
                            "org.apache.spark" %% "spark-core" % "2.4.0")
// Enable Play
lazy val root = (project in file(".")).enablePlugins(PlayScala)
libraryDependencies += guice
