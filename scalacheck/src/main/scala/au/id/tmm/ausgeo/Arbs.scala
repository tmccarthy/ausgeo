package au.id.tmm.ausgeo

import au.id.tmm.ausgeo.Gens._
import cats.laws.discipline.ExhaustiveCheck
import org.scalacheck.{Arbitrary, Cogen, Gen}

object Gens {
  val genLatLong: Gen[LatLong] = for {
    lat  <- Gen.chooseNum[Double](-90, 90)
    long <- Gen.chooseNum[Double](-180, 180)
  } yield LatLong(lat, long)

  val genPostcode: Gen[Postcode] = Gen.stringOfN(4, Gen.choose[Char]('0', '9')).map(Postcode.makeUnsafe)

  val genState: Gen[State] = Gen.oneOf(State.allStates)

  val genAddress: Gen[Address] = for {
    lines    <- Gen.listOf(Gen.asciiPrintableStr).map(_.toVector)
    suburb   <- Gen.asciiPrintableStr
    postcode <- genPostcode
    state    <- genState
  } yield Address(lines, suburb, postcode, state)

}

object Arbs {

  implicit val arbLatLong: Arbitrary[LatLong]   = Arbitrary(genLatLong)
  implicit val arbPostcode: Arbitrary[Postcode] = Arbitrary(genPostcode)
  implicit val arbState: Arbitrary[State]       = Arbitrary(genState)
  implicit val arbAddress: Arbitrary[Address]   = Arbitrary(genAddress)

  implicit val cogenState: Cogen[State]       = Cogen.cogenInt.contramap(State.ordinalBySize)
  implicit val cogenPostcode: Cogen[Postcode] = Cogen.cogenInt.contramap(_.asString.toInt)
  implicit val cogenLatLong: Cogen[LatLong]   = Cogen.cogenList[Double].contramap(l => List(l.lat, l.long))
  implicit val cogenAddress: Cogen[Address] = Cogen
    .cogenList[String]
    .contramap(a => a.lines.toList :+ a.suburb :+ a.postcode.asString :+ a.state.abbreviation)

  implicit val exhaustiveCheckState: ExhaustiveCheck[State] = ExhaustiveCheck.instance(State.allStates.toList)

}
