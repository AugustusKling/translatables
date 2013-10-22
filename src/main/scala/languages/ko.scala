package languages

import translatables.Language
import java.util.Locale
import domains.numbers.Rule0
import domains.Date
import domains.Time
import domains.Currency
import translatables.Category

object ko extends Language(Locale.KOREAN, List(
  new Rule0(Locale.KOREAN),
  new Date(Locale.KOREAN), new Time(Locale.KOREAN),
  new Currency(Locale.KOREAN)),
  Some(Root))