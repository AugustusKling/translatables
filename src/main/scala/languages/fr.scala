package languages

import translatables.Language
import java.util.Locale
import domains.numbers.Rule2
import domains.Date
import domains.Time
import domains.Currency
import translatables.Category

object fr extends Language(Locale.FRENCH, List(
  new Rule2(Locale.FRENCH),
  new Date(Locale.FRENCH), new Time(Locale.FRENCH),
  new Currency(Locale.FRENCH, List(new Category("digits", _ => true)))),
  Some(Root))