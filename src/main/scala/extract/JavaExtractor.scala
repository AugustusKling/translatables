package extract

import java.io.File
import translatables.Language
import japa.parser.JavaParser
import java.io.StringBufferInputStream
import japa.parser.ast.visitor.VoidVisitorAdapter
import japa.parser.ast.stmt.ExplicitConstructorInvocationStmt
import japa.parser.ast.expr.ObjectCreationExpr
import japa.parser.ast.expr.StringLiteralExpr
import scala.collection.JavaConversions._
import translatables.Translation
import translatables.Format
import languages.de
import java.io.FileReader
import java.io.BufferedReader
import java.io.FileInputStream
import japa.parser.ast.expr.MethodCallExpr

/**
 * Extracts translations for Java source code.
 */
class JavaExtractor(override val file: File, override val hint: ExtractionHint) extends FileExtractor(file, file => {
  if (file.getName() == ".git" || (file.isFile() && !file.getName().endsWith(".java"))) None else Some(file)
}, hint) {

  def extractSingleFile(file: File): Set[String] = {
    val source = new FileInputStream(file)
    val cu = JavaParser.parse(source)
    source.close()
    val results = scala.collection.mutable.Set[String]()
    new VA(results, hint).visit(cu, null)
    results.toSet
  }
}

/**
 * Visitor to look for constructor calls of translation function.
 */
class VA(val results: scala.collection.mutable.Set[String], val hint: ExtractionHint) extends VoidVisitorAdapter[AnyRef] {
  /**
   * Extracts all invocations to methods called {@code define}.
   */
  override def visit(n: MethodCallExpr, args: AnyRef) = {
    if (hint.calls.contains(n.getName)) {
      val firstArg = n.getArgs().head
      firstArg match {
        case a: StringLiteralExpr =>
          results += a.getValue()
        case _ => Unit
      }
    }
  }

  /**
   * Extracts direct instantiations of the {@link Translation} object.
   */
  override def visit(n: ObjectCreationExpr, arg: AnyRef) = {
    if (hint.calls.contains(n.getType().getName)) {
      val args = n.getArgs()
      if (args != null) {
        val argsList = for (a <- args) yield a
        argsList match {
          case Seq(a: StringLiteralExpr, b) => results += a.getValue()
          case _ => Unit
        }
      }
    }
  }
}