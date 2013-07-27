package domains.numbers

import java.util.Locale
import domains.Number
import translatables.Category

/**
 * Families: Asian (Chinese, Japanese, Korean, Vietnamese), Persian, Turkic/Altaic (Turkish), Thai, Lao
 */
class Rule0(locale: Locale) extends Number(locale, List(
  Rule0.zero))

object Rule0 {
  // everything: 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
  //  28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, …
  val zero = new Category("zero", _ => true)
}