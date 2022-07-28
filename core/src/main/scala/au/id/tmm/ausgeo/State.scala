package au.id.tmm.ausgeo

import au.id.tmm.ausgeo.State._

sealed abstract class State {

  def name: String = this match {
    case NSW => "New South Wales"
    case VIC => "Victoria"
    case QLD => "Queensland"
    case WA  => "Western Australia"
    case SA  => "South Australia"
    case TAS => "Tasmania"
    case NT  => "Northern Territory"
    case ACT => "Australian Capital Territory"
  }

  def abbreviation: String = this match {
    case NSW => "NSW"
    case VIC => "VIC"
    case QLD => "QLD"
    case WA  => "WA"
    case SA  => "SA"
    case TAS => "TAS"
    case NT  => "NT"
    case ACT => "ACT"
  }

  def isStateProper: Boolean = this match {
    case _: StateProper => true
    case _: Territory   => false
  }

  def isTerritory: Boolean = !isStateProper

  /**
    * Whether the state name requires a definite article ("<bold>the</bold> ACT").
    */
  def requiresDefiniteArticle: Boolean = isTerritory

  /**
    * The full name of the state, with a definite article if needed.
    */
  def niceName: String =
    if (requiresDefiniteArticle) {
      s"the $name"
    } else {
      name
    }

  override def hashCode(): Int = State.ordinalBySize(this)

}

object State {

  sealed trait StateProper extends State
  sealed trait Territory   extends State

  case object NSW extends StateProper
  case object VIC extends StateProper
  case object QLD extends StateProper
  case object WA  extends StateProper
  case object SA  extends StateProper
  case object TAS extends StateProper
  case object NT  extends Territory
  case object ACT extends Territory

  def fromAbbreviation(abbreviation: String): Option[State] = abbreviation.toUpperCase match {
    case "NSW" => Some(NSW)
    case "VIC" => Some(VIC)
    case "QLD" => Some(QLD)
    case "WA"  => Some(WA)
    case "SA"  => Some(SA)
    case "TAS" => Some(TAS)
    case "NT"  => Some(NT)
    case "ACT" => Some(ACT)
    case _     => None
  }

  def fromAbbreviationOrError(abbreviation: String): Either[ParsedFromAbbreviationException, State] =
    fromAbbreviation(abbreviation).toRight(ParsedFromAbbreviationException(abbreviation))

  def fromName(name: String): Option[State] = name.toUpperCase() match {
    case "NEW SOUTH WALES"              => Some(NSW)
    case "VICTORIA"                     => Some(VIC)
    case "QUEENSLAND"                   => Some(QLD)
    case "WESTERN AUSTRALIA"            => Some(WA)
    case "SOUTH AUSTRALIA"              => Some(SA)
    case "TASMANIA"                     => Some(TAS)
    case "NORTHERN TERRITORY"           => Some(NT)
    case "AUSTRALIAN CAPITAL TERRITORY" => Some(ACT)
    case _                              => None
  }

  def fromNameOrError(name: String): Either[ParsedFromNameException, State] =
    fromName(name).toRight(ParsedFromNameException(name))

  val allStates: Set[State] = Set(NSW, VIC, QLD, WA, SA, TAS, NT, ACT)

  def ordinalBySize(state: State): Int = state match {
    case NSW => 0
    case VIC => 1
    case QLD => 2
    case WA  => 3
    case SA  => 4
    case TAS => 5
    case NT  => 6
    case ACT => 7
  }

  implicit val orderBySize: Ordering[State] = Ordering.by[State, Int](ordinalBySize)

  final case class ParsedFromAbbreviationException(badAbbreviation: String)
      extends Exception(s"""Bad abbreviation for state: "$badAbbreviation"""")

  final case class ParsedFromNameException(badName: String) extends Exception(s"""Bad name for state: "$badName"""")

}
