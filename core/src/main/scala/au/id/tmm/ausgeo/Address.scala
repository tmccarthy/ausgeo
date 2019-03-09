package au.id.tmm.ausgeo

final case class Address(
                          lines: Vector[String],
                          suburb: String,
                          postcode: Postcode,
                          state: State,
                        )
