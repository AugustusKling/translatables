package languages

import translatables.Language
import translatables.Domain
import domains.Number
import translatables.Category
import domains.Gender
import java.util.Locale
import domains.Date
import domains.Time
import domains.Currency
import domains.numbers.Rule1

/**
 * German language as spoken in Germany.
 */
object de_DE extends Language(Locale.GERMANY, List(
  // Two classes of numbers
  new Rule1(Locale.GERMANY),
  new Domain("gender", List(
    new Category(Gender.Neuter.toString, _ == Gender.Neuter),
    new Category(Gender.Male.toString, _ == Gender.Male),
    new Category(Gender.Female.toString, _ == Gender.Female))),
  new Date(Locale.GERMANY), new Time(Locale.GERMANY),
  new Currency(Locale.GERMANY)),
  // Fallback language.
  Some(de)) {

}