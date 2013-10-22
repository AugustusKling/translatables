package translatables

import java.util.Locale
import languages.Root
import domains.Number
import domains.Date
import domains.Time
import domains.Currency
import domains.numbers.Rule0
import domains.numbers.Rule1
import domains.numbers.Rule2
import domains.numbers.Rule3
import domains.numbers.Rule4
import domains.numbers.Rule5
import domains.numbers.Rule6
import domains.numbers.Rule7
import domains.numbers.Rule8
import domains.numbers.Rule9
import domains.numbers.Rule10
import domains.numbers.Rule11
import domains.numbers.Rule12
import domains.numbers.Rule13
import domains.numbers.Rule14
import domains.numbers.Rule15
import domains.numbers.Rule16
import domains.numbers.Rule17
import domains.numbers.Rule18
import domains.numbers.Rule19
import domains.numbers.Rule20
import domains.numbers.Rule21
import domains.numbers.Rule22
import languages.de_DE
import languages.de
import languages.en_CA
import languages.en_GB
import languages.en_US
import languages.en
import languages.fr_CA
import languages.fr_FR
import languages.fr
import languages.it_IT
import languages.it
import languages.ja
import languages.ja_JP
import languages.ko_KR
import languages.ko
import languages.zh_CN
import languages.zh_TW
import languages.zh
import domains.numbers.Rule23
import domains.numbers.Rule24
import domains.numbers.Rule25
import scala.collection.JavaConversions

/**
 * Target language that consists of domains. It serves as translation target and translation memory.
 * @param code Language code such as de or de-CH.
 * @param extensionDomains The contained domains define the grammar rules of the language.
 * @param fallback Other language that is queried for domains and translation if missing in this language.
 */
class Language(val code: Locale, extensionDomains: List[Domain], val fallback: Option[Language]) {
  /**
   * @param code Language code such as de or de-CH.
   * @param extensionDomains The contained domains define the grammar rules of the language.
   * @param fallback Other language that is queried for domains and translation if missing in this language.
   */
  def this(code: Locale, extensionDomains: java.util.List[Domain], fallback: Language) {
    // Convert Java types to Scala types.
    this(code, JavaConversions.collectionAsScalaIterable(extensionDomains).toList, Option(fallback))
  }

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
   * Looks up translation by key
   */
  def getTranslation(translationKey: String, adapter: Adapter): List[Placeholder] = {
    try {
      val translation = adapter.get(translationKey);
      Format.parseTranslation(translation)
    } catch {
      case e: NotTranslated => fallback match {
        case None =>
          Format.buildFallback(translationKey)
        case fallback => fallback.get.getTranslation(translationKey, adapter)
      }
    }
  }

  /**
   * Language and region represented by this {@code Language}.
   */
  def locale = code
}

