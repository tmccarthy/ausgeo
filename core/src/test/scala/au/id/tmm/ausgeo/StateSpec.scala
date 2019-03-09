package au.id.tmm.ausgeo

import au.id.tmm.ausgeo.State._
import au.id.tmm.utilities.testing.ImprovedFlatSpec

class StateSpec extends ImprovedFlatSpec {

  def testsForState(
                     state: State,
                     expectedAbbreviation: String,
                     expectedName: String,
                     expectedNiceName: String,
                     expectedIsTerritory: Boolean,
                   ): Unit = {
    behaviour of state.toString

    it should s"be abbreviated as '$expectedAbbreviation" in assert(state.abbreviation === expectedAbbreviation)
    it should s"have the full name '$expectedName'" in assert(state.name === expectedName)
    it should s"have the nice name '$expectedNiceName'" in assert(state.niceName === expectedNiceName)

    if (expectedIsTerritory) {
      it should s"be an instance of Territory" in {
        state match {
          case _: Territory => succeed
          case _: StateProper => fail
        }
      }

      it should "be a territory" in assert(state.isTerritory === true)
      it should "not be a state" in assert(state.isStateProper === false)
    } else {
      it should s"be an instance of StateProper" in {
        state match {
          case _: Territory => fail
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

  "the default ordering of the states" should "be by size descending" in {
    assert(State.allStates.toList.sorted === List(NSW, VIC, QLD, WA, SA, TAS, NT, ACT))
  }

}
