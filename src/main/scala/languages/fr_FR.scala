package languages

import translatables.Language
import java.util.Locale
import domains.numbers.Rule2
import domains.Date
import domains.Time
import domains.Currency
import translatables.Category

object fr_FR extends Language(Locale.FRANCE, List(
  new Rule2(Locale.FRANCE),
  new Date(Locale.FRANCE), new Time(Locale.FRANCE),
  new Currency(Locale.FRANCE, List(new Category("digits", _ => true)))),
  Some(Root))