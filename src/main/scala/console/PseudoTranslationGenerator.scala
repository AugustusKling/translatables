package console

import scala.util.matching.Regex

import translatables.ConstantPlaceholder
import translatables.Format
import translatables.Placeholder
import translatables.Replaceable

/**
 * Makes up translations to make texts more obvious where the translation function call was forgotten.
 */
trait PseudoTranslationGenerator {
  /**
   * Build a translation that uses diacritic characters only to show encoding issues and make translated text obvious.
   * Also wraps input to make it slightly longer.
   * @param translationKey Translation key.
   * @return Made up translation. Basically the translation key with added diacritics.
   */
  def generatePseudoTranslation(translationKey: String): String = {
    val parts = Format.parseTranslation(translationKey)
    val madeUp = parts map ((p: Placeholder) => p match {
      // Add diacritics to characters of constant text.
      case ConstantPlaceholder(text) => text map ((char) => char match {
        case 'a' => 'á'
        case 'b' => 'ḇ'
        case 'c' => 'ḉ'
        case 'd' => 'ḓ'
        case 'e' => 'ḕ'
        case 'f' => 'ḟ'
        case 'g' => 'ḡ'
        case 'h' => 'ḣ'
        case 'i' => 'ı'
        case 'j' => 'ĵ'
        case 'k' => 'ĸ'
        case 'l' => 'ł'
        case 'm' => 'ḿ'
        case 'n' => 'ñ'
        case 'o' => 'ǫ'
        case 'p' => 'ṗ'
        case 'q' => 'ｑ'
        case 'r' => 'ȑ'
        case 's' => 'ș'
        case 't' => 'ŧ'
        case 'u' => 'ū'
        case 'v' => 'ṿ'
        case 'w' => 'ẘ'
        case 'x' => 'ẍ'
        case 'y' => 'ẏ'
        case 'z' => 'ž'
        case 'A' => 'Á'
        case 'B' => 'Ḇ'
        case 'C' => 'Ḉ'
        case 'D' => 'Ḓ'
        case 'E' => 'Ḕ'
        case 'F' => 'Ḟ'
        case 'G' => 'Ḡ'
        case 'H' => 'Ḣ'
        case 'I' => 'Ĩ'
        case 'J' => 'Ĵ'
        case 'K' => 'Ḱ'
        case 'L' => 'Ł'
        case 'M' => 'Ḿ'
        case 'N' => 'Ñ'
        case 'O' => 'Ǫ'
        case 'P' => 'Ṗ'
        case 'Q' => 'ℚ'
        case 'R' => 'Ȑ'
        case 'S' => 'Ș'
        case 'T' => 'Ŧ'
        case 'U' => 'Ū'
        case 'V' => 'Ṽ'
        case 'W' => 'Ŵ'
        case 'X' => 'Ẍ'
        case 'Y' => 'Ẏ'
        case 'Z' => 'Ž'

        case character => character
      })
      // Leave character of placeholder names untouched but remove category and domain.
      case Replaceable(raw) => {
        val typedPlaceholder = new Regex("^([^(]+)\\(([^(]+)\\(([^)]+)\\)\\)$", "category", "domain", "name")
        val name = raw match {
          case typedPlaceholder(category, domain, name) => name
        }
        "{" + name + "}"
      }
    })
    // Enclose in markers to text expands a wee bit to simulate languages with requirement for more space. The markers
    // should make it easy to spot missing translations during tests, too.
    "[~·" + madeUp.mkString + "·~]"
  }
}