package domains.numbers

import java.util.Locale
import domains.Number
import translatables.Category

/**
 * Families: Germanic (Danish, Dutch, English, Faroese, Frisian, German, Norwegian, Swedish), Finno-Ugric (Estonian,
 *  Finnish, Hungarian), Language isolate (Basque), Latin/Greek (Greek), Semitic (Hebrew), Romanic (Italian,
 *  Portuguese, Spanish, Catalan)
 */
class Rule1(locale: Locale) extends Number(locale, List(Rule1.zero, Rule1.one))

object Rule1 {
  //  is 1: 1
  val one = new Category("one", _ == 1)
  // everything else: 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
  //  28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, …
  val zero = new Category("zero", _ != 1)
}