package serialization

import java.io.File
import scala.io.Source
import translatables.StoredTranslation
import translatables.Language
import console.Main.autoClose
import java.io.FileWriter

/**
 * Handles JSON files.
 */
object JsonFormat extends Format {
  override def read(source: File) = Json.read(Source.fromFile(source).mkString).map { entry =>
    (entry._1, StoredTranslation(entry._1, entry._2, None))
  }

  override def write(target: File, merged: Map[String, String], existingTranslations: Map[String, StoredTranslation[_]],
    language: Language) = autoClose(new FileWriter(target)) { fw =>
    // Persist extracted translations.
    fw.write(Json.write(merged))
  }
}