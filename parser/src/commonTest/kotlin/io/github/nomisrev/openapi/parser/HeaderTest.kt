package io.github.nomisrev.openapi.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class HeaderTest {

    // ── Helper ────────────────────────────────────────────────────────────────

    private fun decode(json: String): Header =
        OpenAPI.Json.decodeFromString(Header.serializer(), json)

    // ── Minimal ───────────────────────────────────────────────────────────────

    @Test
    fun `empty header object deserializes to defaults`() {
        val json = """{}"""
        val header = decode(json)
        assertNull(header.description)
        assertNull(header.required)
        assertNull(header.deprecated)
        assertNull(header.allowEmptyValue)
        assertNull(header.explode)
        assertNull(header.example)
        assertNull(header.examples)
        assertNull(header.schema)
    }

    // ── Required ──────────────────────────────────────────────────────────────

    @Test
    fun `required header deserializes`() {
        val json = """
            {
              "required": true,
              "description": "An API key",
              "schema": {"type": "string"}
            }
        """.trimIndent()

        val header = decode(json)
        assertEquals(true, header.required)
        assertEquals("An API key", header.description)
        val schemaValue = assertIs<ReferenceOr.Value<Schema>>(header.schema)
        assertEquals(Schema.Type.Basic.String, schemaValue.value.type as Schema.Type.Basic)
    }

    @Test
    fun `non-required header deserializes`() {
        val json = """
            {
              "required": false,
              "description": "Optional tracking header"
            }
        """.trimIndent()

        val header = decode(json)
        assertEquals(false, header.required)
        assertEquals("Optional tracking header", header.description)
    }

    // ── Schema ────────────────────────────────────────────────────────────────

    @Test
    fun `header with inline schema deserializes`() {
        val json = """
            {
              "schema": {"type": "integer"}
            }
        """.trimIndent()

        val header = decode(json)
        val schemaValue = assertIs<ReferenceOr.Value<Schema>>(header.schema)
        assertEquals(Schema.Type.Basic.Integer, schemaValue.value.type as Schema.Type.Basic)
    }

    @Test
    fun `header with schema ref deserializes`() {
        val json = """
            {
              "schema": {"${"$"}ref": "#/components/schemas/ApiKey"}
            }
        """.trimIndent()

        val header = decode(json)
        val ref = assertIs<ReferenceOr.Reference>(header.schema)
        assertEquals("#/components/schemas/ApiKey", ref.ref)
    }

    @Test
    fun `header with string schema and constraints deserializes`() {
        val json = """
            {
              "required": true,
              "schema": {
                "type": "string",
                "minLength": 1,
                "maxLength": 256
              }
            }
        """.trimIndent()

        val header = decode(json)
        assertEquals(true, header.required)
        val schemaValue = assertIs<ReferenceOr.Value<Schema>>(header.schema)
        val schema = schemaValue.value
        assertEquals(Schema.Type.Basic.String, schema.type as Schema.Type.Basic)
        assertEquals(1, schema.minLength)
        assertEquals(256, schema.maxLength)
    }

    // ── Deprecated ───────────────────────────────────────────────────────────

    @Test
    fun `deprecated header deserializes`() {
        val json = """
            {
              "deprecated": true,
              "description": "Use X-New-Header instead",
              "schema": {"type": "string"}
            }
        """.trimIndent()

        val header = decode(json)
        assertEquals(true, header.deprecated)
        assertEquals("Use X-New-Header instead", header.description)
    }

    // ── Explode ───────────────────────────────────────────────────────────────

    @Test
    fun `header with explode deserializes`() {
        val json = """
            {
              "explode": true,
              "schema": {"type": "string"}
            }
        """.trimIndent()

        val header = decode(json)
        assertEquals(true, header.explode)
    }

    // ── Example ───────────────────────────────────────────────────────────────

    @Test
    fun `header with single example value deserializes`() {
        val json = """
            {
              "schema": {"type": "string"},
              "example": "Bearer eyJhbGci..."
            }
        """.trimIndent()

        val header = decode(json)
        val example = assertIs<ExampleValue.Single>(assertNotNull(header.example))
        assertEquals("Bearer eyJhbGci...", example.value)
    }

    // ── Examples map ──────────────────────────────────────────────────────────

    @Test
    fun `header with named examples deserializes`() {
        val json = """
            {
              "schema": {"type": "string"},
              "examples": {
                "bearer": {
                  "summary": "Bearer token example",
                  "value": "Bearer eyJhbGci..."
                },
                "apikey": {
                  "summary": "API key example",
                  "value": "my-api-key-12345"
                }
              }
            }
        """.trimIndent()

        val header = decode(json)
        val examples = assertNotNull(header.examples)
        assertEquals(2, examples.size)

        val bearer = assertIs<ReferenceOr.Value<Example>>(examples["bearer"])
        assertEquals("Bearer token example", bearer.value.summary)

        val apikey = assertIs<ReferenceOr.Value<Example>>(examples["apikey"])
        assertEquals("API key example", apikey.value.summary)
    }

    @Test
    fun `header with example ref deserializes`() {
        val json = """
            {
              "schema": {"type": "string"},
              "examples": {
                "tokenExample": {
                  "${"$"}ref": "#/components/examples/BearerToken"
                }
              }
            }
        """.trimIndent()

        val header = decode(json)
        val examples = assertNotNull(header.examples)
        val tokenExample = assertIs<ReferenceOr.Reference>(examples["tokenExample"])
        assertEquals("#/components/examples/BearerToken", tokenExample.ref)
    }

    // ── Unknown fields ────────────────────────────────────────────────────────

    @Test
    fun `unknown fields are silently ignored`() {
        val json = """
            {
              "required": true,
              "schema": {"type": "string"},
              "unknownField": "ignored",
              "anotherUnknown": 99
            }
        """.trimIndent()

        val header = decode(json)
        assertEquals(true, header.required)
        val schemaValue = assertIs<ReferenceOr.Value<Schema>>(header.schema)
        assertEquals(Schema.Type.Basic.String, schemaValue.value.type as Schema.Type.Basic)
    }

    // ── YAML ──────────────────────────────────────────────────────────────────

    @Test
    fun `yaml required header with schema deserializes`() {
        val yaml = """
            required: true
            description: Authentication token
            schema:
              type: string
        """.trimIndent()

        val header = OpenAPI.Yaml.decodeFromString(Header.serializer(), yaml)
        assertEquals(true, header.required)
        assertEquals("Authentication token", header.description)
        val schemaValue = assertIs<ReferenceOr.Value<Schema>>(header.schema)
        assertEquals(Schema.Type.Basic.String, schemaValue.value.type as Schema.Type.Basic)
    }

    @Test
    fun `yaml header with schema ref deserializes`() {
        val yaml = """
            schema:
              ${"$"}ref: '#/components/schemas/ApiKey'
        """.trimIndent()

        val header = OpenAPI.Yaml.decodeFromString(Header.serializer(), yaml)
        val ref = assertIs<ReferenceOr.Reference>(header.schema)
        assertEquals("#/components/schemas/ApiKey", ref.ref)
    }

    @Test
    fun `yaml header with examples deserializes`() {
        val yaml = """
            schema:
              type: string
            examples:
              bearer:
                summary: Bearer token
                value: Bearer eyJhbGci...
        """.trimIndent()

        val header = OpenAPI.Yaml.decodeFromString(Header.serializer(), yaml)
        val schemaValue = assertIs<ReferenceOr.Value<Schema>>(header.schema)
        assertEquals(Schema.Type.Basic.String, schemaValue.value.type as Schema.Type.Basic)
        val examples = assertNotNull(header.examples)
        assertEquals(1, examples.size)
        val bearer = assertIs<ReferenceOr.Value<Example>>(examples["bearer"])
        assertEquals("Bearer token", bearer.value.summary)
    }
}
