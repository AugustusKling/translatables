package translatables.adapter

import translatables.Adapter
import java.io.File
import scala.util.parsing.combinator.JavaTokenParsers
import scala.io.Source
import translatables.NotTranslated
import translatables.DoTranslate.doTranslate
import translatables.StoredTranslation
import serialization.POParser

/**
 * Handles gettext PO files.
 */
class POAdapter(input: File) extends Adapter {
  val source = Source.fromFile(input)
  val poContent = source.mkString
  source.close
  val translations = POParser.parsePo(poContent).getOrElse(throw new RuntimeException("Failed to parse PO file"))

  def get(translationKey: String): String = {
    translations.getOrElse(translationKey, throw new NotTranslated(translationKey)).msgstr.head
  }

  def toMap: Map[String, String] = translations.map(entry => (entry._1, entry._2.msgstr.head))

  def toStoredMap: Map[String, StoredTranslation[POParser.POTranslation]] = translations.map(entry => (
    entry._1, StoredTranslation(entry._1, entry._2.msgstr.head, entry._2)))
}