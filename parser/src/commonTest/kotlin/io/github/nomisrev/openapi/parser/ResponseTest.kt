package io.github.nomisrev.openapi.parser

import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ResponseTest {

    // ── Helper ────────────────────────────────────────────────────────────────

    private fun decode(json: String): Response =
        OpenAPI.Json.decodeFromString(Response.serializer(), json)

    // ── Minimal ───────────────────────────────────────────────────────────────

    @Test
    fun `minimal response with description only deserializes`() {
        val json = """{"description": "A successful response"}"""
        val response = decode(json)
        assertEquals("A successful response", response.description)
        assertTrue(response.headers.isEmpty())
        assertTrue(response.content.isEmpty())
        assertTrue(response.links.isEmpty())
        assertTrue(response.extensions.isEmpty())
    }

    @Test
    fun `empty response object deserializes to defaults`() {
        val json = """{}"""
        val response = decode(json)
        assertNull(response.description)
        assertTrue(response.headers.isEmpty())
        assertTrue(response.content.isEmpty())
        assertTrue(response.links.isEmpty())
        assertTrue(response.extensions.isEmpty())
    }

    // ── Headers ───────────────────────────────────────────────────────────────

    @Test
    fun `response with inline header deserializes`() {
        val json = """
            {
              "description": "OK",
              "headers": {
                "X-Rate-Limit": {
                  "description": "Number of requests per hour",
                  "required": true,
                  "schema": {"type": "integer"}
                }
              }
            }
        """.trimIndent()

        val response = decode(json)
        assertEquals(1, response.headers.size)
        val header = assertIs<ReferenceOr.Value<Header>>(response.headers["X-Rate-Limit"])
        assertEquals("Number of requests per hour", header.value.description)
        assertEquals(true, header.value.required)
        val schema = assertIs<ReferenceOr.Value<Schema>>(header.value.schema)
        assertEquals(Schema.Type.Basic.Integer, schema.value.type as Schema.Type.Basic)
    }

    @Test
    fun `response with header ref deserializes`() {
        val json = """
            {
              "description": "OK",
              "headers": {
                "X-Custom-Header": {
                  "${"$"}ref": "#/components/headers/CustomHeader"
                }
              }
            }
        """.trimIndent()

        val response = decode(json)
        val header = assertIs<ReferenceOr.Reference>(response.headers["X-Custom-Header"])
        assertEquals("#/components/headers/CustomHeader", header.ref)
    }

    @Test
    fun `response with multiple headers deserializes`() {
        val json = """
            {
              "description": "OK",
              "headers": {
                "X-Rate-Limit": {
                  "description": "Requests per hour",
                  "schema": {"type": "integer"}
                },
                "X-Expires-After": {
                  "description": "Date after which token expires",
                  "schema": {"type": "string", "format": "date-time"}
                }
              }
            }
        """.trimIndent()

        val response = decode(json)
        assertEquals(2, response.headers.size)
        assertNotNull(response.headers["X-Rate-Limit"])
        assertNotNull(response.headers["X-Expires-After"])
    }

    // ── Content ───────────────────────────────────────────────────────────────

    @Test
    fun `response with json content deserializes`() {
        val json = """
            {
              "description": "A list of pets",
              "content": {
                "application/json": {
                  "schema": {
                    "${"$"}ref": "#/components/schemas/PetList"
                  }
                }
              }
            }
        """.trimIndent()

        val response = decode(json)
        assertEquals(1, response.content.size)
        val mediaType = assertNotNull(response.content["application/json"])
        val schema = assertIs<ReferenceOr.Reference>(mediaType.schema)
        assertEquals("#/components/schemas/PetList", schema.ref)
    }

    @Test
    fun `response with multiple content types deserializes`() {
        val json = """
            {
              "description": "Success",
              "content": {
                "application/json": {
                  "schema": {"type": "object"}
                },
                "application/xml": {
                  "schema": {"type": "object"}
                },
                "text/plain": {
                  "schema": {"type": "string"}
                }
              }
            }
        """.trimIndent()

        val response = decode(json)
        assertEquals(3, response.content.size)
        assertNotNull(response.content["application/json"])
        assertNotNull(response.content["application/xml"])
        assertNotNull(response.content["text/plain"])
    }

    @Test
    fun `response with inline schema content deserializes`() {
        val json = """
            {
              "description": "OK",
              "content": {
                "application/json": {
                  "schema": {
                    "type": "object",
                    "properties": {
                      "id": {"type": "integer"},
                      "name": {"type": "string"}
                    },
                    "required": ["id"]
                  }
                }
              }
            }
        """.trimIndent()

        val response = decode(json)
        val mediaType = assertNotNull(response.content["application/json"])
        val schemaRef = assertIs<ReferenceOr.Value<Schema>>(mediaType.schema)
        val schema = schemaRef.value
        assertEquals(Schema.Type.Basic.Object, schema.type as Schema.Type.Basic)
        assertEquals(listOf("id"), schema.required)
        assertEquals(2, assertNotNull(schema.properties).size)
    }

    // ── Links ─────────────────────────────────────────────────────────────────

    @Test
    fun `response with link by operationId deserializes`() {
        val json = """
            {
              "description": "OK",
              "links": {
                "GetUserById": {
                  "operationId": "getUser",
                  "parameters": {},
                  "requestBody": "body",
                  "server": null
                }
              }
            }
        """.trimIndent()

        val response = decode(json)
        assertEquals(1, response.links.size)
        val link = assertIs<ReferenceOr.Value<Link>>(response.links["GetUserById"])
        assertEquals("getUser", link.value.operationId)
        assertNull(link.value.operationRef)
    }

    @Test
    fun `response with link by operationRef deserializes`() {
        val json = """
            {
              "description": "OK",
              "links": {
                "UserRepositories": {
                  "operationRef": "#/paths/~12.0~1repositories~1{username}/get",
                  "parameters": {},
                  "requestBody": "body",
                  "server": null
                }
              }
            }
        """.trimIndent()

        val response = decode(json)
        val link = assertIs<ReferenceOr.Value<Link>>(response.links["UserRepositories"])
        assertEquals("#/paths/~12.0~1repositories~1{username}/get", link.value.operationRef)
        assertNull(link.value.operationId)
    }

    @Test
    fun `response with link ref deserializes`() {
        val json = """
            {
              "description": "OK",
              "links": {
                "address": {
                  "${"$"}ref": "#/components/links/UserAddress"
                }
              }
            }
        """.trimIndent()

        val response = decode(json)
        val link = assertIs<ReferenceOr.Reference>(response.links["address"])
        assertEquals("#/components/links/UserAddress", link.ref)
    }

    // ── Full response ─────────────────────────────────────────────────────────

    @Test
    fun `response with headers content and links deserializes`() {
        val json = """
            {
              "description": "A complex response",
              "headers": {
                "X-Rate-Limit": {
                  "description": "Calls per hour",
                  "schema": {"type": "integer"}
                }
              },
              "content": {
                "application/json": {
                  "schema": {"${"$"}ref": "#/components/schemas/Pet"}
                }
              },
              "links": {
                "UpdatePet": {
                  "operationId": "updatePet",
                  "parameters": {},
                  "requestBody": "body",
                  "server": null
                }
              }
            }
        """.trimIndent()

        val response = decode(json)
        assertEquals("A complex response", response.description)

        assertEquals(1, response.headers.size)
        val header = assertIs<ReferenceOr.Value<Header>>(response.headers["X-Rate-Limit"])
        assertEquals("Calls per hour", header.value.description)

        assertEquals(1, response.content.size)
        val mediaType = assertNotNull(response.content["application/json"])
        val schema = assertIs<ReferenceOr.Reference>(mediaType.schema)
        assertEquals("#/components/schemas/Pet", schema.ref)

        assertEquals(1, response.links.size)
        val link = assertIs<ReferenceOr.Value<Link>>(response.links["UpdatePet"])
        assertEquals("updatePet", link.value.operationId)
    }

    // ── Extensions ────────────────────────────────────────────────────────────

    @Test
    fun `response with extensions deserializes`() {
        val json = """
            {
              "description": "OK",
              "x-internal": true,
              "x-owner": "team-platform"
            }
        """.trimIndent()

        val response = decode(json)
        assertEquals("OK", response.description)
        assertEquals(2, response.extensions.size)
        assertEquals(JsonPrimitive(true), response.extensions["x-internal"])
        assertEquals(JsonPrimitive("team-platform"), response.extensions["x-owner"])
    }

    // ── Unknown fields ────────────────────────────────────────────────────────

    @Test
    fun `unknown fields are silently ignored`() {
        val json = """
            {
              "description": "OK",
              "unknownField": "ignored",
              "anotherUnknown": 99
            }
        """.trimIndent()

        val response = decode(json)
        assertEquals("OK", response.description)
        assertTrue(response.extensions.isEmpty())
    }

    // ── plus() merging ────────────────────────────────────────────────────────

    @Test
    fun `plus keeps description from left response`() {
        val left = Response(description = "Left description")
        val right = Response(description = "Right description")
        val merged = left + right
        assertEquals("Left description", merged.description)
    }

    @Test
    fun `plus with null description on left uses null`() {
        val left = Response(description = null)
        val right = Response(description = "Right description")
        val merged = left + right
        assertNull(merged.description)
    }

    @Test
    fun `plus merges headers from both responses`() {
        val left = Response(
            headers = mapOf(
                "X-Rate-Limit" to ReferenceOr.Value(Header(description = "Rate limit"))
            )
        )
        val right = Response(
            headers = mapOf(
                "X-Expires-After" to ReferenceOr.Value(Header(description = "Expiry"))
            )
        )
        val merged = left + right
        assertEquals(2, merged.headers.size)
        assertNotNull(merged.headers["X-Rate-Limit"])
        assertNotNull(merged.headers["X-Expires-After"])
    }

    @Test
    fun `plus merges content from both responses`() {
        val left = Response(
            content = mapOf("application/json" to MediaType())
        )
        val right = Response(
            content = mapOf("application/xml" to MediaType())
        )
        val merged = left + right
        assertEquals(2, merged.content.size)
        assertNotNull(merged.content["application/json"])
        assertNotNull(merged.content["application/xml"])
    }

    @Test
    fun `plus merges links from both responses`() {
        val left = Response(
            links = mapOf("LinkA" to ReferenceOr.Reference("#/components/links/A"))
        )
        val right = Response(
            links = mapOf("LinkB" to ReferenceOr.Reference("#/components/links/B"))
        )
        val merged = left + right
        assertEquals(2, merged.links.size)
        assertNotNull(merged.links["LinkA"])
        assertNotNull(merged.links["LinkB"])
    }

    @Test
    fun `plus right headers overwrite left headers with same key`() {
        val leftHeader = ReferenceOr.Value(Header(description = "Left"))
        val rightHeader = ReferenceOr.Value(Header(description = "Right"))
        val left = Response(headers = mapOf("X-Shared" to leftHeader))
        val right = Response(headers = mapOf("X-Shared" to rightHeader))
        val merged = left + right
        assertEquals(1, merged.headers.size)
        val header = assertIs<ReferenceOr.Value<Header>>(merged.headers["X-Shared"])
        assertEquals("Right", header.value.description)
    }

    @Test
    fun `plus merges extensions from both responses`() {
        val left = Response(extensions = mapOf("x-a" to JsonPrimitive("alpha")))
        val right = Response(extensions = mapOf("x-b" to JsonPrimitive("beta")))
        val merged = left + right
        assertEquals(2, merged.extensions.size)
        assertEquals(JsonPrimitive("alpha"), merged.extensions["x-a"])
        assertEquals(JsonPrimitive("beta"), merged.extensions["x-b"])
    }

    @Test
    fun `plus with two empty responses produces empty response`() {
        val merged = Response() + Response()
        assertNull(merged.description)
        assertTrue(merged.headers.isEmpty())
        assertTrue(merged.content.isEmpty())
        assertTrue(merged.links.isEmpty())
        assertTrue(merged.extensions.isEmpty())
    }

    // ── YAML ──────────────────────────────────────────────────────────────────

    @Test
    fun `yaml minimal response deserializes`() {
        val yaml = """
            description: A successful response
        """.trimIndent()

        val response = OpenAPI.Yaml.decodeFromString(Response.serializer(), yaml)
        assertEquals("A successful response", response.description)
        assertTrue(response.headers.isEmpty())
        assertTrue(response.content.isEmpty())
        assertTrue(response.links.isEmpty())
    }

    @Test
    fun `yaml response with headers and content deserializes`() {
        val yaml = """
            description: OK
            headers:
              X-Rate-Limit:
                description: Calls per hour
                schema:
                  type: integer
            content:
              application/json:
                schema:
                  ${"$"}ref: '#/components/schemas/Pet'
        """.trimIndent()

        val response = OpenAPI.Yaml.decodeFromString(Response.serializer(), yaml)
        assertEquals("OK", response.description)

        assertEquals(1, response.headers.size)
        val header = assertIs<ReferenceOr.Value<Header>>(response.headers["X-Rate-Limit"])
        assertEquals("Calls per hour", header.value.description)

        assertEquals(1, response.content.size)
        val mediaType = assertNotNull(response.content["application/json"])
        val schema = assertIs<ReferenceOr.Reference>(mediaType.schema)
        assertEquals("#/components/schemas/Pet", schema.ref)
    }
}
