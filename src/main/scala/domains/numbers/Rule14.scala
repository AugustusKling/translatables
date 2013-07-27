package domains.numbers

import java.util.Locale
import translatables.Category

/**
 * Families: Slavic (Macedonian)
 */
class Rule14(locale: Locale) extends domains.Number(locale, List(
  Rule14.one,
  Rule14.two,
  Rule14.zero))

object Rule14 {
  // ends in 1: 1, 11, 21, 31, 41, 51, 61, 71, 81, 91, 101, 111, 121, 131, 141, 151, 161, 171, 181, 191, 201, 211, 221,
  //  231, 241, 251, 261, 271, 281, 291, …
  val one = new Category("one", _ match {
    case value: Int => value % 10 == 1
    case _ => false
  })
  // ends in 2: 2, 12, 22, 32, 42, 52, 62, 72, 82, 92, 102, 112, 122, 132, 142, 152, 162, 172, 182, 192, 202, 212, 222,
  //  232, 242, 252, 262, 272, 282, 292, …
  val two = new Category("two", _ match {
    case value: Int => value % 10 == 2
    case _ => false
  })
  // everything else: 0, 3, 4, 5, 6, 7, 8, 9, 10, 13, 14, 15, 16, 17, 18, 19, 20, 23, 24, 25, 26, 27, 28, 29, 30, 33,
  //  34, 35, 36, 37, 38, 39, 40, 43, 44, 45, 46, 47, 48, 49, 50, 53, 54, 55, 56, 57, 58, 59, 60, 63, …
  val zero = new Category("zero", _ => true)
}