package io.github.nomisrev.openapi.parser.serializer

import io.github.nomisrev.openapi.parser.AdditionalProperties
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import kotlinx.serialization.SerializationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdditionalPropertiesSerializerTest {

    // ── JSON boolean ──────────────────────────────────────────────────────────

    @Test
    fun `json boolean true deserializes to Allowed true`() {
        val schema = Schema.fromJson("""{"additionalProperties":true}""")
        val ap = assertIs<AdditionalProperties.Allowed>(schema.additionalProperties)
        assertTrue(ap.value)
    }

    @Test
    fun `json boolean false deserializes to Allowed false`() {
        val schema = Schema.fromJson("""{"additionalProperties":false}""")
        val ap = assertIs<AdditionalProperties.Allowed>(schema.additionalProperties)
        assertFalse(ap.value)
    }

    // ── JSON inline schema object ─────────────────────────────────────────────

    @Test
    fun `json inline schema object deserializes to PSchema with Value`() {
        val schema = Schema.fromJson("""{"additionalProperties":{"type":"string"}}""")
        val ap = assertIs<AdditionalProperties.PSchema>(schema.additionalProperties)
        val inner = assertIs<ReferenceOr.Value<Schema>>(ap.value)
        assertEquals(Schema.Type.Basic.String, inner.value.type)
    }

    @Test
    fun `json empty schema object deserializes to PSchema`() {
        val schema = Schema.fromJson("""{"additionalProperties":{}}""")
        assertIs<AdditionalProperties.PSchema>(schema.additionalProperties)
    }

    @Test
    fun `json ref inside additionalProperties deserializes to PSchema with Reference`() {
        val schema = Schema.fromJson(
            """{"additionalProperties":{"${"$"}ref":"#/components/schemas/Foo"}}"""
        )
        val ap = assertIs<AdditionalProperties.PSchema>(schema.additionalProperties)
        val ref = assertIs<ReferenceOr.Reference>(ap.value)
        assertEquals("#/components/schemas/Foo", ref.ref)
    }

    // ── JSON invalid values ───────────────────────────────────────────────────

    @Test
    fun `json string value throws SerializationException`() {
        assertFailsWith<SerializationException> {
            Schema.fromJson("""{"additionalProperties":"not-a-bool"}""")
        }
    }

    @Test
    fun `json number value throws SerializationException`() {
        assertFailsWith<SerializationException> {
            Schema.fromJson("""{"additionalProperties":42}""")
        }
    }

    // ── JSON serialization (roundtrip) ────────────────────────────────────────

    @Test
    fun `Allowed true serializes back to boolean true`() {
        val schema = Schema(additionalProperties = AdditionalProperties.Allowed(true))
        val json = OpenAPI.Json.encodeToString(Schema.serializer(), schema)
        assertTrue(json.contains("\"additionalProperties\""), "Missing key in: $json")
        assertTrue(json.contains("true"), "Expected true in: $json")
    }

    @Test
    fun `Allowed false serializes back to boolean false`() {
        val schema = Schema(additionalProperties = AdditionalProperties.Allowed(false))
        val json = OpenAPI.Json.encodeToString(Schema.serializer(), schema)
        assertTrue(json.contains("\"additionalProperties\""), "Missing key in: $json")
        assertTrue(json.contains("false"), "Expected false in: $json")
    }

    @Test
    fun `PSchema with inline schema serializes back to object`() {
        val schema = Schema(
            additionalProperties = AdditionalProperties.PSchema(
                ReferenceOr.Value(Schema(type = Schema.Type.Basic.Integer))
            )
        )
        val json = OpenAPI.Json.encodeToString(Schema.serializer(), schema)
        assertTrue(json.contains("\"additionalProperties\""), "Missing key in: $json")
        assertTrue(json.contains("\"integer\""), "Expected 'integer' in: $json")
    }

    // ── YAML boolean ──────────────────────────────────────────────────────────

    @Test
    fun `yaml boolean true deserializes to Allowed true`() {
        val schema = Schema.fromYaml("additionalProperties: true")
        val ap = assertIs<AdditionalProperties.Allowed>(schema.additionalProperties)
        assertTrue(ap.value)
    }

    @Test
    fun `yaml boolean false deserializes to Allowed false`() {
        val schema = Schema.fromYaml("additionalProperties: false")
        val ap = assertIs<AdditionalProperties.Allowed>(schema.additionalProperties)
        assertFalse(ap.value)
    }

    // ── YAML inline schema object ─────────────────────────────────────────────

    @Test
    fun `yaml inline schema map deserializes to PSchema with Value`() {
        val yaml = """
            additionalProperties:
              type: string
        """.trimIndent()
        val schema = Schema.fromYaml(yaml)
        val ap = assertIs<AdditionalProperties.PSchema>(schema.additionalProperties)
        val inner = assertIs<ReferenceOr.Value<Schema>>(ap.value)
        assertEquals(Schema.Type.Basic.String, inner.value.type)
    }

    @Test
    fun `yaml ref inside additionalProperties deserializes to PSchema with Reference`() {
        val yaml = """
            additionalProperties:
              ${"$"}ref: "#/components/schemas/Foo"
        """.trimIndent()
        val schema = Schema.fromYaml(yaml)
        val ap = assertIs<AdditionalProperties.PSchema>(schema.additionalProperties)
        val ref = assertIs<ReferenceOr.Reference>(ap.value)
        assertEquals("#/components/schemas/Foo", ref.ref)
    }
}
