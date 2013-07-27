package languages

import translatables.Language
import java.util.Locale
import domains.numbers.Rule1
import domains.Date
import domains.Time
import domains.Currency
import translatables.Category

object it_IT extends Language(Locale.ITALY, List(
  new Rule1(Locale.ITALY),
  new Date(Locale.ITALY), new Time(Locale.ITALY),
  new Currency(Locale.ITALY, List(new Category("digits", _ => true)))),
  Some(Root))