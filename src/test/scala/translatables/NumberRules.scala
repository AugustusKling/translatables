package translatables

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.util.Locale
import domains.numbers.Rule3
import org.scalatest.exceptions.TestFailedException
import domains.numbers.Rule0
import domains.numbers.Rule1
import domains.numbers.Rule2
import domains.numbers.Rule4
import domains.numbers.Rule5
import domains.numbers.Rule6
import domains.numbers.Rule7
import domains.numbers.Rule8
import domains.numbers.Rule9
import domains.numbers.Rule10
import domains.numbers.Rule11
import domains.numbers.Rule12
import domains.numbers.Rule13
import domains.numbers.Rule14
import domains.numbers.Rule15
import domains.numbers.Rule16
import domains.numbers.Rule17
import domains.numbers.Rule18
import domains.numbers.Rule19
import domains.numbers.Rule20
import domains.numbers.Rule21
import domains.numbers.Rule22

@RunWith(classOf[JUnitRunner])
class NumberRules extends FunSuite {
  /**
   * Verifies applying the rule to all values yields the category.
   * @param rule Domain of category.
   * @param values List of test cases.
   * @param category Expected category for all values.
   * @throws TestFailedException In case any value is not covered by the expected category.
   */
  def assertCategory(rule: Domain, values: List[Any], category: Category) = {
    values.foreach(value => {
      val determinedCategory = rule.getCategory(value)
      if (determinedCategory != category) throw new TestFailedException("Value " + value + " not covered by " + category.name + " but claimed by " + determinedCategory.name, 1)
    })
  }

  test("Rule0") {
    val rule = new Rule0(Locale.ROOT)
    assertCategory(rule, (0 until (50)).toList, Rule0.zero)
  }

  test("Rule1") {
    val rule = new Rule1(Locale.ROOT)
    assertCategory(rule, List(1), Rule1.one)
    assertCategory(rule, List(0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
      25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50), Rule1.zero)
  }

  test("Rule2") {
    val rule = new Rule2(Locale.ROOT)
    assertCategory(rule, List(0, 1), Rule2.zero)
    assertCategory(rule, List(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,
      26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51), Rule2.two)
  }

  test("Rule3") {
    val rule = new Rule3(Locale.ROOT)
    assert(rule.getCategory(0) === Rule3.zero)
    assertCategory(rule, List(1, 21, 31, 41, 51, 61, 71, 81, 91, 101, 121, 131, 141, 151, 161, 171, 181, 191, 201, 221,
      231, 241, 251, 261, 271, 281, 291), Rule3.one)
    assertCategory(rule, List(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 22, 23, 24, 25, 26, 27, 28,
      29, 30, 32, 33, 34, 35, 36, 37, 38, 39, 40, 42, 43, 44, 45, 46, 47, 48, 49, 50, 52, 53, 54, 55), Rule3.two)
  }

  test("Rule4") {
    val rule = new Rule4(Locale.ROOT)
    assertCategory(rule, List(1, 11), Rule4.one)
    assertCategory(rule, List(2, 12), Rule4.two)
    assertCategory(rule, List(3, 4, 5, 6, 7, 8, 9, 10, 13, 14, 15, 16, 17, 18, 19), Rule4.three)
    assertCategory(rule, List(0, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
      41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51), Rule4.zero)
  }

  test("Rule5") {
    val rule = new Rule5(Locale.ROOT)
    assertCategory(rule, List(1), Rule5.one)
    assertCategory(rule, List(0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 101, 102, 103, 104,
      105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 201, 202, 203, 204, 205, 206, 207,
      208, 209, 210, 211, 212), Rule5.zero)
    assertCategory(rule, List(20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41,
      42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68,
      69), Rule5.twenty)
  }

  test("Rule6") {
    val rule = new Rule6(Locale.ROOT)
    assertCategory(rule, List(1, 21, 31, 41, 51, 61, 71, 81, 91, 101, 121, 131, 141, 151, 161, 171, 181, 191, 201, 221,
      231, 241, 251, 261, 271, 281, 291), Rule6.one)
    assertCategory(rule, List(0, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 111,
      112, 113, 114, 115, 116, 117, 118, 119, 120, 130, 140, 150, 160, 170, 180, 190, 200, 210, 211, 212, 213, 214,
      215, 216, 217, 218, 219, 220), Rule6.zero)
    assertCategory(rule, List(2, 3, 4, 5, 6, 7, 8, 9, 22, 23, 24, 25, 26, 27, 28, 29, 32, 33, 34, 35, 36, 37, 38, 39,
      42, 43, 44, 45, 46, 47, 48, 49, 52, 53, 54, 55, 56, 57, 58, 59, 62, 63, 64, 65, 66, 67, 68, 69, 72, 73), Rule6.two)
  }

