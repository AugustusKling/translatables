package domains

import translatables.Domain
import translatables.Category
import java.text.NumberFormat
import java.util.Locale
import java.text.DateFormat
import java.util.Calendar
import java.text.SimpleDateFormat

/**
 * Date without time.
 * @param locale Formatting settings.
 */
class Date(locale: Locale) extends Domain("date", List(new Category("digits", _ => true))) {
  val dateFormatter = if (locale == Locale.ROOT) {
    // Use something that confirms to ISO 8601, is unambiguous and does not contain English text.
    new SimpleDateFormat("yyyy-MM-dd")
  } else {
    DateFormat.getDateInstance(DateFormat.DEFAULT, locale)
  }
  override def format(value: Any): String = value match {
    // Format Date which is supported.
    case value: Date => dateFormatter.format(value)
    // Convert to Date then format.
    case value: Calendar => dateFormatter.format(value.getTime)
    // Simply provide value to formatter and hope it handles the type. Aborts for unsupported types with
    // IllegalArgumentException.
    case _ => dateFormatter.format(value)
  }
}