package domains.numbers

import java.util.Locale
import translatables.Category

/**
 * Families: Moldavian
 */
class Rule24(locale: Locale) extends domains.Number(locale, List(
  Rule24.zero,
  Rule24.one,
  Rule24.twenty))

object Rule24 {
  // is 0 or ends in 1 to 19: 0, 2-19, 101-119, 201-219
  val zero = new Category("zero", _ match {
    case 0 => true
    case 1 => false
    case value: Int if 1 to 19 contains (value % 100) => true
    case _ => false
  })
  // is 1: 1
  val one = new Category("one", _ == 1)
  // everything else: 20-100, 120-200, 220-300, 1.2, 2.07, 20.94
  val twenty = new Category("eleven", _ => true)
}