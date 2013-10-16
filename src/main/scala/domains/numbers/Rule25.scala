package domains.numbers

import java.util.Locale
import domains.Number
import translatables.Category
import math.Ordering.Implicits.infixOrderingOps
import scala.math.Integral

/**
 * Families: Tachelhit
 */
class Rule25(locale: Locale) extends Number(locale, List(
  Rule25.zero,
  Rule25.two,
  Rule25.eleven))

object Rule25 {
  // is 0 or 1: 0, 1
  val zero = new Category("zero", value => value == 0 || value == 1)
  // 2 to 10: 2, 3, 4, 5, 6, 7, 8, 9, 10
  val two = new Category("two", _ match {
    case value: Int => 2 to 10 contains value
    case _ => false
  })
  // everything else: 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28,
  //  29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, …
  val eleven = new Category("eleven", _ => true)
}