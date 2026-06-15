package io.github.nomisrev.openapi.parser.serializer

import io.github.nomisrev.openapi.parser.Schema
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ExclusiveLimitSerializerTest {

    @Test
    fun `json minimum and maximum deserialize`() {
        val schema = Schema.fromJson("""{"type":"number","minimum":0.0,"maximum":100.0}""")
        assertEquals(0.0, schema.minimum)
        assertEquals(100.0, schema.maximum)
    }

    @Test
    fun `json exclusiveMinimum and exclusiveMaximum as booleans deserialize`() {
        val schema =
            Schema.fromJson("""{"type":"number","minimum":0.0,"exclusiveMinimum":true,"maximum":100.0,"exclusiveMaximum":false}""")
        assertEquals(Schema.ExclusiveLimit.BooleanValue(true), schema.exclusiveMinimum)
        assertEquals(Schema.ExclusiveLimit.BooleanValue(false), schema.exclusiveMaximum)
    }

    @Test
    fun `json exclusiveMinimum and exclusiveMaximum as numbers deserialize`() {
        val schema = Schema.fromJson("""{"type":"number","exclusiveMinimum":0.0,"exclusiveMaximum":100.0}""")
        assertEquals(Schema.ExclusiveLimit.NumberValue(0.0), schema.exclusiveMinimum)
        assertEquals(Schema.ExclusiveLimit.NumberValue(100.0), schema.exclusiveMaximum)
    }

    @Test
    fun `yaml exclusiveMinimum and exclusiveMaximum as booleans deserialize`() {
        val schema = Schema.fromYaml(
            """
            type: number
            minimum: 0.0
            exclusiveMinimum: true
            maximum: 100.0
            exclusiveMaximum: false
            """.trimIndent()
        )
        assertEquals(Schema.ExclusiveLimit.BooleanValue(true), schema.exclusiveMinimum)
        assertEquals(Schema.ExclusiveLimit.BooleanValue(false), schema.exclusiveMaximum)
    }

    @Test
    fun `yaml exclusiveMinimum and exclusiveMaximum as numbers deserialize`() {
        val schema = Schema.fromYaml(
            """
            type: number
            exclusiveMinimum: 0.0
            exclusiveMaximum: 100.0
            """.trimIndent()
        )
        assertEquals(Schema.ExclusiveLimit.NumberValue(0.0), schema.exclusiveMinimum)
        assertEquals(Schema.ExclusiveLimit.NumberValue(100.0), schema.exclusiveMaximum)
    }

    @Test
    fun `json exclusive bounds preserve boolean values when encoded`() {
        val schema =
            Schema.fromJson("""{"type":"number","minimum":0.0,"exclusiveMinimum":true,"maximum":100.0,"exclusiveMaximum":false}""")
        val json = Json.parseToJsonElement(schema.toString()).jsonObject
        assertEquals(true, (json["exclusiveMinimum"] as JsonPrimitive).booleanOrNull)
        assertEquals(false, (json["exclusiveMaximum"] as JsonPrimitive).booleanOrNull)
    }

    @Test
    fun `json exclusive bounds preserve number values when encoded`() {
        val schema = Schema.fromJson("""{"type":"number","exclusiveMinimum":0.0,"exclusiveMaximum":100.0}""")
        val json = Json.parseToJsonElement(schema.toString()).jsonObject
        assertEquals(0.0, (json["exclusiveMinimum"] as JsonPrimitive).doubleOrNull)
        assertEquals(100.0, (json["exclusiveMaximum"] as JsonPrimitive).doubleOrNull)
    }

    @Test
    fun `json multipleOf deserializes`() {
        val schema = Schema.fromJson("""{"type":"number","multipleOf":5.0}""")
        assertEquals(5.0, schema.multipleOf)
    }


    @Test
    fun `json string exclusiveMinimum throws SerializationException`() {
        assertFailsWith<SerializationException> {
            Schema.fromJson("""{"exclusiveMinimum":"true"}""")
        }
    }

    @Test
    fun `json object exclusiveMinimum throws SerializationException`() {
        assertFailsWith<SerializationException> {
            Schema.fromJson("""{"exclusiveMinimum":{}}""")
        }
    }

    @Test
    fun `json array exclusiveMaximum throws SerializationException`() {
        assertFailsWith<SerializationException> {
            Schema.fromJson("""{"exclusiveMaximum":[]}""")
        }
    }

    // ── YAML invalid values ───────────────────────────────────────────────────

    @Test
    fun `yaml string exclusiveMinimum throws SerializationException`() {
        assertFailsWith<SerializationException> {
            Schema.fromYaml("exclusiveMinimum: not-a-limit")
        }
    }

    @Test
    fun `yaml mapping exclusiveMinimum throws SerializationException`() {
        assertFailsWith<SerializationException> {
            Schema.fromYaml(
                """
                exclusiveMinimum:
                  value: true
                """.trimIndent()
            )
        }
    }

    @Test
    fun `yaml sequence exclusiveMaximum throws SerializationException`() {
        assertFailsWith<SerializationException> {
            Schema.fromYaml(
                """
                exclusiveMaximum:
                  - true
                """.trimIndent()
            )
        }
    }
}
