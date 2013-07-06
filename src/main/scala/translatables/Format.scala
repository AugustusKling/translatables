package translatables

import scala.util.matching.Regex
import languages.Root
import scala.util.matching.Regex.Match

/**
 * Format of translation files. This implements the serialization of translations.
 */
object Format {
  /**
   * Extracts placeholders from source keys.
   * @param sourceKey Message identifier as given in source code
   * @return All found placeholders
   */
  def findPlaceholders(sourceKey: String, language:Language): Map[String, TypedPlaceholder] = {
    val regexp = "\\{([^{}]*)\\}".r
    val placeholders = for (m <- (regexp.findAllIn(sourceKey).matchData).toSeq; placeholderRaw <- m.subgroups) yield {
      val placeholder = buildPlaceholder(placeholderRaw, language)
      (placeholder.name, placeholder)
    }
    val ambiguoslyTyped = placeholders.groupBy(_._2.name).map(g => (g._1, g._2.map(_._2.domain))).filterNot {
      case (_, occurences) => occurences.distinct.size == 1
    }
    if (ambiguoslyTyped.isEmpty) placeholders.toMap
    else
      throw new IllegalArgumentException("Placeholders ambiguosly typed: " +
        ambiguoslyTyped.map(g => g._1 + g._2.distinct.mkString("(", ",", ")")).mkString(", ") + " when parsing "+sourceKey)
  }

  /**
   * Deserializes a placeholder
   * @param placeholderRaw Placeholder as it appears in source key
   */
  def buildPlaceholder(placeholderRaw: String, language:Language) = {
    val typedPlaceholder = new Regex("^([^()]+)\\(([^()]*)\\)$", "domain", "name")
    val (name, domain) = placeholderRaw match {
      case typedPlaceholder(domain, name) => (name, domain)
      case _ => (placeholderRaw, "plain")
    }
    new TypedPlaceholder(name, language.getDomain(domain) match {
      case Some(domain) => domain
      case None => throw new NoSuchElementException("Domain " + domain + " not supported by language"+language+".")
    })
  }

  /**
   * Parses value provided by translator in constant and replaceable chunks.
   */
  def parseTranslation(translation: String): List[Placeholder] = {
    val fragments = "\\{[^{}]+\\}".r.split(translation) map (new ConstantPlaceholder(_))
    val placeholders = (for (m <- "\\{([^{}]+)\\}".r.findAllIn(translation).matchData) yield new Replaceable(m.subgroups(0))).toList
    val coll = for (i <- 0 until (fragments.size + placeholders.size)) yield if (i % 2 == 0) fragments(i / 2) else placeholders(i / 2)
    coll.filterNot(_ match {
      case ConstantPlaceholder(content) => content == ""
      case _ => false
    }).toList
  }
  
  /**
   * @return A dummy translation
   */
  def buildFallback(translationKey: String) = {
    val domainStripper = "\\{[^(]+\\(([^}+]+)\\)\\}".r
    def dropQualifier(key: String) =
      domainStripper.replaceAllIn(key, (m: Match) => "{" + m.subgroups(0) + "}")
    val categoriesAndDomainsStripped = dropQualifier(dropQualifier(translationKey))
    parseTranslation(categoriesAndDomainsStripped)
  }

  /**
   * Deserializes a source key
   */
  def parseSourceKey(translation: Translation): List[Placeholder] = {
    val parsed = parseTranslation(translation.sourceKey)
    val PlaceholderWithDomain = "([^(]+)\\(([^{}]+)\\)".r

    def getDomain(domain: String) = translation.language.getDomain(domain) match {
      case Some(domain) => domain
      case None => throw new NoSuchElementException("Domain " + domain)
    }
    parsed.map(_ match {
      case ConstantPlaceholder(content) => ConstantPlaceholder(content)
      case Replaceable(PlaceholderWithDomain(domain, name)) => TypedPlaceholder(name, getDomain(domain))
      case Replaceable(name) => TypedPlaceholder(name, getDomain("plain"))
    })
  }

  /**
   * Calculates the translation key that shall serve to look up a translation given the given values shall be bound to placeholders.
   */
  def buildTranslationKey(translation: Translation, replacements: Map[String, Any]): String = {
    val trans = parseSourceKey(translation)
    trans.map (_ match {
      case ConstantPlaceholder(content) => content
      case TypedPlaceholder(name, domain) => {
        val categoryName = domain.getCategory(replacements get (name) match {
          case Some(value) => value
          case None => throw new NoSuchElementException(DoTranslate.doTranslate("Missing value for placeholder {0} of source key {1}.", name, translation.sourceKey))
        }).name
        "{" + categoryName + "(" + domain.name + "(" + name + "))}"
      }
    }).mkString
  }

  /**
   * Given a source key calculates all translation keys for a specific language.
   *
   * The generated keys are language specific because of the different rules that apply due to the
   * way a language's grammar threats a domain. For example the number of differnent plural forms
   * differs and so is when to apply which plural form.
   */
  def buildAllTranslationKeys(translation: Translation): Set[String] = {
    val trans = parseSourceKey(translation)
    val variants = trans.filter(_.isInstanceOf[TypedPlaceholder]).flatMap {
      case TypedPlaceholder(name, domain) => for (cat <- domain.categories) yield (name, domain.name, cat.name)
    }.toSet
    val grouped = variants.groupBy(_._1)
    def bind(trans: Set[List[Placeholder]], groups: Map[String, Set[(String, String, String)]]): Set[String] = {
      if (groups.isEmpty) {
        for (te <- trans) yield te.map { case ConstantPlaceholder(content) => content }.mkString
      } else {
        val (phName, fullCategories) = groups.head
        val ex = for (fc <- fullCategories; te <- trans) yield te.map(_ match {
          case TypedPlaceholder(name, domain) if name == phName => ConstantPlaceholder("{" + fc._3 + "(" + fc._2 + "(" + phName + "))}")
          case other => other
        })
        bind(ex, groups.tail)
      }
    }
    bind(Set(trans), grouped)
  }
}