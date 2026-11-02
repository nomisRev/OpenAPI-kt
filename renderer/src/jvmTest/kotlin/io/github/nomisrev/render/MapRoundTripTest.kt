package io.github.nomisrev.render

import io.github.nomisrev.render.test.map.`property`.Holder
import io.github.nomisrev.render.test.map.`property`.Labels as PropertyLabels
import io.github.nomisrev.render.test.map.top.level.primitive.Labels
import io.github.nomisrev.render.test.map.top.level.ref.values.Pet
import io.github.nomisrev.render.test.map.top.level.ref.values.Pets
import io.github.nomisrev.render.test.map.union.case.Setting
import io.github.nomisrev.render.test.object_.nullable.anyof.top.level.schema.Metadata
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * A `type: object` schema with only `additionalProperties: <schema>` is a JSON object
 * `{"a": "x"}` on the wire, so its generated model must decode from and encode to that object.
 * Uses the generated goldens under `kotlinTestData/` directly.
 */
class MapRoundTripTest {
    private val json = Json

    @Test
    fun topLevelMapIsAJsonObject() {
        val wire = """{"a":"x","b":"y"}"""
        val labels = Labels(mapOf("a" to "x", "b" to "y"))
        assertEquals(labels, json.decodeFromString<Labels>(wire))
        assertEquals(wire, json.encodeToString(labels))
    }

    @Test
    fun nullableAnyOfTopLevelMapIsAJsonObject() {
        val wire = """{"a":"x","b":"y"}"""
        val metadata = Metadata(mapOf("a" to "x", "b" to "y"))
        assertEquals(metadata, json.decodeFromString<Metadata>(wire))
        assertEquals(wire, json.encodeToString(metadata))
        assertEquals(null, json.decodeFromString<Metadata?>("null"))
    }

    @Test
    fun topLevelMapWithReferencedValuesIsAJsonObject() {
        val wire = """{"a":{"name":"x"},"b":{"name":"y","age":2}}"""
        val pets = Pets(mapOf("a" to Pet("x"), "b" to Pet("y", 2)))
        assertEquals(pets, json.decodeFromString<Pets>(wire))
        assertEquals(wire, json.encodeToString(pets))
    }

    @Test
    fun inlineAndReferencedMapPropertiesAreJsonObjects() {
        val wire = """{"inline":{"a":1,"b":2},"labels":{"a":"x","b":"y"}}"""
        val holder = Holder(mapOf("a" to 1, "b" to 2), PropertyLabels(mapOf("a" to "x", "b" to "y")))
        assertEquals(holder, json.decodeFromString<Holder>(wire))
        assertEquals(wire, json.encodeToString(holder))
    }

    @Test
    fun mapUnionCaseIsAJsonObject() {
        val wire = """{"a":1,"b":2}"""
        val setting: Setting = Setting.CaseIntsMap(mapOf("a" to 1, "b" to 2))
        assertEquals(setting, json.decodeFromString<Setting>(wire))
        assertEquals(wire, json.encodeToString(setting))
        assertEquals(Setting.CaseString("x"), json.decodeFromString<Setting>("\"x\""))
    }
}
