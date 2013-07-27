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
 * English language as spoken in Britain
 */
object en_GB extends Language(Locale.UK, List(
  // Two classes of numbers
  new Rule1(Locale.UK),
  new Domain("gender", List(
    new Category(Gender.Neuter.toString, _ == Gender.Neuter),
    new Category(Gender.Male.toString, _ == Gender.Male),
    new Category(Gender.Female.toString, _ == Gender.Female))),
  new Date(Locale.UK), new Time(Locale.UK),
  new Currency(Locale.UK, List(new Category("digits", _ => true)))),
  // Fallback language.
  Some(Root)) {

}