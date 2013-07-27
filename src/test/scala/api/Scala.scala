package api

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import translatables.Adapter
import translatables.Translation
import languages.de
import translatables.adapter.MapAdapter
import translatables.Language
import java.util.Locale
import domains.numbers.Rule11
import languages.de_DE

@RunWith(classOf[JUnitRunner])
class Scala extends FunSuite {
  test("Get hand-made languages") {
    val germany = Language.fromLocale(Locale.GERMANY)
    assert(germany === de_DE)
  }

  test("Get auto-constructed languages") {
    val irishLocale = new Locale("ga", "IE")
    val irish = Language.fromLocale(irishLocale)
    
    assert(irish.code === irishLocale)
    assert(irish.getDomain("number").get.isInstanceOf[Rule11])
    // Verify what we expect from all languages holds.
    assert(irish.getDomain("plain").isEmpty === false)
    assert(irish.getDomain("gender").isEmpty === false)
    assert(irish.getDomain("date").isEmpty === false)
    assert(irish.getDomain("time").isEmpty === false)
    assert(irish.getDomain("currency").isEmpty === false)
  }
}