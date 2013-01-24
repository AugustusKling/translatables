package translatables

import languages.Root

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
  val placeholders = Format.findPlaceholders(sourceKey)

  /**
   * Binds named placeholders and returns the filled translation.
   */
  def apply(replacements: (String, Any)*): String = {
    val m = Map() ++ replacements
    val translationKey = Format.buildTranslationKey(this, m)
    val translation: List[Placeholder] = language.getTranslation(translationKey)
    translation.map(_ match {
      case Replaceable(name) => {
        val value = m(name)
        placeholders get(name) match {
          case Some(TypedPlaceholder(_, domain)) => domain format(value)
          case _ => throw new IllegalArgumentException("Placeholder to be bound that wasn't specified in source key.")
        }
      }
      case ConstantPlaceholder(text) => text
    }) mkString
  }
}

object Translation {
  def define(text:String) = {
    new Translation(text, Root)
  }
}