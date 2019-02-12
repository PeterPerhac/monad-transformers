lazy val root = (project in file("."))
  .settings(
    inThisBuild(
      List(
        organization := "com.perhac",
        scalaVersion := "2.12.8",
        version := "0.1.0-SNAPSHOT"
      )),
    name := "Monad Transformers",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "1.6.0",
      "org.typelevel" %% "mouse"     % "0.20",
      "org.scalatest" %% "scalatest" % "3.0.5" % "test"
    )
  )

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Ypartial-unification")
mainClass in (Compile, run) := Some("com.perhac.blog.Main")
