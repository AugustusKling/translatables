package translatables

/**
 * Target language that consists of domains. It serves as translation target and translation memory.
 * @param code Language code such as de or de-CH.
 * @param extensionDomains The contained domains define the grammar rules of the language.
 * @param fallback Other language that is queried for domains and translation if missing in this language.
 */
class Language(val code: String, val extensionDomains: List[Domain], val fallback: Option[Language]) {
  /** From tranlationKey to translation */
  val translations: collection.mutable.Map[String, List[Placeholder]] = collection.mutable.Map()

  // Merge extension domains with fallback domains so that domains of the fallback are overridden by extension domains with the same name
  private val fallbackDomains: List[Domain] = fallback map (_.domains) getOrElse (List())
  val domains: List[Domain] = {
    fallbackDomains map ((fb: Domain) => extensionDomains.find(_.name == fb.name) getOrElse (fb))
  } ++ extensionDomains.filterNot(fallbackDomains.contains(_))

  /**
   * @return Domain implementations which contains grammar rules for language
   */
  def getDomain(name: String) = {
    domains.find(_.name == name) match {
      case Some(domain) => Some(domain)
      case None => fallback match {
        case Some(fallback) => fallback.domains.find(_.name == name)
        case None => None
      }
    }
  }

  /**
   * Makes the given translations known to the language
   * @param translationKeyTranslation Mapping from translation key to translation
   */
  def updateTranslations(translationKeyTranslation: Map[String, String]) = {
    translationKeyTranslation.foreach {
      case (translationKey, translation) => translations.update(translationKey, Format.parseTranslation(translation))
    }
  }
  /**
   * Looks up translation by key
   */
  def getTranslation(translationKey: String): List[Placeholder] = translations getOrElse (translationKey,
    fallback match {
      case None =>
        Format.buildFallback(translationKey)
      case fallback => fallback.get.getTranslation(translationKey)
    })
}