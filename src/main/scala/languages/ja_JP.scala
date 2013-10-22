package languages

import translatables.Language
import java.util.Locale
import domains.numbers.Rule0
import domains.Date
import domains.Time
import domains.Currency
import translatables.Category

object ja_JP extends Language(Locale.JAPAN, List(
  new Rule0(Locale.JAPAN),
  new Date(Locale.JAPAN), new Time(Locale.JAPAN),
  new Currency(Locale.JAPAN)),
  Some(Root))