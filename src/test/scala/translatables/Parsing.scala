package translatables

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import languages.Root
import languages.de

@RunWith(classOf[JUnitRunner])
class Parsing extends FunSuite {
  val plainDomain = Root.getDomain("plain").get
  val numberDomain = Root.getDomain("number").get
  val genderDomain = Root.getDomain("gender").get
  val dummy = new Language("dummy", List(plainDomain, numberDomain, genderDomain), None)

  test("Find placeholders") {
    assert(new Translation("Hello {name} {surname}", dummy).placeholders.toSet === Set(
      "name" -> new TypedPlaceholder("name", plainDomain),
      "surname" -> new TypedPlaceholder("surname", plainDomain)))
    assert(
      new Translation("Hello {number(name)} {gender(surname)}", dummy).placeholders.toSet
        ===
        Set(
          "name" -> new TypedPlaceholder("name", numberDomain),
          "surname" -> new TypedPlaceholder("surname", genderDomain)))
  }

  test("Ambiguos placeholder types") {
    intercept[IllegalArgumentException] {
      new Translation("{x}{y}{y}{number(x)}{z}{plain(z)}{plain(y)}", dummy)
    }
  }

  test("translate") {
    Root.updateTranslations(Map("Hello {plain(plain(name))}" -> "Hallo {name}"))
    val translated = new Translation("Hello {name}", Root)("name" -> "Fairy")
    assert(translated === "Hallo Fairy")
  }

  test("parse translation") {
    val translation = Format.parseTranslation("abc {test}{f} def {test2} ghi")
    assert(translation === List(ConstantPlaceholder("abc "), Replaceable("test"), Replaceable("f"), ConstantPlaceholder(" def "), Replaceable("test2"), ConstantPlaceholder(" ghi")))
  }

  test("build translation key from values") {
    val translationKey = Format.buildTranslationKey(new Translation("{number(x)} trees", de), Map("x" -> 0))
    assert(translationKey === "{zero(number(x))} trees")
  }

  test("translate number") {
    de.updateTranslations(Map(
      "{zero(number(x))} trees" -> "{x} Bäume",
      "{one(number(x))} trees" -> "{x} Baum"))
    val sample = new Translation("{number(x)} trees", de)
    assert(sample("x" -> 0) === "0 Bäume")
    assert(sample("x" -> 1) === "1 Baum")
    assert(sample("x" -> 13) === "13 Bäume")
  }

  test("extract translation keys") {
    val simple = new Translation("plain text", Root)
    assert(Format.buildAllTranslationKeys(simple) === Set("plain text"))

    val sample = new Translation("{number(x)} trees", de)
    val keys = Format.buildAllTranslationKeys(sample)
    assert(keys === Set("{zero(number(x))} trees", "{one(number(x))} trees"))

    val twoNum = new Translation("a {number(x)} b {number(y)} c", de)
    assert(Format.buildAllTranslationKeys(twoNum) === Set(
      "a {zero(number(x))} b {zero(number(y))} c",
      "a {zero(number(x))} b {one(number(y))} c",
      "a {one(number(x))} b {zero(number(y))} c",
      "a {one(number(x))} b {one(number(y))} c"))
  }

  test("build fallback") {
    assert(Format.buildFallback("Simple") === List(ConstantPlaceholder("Simple")))
    assert(Format.buildFallback("Some {test}") === List(ConstantPlaceholder("Some "), Replaceable("test")))
    assert(Format.buildFallback("Some {number(test)}") === List(ConstantPlaceholder("Some "), Replaceable("test")))
  }

  test("translate using fallback") {
    assert(new Translation("fifnzfn832{number(x)}jsd skjfsd {y}jsd", Root)("x" -> 5, "y" -> "--") === "fifnzfn8325jsd skjfsd --jsd")
  }

}