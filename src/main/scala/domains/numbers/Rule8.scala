package domains.numbers

import java.util.Locale
import translatables.Category

/**
 * Families: Slavic (Slovak, Czech)
 */
class Rule8(locale: Locale) extends domains.Number(locale, List(
  Rule8.one,
  Rule8.two,
  Rule8.zero))

object Rule8 {
  // is 1: 1
  val one = new Category("one", _ == 1)
  // is 2-4: 2, 3, 4
  val two = new Category("two", value => List(2, 3, 4) contains (value))
  // everything else: 0, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
  //  30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, …
  val zero = new Category("zero", _ => true)
}