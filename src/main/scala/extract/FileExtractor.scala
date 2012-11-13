package extract

import translatables.Language
import java.io.File
import translatables.Translation
import translatables.Format

/**
 * Searches source code file for translations
 * @param file Source code file to examine
 */
abstract class FileExtractor(val file: File) {
  lazy val sourceKey = extractSourceKeys
  
  /**
   * @param language Target language
   * @return Translation keys required for localizing to given language
   */
  def extract(language: Language): Set[String] = {
    val sourceKeys = extractSourceKeys
    sourceKeys.flatMap(value => {
      val trans = new Translation(value, language)
      Format.buildAllTranslationKeys(trans)
    })
  }

  /**
   * Parses source code file for included translations
   * @return All found source keys (language agnostic)
   */
  protected[this] def extractSourceKeys(): Set[String]
}