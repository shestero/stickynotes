ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "stickynotes"
  )


val http4sVersion = "0.23.13" // "0.23.18"

// Only necessary for SNAPSHOT releases
//resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion
)

val catsVersion = "2.5.4" // "3.4.8"
libraryDependencies += "org.typelevel" %% "cats-effect" % catsVersion
ThisBuild / libraryDependencySchemes += "org.typelevel" %% "cats-effect" % "always"

// Tapir
val TapirVersion = "1.5.5"
libraryDependencies ++= Seq(
  "com.softwaremill.sttp.tapir" %% "tapir-files" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % TapirVersion,

  "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % "1.2.9"
)

libraryDependencies += "com.softwaremill.sttp.client3" %% "core" % "3.8.12"
// "com.softwaremill.sttp.client3" %% "upickle" % "3.8.12"
