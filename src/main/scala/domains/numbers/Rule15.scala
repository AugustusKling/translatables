package domains.numbers

import java.util.Locale
import translatables.Category

/**
 * Families: Icelandic
 */
class Rule15(locale: Locale) extends domains.Number(locale, List(
  Rule15.one,
  Rule15.zero))

object Rule15 {
  // ends in 1, excluding 11: 1, 21, 31, 41, 51, 61, 71, 81, 91, 101, 121, 131, 141, 151, 161, 171, 181, 191, 201, 221,
  //  231, 241, 251, 261, 271, 281, 291, …
  val one = new Category("one", _ match {
    case value: Int if value != 11 => value % 10 == 1
    case _ => false
  })
  // everything else: 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 22, 23, 24, 25, 26, 27,
  //  28, 29, 30, 32, 33, 34, 35, 36, 37, 38, 39, 40, 42, 43, 44, 45, 46, 47, 48, 49, 50, 52, 53, 54, …
  val zero = new Category("zero", _ => true)
}