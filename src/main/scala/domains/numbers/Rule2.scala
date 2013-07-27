package domains.numbers

import java.util.Locale
import domains.Number
import translatables.Category

/**
 * Families: Romanic (French, Brazilian Portuguese)
 */
class Rule2(locale: Locale) extends Number(locale, List(
  Rule2.zero,
  Rule2.two))

object Rule2 {
  // is 0 or 1: 0, 1
  val zero = new Category("zero", value => value == 0 || value == 1)
  // everything else: 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28,
  //  29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, …
  val two = new Category("two", _ => true)
}