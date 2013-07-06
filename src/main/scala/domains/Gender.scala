package domains

/**
 * Language independent categorization. Each language must interpret all given value
 *  but can choose to use the same category for multiple categorizations.
 *
 * Unless a concept is shared across languages a general data type shall be used.
 * Note that using an application defined data type is perfectly fine as long as its concept is language agnostic.
 */
object Gender extends Enumeration {
  val Neuter = Value("neuter")
  val Male = Value("male")
  val Female = Value("female")
}