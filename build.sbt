name := "aiengine"

version := "0.1"

scalaVersion := "2.11.4"

libraryDependencies ++= {
  val sparkVer = "2.1.0"
  Seq(
    "org.apache.spark" %% "spark-core" % sparkVer,
    "com.typesafe.akka" %% "akka-http"   % "10.1.0-RC1",
    "com.typesafe.akka" %% "akka-stream" % "2.5.9"
  )
}