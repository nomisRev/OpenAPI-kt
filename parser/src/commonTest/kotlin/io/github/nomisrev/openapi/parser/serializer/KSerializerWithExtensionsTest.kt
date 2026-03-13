package io.github.nomisrev.openapi.parser.serializer

import io.github.nomisrev.openapi.parser.MediaType
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Operation
import io.github.nomisrev.openapi.parser.Response
import io.github.nomisrev.openapi.parser.Responses
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Tests for [io.github.nomisrev.openapi.parser.KSerializerWithExtensions].
 *
 * The abstract class is exercised via its concrete implementations.
 * [MediaType] is used for most tests (minimal required fields), and
 * [Operation] is used for additional coverage.
 */
class KSerializerWithExtensionsTest {

    // ── JSON — extensions are preserved ──────────────────────────────────────

    @Test
    fun `json single x- extension is preserved on MediaType`() {
        val json = """{"x-internal":"true"}"""
        val mediaType = OpenAPI.Json.decodeFromString(MediaType.serializer(), json)
        assertEquals(JsonPrimitive("true"), mediaType.extensions["x-internal"])
    }

    @Test
    fun `json multiple x- extensions are all preserved on MediaType`() {
        val json = """{"x-foo":"bar","x-baz":"qux"}"""
        val mediaType = OpenAPI.Json.decodeFromString(MediaType.serializer(), json)
        assertEquals(JsonPrimitive("bar"), mediaType.extensions["x-foo"])
        assertEquals(JsonPrimitive("qux"), mediaType.extensions["x-baz"])
    }

    @Test
    fun `json x- extensions with various value types are preserved`() {
        val json = """{"x-bool":true,"x-num":42,"x-null":null}"""
        val mediaType = OpenAPI.Json.decodeFromString(MediaType.serializer(), json)
        assertEquals(JsonPrimitive(true), mediaType.extensions["x-bool"])
        assertEquals(JsonPrimitive(42), mediaType.extensions["x-num"])
    }

    // ── JSON — object without extensions ─────────────────────────────────────

    @Test
    fun `json object without any x- fields has empty extensions map`() {
        val json = """{"schema":{"type":"string"}}"""
        val mediaType = OpenAPI.Json.decodeFromString(MediaType.serializer(), json)
        assertTrue(mediaType.extensions.isEmpty())
        assertEquals(Schema.Type.Basic.String, (mediaType.schema as? ReferenceOr.Value<Schema>)?.value?.type)
    }

    @Test
    fun `json empty object produces MediaType with empty extensions`() {
        val json = """{}"""
        val mediaType = OpenAPI.Json.decodeFromString(MediaType.serializer(), json)
        assertTrue(mediaType.extensions.isEmpty())
    }

    // ── JSON — extensions do not bleed into the model fields ─────────────────

    @Test
    fun `json x- keys are not reflected in normal fields`() {
        val json = """{"x-custom":"value","schema":{"type":"integer"}}"""
        val mediaType = OpenAPI.Json.decodeFromString(MediaType.serializer(), json)
        assertEquals(1, mediaType.extensions.size)
        val schema = assertSchemaType(mediaType, Schema.Type.Basic.Integer)
        assertEquals(Schema.Type.Basic.Integer, schema.type)
    }

    // ── JSON — extensions survive a full roundtrip ────────────────────────────

    @Test
    fun `json extensions survive encode-decode roundtrip on MediaType`() {
        val original = MediaType(
            schema = ReferenceOr.Value(Schema(type = Schema.Type.Basic.String)),
            extensions = mapOf("x-hint" to JsonPrimitive("useful")),
        )
        val json = OpenAPI.Json.encodeToString(MediaType.serializer(), original)
        val decoded = OpenAPI.Json.decodeFromString(MediaType.serializer(), json)
        assertEquals(original.extensions, decoded.extensions)
        assertEquals(original.schema, decoded.schema)
    }

    @Test
    fun `json extensions field is merged into top-level json object on serialization`() {
        val mediaType = MediaType(
            extensions = mapOf("x-vendor" to JsonPrimitive("info")),
        )
        val json = OpenAPI.Json.encodeToString(MediaType.serializer(), mediaType)
        // extensions must appear at the top level, NOT nested under an "extensions" key
        assertTrue(json.contains("\"x-vendor\""), "Expected top-level x-vendor in: $json")
        assertFalse(json.contains("\"extensions\""), "Should not have nested 'extensions' key in: $json")
    }

    // ── JSON — Operation (another user of KSerializerWithExtensions) ──────────

    @Test
    fun `json operation x- extensions are preserved`() {
        val minimalResponses = """{"200":{"description":"OK"}}"""
        val json = """{"responses":$minimalResponses,"x-internal-op":"true"}"""
        val op = OpenAPI.Json.decodeFromString(Operation.serializer(), json)
        assertEquals(JsonPrimitive("true"), op.extensions["x-internal-op"])
    }

    @Test
    fun `json operation without extensions has empty extensions map`() {
        val json = """{"responses":{"200":{"description":"OK"}}}"""
        val op = OpenAPI.Json.decodeFromString(Operation.serializer(), json)
        assertTrue(op.extensions.isEmpty())
    }

