package languages

import translatables.Language
import translatables.Domain
import domains.Date
import domains.Time
import domains.Currency
import domains.Number
import translatables.Category
import domains.Gender
import java.util.Locale
import domains.numbers.Rule0

/**
 * Chinese
 */
object zh_CN extends Language(Locale.CHINA, List(
  new Rule0(Locale.CHINA),
  new Date(Locale.CHINA), new Time(Locale.CHINA),
  new Currency(Locale.CHINA)),
  // Fallback language.
  Some(Root)) {

}