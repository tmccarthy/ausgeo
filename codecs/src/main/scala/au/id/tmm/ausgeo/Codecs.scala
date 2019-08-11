package au.id.tmm.ausgeo

import io.circe.{Decoder, Encoder}

object Codecs {
  implicit val latLongEncoder: Encoder[LatLong] = Encoder.forProduct2("lat", "long")(l => (l.lat, l.long))
  implicit val latLongDecoder: Decoder[LatLong] = Decoder.forProduct2("lat", "long")(LatLong.apply)

  implicit val postcodeEncoder: Encoder[Postcode] = Encoder.encodeString.contramap(_.asString)
  implicit val postcodeDecoder: Decoder[Postcode] = Decoder.decodeString.emap(Postcode(_).left.map(_.getMessage))

  private val concreteStateEncoder: Encoder[State]  = Encoder.encodeString.contramap(_.abbreviation)
  implicit def stateEncoder[E <: State]: Encoder[E] = concreteStateEncoder.asInstanceOf[Encoder[E]]
  implicit val stateDecoder: Decoder[State] = Decoder.decodeString.emap { abbreviation =>
    State.fromAbbreviation(abbreviation).toRight(s"Invalid state $abbreviation")
  }

  implicit val addressEncoder: Encoder[Address] =
    Encoder.forProduct4("lines", "suburb", "postcode", "state")(a => (a.lines, a.suburb, a.postcode, a.state))

  implicit val addressDecoder: Decoder[Address] =
    Decoder.forProduct4("lines", "suburb", "postcode", "state")(Address.apply)
}
