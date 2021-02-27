name := "toolbox"
version := "0.1"
scalaVersion := "2.13.5"
organization := "net.chekuri"
developers := List(
  Developer(
    "bharanikrishna7",
    "Venkata Bharani Krishna Chekuri",
    "bharanikrishna7@gmail.com",
    url("https://github.com/bharanikrishna7/")
  )
)

crossScalaVersions := Seq(
  scalaVersion.value,
  "2.13.4",
  "2.13.3",
  "2.13.2",
  "2.13.1",
  "2.13.0",
  "2.12.13",
  "2.12.12",
  "2.12.11",
  "2.12.10",
  "2.12.9",
  "2.12.8",
  "2.12.7",
  "2.12.6",
  "2.12.5",
  "2.12.4",
  "2.12.3"
)

val root = (project in file("."))
  .settings(
    /* logging dependencies */
    libraryDependencies += scala_logging_library,
    /* json dependencies */
    libraryDependencies += json4s_library,
    /* http client dependencies */
    libraryDependencies += okhttp_library,
    /* logging [test] dependencies */
    libraryDependencies += logback_classic_library,
    /* unit test [test] dependencies */
    libraryDependencies += scalatest_library
  )

// library versions
val scala_logging_version = "3.9.2"
val json4s_version = "3.6.10"
val okhttp_version = "4.9.1"
val logback_classic_version = "1.2.3"
val scalatest_version = "3.2.5"

// library dependencies
val scala_logging_library = "com.typesafe.scala-logging" %% "scala-logging" % scala_logging_version
val json4s_library = "org.json4s" %% "json4s-native" % json4s_version
val okhttp_library = "com.squareup.okhttp3" % "okhttp" % okhttp_version
val logback_classic_library = "ch.qos.logback" % "logback-classic" % logback_classic_version % Test
val scalatest_library = "org.scalatest" %% "scalatest" % scalatest_version % Test
