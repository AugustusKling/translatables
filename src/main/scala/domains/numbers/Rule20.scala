package domains.numbers

import java.util.Locale
import translatables.Category

/**
 * Javanese, spoken in the Indonesia.
 */
class Rule20(locale: Locale) extends domains.Number(locale, List(
  Rule20.zero,
  Rule20.one))

object Rule20 {
  val zero = new Category("zero", _ == 0)
  val one = new Category("one", _ => true)
}