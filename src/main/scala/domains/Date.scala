package domains

import translatables.Domain
import translatables.Category
import java.text.NumberFormat
import java.util.Locale
import java.text.DateFormat

/**
 * Date without time.
 * @param locale Formatting settings.
 */
class Date(locale: Locale) extends Domain("date", List(new Category("digits", _ => true))) {
  override def format(value: Any): String = DateFormat.getDateInstance(DateFormat.DEFAULT, locale).format(value)
}