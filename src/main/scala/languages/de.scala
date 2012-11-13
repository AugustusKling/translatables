package languages

import translatables.Language
import translatables.Domain
import translatables.Category

/**
 * German language
 */
object de extends Language("de", List(
  // Two classes of numbers
  new Domain("number", List(
    // Zero or many
    new Category("zero", _ != 1),
    // Exactly one
    new Category("one", _ == 1)))), Some(Root)) {

}