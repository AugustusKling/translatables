package translatables.adapter

import translatables.Adapter
import java.io.File
import serialization.Json
import scala.io.Source
import scala.collection.JavaConversions
import java.io.InputStream

/**
 * Makes translations from JSON files available.
 * @param source JSON serialized translations. Expected to be a flat map.
 */
class JsonAdapter(private val source: Source) extends Adapter {
  /**
   * @param source JSON file.
   */
  def this(source: File)={
    this(Source.fromFile(source))
  }
  /**
   * @param inputStream Serialized JSON.
   */
  def this(inputStream:InputStream)={
    this(Source.fromInputStream(inputStream))
  }
  
  val existingTranslations:Map[String, String] = Json.read(source.mkString);
  val translations = new MapAdapter(JavaConversions.mapAsJavaMap(existingTranslations));
  
  def get(translationKey: String): String = {
    return translations.get(translationKey);
  }
}