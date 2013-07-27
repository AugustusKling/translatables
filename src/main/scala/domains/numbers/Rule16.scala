package domains.numbers

import java.util.Locale
import translatables.Category

/**
 * Families: Celtic (Breton)
 */
class Rule16(locale: Locale) extends domains.Number(locale, List(
  Rule16.one,
  Rule16.twentyOne,
  Rule16.two,
  Rule16.three,
  Rule16.million,
  Rule16.zero))

// TODO Verify rules. These are of Mozilla but Lauchpad claims French rules (2 plurals).
object Rule16 {
  // is 1: 1
  val one = new Category("one", _ == 1)
  // ends in 1, excluding 1, 11, 71, 91: 21, 31, 41, 51, 61, 81, 101, 121, 131, 141, 151, 161, 181, 201, 221, 231, 241,
  //  251, 261, 281, ...
  val twentyOne = new Category("twentyOne", _ match {
    case value: Int => (value % 10 == 1) && !(List(1, 11, 71, 91) contains value)
    case _ => false
  })
  // ends in 2, excluding 12, 72, 92: 2, 22, 32, 42, 52, 62, 82, 102, 122, 132, 142, 152, 162, 182, 202, 222, 232, 242,
  //  252, 262, 282, ...
  val two = new Category("two", _ match {
    case value: Int => (value % 10 == 2) && !(List(12, 72, 92) contains value)
    case _ => false
  })
  // ends in 3, 4 or 9 excluding 13, 14, 19, 73, 74, 79, 93, 94, 99: 3, 4, 9, 23, 24, 29, 33, 34, 39, 43, 44, 49, 53,
  //  54, 59, ...
  val three = new Category("three", _ match {
    case value: Int => (List(3, 4, 9) contains (value % 10)) && !(List(13, 14, 19, 73, 74, 79, 93, 94, 99) contains value)
    case _ => false
  })
  // ends in 1000000: 1000000: 1000000, 2000000, 3000000, 4000000, 5000000, 6000000, 7000000, 8000000, 9000000,
  //  10000000, ...
  val million = new Category("million", _ match {
    case value: Int if value != 0 => value % 1000000 == 0
    case _ => false
  })
  // everything else: 0, 5, 6, 7, 8, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 25, 26, 27, 28, 30, 35, 36, 37, 38,
  //  40, ...
  val zero = new Category("zero", _ => true)
}