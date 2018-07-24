name := """SOSECommunity"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

resolvers += "Neo4j Maven 2 snapshot repository" at "http://m2.neo4j.org/content/repositories/snapshots"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.neo4j.driver" % "neo4j-java-driver" % "1.1.0-M06",
  "org.neo4j" % "neo4j-ogm-core" % "3.0.2",
  "org.neo4j" % "neo4j-ogm-bolt-driver" % "3.0.2"
)

fork in run := false
