package api

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import translatables.Adapter
import translatables.Translation
import languages.de
import translatables.adapter.MapAdapter

@RunWith(classOf[JUnitRunner])
class Java extends FunSuite {
  test("Variadic static") {
    val adapter = new Adapter {
      def get(translationKey: String): String = {
        return "Meine {0} schönen Hüte."
      }
    }
    assert(Translation.getVariadic(de, adapter, "My nice {number(0)} hat.", 5:Integer) === "Meine 5 schönen Hüte.")

    val javaMap = new java.util.HashMap[String, String]()
    javaMap.put("My nice {zero(number(0))} hat.", "Meine {0} schönen Hüte.")
    javaMap.put("My nice {one(number(0))} hat.", "Mein {0} schöner Hut.")
    val mapAdapter = new MapAdapter(javaMap)
    assert(Translation.getVariadic(de, mapAdapter, "My nice {number(0)} hat.", 5:Integer) === "Meine 5 schönen Hüte.")
    assert(Translation.getVariadic(de, mapAdapter, "My nice {number(0)} hat.", 1:Integer) === "Mein 1 schöner Hut.")
  }
}