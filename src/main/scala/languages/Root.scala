package languages

import translatables.Language
import translatables.Domain
import translatables.Category
import java.util.Locale
import domains.Date
import domains.Time
import domains.Currency
import domains.Number

/**
 * Base language that provides a minimal set of domains so that users can trust them to be there.
 */
object Root extends Language(Locale.ROOT, List(
  new Domain("plain", List(new Category("plain", _ => true))),
  new Domain("number", List(new Category("one", _ => true))),
  new Domain("gender", List(new Category("neuter", _ => true))),
  new Date(Locale.GERMAN), new Time(Locale.GERMAN),
  new Currency(Locale.GERMAN, List(new Category("digits", _ => true)))), None) {
}