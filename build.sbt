import org.apache.logging.log4j.core.config.composite.MergeStrategy

name := "media-stream-lambda"

version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-lambda-java-events" % "2.2.6",
  "com.amazonaws" % "aws-lambda-java-core" % "1.2.0",
  "com.rometools" % "rome" % "1.16.0"

)


