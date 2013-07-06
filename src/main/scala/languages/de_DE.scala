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

/**
 * German language as spoken in Germany.
 */
object de_DE extends Language(Locale.GERMANY, List(
  // Two classes of numbers
  new Number(Locale.GERMANY, List(
    // Zero or many
    new Category("zero", _ != 1),
    // Exactly one
    new Category("one", _ == 1))),
  new Domain("gender", List(
    new Category(Gender.Neuter.toString, _ == Gender.Neuter),
    new Category(Gender.Male.toString, _ == Gender.Male),
    new Category(Gender.Female.toString, _ == Gender.Female))),
  new Date(Locale.GERMANY), new Time(Locale.GERMANY),
  new Currency(Locale.GERMANY, List(new Category("digits", _ => true)))),
  // Fallback language.
  Some(de)) {

}