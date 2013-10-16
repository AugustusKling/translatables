package extract

import java.io.File

import scala.collection.JavaConversions.asScalaBuffer
import scala.io.Source

import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.jdt.core.dom.ASTVisitor
import org.eclipse.jdt.core.dom.FieldDeclaration
import org.eclipse.jdt.core.dom.MarkerAnnotation
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.Modifier
import org.eclipse.jdt.core.dom.StringLiteral
import org.eclipse.jdt.core.dom.VariableDeclarationFragment

/**
 * Extracts translations for Java source code.
 */
class JavaExtractor(override val file: File, override val hint: ExtractionHint) extends FileExtractor(file, file => {
  if (file.getName() == ".git" || (file.isFile() && !file.getName().endsWith(".java"))) None else Some(file)
}, hint) {

  def extractSingleFile(file: File): Set[String] = {
    val bs = Source.fromFile(file)
    val results = scala.collection.mutable.Set[String]()

    val parser: ASTParser = ASTParser.newParser(AST.JLS4)
    parser.setSource(bs.mkString.toCharArray())
    parser.setKind(ASTParser.K_COMPILATION_UNIT)
    val cu = parser.createAST(null)
    cu.accept(new EclipseVisitor(results, hint))

    results.toSet
  }

}

/**
 * Visitor to look for constructor calls of translation function.
 */
class EclipseVisitor(val results: scala.collection.mutable.Set[String], val hint: ExtractionHint) extends ASTVisitor() {
  /**
   * Extracts all invocations to methods called.
   */
  override def visit(node: MethodInvocation): Boolean = {
    val name = node.getName().getFullyQualifiedName()
    if (hint.calls.contains(name)) {
      val arguments: java.util.List[_] = node.arguments
      if (arguments.size > 0) {
        arguments.head match {
          case a: StringLiteral => results += a.getLiteralValue()
          case _ => Unit
        }
      }
    }
    true
  }

  /**
   * Add every value to extracted source keys for final String fields when they are annotated with TranslationKey.
   */
  override def visit(n: FieldDeclaration): Boolean = {
    val isFinal = n.getModifiers() & Modifier.FINAL
    val mod = n.modifiers()
    val ma = n.modifiers().exists((a: Any) => a match {
      case a: MarkerAnnotation if a.getTypeName().getFullyQualifiedName() == "TranslationKey" => true
      case _ => false
    })
    val initialValue = n.fragments().head.asInstanceOf[VariableDeclarationFragment]
    initialValue.getInitializer() match {
      case a: StringLiteral if ma => results += a.getLiteralValue()
      case _ => Unit
    }
    true
  }
}