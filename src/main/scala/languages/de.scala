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
import domains.numbers.Rule1

/**
 * German language
 */
object de extends Language(Locale.GERMAN, List(
  // Two classes of numbers
  new Rule1(Locale.GERMAN),
  new Domain("gender", List(
    new Category(Gender.Neuter.toString, _ == Gender.Neuter),
    new Category(Gender.Male.toString, _ == Gender.Male),
    new Category(Gender.Female.toString, _ == Gender.Female))),
  new Date(Locale.GERMAN), new Time(Locale.GERMAN),
  new Currency(Locale.GERMAN)),
  // Fallback language.
  Some(Root)) {

}