package domains.numbers

import java.util.Locale
import translatables.Category

/**
 * Families: Celtic (Irish Gaelic)
 */
class Rule11(locale: Locale) extends domains.Number(locale, List(
  Rule11.one,
  Rule11.two,
  Rule11.three,
  Rule11.seven,
  Rule11.zero))

// TODO These are the rules of Mozilla but Lauchpad put cases for three, seven and zero together.
object Rule11 {
  // is 1: 1
  val one = new Category("one", _ == 1)
  // is 2: 2
  val two = new Category("two", _ == 2)
  // is 3-6: 3, 4, 5, 6
  val three = new Category("three", value => List(3, 4, 5, 6) contains (value))
  // is 7-10: 7, 8, 9, 10
  val seven = new Category("seven", value => List(7, 8, 9, 10) contains (value))
  // everything else: 0, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33,
  //  34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, …
  val zero = new Category("zero", _ => true)
}