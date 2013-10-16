import sbt._
import sbtassembly.Plugin._
import AssemblyKeys._
import Keys._

/**
 * Use sub projects to build multiple assembled files. Having the save base pulls the very same inputs.
 */
object TranslatablesBuild extends Build {
  val mergeRules = mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
    {
      case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first
      case PathList("org", "eclipse", xs @ _*) => MergeStrategy.discard
      case "application.conf" => MergeStrategy.concat
      case ".api_description" => MergeStrategy.discard
      case ".options" => MergeStrategy.discard
      case "topics_Reference.xml" => MergeStrategy.discard
      case "toc.xml" => MergeStrategy.discard
      case "plugin.properties" => MergeStrategy.discard
      case "OSGI-INF/l10n/bundle-src.properties" => MergeStrategy.discard
      case "index/indexed_dependencies" => MergeStrategy.discard
      case "META-INF/MANIFEST.MF" => MergeStrategy.discard
      case "META-INF/ECLIPSE_.RSA" => MergeStrategy.discard
      case "META-INF/ECLIPSE_.SF" => MergeStrategy.discard
      case "java.policy.applet" => MergeStrategy.discard
      case x => MergeStrategy.first //old(x)
    }
  }

  /**
   * Builds library that expects a developer to supply Scala as dependency along with the library. The library cannot
   * be run from the command line and it cannot be used to extract translations. It can only be used to render existent
   * translations.
   */
  lazy val minimal = Project(id = "minimal", base = file("."))
    .settings(buildSettings: _*)
    .settings(assemblySettings: _*)
    .settings(
      mergeRules,
      jarName in assembly := "translatables-minimal.jar",
      test in assembly := {},
      assemblyOption in assembly ~= { _.copy(includeDependency = true) },
      assemblyOption in assembly ~= { _.copy(includeScala = false) })

  /**
   * translatables as standalone application.
   *
   * Can be used as library or called from command line to extract translations.
   */
  lazy val extractor = Project(id = "extractor", base = file("."))
    .settings(buildSettings: _*)
    .settings(assemblySettings: _*)
    .settings(mergeRules)
    .settings(
      jarName in assembly := "translatables-extractor.jar",
      mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
        {
          // Retain the Eclipse compiler.
          case PathList("org", "eclipse", xs @ _*) => MergeStrategy.first

          case x => old(x)
        }
      },
      test in assembly := {},
      assemblyOption in assembly ~= { _.copy(includeDependency = true) },
      assemblyOption in assembly ~= { _.copy(includeBin = true) })

  lazy val root = Project(id = "translatables", base = file("."))
}