name := "translatables"

version := "1.0"

scalaVersion := "2.10.2"

autoScalaLibrary := false

// Properly set the classpath for unit test to include Scala.
fork in Test := true

libraryDependencies += "org.scala-lang" % "scala-library" % "2.10.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"

libraryDependencies += "junit" % "junit" % "4.10" % "test"

libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.10.2"

libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.10.2"

libraryDependencies += "com.github.scopt" %% "scopt" % "3.0.0"

seq(GenJavaDoc.javadocSettings :_*)
