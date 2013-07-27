package domains.numbers

import java.util.Locale
import translatables.Category

/**
 * Families: Slavic (Belarusian, Bosnian, Croatian, Serbian, Russian, Ukrainian)
 */
class Rule7(locale: Locale) extends domains.Number(locale, List(
  Rule7.one,
  Rule7.two,
  Rule7.zero))

object Rule7 {
  // ends in 1, excluding 11: 1, 21, 31, 41, 51, 61, 71, 81, 91, 101, 121, 131, 141, 151, 161, 171, 181, 191, 201, 221, 231, 241, 251, 261, 271, 281, 291, …
  val one = new Category("one", _ match {
    case value: Int => value % 100 != 11 && value % 10 == 1
    case _ => false
  })
  // ends in 2-4, excluding 12-14: 2, 3, 4, 22, 23, 24, 32, 33, 34, 42, 43, 44, 52, 53, 54, 62, 63, 64, 72, 73, 74, 82, 83, 84, 92, 93, 94, 102, 103, 104, 122, 123, 124, 132, 133, 134, 142, 143, 144, 152, 153, 154, 162, 163, 164, 172, 173, 174, 182, 183, …
  val two = new Category("two", _ match {
    case value: Int => value != 12 && value != 13 && value != 14 && (value % 10 > 1 && value % 10 < 5)
    case _ => false
  })
  // everything else: 0, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 25, 26, 27, 28, 29, 30, 35, 36, 37, 38, 39, 40, 45, 46, 47, 48, 49, 50, 55, 56, 57, 58, 59, 60, 65, 66, 67, 68, 69, 70, 75, 76, 77, …
  val zero = new Category("zero", _ => true)
}