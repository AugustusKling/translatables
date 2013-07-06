package extract

import scala.reflect.api.Universe

/**
 * List of methods or functions that an application defines to wrap translatables.
 * @param calls Names of extraction functions.
 */
class ExtractionHint(val calls:Seq[String])