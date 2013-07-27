package domains.numbers

import java.util.Locale
import translatables.Category

/**
 * Families: Semitic (Arabic)
 */
class Rule12(locale: Locale) extends domains.Number(locale, List(
  Rule12.zero,
  Rule12.one,
  Rule12.two,
  Rule12.three,
  Rule12.hundred,
  Rule12.eleven))

object Rule12 {
  // is 0: 0
  val zero = new Category("zero", _ == 0)
  // is 1: 1
  val one = new Category("one", _ == 1)
  // is 2: 2
  val two = new Category("two", _ == 2)
  // ends in 03-10: 3, 4, 5, 6, 7, 8, 9, 10, 103, 104, 105, 106, 107, 108, 109, 110, 203, 204, 205, 206, 207, 208, 209,
  //  210, …
  val three = new Category("three", _ match {
    case value: Int => value % 100 > 2 && value % 100 < 11
    case _ => false
  })
  // ends in 00-02, excluding 0-2: 100, 101, 102, 200, 201, 202, …
  val hundred = new Category("hundred", _ match {
    case value: Int => (value % 100 >= 0 && value % 100 < 3) && !(List(0, 1, 2) contains (value))
    case _ => false
  })
  // everything else but is 0 and ends in 00-02, excluding 0-2: 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
  //  25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, …
  val eleven = new Category("eleven", _ => true)
}