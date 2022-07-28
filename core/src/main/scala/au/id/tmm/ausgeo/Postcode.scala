package au.id.tmm.ausgeo

import scala.runtime.ScalaRunTime
import scala.util.matching.Regex

final case class Postcode private (asString: String) extends AnyVal {
  def copy(asString: String = this.asString): Either[Postcode.CreationError, Postcode] = Postcode(asString)
}

object Postcode {

  private val validPostcodePattern: Regex = """^[0-9]{4}$""".r
  private val onlyNumbersPattern: Regex   = """^[0-9]+$""".r

  def apply(asString: String): Either[CreationError, Postcode] = asString match {
    case validPostcodePattern() => Right(new Postcode(asString))
    case onlyNumbersPattern()   => Left(CreationError.InvalidLength(asString.length))
    case nonDigitsString        => Left(CreationError.NotNumeric(nonDigitsString))
  }

  def makeUnsafe(asString: String): Postcode = Postcode(asString) match {
    case Right(postcode) => postcode
    case Left(e)         => throw e
  }

  sealed abstract class CreationError extends Exception with Product {
    override def getMessage: String = ScalaRunTime._toString(this)
  }

  object CreationError {
    final case class InvalidLength(actualLength: Int) extends CreationError
    final case class NotNumeric(badPostcode: String)  extends CreationError
  }

  implicit val ordering: Ordering[Postcode] = Ordering.by(_.asString)

}
