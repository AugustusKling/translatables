package languages

import translatables.Language
import java.util.Locale
import domains.numbers.Rule2
import domains.Date
import domains.Time
import domains.Currency
import translatables.Category

object fr_CA extends Language(Locale.CANADA_FRENCH, List(
  new Rule2(Locale.CANADA_FRENCH),
  new Date(Locale.CANADA_FRENCH), new Time(Locale.CANADA_FRENCH),
  new Currency(Locale.CANADA_FRENCH, List(new Category("digits", _ => true)))),
  Some(Root))