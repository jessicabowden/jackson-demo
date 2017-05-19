name := "jackson-demo"

version := "1.0"

scalaVersion := "2.11.8"

lazy val versions = new {
  val spark = "2.1.0"
}

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % versions.spark withSources() excludeAll (
    ExclusionRule(name = "commons-httpclient"),
    ExclusionRule(organization = "org.apache.httpcomponents"),
    ExclusionRule(organization = "org.scalatest")
  ),
  "org.apache.spark" %% "spark-sql" % versions.spark,

  "org.mockito" % "mockito-core" % "1.9.5" % "test",

  "org.scalatest" %% "scalatest" % "3.0.1" % "test",

  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.8.8"
)
