package translatables

import languages.Root
import scala.collection.JavaConversions
import translatables.adapter.MapAdapter

/**
 * A translatable text.
 *
 * @param sourceKey Language agnostic text. Although using a natural language text is perfectly
 * 	acceptable, the text serves as meaningless identifier.
 * 	The syntax for specifying placeholders and domains is defined purely by the used
 *  {@link translatables.Format}.
 * @param language Gives the available domains
 */
class Translation(val sourceKey: String, val language: Language) {
  val placeholders = Format.findPlaceholders(sourceKey, language)

  /**
   * Binds named placeholders and returns the filled translation.
   */
  def apply(adapter: Adapter, replacements: (String, Any)*): String = {
    val m = Map() ++ replacements
    val translationKey = Format.buildTranslationKey(this, m)
    val translation: List[Placeholder] = language.getTranslation(translationKey, adapter)
    translation.map(_ match {
      case Replaceable(name) => {
        val value = m(name)
        placeholders get (name) match {
          case Some(TypedPlaceholder(_, domain)) => domain format (value)
          case _ => throw new IllegalArgumentException("Placeholder to be bound that wasn't specified in source key.")
        }
      }
      case ConstantPlaceholder(text) => text
    }).mkString
  }
}

/**
 * Basic API.
 */
object Translation extends TranslationApi {
  /**
   * @param replacements Any number of values to replace. Translations is expected to define placeholders called 0, 1, ….
   * @return Translated text.
   */
  def getVariadic(lang: Language, adapter: Adapter, sourceKey: String, replacements: Object*): String = {
    val replacementMap = (0 to replacements.length).map(_.toString).zip(replacements)
    new Translation(sourceKey, lang)(adapter, replacementMap: _*)
  }
  /**
   * @param replacements Any number of values to replace. Translations is expected to define placeholders called 0, 1, ….
   * @return Translated text.
   */
  def getList(lang: Language, adapter: Adapter, sourceKey: String, replacements: java.util.List[Any]): String={
    getVariadic(lang, adapter, sourceKey, replacements.toArray: _ *)
  }
  /**
   * @param replacements Values to replace. Keys match placeholder names.
   * @return Translated text.
   */
  def get(lang: Language, adapter: Adapter, sourceKey: String, replacements: (String, Any)*): String = {
    new Translation(sourceKey, lang)(adapter, replacements: _*)
  }
  /**
   * @param replacements Values to replace. Keys match placeholder names.
   * @return Translated text.
   */
  def get(lang: Language, adapter: Adapter, sourceKey: String, replacements: java.util.Map[String, Object]): String = {
    get(lang, adapter, sourceKey, JavaConversions.mapAsScalaMap(replacements).toSeq: _*)
  }
}

/**
 * Internal use of translatables.
 */
object DoTranslate {
  var lang:Language = Root
  var adapter:Adapter = new MapAdapter(Map.empty[String, String])
  
  def setParams(language:Language, adapter:Adapter)={
    lang = language
    this.adapter=adapter
  }
  
  def doTranslate(sourceKey: String, replacements: Object*):String = {
    return Translation.getVariadic(lang, adapter, sourceKey, replacements: _ *);
  }
}