package domains.numbers

import java.util.Locale
import translatables.Category

/**
 * Colognian
 */
class Rule23(locale: Locale) extends domains.Number(locale, List(
  Rule23.zero,
  Rule23.one,
  Rule23.two))

object Rule23 {
  val zero = new Category("zero", _ == 0)
  // is 1: 1
  val one = new Category("one", _ == 1)
  // is 2: 2
  val two = new Category("two", _ => true)
}