name := "sudden_killer_in_scala"

version := "0.1"

scalaVersion := "2.10.1"

libraryDependencies ++= Seq(
  "org.apache.logging.log4j" %  "log4j-api"        % "2.0-beta6",
  "org.apache.logging.log4j" %  "log4j-core"       % "2.0-beta6",
  "org.specs2"               %% "specs2"           % "1.14" % "test",
  "org.atilika.kuromoji"     %  "kuromoji"         % "0.7.7",
  "org.twitter4j"            %  "twitter4j-core"   % "[3.0,)",
  "org.twitter4j"            %  "twitter4j-stream" % "[3.0,)",
  "org.mockito"              %  "mockito-all"      % "1.8.4"
)

resolvers ++= Seq("specs2 snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                  "specs2 releases"  at "http://oss.sonatype.org/content/repositories/releases",
                  "kuromoji"         at "http://www.atilika.org/nexus/content/repositories/atilika",
                  "twitter4j"        at "http://twitter4j.org/maven2")

