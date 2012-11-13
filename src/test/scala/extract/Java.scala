package extract

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.io.File
import languages.de
import languages.Root

@RunWith(classOf[JUnitRunner])
class Java extends FunSuite {

  test("Java source"){
    val je = new JavaExtractor(new File("/tmp"))
    assert(je.extract(Root)===Set("{one(number(varName))} blub!"))
    assert(je.extract(de)===Set("{one(number(varName))} blub!", "{zero(number(varName))} blub!"))
  }
}