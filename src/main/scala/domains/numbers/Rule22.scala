package domains.numbers

import java.util.Locale
import translatables.Category

/**
 * Central Atlas Tamazight
 */
class Rule22(locale: Locale) extends domains.Number(locale, List(
  Rule22.zero,
  Rule22.two))

object Rule22 {
  // n in 0..1 or n in 11..99.
  val zero = new Category("zero", value => 0 :: 1 :: (11 to 99).toList contains (value))
  // everything else.
  val two = new Category("two", _ => true)
}