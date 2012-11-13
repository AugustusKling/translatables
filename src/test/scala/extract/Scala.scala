package extract

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import languages.Root
import languages.de
import java.io.File
import java.io.FileWriter

@RunWith(classOf[JUnitRunner])
class Scala extends FunSuite {
  // TODO Clear up temporary files
  val tmpDir = new File(System.getProperty("java.io.tmpdir"));
  new File(tmpDir + "/test").mkdir()
  new File(tmpDir + "/test/sub1").mkdir()
  val sub2 = new File(tmpDir + "/test/sub2");
  sub2.mkdir()
  val subFile = new File(sub2 + "/testfile")
  subFile.createNewFile();
  assert(subFile.exists())
  val fw = new FileWriter(subFile);
  try {
    fw.write("new Translation(\"test {number(hoi)} testend\", de)")
  } finally {
    fw.close()
  }

  test("Walk") {
    val fw = new FileWalker
    fw.walk(new File("/tmp/test"), Some(_), f => {
      val fe = new ScalaExtractor(f)
      val extracted = fe.extract(de)
      assert(extracted===Set("test {zero(number(hoi))} testend", "test {one(number(hoi))} testend"))
    })
  }
}
