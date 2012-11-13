package translatables

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import languages.Root
import languages.de

@RunWith(classOf[JUnitRunner])
class German extends FunSuite {
  test("German numbers"){
    de.updateTranslations(Map(
		"There are {zero(number(a))} birds on {zero(number(b))} trees." -> "Es sind {a} Vögel auf {b} Bäumen.",
        "There are {zero(number(a))} birds on {one(number(b))} trees." -> "Es sind {a} Vögel auf {b} Baum.",
        "There are {one(number(a))} birds on {zero(number(b))} trees." -> "Es ist {a} Vogel auf {b} Bäumen.",
        "There are {one(number(a))} birds on {one(number(b))} trees." -> "Es ist {a} Vogel auf {b} Baum."
    ))
    
    val t= new Translation("There are {number(a)} birds on {number(b)} trees.", de);
    assert(t("a"->0, "b"->0)==="Es sind 0 Vögel auf 0 Bäumen.")
    assert(t("a"->0, "b"->1)==="Es sind 0 Vögel auf 1 Baum.")
    assert(t("a"->1, "b"->0)==="Es ist 1 Vogel auf 0 Bäumen.")
    assert(t("a"->1, "b"->1)==="Es ist 1 Vogel auf 1 Baum.")
  }
  
  test("Placeholders omissable"){
    de updateTranslations(Map({
      "A {female(gender(test))} child" -> "Ein Mädchen"
    }))
  }
}