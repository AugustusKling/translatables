package domains.numbers

import java.util.Locale
import translatables.Category

/**
 * Families: Slavic (Slovenian, Sorbian)
 */
class Rule10(locale: Locale) extends domains.Number(locale, List(
  Rule10.one,
  Rule10.two,
  Rule10.three,
  Rule10.zero))

object Rule10 {
  // ends in 01: 1, 101, 201, …
  val one = new Category("one", _ match {
    case value: Int => value % 100 == 1
    case _ => false
  })
  // ends in 02: 2, 102, 202, …
  val two = new Category("two", _ match {
    case value: Int => value % 100 == 2
    case _ => false
  })
  // ends in 03-04: 3, 4, 103, 104, 203, 204, …
  val three = new Category("three", _ match {
    case value: Int => value % 100 == 3 || value % 100 == 4
    case _ => false
  })
  // everything else: 0, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
  //  30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, …
  val zero = new Category("zero", _ => true)
}