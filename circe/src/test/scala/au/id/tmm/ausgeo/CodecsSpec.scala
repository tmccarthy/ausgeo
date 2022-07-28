package au.id.tmm.ausgeo

import au.id.tmm.ausgeo.Codecs._
import io.circe.Json
import io.circe.syntax.EncoderOps
import munit.FunSuite

class CodecsSpec extends FunSuite {

  test("encoding LatLong should work") {
    assertEquals(LatLong(-35, 42).asJson, Json.obj("lat" -> (-35).asJson, "long" -> 42.asJson))
  }

  test("decoding LatLong should work") {
    assertEquals(Json.obj("lat" -> (-35).asJson, "long" -> 42.asJson).as[LatLong], Right(LatLong(-35, 42)))
  }

  test("encoding Postcode should work") {
    assertEquals(Postcode.makeUnsafe("0800").asJson, Json.fromString("0800"))
  }

  test("decoding Postcode should work") {
    assertEquals(Json.fromString("0800").as[Postcode], Right(Postcode.makeUnsafe("0800")))
  }

  test("decoding Postcode should fail if the postcode is invalid") {
    assertEquals(Json.fromString("invalid").as[Postcode].left.map(_.message), Left("NotNumeric(invalid)"))
  }

  test("encoding a State should work") {
    assertEquals((State.VIC: State).asJson, Json.fromString("VIC"))
  }

  test("encoding a State should work for subtypes of state") {
    assertEquals(State.VIC.asJson, Json.fromString("VIC"))
  }

  test("decoding a State should work") {
    assertEquals(Json.fromString("VIC").as[State], Right(State.VIC))
  }

  test("encoding Address should work") {
    assertEquals(
      Address(Vector("123 First St"), "Melbourne", Postcode.makeUnsafe("3000"), State.VIC).asJson,
      Json.obj(
        "lines"    -> Vector("123 First St").asJson,
        "suburb"   -> "Melbourne".asJson,
        "postcode" -> Postcode.makeUnsafe("3000").asJson,
        "state"    -> State.VIC.asJson,
      ),
    )
  }

  test("decoding Address should work") {
    assertEquals(
      Json
        .obj(
          "lines"    -> Vector("123 First St").asJson,
          "suburb"   -> "Melbourne".asJson,
          "postcode" -> Postcode.makeUnsafe("3000").asJson,
          "state"    -> State.VIC.asJson,
        )
        .as[Address],
      Right(Address(Vector("123 First St"), "Melbourne", Postcode.makeUnsafe("3000"), State.VIC)),
    )
  }

}
