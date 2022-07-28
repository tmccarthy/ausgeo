package au.id.tmm.ausgeo

import munit.FunSuite

class PostcodeSpec extends FunSuite {

  test("a postcode can not be made if it is less than 4 digits") {
    assertEquals(Postcode("123"), Left(Postcode.CreationError.InvalidLength(3)))
  }

  test("a postcode can not be made if it is more than 4 digits") {
    assertEquals(Postcode("12345"), Left(Postcode.CreationError.InvalidLength(5)))
  }

  test("a postcode can not be made if it contains alphabetic characters") {
    assertEquals(Postcode("a234"), Left(Postcode.CreationError.NotNumeric("a234")))
  }

  test("a postcode can not be made if it contains Arabic numerals") {
    assertEquals(Postcode("٣٠٠٠"), Left(Postcode.CreationError.NotNumeric("٣٠٠٠")))
  }

  test("a postcode can not be copied to be invalid") {
    assertEquals(Postcode.makeUnsafe("3000").copy("invalid"), Left(Postcode.CreationError.NotNumeric("invalid")))
  }

  test("a postcode should be ordered according to the code") {
    val unordered = List(
      Postcode.makeUnsafe("5000"),
      Postcode.makeUnsafe("0500"),
      Postcode.makeUnsafe("3000"),
    )

    val expectedSorted = List(
      Postcode.makeUnsafe("0500"),
      Postcode.makeUnsafe("3000"),
      Postcode.makeUnsafe("5000"),
    )

    assertEquals(unordered.sorted, expectedSorted)
  }

}