    @Test
    fun `json operation multiple extensions are all captured`() {
        val json = """{"responses":{"200":{"description":"OK"}},"x-a":"1","x-b":"2"}"""
        val op = OpenAPI.Json.decodeFromString(Operation.serializer(), json)
        assertEquals(JsonPrimitive("1"), op.extensions["x-a"])
        assertEquals(JsonPrimitive("2"), op.extensions["x-b"])
    }

    // ── YAML — extensions ────────────────────────────────────────────────────

    @Test
    fun `yaml object without x- fields decodes normally`() {
        val yaml = "schema:\n  type: string"
        val mediaType = OpenAPI.Yaml.decodeFromString(MediaType.serializer(), yaml)
        val schema = assertIs<ReferenceOr.Value<Schema>>(mediaType.schema)
        assertEquals(Schema.Type.Basic.String, schema.value.type)
    }

    @Test
    fun `yaml string extension is preserved as JsonPrimitive string`() {
        val yaml = "x-team: api-platform"
        val mediaType = OpenAPI.Yaml.decodeFromString(MediaType.serializer(), yaml)
        assertEquals(JsonPrimitive("api-platform"), mediaType.extensions["x-team"])
    }

    @Test
    fun `yaml boolean extension is preserved as JsonPrimitive boolean`() {
        val yaml = "x-enabled: true"
        val mediaType = OpenAPI.Yaml.decodeFromString(MediaType.serializer(), yaml)
        assertEquals(JsonPrimitive(true), mediaType.extensions["x-enabled"])
    }

    @Test
    fun `yaml integer extension is preserved as JsonPrimitive number`() {
        val yaml = "x-version: 2"
        val mediaType = OpenAPI.Yaml.decodeFromString(MediaType.serializer(), yaml)
        assertEquals(JsonPrimitive(2L), mediaType.extensions["x-version"])
    }

    @Test
    fun `yaml double extension is preserved as JsonPrimitive number`() {
        val yaml = "x-ratio: 1.5"
        val mediaType = OpenAPI.Yaml.decodeFromString(MediaType.serializer(), yaml)
        assertEquals(JsonPrimitive(1.5), mediaType.extensions["x-ratio"])
    }

    @Test
    fun `yaml null extension is preserved as JsonPrimitive null`() {
        val yaml = "x-deprecated: null"
        val mediaType = OpenAPI.Yaml.decodeFromString(MediaType.serializer(), yaml)
        assertEquals(JsonPrimitive(null as String?), mediaType.extensions["x-deprecated"])
    }

    @Test
    fun `yaml object extension is preserved as JsonObject`() {
        val yaml = """
            x-meta:
              owner: platform
              version: 3
        """.trimIndent()
        val mediaType = OpenAPI.Yaml.decodeFromString(MediaType.serializer(), yaml)
        val obj = assertIs<kotlinx.serialization.json.JsonObject>(mediaType.extensions["x-meta"])
        assertEquals(JsonPrimitive("platform"), obj["owner"])
        assertEquals(JsonPrimitive(3L), obj["version"])
    }

    @Test
    fun `yaml array extension is preserved as JsonArray`() {
        val yaml = """
            x-tags:
              - payments
              - internal
        """.trimIndent()
        val mediaType = OpenAPI.Yaml.decodeFromString(MediaType.serializer(), yaml)
        val arr = assertIs<kotlinx.serialization.json.JsonArray>(mediaType.extensions["x-tags"])
        assertEquals(listOf(JsonPrimitive("payments"), JsonPrimitive("internal")), arr.toList())
    }

    @Test
    fun `yaml multiple extensions are all captured`() {
        val yaml = """
            x-team: platform
            x-version: 4
            x-internal: false
        """.trimIndent()
        val mediaType = OpenAPI.Yaml.decodeFromString(MediaType.serializer(), yaml)
        assertEquals(JsonPrimitive("platform"), mediaType.extensions["x-team"])
        assertEquals(JsonPrimitive(4L), mediaType.extensions["x-version"])
        assertEquals(JsonPrimitive(false), mediaType.extensions["x-internal"])
    }

    @Test
    fun `yaml extensions do not bleed into normal model fields`() {
        val yaml = """
            x-custom: value
            schema:
              type: integer
        """.trimIndent()
        val mediaType = OpenAPI.Yaml.decodeFromString(MediaType.serializer(), yaml)
        assertEquals(1, mediaType.extensions.size)
        assertSchemaType(mediaType, Schema.Type.Basic.Integer)
    }

    // ─────────────────────────────────────────────────────────────────────────

    private fun assertSchemaType(mediaType: MediaType, expected: Schema.Type): Schema {
        val value = mediaType.schema as? ReferenceOr.Value<Schema>
            ?: error("Expected ReferenceOr.Value<Schema>, got ${mediaType.schema}")
        assertEquals(expected, value.value.type)
        return value.value
    }
}

// assertIs is used inline — bring into scope explicitly for readability
@Suppress("NOTHING_TO_INLINE")
private inline fun <reified T : Any> assertIs(value: Any?): T =
    kotlin.test.assertIs(value)
