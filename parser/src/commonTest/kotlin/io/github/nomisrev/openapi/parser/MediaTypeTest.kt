package io.github.nomisrev.openapi.parser

import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MediaTypeTest {

    // ── Helper ────────────────────────────────────────────────────────────────

    private fun decode(json: String): MediaType =
        OpenAPI.Json.decodeFromString(MediaType.serializer(), json)

    // ── Minimal ───────────────────────────────────────────────────────────────

    @Test
    fun `empty media type object deserializes to defaults`() {
        val json = """{}"""
        val mediaType = decode(json)
        assertNull(mediaType.schema)
        assertNull(mediaType.example)
        assertTrue(mediaType.examples.isEmpty())
        assertTrue(mediaType.encoding.isEmpty())
        assertTrue(mediaType.extensions.isEmpty())
    }

    // ── Schema ────────────────────────────────────────────────────────────────

    @Test
    fun `schema reference deserializes`() {
        val json = """
            {
              "schema": {"${"$"}ref": "#/components/schemas/Pet"}
            }
        """.trimIndent()

        val mediaType = decode(json)
        val ref = assertIs<ReferenceOr.Reference>(mediaType.schema)
        assertEquals("#/components/schemas/Pet", ref.ref)
    }

    @Test
    fun `inline schema deserializes`() {
        val json = """
            {
              "schema": {"type": "string"}
            }
        """.trimIndent()

        val mediaType = decode(json)
        val value = assertIs<ReferenceOr.Value<Schema>>(mediaType.schema)
        assertEquals(Schema.Type.Basic.String, (value.value.type as Schema.Type.Basic))
    }

    @Test
    fun `inline object schema with properties deserializes`() {
        val json = """
            {
              "schema": {
                "type": "object",
                "properties": {
                  "id": {"type": "integer"},
                  "name": {"type": "string"}
                },
                "required": ["id"]
              }
            }
        """.trimIndent()

        val mediaType = decode(json)
        val schemaValue = assertIs<ReferenceOr.Value<Schema>>(mediaType.schema)
        val schema = schemaValue.value
        assertEquals(Schema.Type.Basic.Object, schema.type as Schema.Type.Basic)
        assertEquals(listOf("id"), schema.required)
        val properties = assertNotNull(schema.properties)
        assertEquals(2, properties.size)
        val idProp = assertIs<ReferenceOr.Value<Schema>>(properties["id"])
        assertEquals(Schema.Type.Basic.Integer, idProp.value.type as Schema.Type.Basic)
    }

    // ── Example ───────────────────────────────────────────────────────────────

    @Test
    fun `single example value deserializes`() {
        val json = """
            {
              "schema": {"type": "string"},
              "example": "hello world"
            }
        """.trimIndent()

        val mediaType = decode(json)
        val example = assertIs<ExampleValue.Single>(assertNotNull(mediaType.example))
        assertEquals("hello world", example.value)
    }

    @Test
    fun `example object value deserializes to single`() {
        val json = """
            {
              "schema": {"type": "object"},
              "example": {"id": 1, "name": "Fido"}
            }
        """.trimIndent()

        val mediaType = decode(json)
        // JSON objects are encoded as a Single containing the JSON string
        assertIs<ExampleValue.Single>(assertNotNull(mediaType.example))
    }

    // ── Examples map ──────────────────────────────────────────────────────────

    @Test
    fun `multiple named examples deserialize`() {
        val json = """
            {
              "schema": {"type": "string"},
              "examples": {
                "short": {
                  "summary": "A short example",
                  "value": "hi"
                },
                "long": {
                  "summary": "A long example",
                  "value": "hello world"
                }
              }
            }
        """.trimIndent()

        val mediaType = decode(json)
        assertEquals(2, mediaType.examples.size)

        val short = assertIs<ReferenceOr.Value<Example>>(mediaType.examples["short"])
        assertEquals("A short example", short.value.summary)

        val long = assertIs<ReferenceOr.Value<Example>>(mediaType.examples["long"])
        assertEquals("A long example", long.value.summary)
    }

    @Test
    fun `example with external value deserializes`() {
        val json = """
            {
              "examples": {
                "fromUrl": {
                  "summary": "Remote example",
                  "externalValue": "https://example.com/sample.json"
                }
              }
            }
        """.trimIndent()

        val mediaType = decode(json)
        val fromUrl = assertIs<ReferenceOr.Value<Example>>(mediaType.examples["fromUrl"])
        assertEquals("Remote example", fromUrl.value.summary)
        assertEquals("https://example.com/sample.json", fromUrl.value.externalValue)
        assertNull(fromUrl.value.value)
    }

    @Test
    fun `example via ref deserializes`() {
        val json = """
            {
              "examples": {
                "petExample": {
                  "${"$"}ref": "#/components/examples/PetExample"
                }
              }
            }
        """.trimIndent()

        val mediaType = decode(json)
        val petExample = assertIs<ReferenceOr.Reference>(mediaType.examples["petExample"])
        assertEquals("#/components/examples/PetExample", petExample.ref)
    }

    // ── Encoding ──────────────────────────────────────────────────────────────

    @Test
    fun `encoding with content type deserializes`() {
        val json = """
            {
              "encoding": {
                "profileImage": {
                  "contentType": "image/png, image/jpeg"
                }
              }
            }
        """.trimIndent()

        val mediaType = decode(json)
        assertEquals(1, mediaType.encoding.size)
        val encoding = assertNotNull(mediaType.encoding["profileImage"])
        assertEquals("image/png, image/jpeg", encoding.contentType)
    }

    @Test
    fun `encoding with style and explode deserializes`() {
        val json = """
            {
              "encoding": {
                "tags": {
                  "contentType": "text/plain",
                  "style": "form",
                  "explode": true,
                  "allowReserved": false
                }
              }
            }
        """.trimIndent()

        val mediaType = decode(json)
        val encoding = assertNotNull(mediaType.encoding["tags"])
        assertEquals("text/plain", encoding.contentType)
        assertEquals("form", encoding.style)
        assertEquals(true, encoding.explode)
        assertEquals(false, encoding.allowReserved)
    }

    @Test
    fun `encoding with headers deserializes`() {
        val json = """
            {
              "encoding": {
                "attachment": {
                  "contentType": "application/octet-stream",
                  "headers": {
                    "Content-Disposition": {
                      "${"$"}ref": "#/components/headers/ContentDisposition"
                    }
                  }
                }
              }
            }
        """.trimIndent()

        val mediaType = decode(json)
        val encoding = assertNotNull(mediaType.encoding["attachment"])
        assertEquals(1, encoding.headers.size)
        val header = assertIs<ReferenceOr.Reference>(encoding.headers["Content-Disposition"])
        assertEquals("#/components/headers/ContentDisposition", header.ref)
    }

    @Test
    fun `multiple encoding entries deserialize`() {
        val json = """
            {
              "encoding": {
                "name": {
                  "contentType": "text/plain"
                },
                "file": {
                  "contentType": "application/octet-stream"
                }
              }
            }
        """.trimIndent()

        val mediaType = decode(json)
        assertEquals(2, mediaType.encoding.size)
        assertNotNull(mediaType.encoding["name"])
        assertNotNull(mediaType.encoding["file"])
    }

    // ── Extensions ────────────────────────────────────────────────────────────

    @Test
    fun `extensions deserialize`() {
        val json = """
            {
              "schema": {"type": "string"},
              "x-internal": true,
              "x-owner": "team-api"
            }
        """.trimIndent()

        val mediaType = decode(json)
        assertEquals(2, mediaType.extensions.size)
        assertEquals(JsonPrimitive(true), mediaType.extensions["x-internal"])
        assertEquals(JsonPrimitive("team-api"), mediaType.extensions["x-owner"])
    }

    @Test
    fun `encoding with extensions deserializes`() {
        val json = """
            {
              "encoding": {
                "profileImage": {
                  "contentType": "image/png",
                  "x-max-size": 5242880
                }
              }
            }
        """.trimIndent()

        val mediaType = decode(json)
        val encoding = assertNotNull(mediaType.encoding["profileImage"])
        assertEquals(JsonPrimitive(5242880), encoding.extensions["x-max-size"])
    }

    // ── Unknown fields ────────────────────────────────────────────────────────

    @Test
    fun `unknown fields are silently ignored`() {
        val json = """
            {
              "schema": {"type": "string"},
              "unknownField": "ignored",
              "anotherUnknown": 99
            }
        """.trimIndent()

        val mediaType = decode(json)
        val schemaValue = assertIs<ReferenceOr.Value<Schema>>(mediaType.schema)
        assertEquals(Schema.Type.Basic.String, schemaValue.value.type as Schema.Type.Basic)
        assertTrue(mediaType.extensions.isEmpty())
    }

    // ── YAML ──────────────────────────────────────────────────────────────────

    @Test
    fun `yaml media type with schema ref deserializes`() {
        val yaml = """
            schema:
              ${"$"}ref: '#/components/schemas/Pet'
        """.trimIndent()

        val mediaType = OpenAPI.Yaml.decodeFromString(MediaType.serializer(), yaml)
        val ref = assertIs<ReferenceOr.Reference>(mediaType.schema)
        assertEquals("#/components/schemas/Pet", ref.ref)
    }

    @Test
    fun `yaml media type with inline schema and examples deserializes`() {
        val yaml = """
            schema:
              type: string
            examples:
              simple:
                summary: A simple example
                value: hello
        """.trimIndent()

        val mediaType = OpenAPI.Yaml.decodeFromString(MediaType.serializer(), yaml)
        val schemaValue = assertIs<ReferenceOr.Value<Schema>>(mediaType.schema)
        assertEquals(Schema.Type.Basic.String, schemaValue.value.type as Schema.Type.Basic)
        assertEquals(1, mediaType.examples.size)
        val simple = assertIs<ReferenceOr.Value<Example>>(mediaType.examples["simple"])
        assertEquals("A simple example", simple.value.summary)
    }

    @Test
    fun `yaml media type with encoding deserializes`() {
        val yaml = """
            encoding:
              profileImage:
                contentType: image/png, image/jpeg
                allowReserved: false
        """.trimIndent()

        val mediaType = OpenAPI.Yaml.decodeFromString(MediaType.serializer(), yaml)
        val encoding = assertNotNull(mediaType.encoding["profileImage"])
        assertEquals("image/png, image/jpeg", encoding.contentType)
        assertEquals(false, encoding.allowReserved)
    }
}
