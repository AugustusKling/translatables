package languages

import translatables.Language
import java.util.Locale
import domains.numbers.Rule0
import domains.Date
import domains.Time
import domains.Currency
import translatables.Category

object ja extends Language(Locale.JAPANESE, List(
  new Rule0(Locale.JAPANESE),
  new Date(Locale.JAPANESE), new Time(Locale.JAPANESE),
  new Currency(Locale.JAPANESE, List(new Category("digits", _ => true)))),
  Some(Root))