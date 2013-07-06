package translatables

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import languages.Root
import languages.de
import domains.Gender
import translatables.adapter.MapAdapter
import java.util.Date
import java.util.Currency
import languages.de_DE

@RunWith(classOf[JUnitRunner])
class German extends FunSuite {
  test("German numbers") {
    val adapter = new MapAdapter(Map(
      "There are {zero(number(a))} birds on {zero(number(b))} trees." -> "Es sind {a} Vögel auf {b} Bäumen.",
      "There are {zero(number(a))} birds on {one(number(b))} trees." -> "Es sind {a} Vögel auf {b} Baum.",
      "There are {one(number(a))} birds on {zero(number(b))} trees." -> "Es ist {a} Vogel auf {b} Bäumen.",
      "There are {one(number(a))} birds on {one(number(b))} trees." -> "Es ist {a} Vogel auf {b} Baum."))

    val t = new Translation("There are {number(a)} birds on {number(b)} trees.", de);
    assert(t(adapter, "a" -> 0, "b" -> 0) === "Es sind 0 Vögel auf 0 Bäumen.")
    assert(t(adapter, "a" -> 0, "b" -> 1) === "Es sind 0 Vögel auf 1 Baum.")
    assert(t(adapter, "a" -> 1, "b" -> 0) === "Es ist 1 Vogel auf 0 Bäumen.")
    assert(t(adapter, "a" -> 1, "b" -> 1) === "Es ist 1 Vogel auf 1 Baum.")
  }

  test("Placeholders omissable") {
    val adapter = new MapAdapter(Map(
      "A {neuter(gender(test))} child" -> "Ein Kind",
      "A {female(gender(test))} child" -> "Ein Mädchen",
      "A {male(gender(test))} child" -> "Ein Junge"))

    val t = new Translation("A {gender(test)} child", de)
    assert(t(adapter, "test" -> Gender.Neuter) === "Ein Kind")
    assert(t(adapter, "test" -> Gender.Female) === "Ein Mädchen")
    assert(t(adapter, "test" -> Gender.Male) === "Ein Junge")
  }

  test("Formatters") {
    val adapter = new MapAdapter(Map(
      "{zero(number(num))} flies fly." -> "Es fliegen {num} Fliegen.",
      "{zero(number(x))}g" -> "{x}g",
      "Birthday on {digits(date(xyz))}." -> "Geburtstag am {xyz}.",
      "You spent {digits(currency(xyz))} on the present?" -> "Du hast {xyz} für das Geschenk ausgegeben?",
      "Wake up! It's {local(time(xyz))}." -> "Aufwachen! Es ist {xyz}."
      ))
    val t = new Translation("{number(num)} flies fly.", de)
    assert(t(adapter, "num" -> 1000) === "Es fliegen 1.000 Fliegen.")
    val t2 = new Translation("{number(x)}g", de)
    assert(t2(adapter, "x" -> 1758.42) === "1.758,42g")
    val tDate = new Translation("Birthday on {date(xyz)}.", de)
    assert(tDate(adapter, "xyz" -> new Date(5)) === "Geburtstag am 01.01.1970.")
    val tTime = new Translation("Wake up! It's {time(xyz)}.", de)
    assert(tTime(adapter, "xyz" -> new Date(51661651)) === "Aufwachen! Es ist 15:21.")
    val tCurrency = new Translation("You spent {currency(xyz)} on the present?", de_DE)
    assert(tCurrency(adapter, "xyz" -> 79.98) === "Du hast 79,98 ¤ für das Geschenk ausgegeben?")
    assert(tCurrency(adapter, "xyz" -> (79.98, Currency.getInstance("EUR"))) === "Du hast 79,98 € für das Geschenk ausgegeben?")
  }
}