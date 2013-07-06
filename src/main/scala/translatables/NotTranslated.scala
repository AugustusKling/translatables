package translatables

import scala.util.control.Exception

/**
 * A translation was requested but missing.
 * @param translationKey Translation that has been requested.
 */
class NotTranslated(val translationKey: String) extends Exception {
  override def getLocalizedMessage() = DoTranslate.doTranslate("Missing translation: {0}", translationKey);
}