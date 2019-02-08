lazy val root = (project in file("."))
 .settings(
    inThisBuild(List(
      organization := "com.perhac",
      scalaVersion := "2.12.8",
      version := "0.1.0-SNAPSHOT"
    )),
    name := "Experimental App with Cats",
    libraryDependencies += "org.typelevel" %% "cats-core" % "1.6.0"
  )

 scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ypartial-unification")

