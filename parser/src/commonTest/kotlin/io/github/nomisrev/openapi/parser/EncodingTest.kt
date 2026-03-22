package io.github.nomisrev.openapi.parser

import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EncodingTest {

    // ── Helper ────────────────────────────────────────────────────────────────

    private fun decode(json: String): Encoding =
        OpenAPI.Json.decodeFromString(Encoding.serializer(), json)

    // ── Content type ──────────────────────────────────────────────────────────

    @Test
    fun `content type deserializes`() {
        val encoding = decode("""{"contentType":"application/json"}""")
        assertEquals("application/json", encoding.contentType)
        assertTrue(encoding.headers.isEmpty())
        assertEquals(null, encoding.style)
        assertEquals(false, encoding.explode)
        assertEquals(false, encoding.allowReserved)
    }

    @Test
    fun `comma separated content type deserializes`() {
        val encoding = decode("""{"contentType":"image/png, image/jpeg"}""")
        assertEquals("image/png, image/jpeg", encoding.contentType)
    }

    // ── Headers ───────────────────────────────────────────────────────────────

    @Test
    fun `headers with ref deserializes`() {
        val json = """
            {
              "contentType": "application/octet-stream",
              "headers": {
                "Content-Disposition": {
                  "${"$"}ref": "#/components/headers/ContentDisposition"
                }
              }
            }
        """.trimIndent()

        val encoding = decode(json)
        assertEquals(1, encoding.headers.size)
        val header = assertIs<ReferenceOr.Reference>(encoding.headers["Content-Disposition"])
        assertEquals("#/components/headers/ContentDisposition", header.ref)
    }

    @Test
    fun `headers with inline header deserializes`() {
        val json = """
            {
              "contentType": "text/plain",
              "headers": {
                "X-Trace-Id": {
                  "required": true,
                  "schema": { "type": "string" }
                }
              }
            }
        """.trimIndent()

        val encoding = decode(json)
        val header = assertIs<ReferenceOr.Value<Header>>(encoding.headers["X-Trace-Id"])
        assertEquals(true, header.value.required)
        val schema = assertIs<ReferenceOr.Value<Schema>>(header.value.schema)
        assertEquals(Schema.Type.Basic.String, (schema.value.type ?: error("Expected schema type")) as Schema.Type.Basic)
    }

    // ── Style, explode, allowReserved ────────────────────────────────────────

    @Test
    fun `style form defaults explode to true`() {
        val encoding = decode("""{"contentType":"text/plain","style":"form"}""")
        assertEquals("form", encoding.style)
        assertEquals(true, encoding.explode)
    }

    @Test
    fun `non-form style defaults explode to false`() {
        val encoding = decode("""{"contentType":"text/plain","style":"simple"}""")
        assertEquals("simple", encoding.style)
        assertEquals(false, encoding.explode)
    }

    @Test
    fun `explicit explode overrides style default`() {
        val encoding = decode("""{"contentType":"text/plain","style":"form","explode":false}""")
        assertEquals("form", encoding.style)
        assertEquals(false, encoding.explode)
    }

    @Test
    fun `allow reserved deserializes`() {
        val encoding = decode("""{"contentType":"text/plain","allowReserved":true}""")
        assertEquals(true, encoding.allowReserved)
    }

    // ── Extensions ────────────────────────────────────────────────────────────

    @Test
    fun `extensions deserialize`() {
        val json = """
            {
              "contentType": "application/json",
              "x-max-size": 1024,
              "x-binary": true
            }
        """.trimIndent()

        val encoding = decode(json)
        assertEquals(2, encoding.extensions.size)
        assertEquals(JsonPrimitive(1024), encoding.extensions["x-max-size"])
        assertEquals(JsonPrimitive(true), encoding.extensions["x-binary"])
    }

    // ── Unknown fields ────────────────────────────────────────────────────────

    @Test
    fun `unknown fields are silently ignored`() {
        val json = """
            {
              "contentType": "application/json",
              "unknownField": "ignored",
              "anotherUnknown": 42
            }
        """.trimIndent()

        val encoding = decode(json)
        assertEquals("application/json", encoding.contentType)
        assertTrue(encoding.extensions.isEmpty())
    }

    // ── YAML ──────────────────────────────────────────────────────────────────

    @Test
    fun `yaml encoding with content type and style deserializes`() {
        val yaml = """
            contentType: text/plain
            style: form
            allowReserved: false
        """.trimIndent()

        val encoding = OpenAPI.Yaml.decodeFromString(Encoding.serializer(), yaml)
        assertEquals("text/plain", encoding.contentType)
        assertEquals("form", encoding.style)
        assertEquals(true, encoding.explode)
        assertEquals(false, encoding.allowReserved)
    }

    @Test
    fun `yaml encoding with headers deserializes`() {
        val yaml = """
            contentType: application/octet-stream
            headers:
              Content-Disposition:
                ${"$"}ref: '#/components/headers/ContentDisposition'
        """.trimIndent()

        val encoding = OpenAPI.Yaml.decodeFromString(Encoding.serializer(), yaml)
        val header = assertIs<ReferenceOr.Reference>(encoding.headers["Content-Disposition"])
        assertEquals("#/components/headers/ContentDisposition", header.ref)
    }

    @Test
    fun `yaml encoding with extension deserializes`() {
        val yaml = """
            contentType: application/json
            x-team: api-platform
        """.trimIndent()

        val encoding = OpenAPI.Yaml.decodeFromString(Encoding.serializer(), yaml)
        assertEquals("application/json", encoding.contentType)
        assertEquals(JsonPrimitive("api-platform"), encoding.extensions["x-team"])
        assertNotNull(encoding.extensions["x-team"])
    }
}
