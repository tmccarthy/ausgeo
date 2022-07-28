package au.id.tmm.ausgeo

import au.id.tmm.ausgeo.State._
import munit.{FunSuite, Location}

class StateSpec extends FunSuite {

  def testsForState(
    state: State,
    expectedAbbreviation: String,
    expectedName: String,
    expectedNiceName: String,
    expectedIsTerritory: Boolean,
  )(implicit
    loc: Location,
  ): Unit = {
    test(s"$state should be abbreviated as '$expectedAbbreviation'")(
      assertEquals(state.abbreviation, expectedAbbreviation))
    test(s"$state should have the full name '$expectedName'")(assertEquals(state.name, expectedName))
    test(s"$state should have the nice name '$expectedNiceName'")(assertEquals(state.niceName, expectedNiceName))

    def assertBuiltFromAbbreviation(abbreviation: String): Unit =
      test(s"$state can be built from the abbreviation '$abbreviation'") {
        assertEquals(State.fromAbbreviation(abbreviation), Some(state))
      }

    def assertBuiltFromName(name: String): Unit =
      test(s"$state can be built from the name '$name'") {
        assertEquals(State.fromName(name), Some(state))
      }

    assertBuiltFromAbbreviation(expectedAbbreviation.toUpperCase)
    assertBuiltFromAbbreviation(expectedAbbreviation.toLowerCase)
    assertBuiltFromAbbreviation(expectedAbbreviation.toLowerCase.capitalize)

    test(s"$state can not be built from the abbreviation 'asdf'") {
      assertEquals(State.fromAbbreviationOrError("asdf"), Left(State.ParsedFromAbbreviationException("asdf")))
    }

    assertBuiltFromName(expectedName.toUpperCase)
    assertBuiltFromName(expectedName.toLowerCase)
    assertBuiltFromName(expectedName.toLowerCase.capitalize)

    test(s"$state can not be built from the name 'asdf'") {
      assertEquals(State.fromNameOrError("asdf"), Left(State.ParsedFromNameException("asdf")))
    }

    if (expectedIsTerritory) {
      test(s"$state should be an instance of Territory") {
        state match {
          case _: Territory   => ()
          case _: StateProper => fail("Expected territory but was state")
        }
      }

      test(s"$state should be a territory")(assertEquals(state.isTerritory, true))
      test(s"$state should not be a state")(assertEquals(state.isStateProper, false))
    } else {
      test(s"$state should be an instance of StateProper") {
        state match {
          case _: Territory   => fail("Expected state but was territory")
          case _: StateProper => ()
        }
      }

      test(s"$state should not be a territory")(assertEquals(state.isTerritory, false))
      test(s"$state should not be a state")(assertEquals(state.isStateProper, true))
    }
  }

  testsForState(
    state = NSW,
    expectedAbbreviation = "NSW",
    expectedName = "New South Wales",
    expectedNiceName = "New South Wales",
    expectedIsTerritory = false,
  )

  testsForState(
    state = QLD,
    expectedAbbreviation = "QLD",
    expectedName = "Queensland",
    expectedNiceName = "Queensland",
    expectedIsTerritory = false,
  )

  testsForState(
    state = SA,
    expectedAbbreviation = "SA",
    expectedName = "South Australia",
    expectedNiceName = "South Australia",
    expectedIsTerritory = false,
  )

  testsForState(
    state = TAS,
    expectedAbbreviation = "TAS",
    expectedName = "Tasmania",
    expectedNiceName = "Tasmania",
    expectedIsTerritory = false,
  )

  testsForState(
    state = VIC,
    expectedAbbreviation = "VIC",
    expectedName = "Victoria",
    expectedNiceName = "Victoria",
    expectedIsTerritory = false,
  )

  testsForState(
    state = WA,
    expectedAbbreviation = "WA",
    expectedName = "Western Australia",
    expectedNiceName = "Western Australia",
    expectedIsTerritory = false,
  )

  testsForState(
    state = NT,
    expectedAbbreviation = "NT",
    expectedName = "Northern Territory",
    expectedNiceName = "the Northern Territory",
    expectedIsTerritory = true,
  )

  testsForState(
    state = ACT,
    expectedAbbreviation = "ACT",
    expectedName = "Australian Capital Territory",
    expectedNiceName = "the Australian Capital Territory",
    expectedIsTerritory = true,
  )

  test("building a state from an abbreviation fail for invalid input") {
    assertEquals(State.fromAbbreviation("invalid"), None)
  }

  test("the default ordering of the states be by size descending") {
    assertEquals(State.allStates.toList.sorted, List(NSW, VIC, QLD, WA, SA, TAS, NT, ACT))
  }

}
