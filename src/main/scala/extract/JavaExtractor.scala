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

/**
 * Extracts translations for Java source code.
 */
class JavaExtractor(override val file: File) extends FileExtractor(file) {
  def extractSourceKeys(): Set[String] = {
    val source = "class Test { public Test(){ new Translation(\"{number(varName)} blub!\", de); Translation(\"x\", 4);} private void Translation(String a, String b){}}"
    val cu = JavaParser.parse(new StringBufferInputStream(source))
    val results = scala.collection.mutable.Set[String]()
    new VA(results).visit(cu, null)
    results.toSet
  }
}

/**
 * Visitor to look for constructor calls of translation function.
 */
class VA(val results:scala.collection.mutable.Set[String]) extends VoidVisitorAdapter[AnyRef] {
  override def visit(n: ObjectCreationExpr, arg: AnyRef) = {
    if (n.getType().getName() == "Translation") {
      val args = n.getArgs()
      if(args!=null){
        val argsList = for(a <- args) yield a
        argsList match {
          case Seq(a:StringLiteralExpr, b) => results += a.getValue()
          case _ => Unit
        }
      }
    }
  }
}