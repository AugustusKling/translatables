package languages

import translatables.Language
import java.util.Locale
import domains.numbers.Rule1
import domains.Date
import domains.Time
import domains.Currency
import translatables.Category

object en_CA extends Language(Locale.CANADA, List(
  new Rule1(Locale.CANADA),
  new Date(Locale.CANADA), new Time(Locale.CANADA),
  new Currency(Locale.CANADA, List(new Category("digits", _ => true)))),
  Some(Root))