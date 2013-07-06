package serialization

import scala.util.parsing.json.JSONObject
import scala.util.parsing.json.JSON
import scala.util.parsing.json.JSONObject
import scala.util.parsing.json.JSONFormat

/**
 * Serializes JSON with formatting so humans can edit the files easily. Deserializes JSON, too.
 */
object Json {
  /**
   * Defines formatting with line breaks.
   */
  class JSONObjectWithBreaks(obj: Map[String, String]) extends JSONObject(obj) {
    override def toString(formatter: JSONFormat.ValueFormatter) =
      "{\n" + obj.map({ case (k, v) => "\t" + formatter(k.toString) + " : " + formatter(v) }).mkString(",\n") + "\n}"
  }

  /**
   * @param translations Value to serialize.
   * @return JSON serialization.
   */
  def write(translations: Map[String, String]): String = {
    new JSONObjectWithBreaks(translations).toString
  }

  /**
   * @param translations JSON serialization.
   * @return Deserialized data.
   */
  def read(translations: String): Map[String, String] = {
    JSON.parseFull(translations).getOrElse(throw new Exception("Failed to parse translations from JSON")) match {
      case map: Map[_, _] => map.asInstanceOf[Map[String, String]]
      case map: JSONObject => map.obj.asInstanceOf[Map[String, String]]
    }
  }
}