package domains.numbers

import java.util.Locale
import translatables.Category

/**
 * Families: Semitic (Maltese)
 */
class Rule13(locale: Locale) extends domains.Number(locale, List(
  Rule13.one,
  Rule13.zero,
  Rule13.eleven,
  Rule13.twenty))

object Rule13 {
  // is 1: 1
  val one = new Category("one", _ == 1)
  // is 0 or ends in 01-10, excluding 1: 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 101, 102, 103, 104, 105, 106, 107, 108, 109,
  //  110, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, …
  val zero = new Category("zero", _ match {
    case 0 => true
    case value: Int if value != 1 => value % 100 > 0 && value % 100 < 11
    case _ => false
  })
  // ends in 11-19: 11, 12, 13, 14, 15, 16, 17, 18, 19, 111, 112, 113, 114, 115, 116, 117, 118, 119, 211, 212, 213,
  //  214, 215, 216, 217, 218, 219, …
  val eleven = new Category("eleven", _ match {
    case value: Int => value % 100 > 10 && value % 100 < 20
    case _ => false
  })
  // everything else: 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43,
  //  44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, …
  val twenty = new Category("twenty", _ => true)
}