package io.github.nomisrev.openapi.parser

import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RequestBodyTest {

    // ── Helper ────────────────────────────────────────────────────────────────

    private fun decode(json: String): RequestBody =
        OpenAPI.Json.decodeFromString(RequestBody.serializer(), json)

    // ── Required body with content map ───────────────────────────────────────

    @Test
    fun `required request body with content map deserializes`() {
        val json = """
            {
              "description": "Payload for creating a pet",
              "required": true,
              "content": {
                "application/json": {
                  "schema": {
                    "type": "object",
                    "properties": {
                      "name": {"type": "string"}
                    },
                    "required": ["name"]
                  }
                },
                "application/xml": {
                  "schema": {"${"$"}ref": "#/components/schemas/Pet"}
                }
              }
            }
        """.trimIndent()

        val body = decode(json)
        assertEquals("Payload for creating a pet", body.description)
        assertTrue(body.required)
        assertEquals(2, body.content.size)

        val jsonMedia = assertNotNull(body.content["application/json"])
        val jsonSchema = assertIs<ReferenceOr.Value<Schema>>(jsonMedia.schema).value
        assertEquals(Schema.Type.Basic.Object, (jsonSchema.type ?: error("Expected schema type")) as Schema.Type.Basic)
        assertEquals(listOf("name"), jsonSchema.required)

        val xmlMedia = assertNotNull(body.content["application/xml"])
        val xmlSchema = assertIs<ReferenceOr.Reference>(xmlMedia.schema)
        assertEquals("#/components/schemas/Pet", xmlSchema.ref)
    }

    // ── Optional body ─────────────────────────────────────────────────────────

    @Test
    fun `optional request body defaults required to false`() {
        val json = """
            {
              "description": "Optional payload",
              "content": {
                "application/json": {
                  "schema": {"type": "string"}
                }
              }
            }
        """.trimIndent()

        val body = decode(json)
        assertEquals("Optional payload", body.description)
        assertEquals(false, body.required)
        assertEquals(1, body.content.size)
    }

    @Test
    fun `minimal request body with content only deserializes`() {
        val json = """
            {
              "content": {
                "text/plain": {
                  "schema": {"type": "string"}
                }
              }
            }
        """.trimIndent()

        val body = decode(json)
        assertNull(body.description)
        assertEquals(false, body.required)
        assertEquals(1, body.content.size)
        assertTrue(body.extensions.isEmpty())
    }

    // ── Extensions ────────────────────────────────────────────────────────────

    @Test
    fun `request body with extensions preserves x fields`() {
        val json = """
            {
              "content": {
                "application/json": {
                  "schema": {"type": "object"}
                }
              },
              "extensions": {
                "x-trace-id": "abc-123",
                "x-retryable": true
              }
            }
        """.trimIndent()

        val body = decode(json)
        assertEquals(2, body.extensions.size)
        assertEquals(JsonPrimitive("abc-123"), body.extensions["x-trace-id"])
        assertEquals(JsonPrimitive(true), body.extensions["x-retryable"])
    }
}
