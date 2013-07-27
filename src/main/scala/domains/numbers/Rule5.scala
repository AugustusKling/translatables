package domains.numbers

import java.util.Locale
import translatables.Category

/**
 * Families: Romanic (Romanian)
 */
class Rule5(locale: Locale) extends domains.Number(locale, List(
  Rule5.one,
  Rule5.zero,
  Rule5.twenty))

object Rule5 {
  // is 1: 1
  val one = new Category("one", _ == 1)
  // is 0 or ends in 01-19, excluding 1: 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 101, 102,
  //  103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 201, 202, 203, 204, 205,
  //  206, 207, 208, 209, 210, 211, 212, …
  val zero = new Category("zero", value => value == 0 || (value match {
    case value: Int => value % 100 < 20
    case _ => false
  }))
  // everything else: 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43,
  //  44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, …
  val twenty = new Category("twenty", _ => true)
}