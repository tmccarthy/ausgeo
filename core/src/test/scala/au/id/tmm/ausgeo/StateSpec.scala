package au.id.tmm.ausgeo

import au.id.tmm.ausgeo.State._
import org.scalatest.FlatSpec

class StateSpec extends FlatSpec {

  def testsForState(
    state: State,
    expectedAbbreviation: String,
    expectedName: String,
    expectedNiceName: String,
    expectedIsTerritory: Boolean,
  ): Unit = {
    behavior of state.toString

    it should s"be abbreviated as '$expectedAbbreviation'" in assert(state.abbreviation === expectedAbbreviation)
    it should s"have the full name '$expectedName'" in assert(state.name === expectedName)
    it should s"have the nice name '$expectedNiceName'" in assert(state.niceName === expectedNiceName)

    def assertBuiltFromAbbreviation(abbreviation: String): Unit =
      it can s"be built from the abbreviation '$abbreviation'" in {
        assert(State.fromAbbreviation(abbreviation) === Some(state))
      }

    def assertBuiltFromName(name: String): Unit =
      it can s"be built from the name '$name'" in {
        assert(State.fromName(name) === Some(state))
      }

    assertBuiltFromAbbreviation(expectedAbbreviation.toUpperCase)
    assertBuiltFromAbbreviation(expectedAbbreviation.toLowerCase)
    assertBuiltFromAbbreviation(expectedAbbreviation.toLowerCase.capitalize)

    it can "not be built from the abbreviation 'asdf'" in {
      assert(State.fromAbbreviationOrError("asdf") === Left(State.ParsedFromAbbreviationException("asdf")))
    }

    assertBuiltFromName(expectedName.toUpperCase)
    assertBuiltFromName(expectedName.toLowerCase)
    assertBuiltFromName(expectedName.toLowerCase.capitalize)

    it can "not be built from the name 'asdf'" in {
      assert(State.fromNameOrError("asdf") === Left(State.ParsedFromNameException("asdf")))
    }

    if (expectedIsTerritory) {
      it should s"be an instance of Territory" in {
        state match {
          case _: Territory   => succeed
          case _: StateProper => fail
        }
      }

      it should "be a territory" in assert(state.isTerritory === true)
      it should "not be a state" in assert(state.isStateProper === false)
    } else {
      it should s"be an instance of StateProper" in {
        state match {
          case _: Territory   => fail
          case _: StateProper => succeed
        }
      }

      it should "not be a territory" in assert(state.isTerritory === false)
      it should "not be a state" in assert(state.isStateProper === true)
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

  "building a state from an abbreviation" should "fail for invalid input" in {
    assert(State.fromAbbreviation("invalid") === None)
  }

  "the default ordering of the states" should "be by size descending" in {
    assert(State.allStates.toList.sorted === List(NSW, VIC, QLD, WA, SA, TAS, NT, ACT))
  }

}
