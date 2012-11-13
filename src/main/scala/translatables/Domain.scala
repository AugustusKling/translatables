package translatables

/**
 * Class of values such as number, plain text or gender.
 * @param name Identifier in translation keys. Also used to specify rules to use when generating translation keys.
 */
case class Domain(val name: String, val categories: List[Category]) {
  /**
   * Fetches the contained category object that is responsible for representing the given value
   * @param value Any value that is convered by the domain
   */
  def getCategory(value: Any): Category = categories.find(_.covers(value)) getOrElse (throw new NoSuchElementException("Domain " + name + " does not cover " + value))

  /**
   * Transforms value to a locale dependent textual representation
   * @param value Value for placeholder as given to translation call
   * @return Textual representation for the value to be read by a user
   */
  def format(value:Any):String = value toString
}