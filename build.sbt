name := """learn-play"""
version := "1.0-SNAPSHOT"
scalaVersion := "2.12.6"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
pipelineStages := Seq(digest)

libraryDependencies ++= Seq(
  jdbc,
  ehcache,
  ws,
  "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
)

resolvers += "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"