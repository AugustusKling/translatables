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
object zh_TW extends Language(Locale.TAIWAN, List(
  new Rule0(Locale.TAIWAN),
  new Date(Locale.TAIWAN), new Time(Locale.TAIWAN),
  new Currency(Locale.TAIWAN, List(new Category("digits", _ => true)))),
  // Fallback language.
  Some(Root)) {

}