package au.id.tmm.ausgeo

import org.scalatest.FlatSpec

class PostcodeSpec extends FlatSpec {

  "a postcode" can "not be made if it is less than 4 digits" in {
    assert(Postcode("123") === Left(Postcode.CreationError.InvalidLength(3)))
  }

  it can "not be made if it is more than 4 digits" in {
    assert(Postcode("12345") === Left(Postcode.CreationError.InvalidLength(5)))
  }

  it can "not be made if it contains alphabetic characters" in {
    assert(Postcode("a234") === Left(Postcode.CreationError.NotNumeric("a234")))
  }

  it can "not be made if it contains Arabic numerals" in {
    assert(Postcode("٣٠٠٠") === Left(Postcode.CreationError.NotNumeric("٣٠٠٠")))
  }

  it can "not be copied to be invalid" in {
    assert(Postcode.makeUnsafe("3000").copy("invalid") === Left(Postcode.CreationError.NotNumeric("invalid")))
  }

  it should "be ordered according to the code" in {
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

    assert(unordered.sorted === expectedSorted)
  }

}
