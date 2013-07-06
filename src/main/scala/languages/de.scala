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

/**
 * German language
 */
object de extends Language(Locale.GERMAN, List(
  // Two classes of numbers
  new Number(Locale.GERMAN, List(
    // Zero or many
    new Category("zero", _ != 1),
    // Exactly one
    new Category("one", _ == 1))),
  new Domain("gender", List(
    new Category(Gender.Neuter.toString, _ == Gender.Neuter),
    new Category(Gender.Male.toString, _ == Gender.Male),
    new Category(Gender.Female.toString, _ == Gender.Female))),
  new Date(Locale.GERMAN), new Time(Locale.GERMAN),
  new Currency(Locale.GERMAN, List(new Category("digits", _ => true)))),
  // Fallback language.
  Some(Root)) {

}