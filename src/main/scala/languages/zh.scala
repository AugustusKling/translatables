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
object zh extends Language(Locale.CHINESE, List(
  new Rule0(Locale.CHINESE),
  new Date(Locale.CHINESE), new Time(Locale.CHINESE),
  new Currency(Locale.CHINESE)),
  // Fallback language.
  Some(Root)) {

}