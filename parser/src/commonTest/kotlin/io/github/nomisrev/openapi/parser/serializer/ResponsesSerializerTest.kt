package io.github.nomisrev.openapi.parser.serializer

import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Response
import io.github.nomisrev.openapi.parser.Responses
import io.github.nomisrev.openapi.parser.ReferenceOr
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ResponsesSerializerTest {

    // ── JSON integer status codes ─────────────────────────────────────────────

    @Test
    fun `json single integer status code key deserializes correctly`() {
        val json = """{"200":{"description":"OK"}}"""
        val responses = OpenAPI.Json.decodeFromString(Responses.serializer(), json)
        assertNull(responses.default)
        assertEquals(1, responses.responses.size)
        val r200 = assertIs<ReferenceOr.Value<Response>>(responses.responses[200])
        assertEquals("OK", r200.value.description)
    }

    @Test
    fun `json multiple integer status codes deserialize to correct map`() {
        val json = """
            {
              "200":{"description":"OK"},
              "404":{"description":"Not Found"},
              "500":{"description":"Internal Server Error"}
            }
        """.trimIndent()
        val responses = OpenAPI.Json.decodeFromString(Responses.serializer(), json)
        assertEquals(3, responses.responses.size)
        val r200 = assertIs<ReferenceOr.Value<Response>>(responses.responses[200])
        assertEquals("OK", r200.value.description)
        val r404 = assertIs<ReferenceOr.Value<Response>>(responses.responses[404])
        assertEquals("Not Found", r404.value.description)
        val r500 = assertIs<ReferenceOr.Value<Response>>(responses.responses[500])
        assertEquals("Internal Server Error", r500.value.description)
    }

    // ── JSON "default" response ───────────────────────────────────────────────

    @Test
    fun `json default key deserializes to default field`() {
        val json = """{"default":{"description":"Unexpected error"}}"""
        val responses = OpenAPI.Json.decodeFromString(Responses.serializer(), json)
        val default = assertNotNull(responses.default)
        val value = assertIs<ReferenceOr.Value<Response>>(default)
        assertEquals("Unexpected error", value.value.description)
        assertEquals(emptyMap(), responses.responses)
    }

    @Test
    fun `json ref default response deserializes to Reference`() {
        val json = """{"default":{"${"$"}ref":"#/components/responses/Error"}}"""
        val responses = OpenAPI.Json.decodeFromString(Responses.serializer(), json)
        val default = assertNotNull(responses.default)
        val ref = assertIs<ReferenceOr.Reference>(default)
        assertEquals("#/components/responses/Error", ref.ref)
    }

    // ── JSON mixed status codes + default ─────────────────────────────────────

    @Test
    fun `json mixed status codes and default deserializes to all fields`() {
        val json = """
            {
              "200":{"description":"Success"},
              "default":{"description":"Error"}
            }
        """.trimIndent()
        val responses = OpenAPI.Json.decodeFromString(Responses.serializer(), json)
        assertNotNull(responses.default)
        assertEquals(1, responses.responses.size)
        assertNotNull(responses.responses[200])
    }

    // ── JSON extensions ───────────────────────────────────────────────────────

    @Test
    fun `json x- extensions are preserved in extensions map`() {
        val json = """
            {
              "200":{"description":"OK"},
              "x-custom":"my-value"
            }
        """.trimIndent()
        val responses = OpenAPI.Json.decodeFromString(Responses.serializer(), json)
        assertEquals(1, responses.responses.size)
        assertEquals(JsonPrimitive("my-value"), responses.extensions["x-custom"])
    }

    @Test
    fun `json multiple extensions are all preserved`() {
        val json = """
            {
              "200":{"description":"OK"},
              "x-a":"1",
              "x-b":"2"
            }
        """.trimIndent()
        val responses = OpenAPI.Json.decodeFromString(Responses.serializer(), json)
        assertEquals(JsonPrimitive("1"), responses.extensions["x-a"])
        assertEquals(JsonPrimitive("2"), responses.extensions["x-b"])
    }

    @Test
    fun `json x- keys are excluded from responses map`() {
        val json = """{"200":{"description":"OK"},"x-foo":"bar"}"""
        val responses = OpenAPI.Json.decodeFromString(Responses.serializer(), json)
        assertEquals(setOf(200), responses.responses.keys)
    }

    // ── JSON empty responses ──────────────────────────────────────────────────

    @Test
    fun `json empty object deserializes to empty responses`() {
        val json = """{}"""
        val responses = OpenAPI.Json.decodeFromString(Responses.serializer(), json)
        assertNull(responses.default)
        assertEquals(emptyMap(), responses.responses)
        assertEquals(emptyMap(), responses.extensions)
    }

    // ── JSON serialization ────────────────────────────────────────────────────

    @Test
    fun `responses with only status codes serializes to flat json object with int keys`() {
        val responses = Responses(
            default = null,
            responses = mapOf(200 to ReferenceOr.Value(Response(description = "OK"))),
        )
        val json = OpenAPI.Json.encodeToString(Responses.serializer(), responses)
        assertTrue(json.contains("\"200\""), "Expected '200' key in: $json")
    }

    // ── YAML integer status codes ─────────────────────────────────────────────

    @Test
    fun `yaml single integer status code key deserializes correctly`() {
        val yaml = """
            200:
              description: OK
        """.trimIndent()
        val responses = OpenAPI.Yaml.decodeFromString(Responses.serializer(), yaml)
        assertNull(responses.default)
        val r200 = assertIs<ReferenceOr.Value<Response>>(responses.responses[200])
        assertEquals("OK", r200.value.description)
    }

    @Test
    fun `yaml multiple status codes deserialize correctly`() {
        val yaml = """
            200:
              description: OK
            404:
              description: Not Found
        """.trimIndent()
        val responses = OpenAPI.Yaml.decodeFromString(Responses.serializer(), yaml)
        assertEquals(2, responses.responses.size)
        val r200 = assertIs<ReferenceOr.Value<Response>>(responses.responses[200])
        assertEquals("OK", r200.value.description)
        val r404 = assertIs<ReferenceOr.Value<Response>>(responses.responses[404])
        assertEquals("Not Found", r404.value.description)
    }

    // ── YAML "default" response ───────────────────────────────────────────────

    @Test
    fun `yaml default key deserializes to default field`() {
        val yaml = """
            default:
              description: Unexpected error
        """.trimIndent()
        val responses = OpenAPI.Yaml.decodeFromString(Responses.serializer(), yaml)
        val default = assertNotNull(responses.default)
        val value = assertIs<ReferenceOr.Value<Response>>(default)
        assertEquals("Unexpected error", value.value.description)
    }

    // ── YAML mixed status codes + default ─────────────────────────────────────

    @Test
    fun `yaml mixed status codes and default deserializes correctly`() {
        val yaml = """
            200:
              description: Success
            default:
              description: Error
        """.trimIndent()
        val responses = OpenAPI.Yaml.decodeFromString(Responses.serializer(), yaml)
        assertNotNull(responses.default)
        assertEquals(1, responses.responses.size)
        assertNotNull(responses.responses[200])
    }

    // ── YAML extensions ───────────────────────────────────────────────────────

    // NOTE: YAML extension decoding in Responses throws MissingTypeTagException due to kaml
    // requiring type tags for polymorphic JsonElement deserialization. This is a known limitation
    // shared with KSerializerWithExtensions.kt (see TODO comment there).
    // The YAML extension path in Responses is therefore not tested here.
}
