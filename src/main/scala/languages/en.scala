package languages

import translatables.Language
import translatables.Domain
import translatables.Category
import domains.Gender
import domains.Date
import domains.Time
import domains.Currency
import domains.Number
import java.util.Locale
import domains.numbers.Rule1

/**
 * English language
 */
object en extends Language(Locale.ENGLISH, List(
  // Two classes of numbers
  new Rule1(Locale.ENGLISH),
  new Domain("gender", List(
    new Category(Gender.Neuter.toString, _ == Gender.Neuter),
    new Category(Gender.Male.toString, _ == Gender.Male),
    new Category(Gender.Female.toString, _ == Gender.Female))),
  new Date(Locale.ENGLISH), new Time(Locale.ENGLISH),
  new Currency(Locale.ENGLISH)),
  // Fallback language.
  Some(Root)) {

}