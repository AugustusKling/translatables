package serialization

import scala.util.parsing.combinator.JavaTokenParsers
import translatables.DoTranslate.doTranslate

// The following is an adapter version of Ngoc Dao's parser from scaposer.
// Further information can be found on https://github.com/ngocdaothanh/scaposer/
//
// Changes have been made to handle escaping better and restrictions have been imposed to target only the subset of PO
// files that makes sense to be used in conjunction with translatables. Most importantly, gettext's pluralization is
// not supported by the adapted version.
//
// Original code was released as: 
// Copyright (c) 2011 Ngoc Dao
//
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
/**
 * Parses a subset of gettext PO files. The subset explicitly excludes plural forms. One should generate the PO files
 * with translatables and only provide translations in the singular with a translation tool of choice.
 */
object POParser extends JavaTokenParsers {
  /**
   * Translation with meta data as stored in PO file.
   * @param comments Comments and flags of the translation.
   * @param msgctxt Comment.
   * @param msgid Translation key.
   * @param msgstr Fragments of stored translation.
   */
  case class POTranslation(comments: Option[List[String]], msgctxt: Option[String], msgid: String, msgstr: Array[String])

  private def mergeStrs(quoteds: List[String]): String = {
    // Removes the first and last quote (") character of strings
    // and concats them
    val unquoted = quoteds.foldLeft("") { (acc, quoted) =>
      acc + quoted.substring(1, quoted.length - 1)
    }

    // Unescape
    unquoted.
      replace("\\n", "\n").
      replace("\\r", "\r").
      replace("\\t", "\t").
      replace("\\\"", "\"").
      replace("\\\\", "\\")
  }

  /**
   * Double quotes (`"`) enclosing a sequence of:
   *
   * - Any character except double quotes, control characters or backslash (`\`)
   * - A backslash followed by a slash, another backslash, or one of the letters
   * `b`, `f`, `n`, `r` or `t`.
   * - `\` followed by `u` followed by four hexadecimal digits
   */
  private val reStringLiteral: Parser[String] =
    ("\"" + """((\\\")|\p{Space}|\\u[a-fA-F0-9]{4}|[^"\p{Cntrl}\\]|\\[\\/bfnrt])*""" + "\"").r

  // Scala regex is single line by default
  private def comment = rep(regex("^#.*".r))

  private def msgctxt = "msgctxt" ~ rep(reStringLiteral) ^^ {
    case _ ~ quoteds => mergeStrs(quoteds)
  }

  private def msgid = "msgid" ~ rep(reStringLiteral) ^^ {
    case _ ~ quoteds => mergeStrs(quoteds)
  }

  private def msgidPlural = "msgid_plural" ~ rep(reStringLiteral) ^^ {
    case _ ~ quoteds => mergeStrs(quoteds)
  }

  private def msgstr = "msgstr" ~ rep(reStringLiteral) ^^ {
    case _ ~ quoteds => mergeStrs(quoteds)
  }

  private def msgstrN = "msgstr[" ~ wholeNumber ~ "]" ~ rep(reStringLiteral) ^^ {
    case _ ~ number ~ _ ~ quoteds => (number.toInt, mergeStrs(quoteds))
  }

  private def singular =
    (opt(comment) ~ opt(msgctxt) ~
      opt(comment) ~ msgid ~
      opt(comment) ~ msgstr) ^^ {
        case comments ~ ctxo ~ comments2 ~ id ~ _ ~ str =>
          new POTranslation(Option(comments.getOrElse(List()) ++ comments2.getOrElse(List[String]())), ctxo, id, Array(str))
      }

  private def plural =
    (opt(comment) ~ opt(msgctxt) ~
      opt(comment) ~ msgid ~
      opt(comment) ~ msgidPlural ~
      opt(comment) ~ rep(msgstrN)) ^^ {
        case comments ~ ctxo ~ _ ~ id ~ _ ~ _ ~ _ ~ n_strs =>
          val strs = n_strs.sorted.map { case (n, str) => str }
          new POTranslation(comments, ctxo, id, strs.toArray)
      }

  private def exp = rep(singular | plural)

  /**
   * Parses a PO file's content to translation map.
   * @param po PO content.
   * @return Map of msgid to translation.
   */
  def parsePo(po: String): Option[Map[String, POTranslation]] = {
    val parseRet = parseAll(exp, po)
    if (parseRet.successful) {
      val translations = parseRet.get
      val body = translations.foldLeft(
        Map[String, POTranslation]()) { (acc, t) =>
          if (t.msgstr.size != 1) throw new IllegalArgumentException(
            doTranslate("Need exactly 1 translation per key but got {0} for key {1}", t.msgstr.size: Integer, t.msgid))
          val item = t.msgid -> t
          acc + item
        }
      Some(body)
    } else {
      None
    }
  }
}