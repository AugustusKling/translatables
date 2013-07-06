package translatables.adapter

import translatables.Adapter
import translatables.Format
import scala.collection.JavaConversions
import scala.collection.MapLike
import translatables.NotTranslated

/**
 * Makes translations from Java or Scala maps available.
 * @param map Translations.
 */
class MapAdapter(private val map: java.util.Map[String, String]) extends Adapter {
  /**
   * @param scalaMap Translations.
   */
  def this(scalaMap: Map[String, String]) = {
    this(JavaConversions.mapAsJavaMap(scalaMap))
  }

  def get(translationKey: String): String = {
    map.get(translationKey) match {
      case "" | null => throw new NotTranslated(translationKey)
      case translation => translation
    }
  }
}