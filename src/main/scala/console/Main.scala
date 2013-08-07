package console

import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.util.Locale
import java.util.Properties
import scala.collection.JavaConverters
import scala.collection.immutable.TreeMap
import scala.io.Source
import extract.ExtractionHint
import extract.JavaExtractor
import extract.ScalaExtractor
import languages.de
import languages.en
import languages.Root
import serialization.Json
import translatables.DoTranslate
import translatables.DoTranslate.doTranslate
import translatables.Language
import translatables.StoredTranslation
import translatables.adapter.JsonAdapter
import translatables.adapter.POAdapter
import serialization.POParser.POTranslation
import translatables.Format
import translatables.Translation
import translatables.ConstantPlaceholder
import translatables.Replaceable
import translatables.Placeholder
import translatables.TypedPlaceholder
import scala.util.matching.Regex
import serialization.POFormat
import serialization.JsonFormat
import serialization.PropertiesFormat

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
  target: File = File.createTempFile("extracted-translations", ".json"),
  /**
   * Dummy translations that help to find untranslated parts of a software.
   */
  pseudo: Boolean = false)

object Main extends App with PseudoTranslationGenerator {
  // Set up translations of this program
  val programLanguageCode = Locale.getDefault().getLanguage();
  val translationFolder = "translations/"
  val programAdapter = new JsonAdapter(Option(getClass().getClassLoader().getResourceAsStream(translationFolder + programLanguageCode + ".json"))
    .getOrElse(Option(getClass().getResourceAsStream(translationFolder + "en.json")).getOrElse(new ByteArrayInputStream("{}".getBytes("UTF-8")))))
  DoTranslate.setParams(if (programLanguageCode == "de") de else en, programAdapter)

  // Define what the program invocation needs to look like.
  val parser = new scopt.OptionParser[Config]("translatables") {
    head(doTranslate("Version {0}, use with care.", "0.x"))

    help("help") text ("Examines a source code to find translations. Writes found translations to file.")

    cmd("extract") text ("Extracts translations and writes them to a JSON file.") children {
      opt[File]("source") unbounded () required () action { (file, c) =>
        c.copy(sources = c.sources :+ file)
      } text ("Path to root of source code.")
      opt[File]("target") unbounded () required () action { (file, c) =>
        c.copy(target = file)
        getExtension(file).get match {
          case "json" | "properties" | "po" => c.copy(target = file)
          case _ => throw new RuntimeException(doTranslate("Translation file format was not detected. Make sure it ends in .json, .po or .properties."))
        }
      } text ("Target file to collect texts to translate.")
      opt[String]("language") action { (languageCode, c) =>
        val withCountry = new Regex("^([^_]+)_([^_]+)")
        val lang = Language.fromLocale(languageCode match {
          case withCountry(language, country) => new Locale(language, country)
          case language => new Locale(language)
        })
        c.copy(language = lang)
      } text ("Target language. Supply a lowercase ISO 639 code.")
      opt[String]("calls") unbounded () required () action { (functionName, c) =>
        c.copy(translationCalls = c.translationCalls :+ functionName)
      } text ("Name of translation methods.")
      opt[Unit]("pseudo-translations") action { (_, c) =>
        c.copy(pseudo = true)
      } text ("When given translations with markers and diacritcs are made up to help spotting forgotten translations. Note that is option overwrites any existing translations!")
      // Workaround for scopt forgetting about last parameter set.
      note("")
    }
  }

  // Parse command line arguments and extract.
  parser.parse(args, Config()) map { config =>
    val format = getExtension(config.target).get match {
      case "json" => JsonFormat
      case "properties" => PropertiesFormat
      case "po" => POFormat
    }

    val existingTranslations: Map[String, StoredTranslation[_]] = if (config.target.exists()) {
      // Use existing translations as default to retain them.
      format.read(config.target)
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
    val merged: Map[String, String] = TreeMap(sourceKeys.map(key => key -> (if (config.pseudo) {
      generatePseudoTranslation(key)
    } else {
      existingTranslations.getOrElse(key, StoredTranslation(key, "", None)).translation
    })): _*)

    format.write(config.target, merged, existingTranslations, config.language)
    println(doTranslate("Merged new translations into {0}.", config.target.getAbsolutePath()))

  } getOrElse {
    // arguments are bad, usage message will have been displayed
    System.err.println(doTranslate("Extraction aborted due to malformed invocation."));
  }

  def autoClose[T <: AutoCloseable](resource: T)(block: T => Unit) {
    try {
      block(resource)
    } finally {
      if (resource != null) resource.close()
    }
  }

  def getExtension(file: File): Option[String] = {
    val segments = file.getName().split("\\.")
    if (segments.size == 0) None else Option(segments.last)
  }

}