object Language {
  /**
   * Gets hand-made language if defined but auto-constructs a language as fallback.
   */
  def fromLocale(locale: Locale): Language = {
    locale match {
      case Locale.GERMANY => de_DE
      case Locale.GERMAN => de
      case Locale.CANADA => en_CA
      case Locale.UK => en_GB
      case Locale.US => en_US
      case Locale.ENGLISH => en
      case Locale.CANADA_FRENCH => fr_CA
      case Locale.FRANCE => fr_FR
      case Locale.FRENCH => fr
      case Locale.ITALY => it_IT
      case Locale.ITALIAN => it
      case Locale.JAPAN => ja_JP
      case Locale.JAPANESE => ja
      case Locale.KOREA => ko_KR
      case Locale.KOREAN => ko
      case Locale.CHINA => zh_CN
      case Locale.TAIWAN => zh_TW
      case Locale.CHINESE => zh
      // No tested implementation available.
      case _ => autoConstruct(locale)
    }
  }
  /**
   * Builds language with fallback to root language which uses Java's localization capabilities. Pluralization rules
   * are not guaranteed to be known for any given locale.
   * @param locale The language (ISO 639 codes from all 6 parts) and its varieties. Falls back to root language in case
   *  the requested language is unsupported. Please do request languages additionally required given they are listed
   *  in any part of ISO 639.
   */
  def autoConstruct(locale: Locale): Language = {
    val pluralizationRuleLanguageCountry = (locale.getLanguage() + "_" + locale.getCountry) match {
      case "pt_BR" => Option(new Rule2(locale))
      case _ => None
    }
    // See NumberRules unit test for overview.
    lazy val pluralizationRuleLanguage = locale.getLanguage match {
      // other → everything
      case "zh" | "ja" | "ko" | "vi" | "fa" | "tr" | "th" | "lo" | "my" | "ka"
        // Former code for id was in.
        | "id" | "in"
        | "dv" | "dz" | "kr" | "km"
        | "bo" | "tk" | "ug" | "hy" | "cv" | "tt" | "uz" | "bm" | "ig" | "kea" | "ses" | "kde" | "sah" | "sg" | "ii"
        | "to"
        // Yoruba does not have plurals but denotes amounts by pluaral words such as àwọn but number placeholders
        // should serve the same purpose.
        | "yo" => Option(new Rule0(locale))
      // one → n is 1;
      // other → everything else
      case "da" | "nl" | "fo" | "fy" | "de" | "nn" | "nb" | "no" | "sv" | "et" | "fi" | "mn"
        // Unicode splits out case for 1 for kk. learn101.org  has a plural, too. Launchpad puts it to Rule0.
        | "kk"
        | "ky"
        // Unicode put hu to Rule0 but Lauchpad, Mozilla nad learn101.org have it in Rule1.
        | "hu"
        | "eu" | "la" | "el"
        // Unicode uses own rule but Lauchpad, Mozilla nad learn101.org have it in Rule1.
        | "he"
        // Former code for he was iw
        | "iw"
        | "it" | "pt" | "es" | "ca" | "bg" | "eo"
        // Former code for yi was ji
        | "yi" | "ji"
        // Unicode put hi to Rule2 but various open source projects use Rule1.
        | "hi"
        | "sq" | "as" | "gu" | "ha"
        // Unicode put kn to Rule0.
        // Lauchpad uses Rule1: https://answers.launchpad.net/launchpad/+question/13561
        // Gnome uses Rule1: https://l10n.gnome.org/teams/kn/
        | "kn"
        | "ml"
        | "om" | "rm" | "si" | "st" | "sw" | "tg" | "te"
        | "ts" | "ur"
        | "xh" | "af" | "an" | "ay" | "ny" | "io" | "rw" | "ku" | "lb" | "mr" | "ne" | "oj"
        | "os" | "ps" | "pa" | "sc" | "zu" | "aa"
        // TODO Unicode put this to Rule0. 
        | "az"
        | "bn" | "ff" | "gl" | "ht" | "ia" | "li"
        // Unicode puts ms to Rule0.
        | "ms"
        | "or"
        | "sd" | "so" | "ta" | "ast" | "asa" | "bem" | "bez" | "brx" | "chr" | "cgg" | "ee" | "fur" | "lg" | "haw"
        | "kaj" | "kkj" | "kl" | "ks" | "jmc" | "mas" | "mgo" | "nah" | "nnh" | "jgo" | "nd" | "nyn" | "pap" | "rof"
        | "rwk" | "ssy" | "saq" | "seh" | "ksb" | "sn" | "xog" | "ckb" | "nr" | "ss" | "gsw" | "syr" | "teo" | "tig"
        | "tn" | "kcg" | "ve" | "vo" | "vun" | "wae" => Option(new Rule1(locale))
      // one → n in 0..1;
      // other → everything else
      case "fr" | "ln" | "oc" | "ti" | "tl" | "ak" | "am" | "mi"
        // Walloon is similar to French.
        | "wa" | "wln"
        // Unicode puts wo to Rule0 but it seems to use articles to denote plurals.
        | "wo"
        | "bh" | "fil" | "guw" | "nso"
        // Fractions should split below and above 2 for kab.
        | "kab"
        // Unicode puts mg to Rule2 but Lauchpad and learn101.org have it in Rule0. Even though Malagasy does not have
        // plurals for nouns it does distinguish 2 forms for pronouns.
        | "mg" => Option(new Rule2(locale))
      case "lv" => Option(new Rule3(locale))
      case "gd" => Option(new Rule4(locale))
      case "ro" => Option(new Rule5(locale))
      case "lt" => Option(new Rule6(locale))
      case "be" | "bs" | "hr" | "sr" | "ru" | "uk" | "sh" => Option(new Rule7(locale))
      case "sk" | "cs" => Option(new Rule8(locale))
      case "pl" => Option(new Rule9(locale))
      case "sl" => Option(new Rule10(locale))
      case "ga" => Option(new Rule11(locale))
      case "ar" => Option(new Rule12(locale))
      case "mt" => Option(new Rule13(locale))
      // Unicode does not split off the ends in 2 case.
      case "mk" => Option(new Rule14(locale))
      // Unicode puts is to Rule1.
      case "is" => Option(new Rule15(locale))
      case "br" => Option(new Rule16(locale))
      // Unicode disagrees with kw beeing Rule17 and puts it into Rule18.
      case "kw" => Option(new Rule17(locale))
      // Unicode uses own rules for gv.
      case "gv"
        // Various Sami languages have a dual cases.
        | "sa" | "smn" | "iu" | "smj" | "naq" | "smi" | "sms" | "sma" | "sjd" | "sje" | "se" | "sme" => Option(new Rule18(locale))
      case "cy" => Option(new Rule19(locale))
      // Unicode does not split out extra case for 0.
      case "jv" => Option(new Rule20(locale))
      case "qu" => Option(new Rule21(locale))
      case "tzm" => Option(new Rule22(locale))
      case "ksh"
        // Fractions should split below and above 2 for lag.
        | "lag" => Option(new Rule23(locale))
      case "mo" => Option(new Rule24(locale))
      case "shi" => Option(new Rule25(locale))
      case _ => None
    }
    new Language(locale, List(
      pluralizationRuleLanguageCountry.getOrElse(pluralizationRuleLanguage.getOrElse(
        new Number(locale, List(new Category("one", _ => true))))),
      new Date(locale), new Time(locale),
      new Currency(locale)), Some(Root))
  }
}