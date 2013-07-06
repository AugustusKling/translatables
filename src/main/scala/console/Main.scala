package console

import java.io.File
import translatables.Language
import languages.Root
import languages.de
import languages.en
import extract.JavaExtractor
import extract.ScalaExtractor
import serialization.Json
import scala.io.Source
import java.io.FileWriter
import translatables.Translation
import extract.ExtractionHint
import junit.framework.Test
import translatables.adapter.MapAdapter
import translatables.DoTranslate.doTranslate
import translatables.DoTranslate
import java.util.Locale
import translatables.adapter.JsonAdapter

/**
 * Configuration for extract command.
 */
case class Config(
  /**
   * Source roots.
   */
  sources: Seq[File] = Seq(),
  /**
   * Names of translation functions. Their first arguments are expected to take translation keys.
   */
  translationCalls: Seq[String] = Seq(),
  /**
   * Language code.
   */
  language: Language = Root,
  /**
   * File where extracted translations end up.
   */
  target: File = File.createTempFile("extracted-translations", ".json"))

object Main extends App {
  // Set up translations of this program
  val programLanguageCode = Locale.getDefault().getLanguage();
  val programAdapter = new JsonAdapter(Option(getClass().getResourceAsStream(programLanguageCode + ".json")).getOrElse(getClass().getResourceAsStream("en.json")))
  DoTranslate.setParams(if (programLanguageCode == "de") de else en, programAdapter)

  // Define what the program invocation needs to look like.
  val parser = new scopt.OptionParser[Config]("translatables") {
    head("Version 0.x, use with care.")

    help("help") text ("Examines a source code to find translations. Writes found translations to file.")

    cmd("extract") text("Extracts translations and writes them to a JSON file.") children {
      opt[File]("source") unbounded () required () action { (file, c) =>
        c.copy(sources = c.sources :+ file)
      } text ("Path to root of source code.")
      opt[File]("target") unbounded () required () action { (file, c) =>
        c.copy(target = file)
      } text ("Target file to collect texts to translate.")
      opt[String]("language") action { (languageCode, c) =>
        val lang = languageCode match {
          case "de" => de
        }
        c.copy(language = lang)
      } text ("Target language. Supply a lowercase ISO 639 code.")
      opt[String]("calls") unbounded () required () action { (functionName, c) =>
        c.copy(translationCalls = c.translationCalls :+ functionName)
      } text ("Name of translation methods.")
      // Workaround for scopt forgetting about last parameter set.
      note("")
    }
  }

  // Parse command line arguments and extract.
  parser.parse(args, Config()) map { config =>
    val existingTranslations: Map[String, String] = if (config.target.exists()) {
      // Use existing translations as default to retain them.
      Json.read(Source.fromFile(config.target).mkString)
    } else {
      // Start with an empty set of translations.
      Map()
    }

    val hint = new ExtractionHint(config.translationCalls)
    val sourceKeys: Seq[String] = config.sources flatMap { source =>
      List(new JavaExtractor(source, hint), new ScalaExtractor(source, hint)) flatMap { extractor =>
        println(doTranslate("Running extractor {0}", extractor))
        // Examine the sources looking for translations.
        extractor.extract(config.language)
      }
    }
    println(doTranslate("Extracted {number(0)} translation.", sourceKeys.size.asInstanceOf[Object]))

    // Only retain keys that are still in use but take over existing translations for such keys.
    val merged: Map[String, String] = Map() ++ sourceKeys.map(key => key -> existingTranslations.getOrElse(key, ""));

    val fw = new FileWriter(config.target)
    try {
      // Persist extracted translations.
      fw.write(Json.write(merged))
      println(doTranslate("Merged new translations into {0}.", config.target))
    } finally {
      fw.close()
    }
  } getOrElse {
    // arguments are bad, usage message will have been displayed
    System.err.println(doTranslate("Extraction aborted due to malformed invocation."));
  }
}