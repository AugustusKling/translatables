package domains.numbers

import java.util.Locale
import translatables.Category

/**
 * Manx, spoken in the United Kingdom. Sanskrit. Sami languages.
 */
class Rule18(locale: Locale) extends domains.Number(locale, List(
  Rule18.one,
  Rule18.two,
  Rule18.zero))

object Rule18 {
  // is 1: 1
  val one = new Category("one", _ == 1)
  // is 2: 2
  val two = new Category("two", _ == 2)
  // everything else: 0, 3, 4, 5, 6, 7, 8
  val zero = new Category("zero", _ => true)
}