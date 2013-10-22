package languages

import translatables.Language
import java.util.Locale
import domains.numbers.Rule1
import domains.Date
import domains.Time
import domains.Currency
import translatables.Category

object it extends Language(Locale.ITALIAN, List(
  new Rule1(Locale.ITALIAN),
  new Date(Locale.ITALIAN), new Time(Locale.ITALIAN),
  new Currency(Locale.ITALIAN)),
  Some(Root))