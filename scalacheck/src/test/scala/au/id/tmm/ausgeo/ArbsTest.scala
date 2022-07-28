package au.id.tmm.ausgeo

import au.id.tmm.ausgeo.Arbs._
import cats.laws.discipline.ExhaustiveCheck
import munit.ScalaCheckSuite
import org.scalacheck.Prop._

class ArbsTest extends ScalaCheckSuite {

  property("LatLong always correct") {
    forAll { (latLong: LatLong) =>
      assert(latLong.lat <= 90d)
      assert(latLong.lat >= -90d)

      assert(latLong.long <= 180d)
      assert(latLong.long >= -180d)
    }
  }

  property("Postcode always 4 digits") {
    forAll { (postcode: Postcode) => postcode.asString.length == 4 }
  }

  property("Postcode always numeric") {
    val digits = Range.inclusive(0, 9).map(_.toString.charAt(0)).toSet

    forAll { (postcode: Postcode) => postcode.asString.forall(c => digits.contains(c)) }
  }

  test("State ExhaustiveCheck") {
    assertEquals(ExhaustiveCheck[State].allValues.toSet, State.allStates)
  }

}
