import sbt._
import sbt.Keys._

/**
 * Loads a plugin that tries to generate JavaDoc for Scala classes.
 */
object GenJavaDoc {

  val JavaDoc = config("genjavadoc") extend Compile

  val javadocSettings = inConfig(JavaDoc)(Defaults.configSettings) ++ Seq(
    libraryDependencies += compilerPlugin("com.typesafe.genjavadoc" %%
      "genjavadoc-plugin" % "0.5" cross CrossVersion.full),
    scalacOptions <+= target map (t => "-P:genjavadoc:out=" + (t / "java")),
    packageDoc in Compile <<= packageDoc in JavaDoc,
    sources in JavaDoc <<=
      (target, compile in Compile, sources in Compile) map ((t, c, s) =>
        (t / "java" ** "*.java").get ++ s.filter(_.getName.endsWith(".java"))),
    javacOptions in JavaDoc := Seq(),
    artifactName in packageDoc in JavaDoc :=
      ((sv, mod, art) =>
        "" + mod.name + "_" + sv.binary + "-" + mod.revision + "-javadoc.jar"))
}