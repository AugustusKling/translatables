package extract

import java.io.File
import translatables.Language
import java.io.FileReader
import java.io.BufferedReader
import translatables.Translation
import translatables.Format
import scala.reflect.internal.Trees
import scala.tools.nsc.Global._
import scala.reflect.api.Universe
import scala.reflect.io.AbstractFile
import scala.reflect.internal.util.BatchSourceFile

/**
 * Parses Scala source code looking for translations.
 */
final class ScalaExtractor(override val file: File, override val hint:ExtractionHint) extends FileExtractor(file, file => {
  if (file.getName() == ".git" || (file.isFile() && !file.getName().endsWith(".scala"))) None else Some(file)
}, hint) {

  def extractSingleFile(file: File) = {

    // Instruct compiler to parse code to AST
    import scala.tools.nsc._
    object Compiler extends Global(new Settings()) {
      new Run

      def parse(path: File) = {
        val code = AbstractFile.getFile(path)
        val bfs = new BatchSourceFile(code, code.toCharArray)
        val parser = new syntaxAnalyzer.UnitParser(new CompilationUnit(bfs))
        parser.smartParse()
      }
    }
    val ast = Compiler.parse(file)

    import Compiler.syntaxAnalyzer.global._
    
    /**
     * Process AST to collect translation keys
     */
    def walkList(list: List[Tree], translationKeyAccu: Set[String]): Set[String] = {
      def walk(tree: Tree, translationKeyAccu: Set[String]): Set[String] = {
        tree match {
          case Import(expr: Tree, selectors: List[ImportSelector]) => translationKeyAccu
          case ModuleDef(mods: Modifiers, name: TermName, impl: Template) => {
            walk(impl, translationKeyAccu)
          }
          case PackageDef(pid: RefTree, stats: List[Tree]) => {
            walkList(stats, translationKeyAccu)
          }
          // @TranslationKey annotation.
          case vd @ ValDef(mods: Modifiers, name: TermName, tpt: Tree, rhs @ Literal(Constant(value:String))) => {
            val translationKeyAnnotation = vd.mods.annotations.find(_ match {
              case Apply(Select(New(Ident(name: Name)) , _), _) => name.toTypeName.toString=="TranslationKey"
              case _ => false
            })
            walkList(List(tpt, rhs), translationKeyAnnotation match {
              case Some(_) => translationKeyAccu + value 
              case _ => translationKeyAccu
            })
          }
          case AppliedTypeTree(tpt: Tree, args: List[Tree]) => {
            walkList(args :+ tpt, translationKeyAccu)
          }
          case EmptyTree => translationKeyAccu
          case Block(stats: List[Tree], expr: Tree) => {
            walkList(stats :+ expr, translationKeyAccu)
          }
          case Match(selector: Tree, cases: List[CaseDef]) =>
            walk(selector, translationKeyAccu)
          case Try(block: Tree, catches: List[CaseDef], finalizer: Tree) => {
            walkList(catches :+ block :+ finalizer, translationKeyAccu)
          }
          case Function(vparams: List[ValDef], body: Tree) => {
            walkList(vparams :+ body, translationKeyAccu)
          }
          case TypeDef(mods: Modifiers, name: TypeName, tparams: List[TypeDef], rhs: Tree) => {
            walkList(tparams :+ rhs, translationKeyAccu)
          }
          case AssignOrNamedArg(lhs: Tree, rhs: Tree) => walkList(List(lhs, rhs), translationKeyAccu)
          case TypeApply(fun: Tree, args: List[Tree]) => walkList(args :+ fun, translationKeyAccu)
          case TypeBoundsTree(lo: Tree, hi: Tree) => walkList(List(lo, hi), translationKeyAccu)
          case Annotated(annot: Tree, arg: Tree) => {
            walkList(List(annot, arg), translationKeyAccu)
          }
          case TypeTree() => {
            translationKeyAccu
          }
          case ClassDef(mods: Modifiers, name: TypeName, tparams: List[TypeDef], impl: Template) => {
            val coll = walkList(tparams, translationKeyAccu)
            walk(impl, coll)
          }
          case Template(parents: List[Tree], self: ValDef, body: List[Tree]) => {
            walkList(body, translationKeyAccu)
          }
          case DefDef(mods: Modifiers, name: TermName, tparams: List[TypeDef],
            vparamss: List[List[ValDef]], tpt: Tree, rhs: Tree) => {
            translationKeyAccu
          }
          case Select(qualifier: Tree, sym) => {
              walkList(qualifier.children, translationKeyAccu)
          }
          case If(cond: Tree, thenp: Tree, elsep: Tree) => {
            walkList(List(cond, thenp, elsep), translationKeyAccu)
          }
          // Constructor invocation
          case New(tpt: Tree) => {
            walk(tree, translationKeyAccu)
          }
          case Literal(value) => value match {
            case Constant(_) => {
              translationKeyAccu
            }
          }
          case Ident(_) => {
            translationKeyAccu
          }
          // Invocation of: new Translation("something", …)
          case a @ Apply(Select(New(tpt: Tree), sym), List(Literal(Constant(value: String)), _*)) if hint.calls.contains(tpt.toString) => {
            translationKeyAccu + value
          }
          // Invocation of: t("something", …)
          case i @ Apply(Ident(name), List(Literal(Constant(value: String)), _*)) if hint.calls.contains(name.toString) => {
            translationKeyAccu + value
          }
          case Apply(fun: Tree, args: List[Tree]) => {
            val coll = walkList(args, translationKeyAccu)
            walk(fun, coll)
          }
          case CaseDef(pat: Tree, guard: Tree, body: Tree) => {
            walkList(List(pat, guard, body), translationKeyAccu)
          }
          case Bind(name: Name, body: Tree) => {
            walk(body, translationKeyAccu)
          }
          case Throw(expr: Tree) => {
            walk(expr, translationKeyAccu)
          }
          case Typed(expr: Tree, tpt: Tree)=>{
            walkList(List(expr, tpt), translationKeyAccu)
          }
          // TODO Replace by proper type-specific handling
          case a => {
            println("Missing handling:" + a.getClass())
            translationKeyAccu
          }
        }
      }
      (list flatMap (walk(_, translationKeyAccu))).toSet
    }

    walkList(List(ast), Set());

  }
}