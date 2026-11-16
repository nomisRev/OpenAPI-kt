package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.allof.additional.properties.Icon
import io.github.nomisrev.render.test.client.allof.additional.properties.Level
import io.github.nomisrev.render.test.client.allof.additional.properties.Thing
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * A closed `allOf` of a string enum is that enum: a known value is the `Icon` case of the union next to it, any other
 * string the `String` case. Uses the goldens under `kotlinTestData/client/allof-additional-properties` directly.
 */
class AllOfAdditionalPropertiesRoundTripTest {
    private val json = Json

    private fun roundTrip(text: String): Thing {
        val decoded = json.decodeFromString<Thing>(text)
        assertEquals(json.parseToJsonElement(text), json.parseToJsonElement(json.encodeToString(decoded)))
        return decoded
    }

    @Test
    fun aKnownIconIsTheEnumCase() {
        assertEquals(
            Thing(uri = Thing.Uri.CaseIcon(Icon.IconBitcoin), level = Level.High),
            roundTrip("""{ "uri": "icon://bitcoin", "level": "high" }"""),
        )
    }

    @Test
    fun anyOtherStringIsTheStringCase() {
        assertEquals(Thing(uri = Thing.Uri.CaseString("https://example.com")), roundTrip("""{ "uri": "https://example.com" }"""))
        assertEquals(Thing(uri = Thing.Uri.CaseString("icon://unknown")), roundTrip("""{ "uri": "icon://unknown" }"""))
    }
}
