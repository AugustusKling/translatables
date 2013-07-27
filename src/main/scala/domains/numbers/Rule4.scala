package domains.numbers

import java.util.Locale
import translatables.Category

/**
 * Families: Celtic (Scottish Gaelic)
 */
class Rule4(locale: Locale) extends domains.Number(locale, List(
  Rule4.one,
  Rule4.two,
  Rule4.three,
  Rule4.zero))

object Rule4 {
  // is 1 or 11: 1, 11
  val one = new Category("one", value => value == 1 || value == 11)
  // is 2 or 12: 2, 12
  val two = new Category("two", value => value == 2 || value == 12)
  // is 3-10 or 13-19: 3, 4, 5, 6, 7, 8, 9, 10, 13, 14, 15, 16, 17, 18, 19
  val three = new Category("three", value => {
    (3 to (10) contains (value)) || (13 to (19) contains (value))
  })
  // everything else: 0, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42,
  //  43, 44, 45, 46, 47, 48, 49, 50, 51, …
  val zero = new Category("zero", _ => true)
}