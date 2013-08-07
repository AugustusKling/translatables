package serialization

import java.io.File
import java.util.Properties
import java.io.FileInputStream
import scala.collection.JavaConverters
import translatables.StoredTranslation
import translatables.Language
import console.Main.autoClose
import java.io.FileOutputStream

/**
 * Handles Java properties.
 */
object PropertiesFormat extends Format {
  override def read(source: File) = {
    val p = new Properties
    p.load(new FileInputStream(source))
    Map() ++ JavaConverters.propertiesAsScalaMapConverter(p).asScala.map { entry =>
      (entry._1, StoredTranslation(entry._1, entry._2, None))
    }
  }
  override def write(target: File, merged: Map[String, String], existingTranslations: Map[String, StoredTranslation[_]],
    language: Language) = {

    val p = new Properties()
    for (entry <- merged) {
      p.setProperty(entry._1, entry._2)
    }
    autoClose(new FileOutputStream(target)) {
      p.store(_, "translatables")
    }
  }
}