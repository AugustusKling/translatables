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

/**
 * English language
 */
object en extends Language(Locale.ENGLISH, List(
  // Two classes of numbers
  new Number(Locale.ENGLISH, List(
    // Zero or many
    new Category("zero", _ != 1),
    // Exactly one
    new Category("one", _ == 1))),
  new Domain("gender", List(
    new Category(Gender.Neuter.toString, _ == Gender.Neuter),
    new Category(Gender.Male.toString, _ == Gender.Male),
    new Category(Gender.Female.toString, _ == Gender.Female))),
  new Date(Locale.ENGLISH), new Time(Locale.ENGLISH),
  new Currency(Locale.ENGLISH, List(new Category("digits", _ => true)))),
  // Fallback language.
  Some(Root)) {

}