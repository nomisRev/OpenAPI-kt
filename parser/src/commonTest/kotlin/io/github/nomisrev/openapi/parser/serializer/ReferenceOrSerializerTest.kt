package io.github.nomisrev.openapi.parser.serializer

import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ReferenceOrSerializerTest {

    // ── JSON ──────────────────────────────────────────────────────────────────

    @Test
    fun `json ref object deserializes to Reference`() {
        val json = """{"${"$"}ref":"#/components/schemas/Foo"}"""
        val result = OpenAPI.Json.decodeFromString(
            ReferenceOr.serializer(Schema.serializer()),
            json,
        )
        val ref = assertIs<ReferenceOr.Reference>(result)
        assertEquals("#/components/schemas/Foo", ref.ref)
    }

    @Test
    fun `json recursiveRef object deserializes to Reference`() {
        val json = """{"${"$"}recursiveRef":"#"}"""
        val result = OpenAPI.Json.decodeFromString(
            ReferenceOr.serializer(Schema.serializer()),
            json,
        )
        val ref = assertIs<ReferenceOr.Reference>(result)
        assertEquals("#", ref.ref)
    }

    @Test
    fun `json inline schema object deserializes to Value`() {
        val json = """{"type":"string"}"""
        val result = OpenAPI.Json.decodeFromString(
            ReferenceOr.serializer(Schema.serializer()),
            json,
        )
        val value = assertIs<ReferenceOr.Value<Schema>>(result)
        assertEquals(Schema.Type.Basic.String, value.value.type)
    }

    @Test
    fun `json inline schema with multiple fields deserializes to Value`() {
        val json = """{"type":"object","title":"Pet"}"""
        val result = OpenAPI.Json.decodeFromString(
            ReferenceOr.serializer(Schema.serializer()),
            json,
        )
        val value = assertIs<ReferenceOr.Value<Schema>>(result)
        assertEquals(Schema.Type.Basic.Object, value.value.type)
        assertEquals("Pet", value.value.title)
    }

    @Test
    fun `json nested reference within properties deserializes correctly`() {
        val json = """{"type":"object","properties":{"id":{"${"$"}ref":"#/components/schemas/Id"}}}"""
        val result = OpenAPI.Json.decodeFromString(
            ReferenceOr.serializer(Schema.serializer()),
            json,
        )
        val value = assertIs<ReferenceOr.Value<Schema>>(result)
        val idProp = assertIs<ReferenceOr.Reference>(value.value.properties?.get("id"))
        assertEquals("#/components/schemas/Id", idProp.ref)
    }

    @Test
    fun `json Reference serializes back to ref object`() {
        val ref = ReferenceOr.Reference("#/components/schemas/Foo")
        val json = OpenAPI.Json.encodeToString(
            ReferenceOr.serializer(Schema.serializer()),
            ref,
        )
        assertTrue(json.contains("\"\$ref\""), "Expected '\$ref' key in: $json")
        assertTrue(json.contains("#/components/schemas/Foo"), "Expected ref value in: $json")
    }

    @Test
    fun `json Value serializes back to inline object`() {
        val value: ReferenceOr<Schema> = ReferenceOr.Value(Schema(type = Schema.Type.Basic.String))
        val json = OpenAPI.Json.encodeToString(
            ReferenceOr.serializer(Schema.serializer()),
            value,
        )
        // The encoded JSON should contain "type":"string" and no $ref key
        assertTrue(json.contains("\"type\""))
        assertFalse(json.contains("\$ref"))
    }

    // ── YAML ──────────────────────────────────────────────────────────────────

    @Test
    fun `yaml ref scalar deserializes to Reference`() {
        val yaml = "\$ref: \"#/components/schemas/Foo\""
        val result = OpenAPI.Yaml.decodeFromString(
            ReferenceOr.serializer(Schema.serializer()),
            yaml,
        )
        val ref = assertIs<ReferenceOr.Reference>(result)
        assertEquals("#/components/schemas/Foo", ref.ref)
    }

    @Test
    fun `yaml recursiveRef deserializes to Reference`() {
        val yaml = "\$recursiveRef: \"#\""
        val result = OpenAPI.Yaml.decodeFromString(
            ReferenceOr.serializer(Schema.serializer()),
            yaml,
        )
        val ref = assertIs<ReferenceOr.Reference>(result)
        assertEquals("#", ref.ref)
    }

    @Test
    fun `yaml inline schema map deserializes to Value`() {
        val yaml = "type: string"
        val result = OpenAPI.Yaml.decodeFromString(
            ReferenceOr.serializer(Schema.serializer()),
            yaml,
        )
        val value = assertIs<ReferenceOr.Value<Schema>>(result)
        assertEquals(Schema.Type.Basic.String, value.value.type)
    }

    @Test
    fun `yaml inline schema with title deserializes to Value`() {
        val yaml = """
            type: object
            title: Pet
        """.trimIndent()
        val result = OpenAPI.Yaml.decodeFromString(
            ReferenceOr.serializer(Schema.serializer()),
            yaml,
        )
        val value = assertIs<ReferenceOr.Value<Schema>>(result)
        assertEquals(Schema.Type.Basic.Object, value.value.type)
        assertEquals("Pet", value.value.title)
    }
}
