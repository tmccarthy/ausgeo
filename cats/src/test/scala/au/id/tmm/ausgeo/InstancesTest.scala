package au.id.tmm.ausgeo

import au.id.tmm.ausgeo.Arbs._
import au.id.tmm.ausgeo.Instances._
import cats.kernel.laws.discipline.{BoundedEnumerableTests, EqTests, OrderTests}
import munit.DisciplineSuite

class InstancesTest extends DisciplineSuite {

  checkAll("Address", EqTests[Address].eqv)
  checkAll("LatLong", EqTests[LatLong].eqv)
  checkAll("Postcode", OrderTests[Postcode].order)
  checkAll("State", BoundedEnumerableTests[State].boundedEnumerable)

}
