name := """frontend"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "team16.cs261" % "dal" % "1.0-SNAPSHOT"
)

resolvers += Resolver.mavenLocal

