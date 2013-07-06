package domains
import java.util.Locale
import translatables.Domain
import translatables.Category
import java.text.NumberFormat
import java.text.DecimalFormat

/**
 * Monetary value. Displays general currency symbol for numbers, for (Number, Currency) tuples values with matching currency symbol.
 * @param locale Formatting settings.
 */
class Currency(locale: Locale, categories: List[Category]) extends Domain("currency", categories) {
  override def format(value: Any): String = {
    val formatter = NumberFormat.getCurrencyInstance(locale)
    value match {
      case plainNumber: java.lang.Number => {
        // Replace locale's currency symbol with an undefined one because the number to format does not carry a currency
        // information. Formatting should not fake more meaning than actually available.
        val d: DecimalFormat = formatter.asInstanceOf[DecimalFormat]
        val symbols = d.getDecimalFormatSymbols()
        symbols.setCurrencySymbol("¤")
        d.setDecimalFormatSymbols(symbols)

        formatter.format(plainNumber)
      }
      case (plainNumber: java.lang.Number, cur: java.util.Currency) => {
        formatter.setCurrency(cur)
        formatter.format(plainNumber)
      }
    }
  }
}