  test("Rule7") {
    val rule = new Rule7(Locale.ROOT)
    assertCategory(rule, List(1, 21, 31, 41, 51, 61, 71, 81, 91, 101, 121, 131, 141, 151, 161, 171, 181, 191, 201, 221,
      231, 241, 251, 261, 271, 281, 291), Rule7.one)
    assertCategory(rule, List(2, 3, 4, 22, 23, 24, 32, 33, 34, 42, 43, 44, 52, 53, 54, 62, 63, 64, 72, 73, 74, 82, 83,
      84, 92, 93, 94, 102, 103, 104, 122, 123, 124, 132, 133, 134, 142, 143, 144, 152, 153, 154, 162, 163, 164, 172,
      173, 174, 182, 183), Rule7.two)
    assertCategory(rule, List(0, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 25, 26, 27, 28, 29, 30, 35,
      36, 37, 38, 39, 40, 45, 46, 47, 48, 49, 50, 55, 56, 57, 58, 59, 60, 65, 66, 67, 68, 69, 70, 75, 76, 77), Rule7.zero)
  }

  test("Rule8") {
    val rule = new Rule8(Locale.ROOT)
    assertCategory(rule, List(1), Rule8.one)
    assertCategory(rule, List(2, 3, 4), Rule8.two)
    assertCategory(rule, List(0, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
      28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53), Rule8.zero)
  }

  test("Rule9") {
    val rule = new Rule9(Locale.ROOT)
    assertCategory(rule, List(1), Rule9.one)
    assertCategory(rule, List(2, 3, 4, 22, 23, 24, 32, 33, 34, 42, 43, 44, 52, 53, 54, 62, 63, 64, 72, 73, 74, 82, 83,
      84, 92, 93, 94, 102, 103, 104, 122, 123, 124, 132, 133, 134, 142, 143, 144, 152, 153, 154, 162, 163, 164, 172,
      173, 174, 182, 183), Rule9.two)
    assertCategory(rule, List(0, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 25, 26, 27, 28, 29, 30,
      31, 35, 36, 37, 38, 39, 40, 41, 45, 46, 47, 48, 49, 50, 51, 55, 56, 57, 58, 59, 60, 61, 65, 66, 67, 68), Rule9.zero)
  }

  test("Rule10") {
    val rule = new Rule10(Locale.ROOT)
    assertCategory(rule, List(1, 101, 201), Rule10.one)
    assertCategory(rule, List(2, 102, 202), Rule10.two)
    assertCategory(rule, List(3, 4, 103, 104, 203, 204), Rule10.three)
    assertCategory(rule, List(0, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
      28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53), Rule10.zero)
  }

  test("Rule11") {
    val rule = new Rule11(Locale.ROOT)
    assertCategory(rule, List(1), Rule11.one)
    assertCategory(rule, List(2), Rule11.two)
    assertCategory(rule, List(3, 4, 5, 6), Rule11.three)
    assertCategory(rule, List(7, 8, 9, 10), Rule11.seven)
    assertCategory(rule, List(0, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
      32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59), Rule11.zero)
  }

  test("Rule12") {
    val rule = new Rule12(Locale.ROOT)
    assertCategory(rule, List(1), Rule12.one)
    assertCategory(rule, List(2), Rule12.two)
    assertCategory(rule, List(3, 4, 5, 6, 7, 8, 9, 10, 103, 104, 105, 106, 107, 108, 109, 110, 203, 204, 205, 206, 207,
      208, 209, 210), Rule12.three)
    assertCategory(rule, List(11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32,
      33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60), Rule12.eleven)
    assertCategory(rule, List(100, 101, 102, 200, 201, 202), Rule12.hundred)
    assertCategory(rule, List(0), Rule12.zero)
  }

  test("Rule13") {
    val rule = new Rule13(Locale.ROOT)
    assertCategory(rule, List(1), Rule13.one)
    assertCategory(rule, List(0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 201,
      202, 203, 204, 205, 206, 207, 208, 209, 210), Rule13.zero)
    assertCategory(rule, List(11, 12, 13, 14, 15, 16, 17, 18, 19, 111, 112, 113, 114, 115, 116, 117, 118, 119, 211,
      212, 213, 214, 215, 216, 217, 218, 219), Rule13.eleven)
    assertCategory(rule, List(20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41,
      42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69), Rule13.twenty)
  }

