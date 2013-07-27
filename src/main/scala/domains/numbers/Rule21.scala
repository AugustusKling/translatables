package domains.numbers

import java.util.Locale
import translatables.Category

/**
 * Quechua, spoken in Bolivia, Chile, Ecuador, Peru.
 */
class Rule21(locale: Locale) extends domains.Number(locale, List(
  Rule21.one,
  Rule21.zero))

// TODO Verify rules are correct.
object Rule21 {
  // 1, 21, 31, 41, 51, 61
  val one = new Category("one", _ match {
    case value: Number => value != 11 && value.intValue() % 10 == 1
    case _ => false
  })

  // 0, 2, 3, 4, 5, 6
  val zero = new Category("zero", _ => true)
}