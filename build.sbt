name := "media-stream-lambda"

version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-lambda-java-events" % "2.2.6",
  "com.amazonaws" % "aws-lambda-java-core" % "1.2.0",

  "com.rometools" % "rome" % "1.16.0",

  "org.scalaj" %% "scalaj-http" % "2.4.2",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.12.4",
"com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.12.4"
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}


