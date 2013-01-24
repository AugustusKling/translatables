package extract

import java.io.File
import translatables.Language
import java.io.FileReader
import java.io.BufferedReader
import translatables.Translation
import translatables.Format
import scala.reflect.generic.Trees

/**
 * Parses Scala source code looking for translations.
 */
final class ScalaExtractor(override val file: File) extends FileExtractor(file, file => {
  if (file.getName() == ".git" || (file.isFile() && !file.getName().endsWith(".scala"))) None else Some(file)
}) {

  def extractSingleFile(file: File) = {
    // Set up Scala compiler for parsing
    import scala.tools.nsc.interpreter.IMain
    import scala.tools.nsc.Settings
    val settings = new Settings()
    settings.embeddedDefaults[ScalaExtractor]
    val interpreter = new IMain(settings)
    val fr = new FileReader(file);
    val br = new BufferedReader(fr)
    val content = Stream.continually(br.readLine()).takeWhile(_!=null).mkString("\n")
    br.close()
    fr.close()
    // Instruct compiler to parse code to AST
    val ast = interpreter.parse(content)

    import interpreter.global._
    /**
     * Process AST to collect translation keys
     */
    def walkList(list: List[Tree], translationKeyAccu: Set[String]): Set[String] = {
      def walk(tree: Tree, translationKeyAccu: Set[String]): Set[String] = {
        tree match {
          case interpreter.global.ClassDef(mods, name, tparams, impl) => {
            val coll = walkList(tparams, translationKeyAccu)
            walk(impl, coll)
          }
          case interpreter.global.Template(parents: List[Tree], self: ValDef, body: List[Tree]) => {
            walkList(body, translationKeyAccu)
          }
          case interpreter.global.DefDef(mods: Modifiers, name: TermName, tparams: List[TypeDef],
            vparamss: List[List[ValDef]], tpt: Tree, rhs: Tree) => {
            translationKeyAccu
          }
          case interpreter.global.Select(qualifier: Tree, sym) => {
            translationKeyAccu
          }
          // Constructor invocation
          case interpreter.global.New(tpt: Tree) => {
            walk(tree, translationKeyAccu)
          }
          case interpreter.global.Literal(value) => value match {
            case interpreter.global.Constant(value: Any) => {
              translationKeyAccu
            }
          }
          case interpreter.global.Ident(_) => {
            translationKeyAccu
          }
          // Invocation of: new Translation("something", …)
          case a @ interpreter.global.Apply(interpreter.global.Select(interpreter.global.New(tpt: Tree), sym), List(interpreter.global.Literal(interpreter.global.Constant(value: String)), _)) if tpt.toString == "Translation" => {
            translationKeyAccu + value
          }
          case interpreter.global.Apply(interpreter.global.Ident(name), List(interpreter.global.Literal(interpreter.global.Constant(value: String)))) if name.toString=="t" => {
            translationKeyAccu + value
          }
          case interpreter.global.Apply(fun: Tree, args: List[Tree]) => {
            walkList(args, translationKeyAccu)
            walk(fun, translationKeyAccu)
          }
          // TODO Replace by proper type-specific handling
          case a => {
            translationKeyAccu
          }
        }
      }
      (list flatMap (walk(_, translationKeyAccu))) toSet
    }

    walkList(ast.get, Set());

  }
}