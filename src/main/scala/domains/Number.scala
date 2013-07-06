package domains

import translatables.Domain
import translatables.Category
import java.text.NumberFormat
import java.util.Locale

/**
 * Numbers (with or without floating).
 * @param locale Formatting settings.
 */
class Number(locale: Locale, categories: List[Category]) extends Domain("number", categories) {
  override def format(value: Any): String = NumberFormat.getInstance(locale).format(value)
}