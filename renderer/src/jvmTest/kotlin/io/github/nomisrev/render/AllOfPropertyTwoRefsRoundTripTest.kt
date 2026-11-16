package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.allof.`property`.two.refs.Level
import io.github.nomisrev.render.test.client.allof.`property`.two.refs.ProductVoice
import io.github.nomisrev.render.test.client.allof.`property`.two.refs.Thing
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * A property two `allOf` members declare with unrelated `$ref`s accepts what both accept: the shared enum, only the
 * values of the smaller enum, and an object with the properties of both. Uses the goldens under
 * `kotlinTestData/client/allof-property-two-refs` directly.
 */
class AllOfPropertyTwoRefsRoundTripTest {
    private val json = Json

    private val valid = """
        { "id": "c1", "product": "VOICE-CALL", "level": "high", "links": { "rename": "r", "configure": "c", "ignore": "i" } }
    """.trimIndent()

    @Test
    fun decodesAndEncodesTheIntersection() {
        val decoded = json.decodeFromString<Thing>(valid)
        assertEquals(
            Thing(id = "c1", product = ProductVoice.VOICECALL, level = Level.High, links = Thing.Links("r", "c", "i")),
            decoded,
        )
        assertEquals(json.parseToJsonElement(valid), json.parseToJsonElement(json.encodeToString(decoded)))
    }

    @Test
    fun rejectsAValueOnlyTheLargerEnumAccepts() {
        assertFailsWith<SerializationException> {
            json.decodeFromString<Thing>(valid.replace(""""level": "high"""", """"level": "critical""""))
        }
    }
}
