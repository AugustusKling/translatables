package domains.numbers

import java.util.Locale
import translatables.Category

/**
 * Families: Baltic (Lithuanian)
 */
class Rule6(locale: Locale) extends domains.Number(locale, List(
  Rule6.one,
  Rule6.zero,
  Rule6.two))

object Rule6 {
  // ends in 1, excluding 11: 1, 21, 31, 41, 51, 61, 71, 81, 91, 101, 121, 131, 141, 151, 161, 171, 181, 191, 201, 221,
  //  231, 241, 251, 261, 271, 281, 291, …
  val one = new Category("one", _ match {
    case value: Int => value % 100 != 11 && value % 10 == 1
    case _ => false
  })
  // ends in 0 or ends in 11-19: 0, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110,
  //  111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 130, 140, 150, 160, 170, 180, 190, 200, 210, 211, 212, 213,
  //  214, 215, 216, 217, 218, 219, 220, …
  val zero = new Category("zero", _ match {
    case value: Int => value % 10 == 0 || (value % 100 > 10 && value % 100 < 20)
    case _ => false
  })
  // everything else: 2, 3, 4, 5, 6, 7, 8, 9, 22, 23, 24, 25, 26, 27, 28, 29, 32, 33, 34, 35, 36, 37, 38, 39, 42, 43,
  //  44, 45, 46, 47, 48, 49, 52, 53, 54, 55, 56, 57, 58, 59, 62, 63, 64, 65, 66, 67, 68, 69, 72, 73, …
  val two = new Category("two", _ => true)
}