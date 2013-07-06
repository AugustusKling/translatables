package extract

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.io.File
import languages.de
import languages.Root
import scala.io.Source
import java.io.FileWriter

@RunWith(classOf[JUnitRunner])
class Java extends FunSuite {

  test("Java source"){
    val src=File.createTempFile("test", ".java")
    val fw =new FileWriter(src)
    fw.write("class Test { public void main(String[] args){ t(\"{number(varName)} blub!\"); } }")
    fw.close()
    
    val je = new JavaExtractor(src, new ExtractionHint(Seq("t")))
    assert(je.extract(Root)===Set("{one(number(varName))} blub!"))
    assert(je.extract(de)===Set("{one(number(varName))} blub!", "{zero(number(varName))} blub!"))
  }
}