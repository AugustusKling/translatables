package translatables

import scala.util.matching.Regex
import languages.Root
import scala.util.matching.Regex.Match

/**
 * Format of translation files. This implements the serialization of translations.
 */
object Format {
  type Escaping = (String, String)
  val translationEscaping: Escaping = "{{" -> "{"
  val sourceEscaping: Escaping = "{{" -> "{{"

  /**
   * Extracts placeholders from source keys.
   * @param sourceKey Message identifier as given in source code
   * @return All found placeholders
   */
  def findPlaceholders(sourceKey: String, language: Language): Map[String, TypedPlaceholder] = {
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
        ambiguoslyTyped.map(g => g._1 + g._2.distinct.mkString("(", ",", ")")).mkString(", ") + " when parsing " + sourceKey)
  }

  /**
   * Deserializes a placeholder
   * @param placeholderRaw Placeholder as it appears in source key
   */
  def buildPlaceholder(placeholderRaw: String, language: Language) = {
    val typedPlaceholder = new Regex("^([^()]+)\\(([^()]*)\\)$", "domain", "name")
    val (name, domain) = placeholderRaw match {
      case typedPlaceholder(domain, name) => (name, domain)
      case _ => (placeholderRaw, "plain")
    }
    new TypedPlaceholder(name, language.getDomain(domain) match {
      case Some(domain) => domain
      case None => throw new NoSuchElementException("Domain " + domain + " not supported by language" + language + ".")
    })
  }

  /**
   * Parses value provided by translator in constant and replaceable chunks.
   */
  def parseTranslation(translation: String): List[Placeholder] = parseKeyOrTranlation(translation, translationEscaping)

  /**
   * Parses either a source key, translation key or translation.
   * @param translation Serialized key or translation.
   * @param escaping How escape character should be replaced before returning.
   * @return Chopped up input where escaped characters are part of {@code ConstantPlaceholder}s and not special anymore.
   */
  private def parseKeyOrTranlation(translation: String, escaping: Escaping): List[Placeholder] = {
    if (translation == escaping._1)
      List(ConstantPlaceholder(escaping._2))
    else {
      // Escaping of { is by typing {{ as a translator.
      // Translation will be split at {{ then fragments without escaping persist which can be handled in a simpler way.
      // Afterwards fragment list needs to be concatenated and adjacent constants need to be merged (to get placeholder
      // list as short as possible).
      val unescaped = "\\{\\{".r.split(translation).toList
      // Build placeholder list for each fragment.
      val placeholderLists = unescaped map { translation =>
        val fragments = "\\{[^{}]+\\}".r.split(translation) map (new ConstantPlaceholder(_))
        val placeholders = (for (m <- "\\{([^{}]+)\\}".r.findAllIn(translation).matchData) yield new Replaceable(m.subgroups(0))).toList
        val coll = for (i <- 0 until (fragments.size + placeholders.size)) yield if (i % 2 == 0) fragments(i / 2) else placeholders(i / 2)
        coll.filterNot(_ match {
          case ConstantPlaceholder(content) => content == ""
          case _ => false
        }).toList
      }
      // Concatenate fragment lists.
      val withAdjacentConstants = placeholderLists.tail.foldLeft(placeholderLists.head)((p1, p2) => p1 ::: ConstantPlaceholder(escaping._2) :: p2)
      // Merge adjacent constants.
      def concat(accu: List[Placeholder], remaining: List[Placeholder]): List[Placeholder] = {
        remaining match {
          case List(ConstantPlaceholder(a), ConstantPlaceholder(b), _*) => concat(accu, ConstantPlaceholder(a + b) :: remaining.tail.tail)
          case List(a, b, _*) => concat(accu ::: a :: Nil, b :: remaining.tail.tail)
          case List(a) => accu ::: a :: Nil
          case Nil => accu
        }
      }
      concat(Nil, withAdjacentConstants)
    }
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
    val parsed = parseKeyOrTranlation(translation.sourceKey, sourceEscaping)
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
    trans.map(_ match {
      case ConstantPlaceholder(content) => content
      case TypedPlaceholder(name, domain) => {
        val categoryName = domain.getCategory(replacements get (name) match {
          case Some(value) => value
          case None => throw new NoSuchElementException(DoTranslate.doTranslate("Missing value for placeholder {0} of source key {1}.", name, translation.sourceKey))
        }).name
        if (domain.categories.isDefinedAt(1)) {
          // Wrap in category and placeholder if domains hosts more than 1 category.
          "{" + categoryName + "(" + domain.name + "(" + name + "))}"
        } else {
          // Only wrap in domain if it hosts only 1 category.
          "{" + domain.name + "(" + name + ")}"
        }
      }
    }).mkString
  }

  /**
   * Given a source key calculates all translation keys for a specific language.
   *
   * The generated keys are language specific because of the different rules that apply due to the
   * way a language's grammar threats a domain. For example the number of different plural forms
   * differs and so is when to apply which plural form.
   *
   * @param translation Include a source key and the target language.
   * @return All translation keys of this source key for the target language.
   */
  def buildAllTranslationKeys(translation: Translation): Set[String] = {
    val trans = parseSourceKey(translation)
    val variants = trans.filter(_.isInstanceOf[TypedPlaceholder]).flatMap {
      case TypedPlaceholder(name, domain) => for (cat <- domain.categories) yield (name, domain, cat.name)
    }.toSet
    val grouped = variants.groupBy(_._1)
    def bind(trans: Set[List[Placeholder]], groups: Map[String, Set[(String, Domain, String)]]): Set[String] = {
      if (groups.isEmpty) {
        for (te <- trans) yield te.map { case ConstantPlaceholder(content) => content }.mkString
      } else {
        val (phName, fullCategories) = groups.head
        val ex = for (fc <- fullCategories; te <- trans) yield te.map(_ match {
          // Placeholder for domain with at least 2 categories.
          case TypedPlaceholder(name, domain) if name == phName && domain.categories.isDefinedAt(1) => ConstantPlaceholder("{" + fc._3 + "(" + fc._2.name + "(" + phName + "))}")
          // Placeholder for domain with 1 category has no category printed.
          case TypedPlaceholder(name, domain) if name == phName => ConstantPlaceholder("{" + fc._2.name + "(" + phName + ")}")
          case other => other
        })
        bind(ex, groups.tail)
      }
    }
    bind(Set(trans), grouped)
  }
}