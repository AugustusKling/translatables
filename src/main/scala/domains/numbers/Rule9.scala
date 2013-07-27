package domains.numbers

import java.util.Locale
import translatables.Category

/**
 * Families: Slavic (Polish)
 */
class Rule9(locale: Locale) extends domains.Number(locale, List(
  Rule9.one,
  Rule9.two,
  Rule9.zero))

object Rule9 {
  // is 1: 1
  val one = new Category("one", _ == 1)
  // ends in 2-4, excluding 12-14: 2, 3, 4, 22, 23, 24, 32, 33, 34, 42, 43, 44, 52, 53, 54, 62, 63, 64, 72, 73, 74, 82,
  //  83, 84, 92, 93, 94, 102, 103, 104, 122, 123, 124, 132, 133, 134, 142, 143, 144, 152, 153, 154, 162, 163, 164,
  //  172, 173, 174, 182, 183, …
  val two = new Category("two", _ match {
    case value: Int => value != 12 && value != 13 && value != 14 && (value % 10 > 1 && value % 10 < 5)
    case _ => false
  })
  // everything else: 0, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 25, 26, 27, 28, 29, 30, 31, 35,
  //  36, 37, 38, 39, 40, 41, 45, 46, 47, 48, 49, 50, 51, 55, 56, 57, 58, 59, 60, 61, 65, 66, 67, 68, …
  val zero = new Category("zero", _ => true)
}