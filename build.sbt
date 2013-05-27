name := "sudden_killer_in_scala"

version := "0.1"

scalaVersion := "2.10.1"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.14" % "test"
)

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                  "releases"  at "http://oss.sonatype.org/content/repositories/releases")
