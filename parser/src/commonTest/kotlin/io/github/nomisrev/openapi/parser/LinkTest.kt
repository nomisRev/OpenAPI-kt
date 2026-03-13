package io.github.nomisrev.openapi.parser

import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LinkTest {

    // ── Helper ────────────────────────────────────────────────────────────────

    private fun decode(json: String): Link =
        OpenAPI.Json.decodeFromString(Link.serializer(), json)

    // ── Minimal / defaults ────────────────────────────────────────────────────

    @Test
    fun `empty link object deserializes to defaults`() {
        val link = decode("{}")
        assertNull(link.operationRef)
        assertNull(link.operationId)
        assertTrue(link.parameters.isEmpty())
        assertNull(link.requestBody)
        assertNull(link.description)
        assertNull(link.server)
        assertTrue(link.extensions.isEmpty())
    }

    // ── operationRef ──────────────────────────────────────────────────────────

    @Test
    fun `link by operationRef deserializes`() {
        val json = """
            {
              "operationRef": "#/paths/~1users~1{userId}/get",
              "description": "Get the user"
            }
        """.trimIndent()
        val link = decode(json)
        assertEquals("#/paths/~1users~1{userId}/get", link.operationRef)
        assertNull(link.operationId)
        assertEquals("Get the user", link.description)
    }

    @Test
    fun `link by absolute operationRef deserializes`() {
        val json = """
            {
              "operationRef": "https://example.com/openapi.json#/paths/~1users/get"
            }
        """.trimIndent()
        val link = decode(json)
        assertEquals("https://example.com/openapi.json#/paths/~1users/get", link.operationRef)
        assertNull(link.operationId)
    }

    // ── operationId ───────────────────────────────────────────────────────────

    @Test
    fun `link by operationId deserializes`() {
        val json = """
            {
              "operationId": "getUserById",
              "description": "Fetch the user created in this response"
            }
        """.trimIndent()
        val link = decode(json)
        assertNull(link.operationRef)
        assertEquals("getUserById", link.operationId)
        assertEquals("Fetch the user created in this response", link.description)
    }

    // ── parameters ────────────────────────────────────────────────────────────

    @Test
    fun `link with expression parameter deserializes`() {
        val json = """
            {
              "operationId": "getUserById",
              "parameters": {
                "userId": "${'$'}response.body#/id"
              }
            }
        """.trimIndent()
        val link = decode(json)
        assertEquals(1, link.parameters.size)
        val userId = assertIs<ExpressionOrValue.Expression>(link.parameters["userId"])
        assertEquals("\$response.body#/id", userId.value)
    }

    @Test
    fun `link with literal value parameter deserializes`() {
        val json = """
            {
              "operationId": "getPetById",
              "parameters": {
                "petId": 42
              }
            }
        """.trimIndent()
        val link = decode(json)
        assertEquals(1, link.parameters.size)
        val petId = assertIs<ExpressionOrValue.Value>(link.parameters["petId"])
        assertEquals(JsonPrimitive(42), petId.value)
    }

    @Test
    fun `link with multiple parameters deserializes`() {
        val json = """
            {
              "operationId": "createMessage",
              "parameters": {
                "userId": "${'$'}response.body#/id",
                "tenantId": "${'$'}request.header.X-Tenant-ID",
                "format": "json"
              }
            }
        """.trimIndent()
        val link = decode(json)
        assertEquals(3, link.parameters.size)
        assertIs<ExpressionOrValue.Expression>(link.parameters["userId"])
        assertIs<ExpressionOrValue.Expression>(link.parameters["tenantId"])
        assertIs<ExpressionOrValue.Value>(link.parameters["format"])
    }

    @Test
    fun `link with string literal value parameter deserializes`() {
        val json = """
            {
              "operationId": "searchPets",
              "parameters": {
                "status": "available"
              }
            }
        """.trimIndent()
        val link = decode(json)
        val status = assertIs<ExpressionOrValue.Value>(link.parameters["status"])
        assertEquals(JsonPrimitive("available"), status.value)
    }

    // ── requestBody ───────────────────────────────────────────────────────────

    @Test
    fun `link with expression requestBody deserializes`() {
        val json = """
            {
              "operationId": "createUser",
              "requestBody": "${'$'}response.body"
            }
        """.trimIndent()
        val link = decode(json)
        val body = assertIs<ExpressionOrValue.Expression>(assertNotNull(link.requestBody))
        assertEquals("\$response.body", body.value)
    }

    @Test
    fun `link with literal requestBody deserializes`() {
        val json = """
            {
              "operationId": "createOrder",
              "requestBody": "fixed-payload"
            }
        """.trimIndent()
        val link = decode(json)
        val body = assertIs<ExpressionOrValue.Value>(assertNotNull(link.requestBody))
        assertEquals(JsonPrimitive("fixed-payload"), body.value)
    }

    // ── server ────────────────────────────────────────────────────────────────

    @Test
    fun `link with server deserializes`() {
        val json = """
            {
              "operationId": "getUserById",
              "server": {
                "url": "https://users.example.com/v2",
                "description": "User service"
              }
            }
        """.trimIndent()
        val link = decode(json)
        val server = assertNotNull(link.server)
        assertEquals("https://users.example.com/v2", server.url)
        assertEquals("User service", server.description)
    }

    // ── full link ─────────────────────────────────────────────────────────────

    @Test
    fun `fully populated link deserializes`() {
        val json = """
            {
              "operationId": "getRepositoryById",
              "parameters": {
                "repoId": "${'$'}response.body#/id",
                "format": "json"
              },
              "requestBody": "${'$'}response.body",
              "description": "Link to the created repository",
              "server": {
                "url": "https://repos.example.com"
              },
              "x-internal": true,
              "x-owner": "platform-team"
            }
        """.trimIndent()
        val link = decode(json)
        assertEquals("getRepositoryById", link.operationId)
        assertNull(link.operationRef)
        assertEquals(2, link.parameters.size)
        assertIs<ExpressionOrValue.Expression>(link.parameters["repoId"])
        assertIs<ExpressionOrValue.Value>(link.parameters["format"])
        assertIs<ExpressionOrValue.Expression>(assertNotNull(link.requestBody))
        assertEquals("Link to the created repository", link.description)
        assertEquals("https://repos.example.com", assertNotNull(link.server).url)
        assertEquals(2, link.extensions.size)
        assertEquals(JsonPrimitive(true), link.extensions["x-internal"])
        assertEquals(JsonPrimitive("platform-team"), link.extensions["x-owner"])
    }

    // ── extensions ────────────────────────────────────────────────────────────

    @Test
    fun `link extensions deserialize`() {
        val json = """
            {
              "operationId": "getUser",
              "x-generated": true,
              "x-version": 2
            }
        """.trimIndent()
        val link = decode(json)
        assertEquals(2, link.extensions.size)
        assertEquals(JsonPrimitive(true), link.extensions["x-generated"])
        assertEquals(JsonPrimitive(2), link.extensions["x-version"])
    }

    @Test
    fun `link without extensions has empty extensions map`() {
        val link = decode("""{"operationId": "getUser"}""")
        assertTrue(link.extensions.isEmpty())
    }

    // ── unknown fields ────────────────────────────────────────────────────────

    @Test
    fun `unknown fields are silently ignored`() {
        val json = """
            {
              "operationId": "getUser",
              "unknownField": "ignored",
              "anotherUnknown": 99
            }
        """.trimIndent()
        val link = decode(json)
        assertEquals("getUser", link.operationId)
        assertTrue(link.extensions.isEmpty())
    }

    // ── YAML ──────────────────────────────────────────────────────────────────

    @Test
    fun `yaml link by operationId deserializes`() {
        val yaml = """
            operationId: getUserById
            description: Fetch the created user
        """.trimIndent()
        val link = OpenAPI.Yaml.decodeFromString(Link.serializer(), yaml)
        assertEquals("getUserById", link.operationId)
        assertEquals("Fetch the created user", link.description)
        assertNull(link.operationRef)
    }

    @Test
    fun `yaml link by operationRef deserializes`() {
        val yaml = """
            operationRef: '#/paths/~1users~1{userId}/get'
        """.trimIndent()
        val link = OpenAPI.Yaml.decodeFromString(Link.serializer(), yaml)
        assertEquals("#/paths/~1users~1{userId}/get", link.operationRef)
        assertNull(link.operationId)
    }

    @Test
    fun `yaml link with parameters deserializes`() {
        val yaml = """
            operationId: getUserById
            parameters:
              userId: ${'$'}response.body#/id
        """.trimIndent()
        val link = OpenAPI.Yaml.decodeFromString(Link.serializer(), yaml)
        assertEquals("getUserById", link.operationId)
        assertEquals(1, link.parameters.size)
        val userId = assertIs<ExpressionOrValue.Expression>(link.parameters["userId"])
        assertEquals("\$response.body#/id", userId.value)
    }

    @Test
    fun `yaml link with server deserializes`() {
        val yaml = """
            operationId: getUserById
            server:
              url: https://users.example.com/v2
              description: User service
        """.trimIndent()
        val link = OpenAPI.Yaml.decodeFromString(Link.serializer(), yaml)
        val server = assertNotNull(link.server)
        assertEquals("https://users.example.com/v2", server.url)
        assertEquals("User service", server.description)
    }
}
