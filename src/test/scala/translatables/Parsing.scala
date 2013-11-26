package translatables

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import languages.Root
import languages.de
import translatables.adapter.MapAdapter
import java.util.Locale
import languages.en
import java.util.Calendar

@RunWith(classOf[JUnitRunner])
class Parsing extends FunSuite {
  val plainDomain = Root.getDomain("plain").get
  val numberDomain = Root.getDomain("number").get
  val genderDomain = Root.getDomain("gender").get
  val dummy = new Language(new Locale("dummy"), List(plainDomain, numberDomain, genderDomain), None)

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
    val adapter = new MapAdapter(Map("Hello {plain(name)}" -> "Hallo {name}"))
    val translated = new Translation("Hello {name}", Root)(adapter, "name" -> "Fairy")
    assert(translated === "Hallo Fairy")
  }

  test("parse translation") {
    val translation = Format.parseTranslation("abc {test}{f} def {test2} ghi")
    assert(translation === List(ConstantPlaceholder("abc "), Replaceable("test"), Replaceable("f"), ConstantPlaceholder(" def "), Replaceable("test2"), ConstantPlaceholder(" ghi")))
  }

  test("parse translation with escaped {") {
    val single = Format.parseTranslation("{{")
    assert(single === List(ConstantPlaceholder("{")))

    val withText = Format.parseTranslation("some {{ test {{ other")
    assert(withText === List(ConstantPlaceholder("some { test { other")))

    val withReplaceables = Format.parseTranslation("some {{ {x} and {test} plus {{{y}} or {{{test2} end")
    assert(withReplaceables === List(ConstantPlaceholder("some { "), Replaceable("x"), ConstantPlaceholder(" and "), Replaceable("test"), ConstantPlaceholder(" plus {"), Replaceable("y"), ConstantPlaceholder("} or {"), Replaceable("test2"), ConstantPlaceholder(" end")))

    val adapter = new MapAdapter(Map("Hello {plain(name)}" -> "Hallo {{{name} {{neu"))
    val translated = new Translation("Hello {name}", Root)(adapter, "name" -> "Fairy")
    assert(translated === "Hallo {Fairy {neu")
  }

  test("parse sourceKey with escaped {") {
    val quotedConstant = new Translation("{{", Root)
    val single = Format.parseSourceKey(quotedConstant)
    assert(single === List(ConstantPlaceholder("{{")))
    assert(Format.buildAllTranslationKeys(quotedConstant) === Set("{{"))

    val withText = Format.parseSourceKey(new Translation("some {{ test {{ other", Root))
    assert(withText === List(ConstantPlaceholder("some {{ test {{ other")))

    val withReplaceables = Format.parseSourceKey(new Translation("some {{ {x} and {test} plus {{{y}} or {{{test2} end", Root))
    assert(withReplaceables === List(ConstantPlaceholder("some {{ "),
      TypedPlaceholder("x", Root.getDomain("plain").getOrElse(throw new NoSuchElementException)),
      ConstantPlaceholder(" and "),
      TypedPlaceholder("test", Root.getDomain("plain").getOrElse(throw new NoSuchElementException)),
      ConstantPlaceholder(" plus {{"),
      TypedPlaceholder("y", Root.getDomain("plain").getOrElse(throw new NoSuchElementException)),
      ConstantPlaceholder("} or {{"),
      TypedPlaceholder("test2", Root.getDomain("plain").getOrElse(throw new NoSuchElementException)),
      ConstantPlaceholder(" end")))

    val adapter = new MapAdapter(Map("Hello {{ {plain(name)}" -> "Hallo {{{name} {{neu"))
    val translated = new Translation("Hello {{ {name}", Root)(adapter, "name" -> "Fairy")
    assert(translated === "Hallo {Fairy {neu")
  }

  test("build translation key from values") {
    val translationKey = Format.buildTranslationKey(new Translation("{number(x)} trees", de), Map("x" -> 0))
    assert(translationKey === "{zero(number(x))} trees")
  }

  test("Single categories are omitted") {
    // Category needs to be omitted if there is only 1 in domain.
    val translationKeySingleCategory = Format.buildTranslationKey(new Translation("{plain(x)} test", de), Map("x" -> "abc"))
    assert(translationKeySingleCategory === "{plain(x)} test")

    val translationKeys = Format.buildAllTranslationKeys(new Translation("a {x} b {plain(y)} c {number(z)} d", en))
    assert(translationKeys === Set(
      "a {plain(x)} b {plain(y)} c {zero(number(z))} d",
      "a {plain(x)} b {plain(y)} c {one(number(z))} d"))
  }

  test("translate number") {
    val adapter = new MapAdapter(Map(
      "{zero(number(x))} trees" -> "{x} Bäume",
      "{one(number(x))} trees" -> "{x} Baum"))
    val sample = new Translation("{number(x)} trees", de)
    assert(sample(adapter, "x" -> 0) === "0 Bäume")
    assert(sample(adapter, "x" -> 1) === "1 Baum")
    assert(sample(adapter, "x" -> 13) === "13 Bäume")
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
    val adapter = new MapAdapter(Map.empty[String, String])
    assert(new Translation("fifnzfn832{number(x)}jsd skjfsd {y}jsd", Root)(adapter, "x" -> 5, "y" -> "--") === "fifnzfn8325jsd skjfsd --jsd")
  }

  test("date only") {
    val adapter = new MapAdapter(Map.empty[String, String])
    val cal = Calendar.getInstance()
    cal.set(2013, 9, 17)
    assert(Translation.getVariadic(Root, adapter, "{date(0)}", cal) === "2013-10-17")
  }

  test("placeholders only") {
    val adapter = new MapAdapter(Map.empty[String, String])
    assert(Translation.getVariadic(Root, adapter, "{0}{1}", "A", "B") === "AB")
  }
}