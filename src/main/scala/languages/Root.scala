package languages

import translatables.Language
import translatables.Domain
import translatables.Category

/**
 * Base language that provides a minimal set of domains so that users can trust them to be there.
 */
object Root extends Language("root", List(
  new Domain("plain", List(new Category("plain", _ => true))),
  new Domain("number", List(new Category("one", _ => true))),
  new Domain("gender", List(new Category("neuter", _ => true)))), None) {
}