package au.id.tmm.ausgeo

import au.id.tmm.ausgeo.Codecs._
import io.circe.Json
import io.circe.syntax.EncoderOps
import org.scalatest.FlatSpec

class CodecsSpec extends FlatSpec {

  "encoding LatLong" should "work" in {
    assert(LatLong(-35, 42).asJson === Json.obj("lat" -> (-35).asJson, "long" -> 42.asJson))
  }

  "decoding LatLong" should "work" in {
    assert(Json.obj("lat" -> (-35).asJson, "long" -> 42.asJson).as[LatLong] === Right(LatLong(-35, 42)))
  }

  "encoding Postcode" should "work" in {
    assert(Postcode.makeUnsafe("0800").asJson === Json.fromString("0800"))
  }

  "decoding Postcode" should "work" in {
    assert(Json.fromString("0800").as[Postcode] === Right(Postcode.makeUnsafe("0800")))
  }

  it should "fail if the postcode is invalid" in {
    assert(Json.fromString("invalid").as[Postcode].left.map(_.message) === Left("NotNumeric(invalid)"))
  }

  "encoding a State" should "work" in {
    assert((State.VIC: State).asJson === Json.fromString("VIC"))
  }

  it should "work for subtypes of state" in {
    assert(State.VIC.asJson === Json.fromString("VIC"))
  }

  "decoding a State" should "work" in {
    assert(Json.fromString("VIC").as[State] === Right(State.VIC))
  }

  "encoding Address" should "work" in {
    assert(
      Address(Vector("123 First St"), "Melbourne", Postcode.makeUnsafe("3000"), State.VIC).asJson ===
        Json.obj(
          "lines" -> Vector("123 First St").asJson,
          "suburb" -> "Melbourne".asJson,
          "postcode" -> Postcode.makeUnsafe("3000").asJson,
          "state" -> State.VIC.asJson,
        )
    )
  }

  "decoding Address" should "work" in {
    assert(
      Json.obj(
        "lines" -> Vector("123 First St").asJson,
        "suburb" -> "Melbourne".asJson,
        "postcode" -> Postcode.makeUnsafe("3000").asJson,
        "state" -> State.VIC.asJson,
      ).as[Address] === Right(Address(Vector("123 First St"), "Melbourne", Postcode.makeUnsafe("3000"), State.VIC))
    )
  }

}
