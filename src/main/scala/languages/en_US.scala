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
 * English language as spoken in the USA.
 */
object en_US extends Language(Locale.US, List(
  // Two classes of numbers
  new Rule1(Locale.US),
  new Domain("gender", List(
    new Category(Gender.Neuter.toString, _ == Gender.Neuter),
    new Category(Gender.Male.toString, _ == Gender.Male),
    new Category(Gender.Female.toString, _ == Gender.Female))),
  new Date(Locale.US), new Time(Locale.US),
  new Currency(Locale.US)),
  // Fallback language.
  Some(Root)) {

}