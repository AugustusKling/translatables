package languages

import translatables.Language
import java.util.Locale
import domains.numbers.Rule0
import domains.Date
import domains.Time
import domains.Currency
import translatables.Category

object ko_KR extends Language(Locale.KOREA, List(
  new Rule0(Locale.KOREA),
  new Date(Locale.KOREA), new Time(Locale.KOREA),
  new Currency(Locale.KOREA)),
  Some(Root))