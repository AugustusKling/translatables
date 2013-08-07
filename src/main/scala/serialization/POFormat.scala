package serialization

import console.Main.autoClose
import java.io.FileWriter
import java.io.File
import translatables.StoredTranslation
import serialization.POParser.POTranslation
import translatables.adapter.POAdapter
import translatables.Language

object POFormat extends Format {
  override def read(source: File) = new POAdapter(source).toStoredMap

  override def write(target: File, merged: Map[String, String], existingTranslations: Map[String, StoredTranslation[_]], language: Language) = autoClose(new FileWriter(target)) { os =>
    os.append(("""msgid ""
              |msgstr ""
              |"Content-Type: text/plain; charset=UTF-8\n"
              |"Language: """ + language.code + """\n"
              |"X-Generator: translatables"
              """).stripMargin)
    for (entry <- merged) {
      for (existing <- existingTranslations.get(entry._1)) {
        // If the stored translation came from a PO file, retain its flags and comments.
        if (existing.extra.isInstanceOf[POTranslation]) {
          val existingPO: POTranslation = existing.extra.asInstanceOf[POTranslation]
          for (comments <- existingPO.comments if !comments.isEmpty) {
            os.append("\n" + comments.mkString("\n"))
          }
          for (msgctxt <- existingPO.msgctxt) {
            os.append("\n" + msgctxt)
          }
        }
      }
      os.append("\nmsgid \"" + entry._1.replace("\"", "\\\"") + "\"\nmsgstr \"" + entry._2.replace("\"", "\\\"") + "\"\n")
    }
  }
}