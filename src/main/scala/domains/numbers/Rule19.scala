package domains.numbers

import java.util.Locale
import translatables.Category

/**
 * Welsh, spoken in the United Kingdom.
 */
class Rule19(locale: Locale) extends domains.Number(locale, List(
  Rule19.one,
  Rule19.two,
  Rule19.zero,
  Rule19.eight))

// TODO Verify rules are correct as Lauchpad and Unicode differ. Lauchpad's version is more similar to other Gaelic
// languages. Extend the tests.
object Rule19 {
  // is 1: 1
  val one = new Category("one", _ == 1)
  // is 2: 2
  val two = new Category("two", _ == 2)

  // 0, 3, 4, 5, 6, 7...
  val zero = new Category("zero", value => value match {
    case 0 => true
    case value: Int => value > 2 && value < 8
  })
  // everything else: 8, 11...
  val eight = new Category("eight", _ => true)
}