  test("Rule14") {
    val rule = new Rule14(Locale.ROOT)
    assertCategory(rule, List(1, 11, 21, 31, 41, 51, 61, 71, 81, 91, 101, 111, 121, 131, 141, 151, 161, 171, 181, 191,
      201, 211, 221, 231, 241, 251, 261, 271, 281, 291), Rule14.one)
    assertCategory(rule, List(2, 12, 22, 32, 42, 52, 62, 72, 82, 92, 102, 112, 122, 132, 142, 152, 162, 172, 182, 192,
      202, 212, 222, 232, 242, 252, 262, 272, 282, 292), Rule14.two)
    assertCategory(rule, List(0, 3, 4, 5, 6, 7, 8, 9, 10, 13, 14, 15, 16, 17, 18, 19, 20, 23, 24, 25, 26, 27, 28, 29,
      30, 33, 34, 35, 36, 37, 38, 39, 40, 43, 44, 45, 46, 47, 48, 49, 50, 53, 54, 55, 56, 57, 58, 59, 60, 63), Rule14.zero)
  }

  test("Rule15") {
    val rule = new Rule15(Locale.ROOT)
    assertCategory(rule, List(1, 21, 31, 41, 51, 61, 71, 81, 91, 101, 121, 131, 141, 151, 161, 171, 181, 191, 201, 221,
      231, 241, 251, 261, 271, 281, 291), Rule15.one)
    assertCategory(rule, List(0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 22, 23, 24, 25,
      26, 27, 28, 29, 30, 32, 33, 34, 35, 36, 37, 38, 39, 40, 42, 43, 44, 45, 46, 47, 48, 49, 50, 52, 53, 54), Rule15.zero)
  }

  test("Rule16") {
    val rule = new Rule16(Locale.ROOT)
    assertCategory(rule, List(1), Rule16.one)
    assertCategory(rule, List(21, 31, 41, 51, 61, 81, 101, 121, 131, 141, 151, 161, 181, 201, 221, 231, 241, 251, 261,
      281), Rule16.twentyOne)
    assertCategory(rule, List(2, 22, 32, 42, 52, 62, 82, 102, 122, 132, 142, 152, 162, 182, 202, 222, 232, 242, 252,
      262, 282), Rule16.two)
    assertCategory(rule, List(3, 4, 9, 23, 24, 29, 33, 34, 39, 43, 44, 49, 53, 54, 59), Rule16.three)
    assertCategory(rule, List(1000000, 2000000, 3000000, 4000000, 5000000, 6000000, 7000000, 8000000, 9000000, 10000000), Rule16.million)
    assertCategory(rule, List(0, 5, 6, 7, 8, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 25, 26, 27, 28, 30, 35, 36,
      37, 38, 40), Rule16.zero)
  }

  test("Rule17") {
    val rule = new Rule17(Locale.ROOT)
    assertCategory(rule, List(1), Rule17.one)
    assertCategory(rule, List(2), Rule17.two)
    assertCategory(rule, List(3), Rule17.three)
    assertCategory(rule, List(0, 4, 5, 6, 7, 8), Rule17.zero)
  }

  test("Rule18") {
    val rule = new Rule18(Locale.ROOT)
    assertCategory(rule, List(1), Rule18.one)
    assertCategory(rule, List(2), Rule18.two)
    assertCategory(rule, List(0, 3, 4, 5, 6, 7, 8), Rule18.zero)
  }

  test("Rule19") {
    val rule = new Rule19(Locale.ROOT)
    assertCategory(rule, List(1), Rule19.one)
    assertCategory(rule, List(2), Rule19.two)
    assertCategory(rule, List(0, 3, 4, 5, 6, 7), Rule19.zero)
    assertCategory(rule, List(8, 11), Rule19.eight)
  }

  test("Rule20") {
    val rule = new Rule20(Locale.ROOT)
    assertCategory(rule, List(0), Rule20.zero)
    assertCategory(rule, List(1, 2, 3, 4, 5, 6), Rule20.one)
  }

  test("Rule21") {
    val rule = new Rule21(Locale.ROOT)
    assertCategory(rule, List(0, 2, 3, 4, 5, 6), Rule21.zero)
    assertCategory(rule, List(1, 21, 31, 41, 51, 61), Rule21.one)
  }

  test("Rule22") {
    val rule = new Rule22(Locale.ROOT)
    assertCategory(rule, List(0, 1, 11, 12, 13, 14, 15, 97, 98, 99), Rule22.zero)
    assertCategory(rule, List(2, 3, 4, 5, 6, 7, 8, 9, 10, 100, 101, 1000), Rule22.two)
  }
}