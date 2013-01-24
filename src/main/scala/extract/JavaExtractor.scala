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
class JavaExtractor(override val file: File) extends FileExtractor(file, file => {
  if (file.getName() == ".git" || (file.isFile() && !file.getName().endsWith(".java"))) None else Some(file)
}) {
  
  def extractSingleFile(file: File): Set[String] = {
    //val source = "class Test { public Test(){ new Translation(\"{number(varName)} blub!\", de); Translation(\"x\", 4);} private void Translation(String a, String b){}}"
    val source = new FileInputStream(file)
    val cu = JavaParser.parse(source)
    source.close()
    val results = scala.collection.mutable.Set[String]()
    new VA(results).visit(cu, null)
    results.toSet
  }
}

/**
 * Visitor to look for constructor calls of translation function.
 */
class VA(val results: scala.collection.mutable.Set[String]) extends VoidVisitorAdapter[AnyRef] {
  override def visit(n:MethodCallExpr, args:AnyRef) = {
    if(n.getName()=="define"){
      results += n.getArgs().head.toString
    }
  }
  
  override def visit(n: ObjectCreationExpr, arg: AnyRef) = {
    if (n.getType().getName() == "Translation") {
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