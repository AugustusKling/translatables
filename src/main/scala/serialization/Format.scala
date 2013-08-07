package serialization

import java.io.File
import translatables.StoredTranslation
import translatables.Language

/**
 * Reading and writing of translation data to files.
 */
trait Format {
  /**
   * @param file File that contains translations.
   * @return Translation key to translation mapping.
   */
  def read(file: File): Map[String, StoredTranslation[_]] = ???

  /**
   * @param target File to create or overwrite.
   * @param merged Translations to be stored (translation key to translation mapping).
   * @param existingTranslations Data from a call to read (possibly of another format).
   */
  def write(target: File, merged: Map[String, String], existingTranslations: Map[String, StoredTranslation[_]],
    language: Language): Unit = ???
}