package au.id.tmm.ausgeo

import au.id.tmm.ausgeo.State._
import cats.kernel.{BoundedEnumerable, Eq, Order}

object Instances {

  implicit val ausgeoEqForAddress: Eq[Address]         = Eq.fromUniversalEquals
  implicit val ausgeoEqForLatLong: Eq[LatLong]         = Eq.by(l => (l.lat, l.long))
  implicit val ausgeoOrderForPostcode: Order[Postcode] = Order.fromOrdering
  implicit val ausgeoInstancesForState: Order[State] with BoundedEnumerable[State] =
    new Order[State] with BoundedEnumerable[State] {
      override def compare(x: State, y: State): Int = State.orderBySize.compare(x, y)

      override def order: Order[State] = this

      override def maxBound: State = ACT

      override def partialPrevious(a: State): Option[State] = a match {
        case NSW => None
        case VIC => Some(NSW)
        case QLD => Some(VIC)
        case WA  => Some(QLD)
        case SA  => Some(WA)
        case TAS => Some(SA)
        case NT  => Some(TAS)
        case ACT => Some(NT)
      }

      override def partialNext(a: State): Option[State] = a match {
        case NSW => Some(VIC)
        case VIC => Some(QLD)
        case QLD => Some(WA)
        case WA  => Some(SA)
        case SA  => Some(TAS)
        case TAS => Some(NT)
        case NT  => Some(ACT)
        case ACT => None
      }

      override def minBound: State = NSW
    }

}
