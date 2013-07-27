package domains.numbers

import java.util.Locale
import translatables.Category

/**
 * Cornish, spoken in the United Kingdom.
 */
class Rule17(locale: Locale) extends domains.Number(locale, List(
  Rule17.one,
  Rule17.two,
  Rule17.three,
  Rule17.zero))

object Rule17 {
  // is 1: 1
  val one = new Category("one", _ == 1)
  // is 2: 2
  val two = new Category("two", _ == 2)
  // is 3: 3
  val three = new Category("three", _ == 3)
  // everything else: 0, 4, 5, 6, 7, 8
  val zero = new Category("zero", _ => true)
}