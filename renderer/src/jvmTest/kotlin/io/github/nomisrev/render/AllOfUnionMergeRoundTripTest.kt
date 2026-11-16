package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.allof.union.merge.Thing
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * An `allOf` of a union and another member decodes each kept branch with the other member's properties. Uses the
 * goldens under `kotlinTestData/client/allof-union-merge` directly.
 */
class AllOfUnionMergeRoundTripTest {
    private val json = Json

    private fun roundTrip(text: String): Thing {
        val decoded = json.decodeFromString<Thing>(text)
        assertEquals(json.parseToJsonElement(text), json.parseToJsonElement(json.encodeToString(decoded)))
        return decoded
    }

    @Test
    fun eachBranchHasTheOtherMembersProperties() {
        assertEquals(
            Thing(code = "abc", shape = Thing.Shape.RadiusAndColor(radius = 1.5, color = "red")),
            roundTrip("""{ "code": "abc", "shape": { "radius": 1.5, "color": "red" } }"""),
        )
        assertEquals(
            Thing(shape = Thing.Shape.SideAndColor(side = 2.0, color = "blue")),
            roundTrip("""{ "shape": { "side": 2.0, "color": "blue" } }"""),
        )
    }
}
