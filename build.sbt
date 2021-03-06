name := "anubis-service"

organization := "com.github.kazuhito_m"

version := "0.0.1"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.3.11" % "test",
  "org.scalatest" %% "scalatest" % "2.2.1-M3" % "test",
  "junit" % "junit" % "4.11" % "test",
  "org.rogach" %% "scallop" % "0.9.5",
  "org.jyaml" % "jyaml" % "1.3"
)

scalacOptions in Test ++= Seq("-Yrangepos")

// Read here for optional dependencies:
// http://etorreborre.github.io/specs2/guide/org.specs2.guide.Runners.html#Dependencies

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

initialCommands := "import com.github.kazuhito_m.anubisservice.parser._"

ideaExcludeFolders += ".idea"

ideaExcludeFolders += ".idea_modules"

assemblySettings
