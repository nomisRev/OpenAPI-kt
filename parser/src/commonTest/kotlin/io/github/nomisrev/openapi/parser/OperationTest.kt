package io.github.nomisrev.openapi.parser

import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class OperationTest {

    // ── Helper ────────────────────────────────────────────────────────────────

    private fun decode(json: String): Operation =
        OpenAPI.Json.decodeFromString(Operation.serializer(), json)

    // ── Minimal operation ─────────────────────────────────────────────────────

    @Test
    fun `minimal operation with only responses deserializes`() {
        val op = decode("""{"responses":{"200":{"description":"OK"}}}""")
        assertNull(op.tags)
        assertNull(op.summary)
        assertNull(op.description)
        assertNull(op.externalDocs)
        assertNull(op.operationId)
        assertTrue(op.parameters.isEmpty())
        assertNull(op.requestBody)
        assertFalse(op.deprecated)
        assertTrue(op.security.isEmpty())
        assertTrue(op.servers.isEmpty())
        assertTrue(op.callbacks.isEmpty())
        assertTrue(op.extensions.isEmpty())
        assertEquals(1, op.responses.responses.size)
        assertNull(op.responses.default)
    }

    // ── Full operation ─────────────────────────────────────────────────────────

    @Suppress("LongMethod")
    @Test
    fun `full operation with all fields deserializes`() {
        val json = """
            {
              "tags": ["pets", "store"],
              "summary": "List all pets",
              "description": "Returns all pets from the system",
              "externalDocs": {
                "description": "Find more info here",
                "url": "https://example.com/docs"
              },
              "operationId": "listPets",
              "parameters": [
                {
                  "name": "limit",
                  "in": "query",
                  "description": "How many items to return",
                  "required": false,
                  "schema": {"type": "integer"}
                }
              ],
              "requestBody": {
                "description": "Pet to add",
                "content": {
                  "application/json": {"schema": {"type": "object"}}
                },
                "required": true
              },
              "responses": {
                "200": {"description": "A list of pets"},
                "default": {"description": "Unexpected error"}
              },
              "callbacks": {
                "onData": {
                  "{${'$'}url}": {
                    "post": {"responses": {"200": {"description": "Callback received"}}}
                  }
                }
              },
              "deprecated": true,
              "security": [
                {"api_key": []},
                {"oauth2": ["read:pets", "write:pets"]}
              ],
              "servers": [
                {"url": "https://api.example.com/v1"}
              ]
            }
        """.trimIndent()

        val op = decode(json)

        // tags
        assertEquals(listOf("pets", "store"), op.tags)

        // summary + description
        assertEquals("List all pets", op.summary)
        assertEquals("Returns all pets from the system", op.description)

        // externalDocs
        val extDocs = assertNotNull(op.externalDocs)
        assertEquals("Find more info here", extDocs.description)
        assertEquals("https://example.com/docs", extDocs.url)

        // operationId
        assertEquals("listPets", op.operationId)

        // parameters
        assertEquals(1, op.parameters.size)
        val param = assertIs<ReferenceOr.Value<Parameter>>(op.parameters[0]).value
        assertEquals("limit", param.name)
        assertEquals(Parameter.Input.Query, param.input)
        assertEquals("How many items to return", param.description)
        assertFalse(param.required)

        // requestBody
        val requestBody = assertIs<ReferenceOr.Value<RequestBody>>(assertNotNull(op.requestBody)).value
        assertEquals("Pet to add", requestBody.description)
        assertTrue(requestBody.required)
        assertTrue(requestBody.content.containsKey("application/json"))

        // responses — 200 + default
        assertEquals(1, op.responses.responses.size)
        val response200 = assertIs<ReferenceOr.Value<Response>>(assertNotNull(op.responses.responses[200])).value
        assertEquals("A list of pets", response200.description)
        val defaultResponse = assertIs<ReferenceOr.Value<Response>>(assertNotNull(op.responses.default)).value
        assertEquals("Unexpected error", defaultResponse.description)

        // deprecated
        assertTrue(op.deprecated)

        // security
        assertEquals(2, op.security.size)
        assertEquals(emptyList<String>(), op.security[0]["api_key"])
        assertEquals(listOf("read:pets", "write:pets"), op.security[1]["oauth2"])

        // servers
        assertEquals(1, op.servers.size)
        assertEquals("https://api.example.com/v1", op.servers[0].url)
    }

    // ── parameters with $ref ──────────────────────────────────────────────────

    @Test
    fun `operation with dollar-ref parameter deserializes`() {
        val json = """
            {
              "responses": {"200": {"description": "OK"}},
              "parameters": [
                {"${'$'}ref": "#/components/parameters/LimitParam"}
              ]
            }
        """.trimIndent()

        val op = decode(json)
        assertEquals(1, op.parameters.size)
        val ref = assertIs<ReferenceOr.Reference>(op.parameters[0])
        assertEquals("#/components/parameters/LimitParam", ref.ref)
    }

    @Test
    fun `operation with mixed inline and ref parameters deserializes`() {
        val json = """
            {
              "responses": {"200": {"description": "OK"}},
              "parameters": [
                {"${'$'}ref": "#/components/parameters/PageParam"},
                {"name": "filter", "in": "query", "schema": {"type": "string"}}
              ]
            }
        """.trimIndent()

        val op = decode(json)
        assertEquals(2, op.parameters.size)
        assertIs<ReferenceOr.Reference>(op.parameters[0])
        val inline = assertIs<ReferenceOr.Value<Parameter>>(op.parameters[1]).value
        assertEquals("filter", inline.name)
        assertEquals(Parameter.Input.Query, inline.input)
    }

    // ── requestBody as $ref ───────────────────────────────────────────────────

    @Test
    fun `operation with dollar-ref requestBody deserializes`() {
        val json = """
            {
              "responses": {"200": {"description": "OK"}},
              "requestBody": {"${'$'}ref": "#/components/requestBodies/PetBody"}
            }
        """.trimIndent()

        val op = decode(json)
        val ref = assertIs<ReferenceOr.Reference>(assertNotNull(op.requestBody))
        assertEquals("#/components/requestBodies/PetBody", ref.ref)
    }

    // ── callbacks ─────────────────────────────────────────────────────────────

    @Test
    fun `operation with callback deserializes`() {
        val json = """
            {
              "responses": {"200": {"description": "OK"}},
              "callbacks": {
                "myCallback": {
                  "{${'$'}url}": {
                    "get": {"responses": {"200": {"description": "Callback OK"}}}
                  }
                }
              }
            }
        """.trimIndent()

        val op = decode(json)
        assertEquals(1, op.callbacks.size)
        val callback = assertIs<ReferenceOr.Value<Callback>>(assertNotNull(op.callbacks["myCallback"])).value
        assertTrue(callback.value.containsKey("{${"\$"}url}"))
    }

    @Test
    fun `operation with dollar-ref callback deserializes`() {
        val json = """
            {
              "responses": {"200": {"description": "OK"}},
              "callbacks": {
                "onEvent": {"${'$'}ref": "#/components/callbacks/EventCallback"}
              }
            }
        """.trimIndent()

        val op = decode(json)
        val ref = assertIs<ReferenceOr.Reference>(assertNotNull(op.callbacks["onEvent"]))
        assertEquals("#/components/callbacks/EventCallback", ref.ref)
    }

    // ── security ──────────────────────────────────────────────────────────────

    @Test
    fun `operation with empty security list has no requirements`() {
        val json = """{"responses":{"200":{"description":"OK"}},"security":[]}"""
        val op = decode(json)
        assertTrue(op.security.isEmpty())
    }

    @Test
    fun `operation with multiple security requirements deserializes`() {
        val json = """
            {
              "responses": {"200": {"description": "OK"}},
              "security": [
                {"bearerAuth": []},
                {"apiKeyAuth": []},
                {"oauth2": ["openid", "profile"]}
              ]
            }
        """.trimIndent()

        val op = decode(json)
        assertEquals(3, op.security.size)
        assertEquals(emptyList<String>(), op.security[0]["bearerAuth"])
        assertEquals(emptyList<String>(), op.security[1]["apiKeyAuth"])
        assertEquals(listOf("openid", "profile"), op.security[2]["oauth2"])
    }

    @Test
    fun `operation with single empty security requirement anonymous access deserializes`() {
        // An empty object in security means the operation can be called without auth
        val json = """{"responses":{"200":{"description":"OK"}},"security":[{}]}"""
        val op = decode(json)
        assertEquals(1, op.security.size)
        assertTrue(op.security[0].isEmpty())
    }

    // ── extensions ────────────────────────────────────────────────────────────

    @Test
    fun `operation extensions are captured`() {
        val json = """
            {
              "responses": {"200": {"description": "OK"}},
              "x-internal": "true",
              "x-rateLimit": 100
            }
        """.trimIndent()

        val op = decode(json)
        assertEquals(JsonPrimitive("true"), op.extensions["x-internal"])
        assertEquals(JsonPrimitive(100), op.extensions["x-rateLimit"])
    }

    @Test
    fun `operation without extensions has empty extensions map`() {
        val json = """{"responses":{"200":{"description":"OK"}}}"""
        val op = decode(json)
        assertTrue(op.extensions.isEmpty())
    }

    // ── servers ───────────────────────────────────────────────────────────────

    @Test
    fun `operation with multiple servers deserializes`() {
        val json = """
            {
              "responses": {"200": {"description": "OK"}},
              "servers": [
                {"url": "https://prod.example.com"},
                {"url": "https://staging.example.com", "description": "Staging server"}
              ]
            }
        """.trimIndent()

        val op = decode(json)
        assertEquals(2, op.servers.size)
        assertEquals("https://prod.example.com", op.servers[0].url)
        assertEquals("https://staging.example.com", op.servers[1].url)
        assertEquals("Staging server", op.servers[1].description)
    }

    // ── deprecated ────────────────────────────────────────────────────────────

    @Test
    fun `operation defaults to not deprecated`() {
        val op = decode("""{"responses":{"200":{"description":"OK"}}}""")
        assertFalse(op.deprecated)
    }

    @Test
    fun `operation deprecated true deserializes`() {
        val op = decode("""{"responses":{"200":{"description":"OK"}},"deprecated":true}""")
        assertTrue(op.deprecated)
    }

    // ── responses ─────────────────────────────────────────────────────────────

    @Test
    fun `operation with default response only deserializes`() {
        val json = """{"responses":{"default":{"description":"Unexpected error"}}}"""
        val op = decode(json)
        assertTrue(op.responses.responses.isEmpty())
        val defaultResponse = assertIs<ReferenceOr.Value<Response>>(assertNotNull(op.responses.default)).value
        assertEquals("Unexpected error", defaultResponse.description)
    }

    @Test
    fun `operation with multiple status code responses deserializes`() {
        val json = """
            {
              "responses": {
                "200": {"description": "Success"},
                "201": {"description": "Created"},
                "404": {"description": "Not Found"},
                "500": {"description": "Server Error"}
              }
            }
        """.trimIndent()

        val op = decode(json)
        assertEquals(4, op.responses.responses.size)
        assertEquals("Success", assertIs<ReferenceOr.Value<Response>>(op.responses.responses[200]).value.description)
        assertEquals("Created", assertIs<ReferenceOr.Value<Response>>(op.responses.responses[201]).value.description)
        assertEquals("Not Found", assertIs<ReferenceOr.Value<Response>>(op.responses.responses[404]).value.description)
        assertEquals("Server Error", assertIs<ReferenceOr.Value<Response>>(op.responses.responses[500]).value.description)
    }

    @Test
    fun `operation with dollar-ref response deserializes`() {
        val json = """
            {
              "responses": {
                "200": {"${'$'}ref": "#/components/responses/SuccessResponse"}
              }
            }
        """.trimIndent()

        val op = decode(json)
        val ref = assertIs<ReferenceOr.Reference>(assertNotNull(op.responses.responses[200]))
        assertEquals("#/components/responses/SuccessResponse", ref.ref)
    }

    // ── externalDocs ─────────────────────────────────────────────────────────

    @Test
    fun `operation with externalDocs url only deserializes`() {
        val json = """
            {
              "responses": {"200": {"description": "OK"}},
              "externalDocs": {"url": "https://example.com"}
            }
        """.trimIndent()

        val op = decode(json)
        val extDocs = assertNotNull(op.externalDocs)
        assertEquals("https://example.com", extDocs.url)
        assertNull(extDocs.description)
    }

    // ── tags ─────────────────────────────────────────────────────────────────

    @Test
    fun `operation with empty tags list deserializes`() {
        val json = """{"responses":{"200":{"description":"OK"}},"tags":[]}"""
        val op = decode(json)
        assertEquals(emptyList(), op.tags)
    }

    @Test
    fun `operation with single tag deserializes`() {
        val json = """{"responses":{"200":{"description":"OK"}},"tags":["pets"]}"""
        val op = decode(json)
        assertEquals(listOf("pets"), op.tags)
    }

    // ── unknown fields ignored ────────────────────────────────────────────────

    @Test
    fun `unknown fields in operation are silently ignored`() {
        val json = """
            {
              "responses": {"200": {"description": "OK"}},
              "unknownField": "someValue",
              "anotherUnknown": 42
            }
        """.trimIndent()

        // Should not throw
        val op = decode(json)
        assertEquals(1, op.responses.responses.size)
    }

    // ── YAML ─────────────────────────────────────────────────────────────────

    @Test
    fun `yaml minimal operation deserializes`() {
        val yaml = """
            responses:
              "200":
                description: OK
        """.trimIndent()

        val op = OpenAPI.Yaml.decodeFromString(Operation.serializer(), yaml)
        assertNull(op.operationId)
        assertTrue(op.parameters.isEmpty())
        assertEquals(1, op.responses.responses.size)
    }

    @Test
    fun `yaml full operation deserializes`() {
        val yaml = """
            operationId: getPets
            tags:
              - pets
            summary: Get all pets
            deprecated: false
            parameters:
              - name: limit
                in: query
                required: false
                schema:
                  type: integer
            responses:
              "200":
                description: A list of pets
              default:
                description: Unexpected error
            security:
              - api_key: []
        """.trimIndent()

        val op = OpenAPI.Yaml.decodeFromString(Operation.serializer(), yaml)
        assertEquals("getPets", op.operationId)
        assertEquals(listOf("pets"), op.tags)
        assertEquals("Get all pets", op.summary)
        assertFalse(op.deprecated)
        assertEquals(1, op.parameters.size)
        assertEquals(2, op.responses.responses.size + (if (op.responses.default != null) 1 else 0))
        assertEquals(1, op.security.size)
        assertEquals(emptyList<String>(), op.security[0]["api_key"])
    }
}
