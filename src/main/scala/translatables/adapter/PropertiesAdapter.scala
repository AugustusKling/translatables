package translatables.adapter

import translatables.Adapter
import scala.io.Source
import java.io.File
import java.io.InputStream
import java.util.Properties
import java.io.BufferedInputStream
import java.io.StringReader
import java.io.ByteArrayInputStream
import java.nio.charset.Charset
import translatables.NotTranslated
import scala.io.Codec
import java.io.FileInputStream

/**
 * Makes a Java properties file useable as translation source.
 * 
 * @param inputStream Java properties in ISO-8859-1 encoding.
 */
class PropertiesAdapter(private val inputStream: InputStream) extends Adapter {
  /**
   * @param source Java properties file in ISO-8859-1 encoding.
   */
  def this(source: File) = {
    this(new FileInputStream(source))
  }
  
  /**
   * @param source Java properties.
   */
  def this(source: Source) = {
    this(new ByteArrayInputStream(source.mkString.getBytes("UTF-8")))
  }

  val translations = new Properties()
  try {
    translations.load(inputStream)
  } finally {
    inputStream.close
  }

  def get(translationKey: String): String = {
    return translations.getProperty(translationKey, "") match {
      case "" => throw new NotTranslated(translationKey)
      case translation => translation
    }
  }
}