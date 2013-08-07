package translatables.adapter

import translatables.Adapter
import java.io.File
import serialization.Json
import scala.io.Source
import scala.collection.JavaConversions
import java.io.InputStream
import java.lang.IllegalArgumentException

/**
 * Makes translations from JSON files available.
 * @param source JSON serialized translations. Expected to be a flat map.
 */
class JsonAdapter(private val source: Source) extends Adapter {
  /**
   * @param source JSON file.
   */
  def this(source: File) = {
    this(if (source != null) Source.fromFile(source) else throw new IllegalArgumentException)
  }
  /**
   * @param inputStream Serialized JSON.
   */
  def this(inputStream: InputStream) = {
    this(if (inputStream != null) Source.fromInputStream(inputStream) else throw new IllegalArgumentException)
  }

  if (source == null) {
    throw new IllegalArgumentException();
  }

  val existingTranslations: Map[String, String] = try {
    Json.read(source.mkString)
  } finally {
    source.close
  }
  val translations = new MapAdapter(JavaConversions.mapAsJavaMap(existingTranslations));

  def get(translationKey: String): String = {
    return translations.get(translationKey);
  }
}