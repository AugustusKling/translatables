package domains

import translatables.Domain
import translatables.Category
import java.text.NumberFormat
import java.util.Locale
import java.text.DateFormat

/**
 * Time without date.
 * @param locale Formatting settings.
 */
class Time(locale: Locale) extends Domain("time", List(new Category("local", _ => true))) {
  override def format(value: Any): String = DateFormat.getTimeInstance(DateFormat.SHORT, locale).format(value)
}