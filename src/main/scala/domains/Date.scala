package domains

import translatables.Domain
import translatables.Category
import java.text.NumberFormat
import java.util.Locale
import java.text.DateFormat
import java.util.Calendar

/**
 * Date without time.
 * @param locale Formatting settings.
 */
class Date(locale: Locale) extends Domain("date", List(new Category("digits", _ => true))) {
  override def format(value: Any): String = value match {
    // Format Date which is supported.
    case value: Date => DateFormat.getDateInstance(DateFormat.DEFAULT, locale).format(value)
    // Convert to Date then format.
    case value: Calendar => DateFormat.getDateInstance(DateFormat.DEFAULT, locale).format(value.getTime)
    // Simply provide value to formatter and hope it handles the type. Aborts for unsupported types with
    // IllegalArgumentException.
    case _ => DateFormat.getDateInstance(DateFormat.DEFAULT, locale).format(value)
  }
}