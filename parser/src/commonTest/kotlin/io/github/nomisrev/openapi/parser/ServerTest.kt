package io.github.nomisrev.openapi.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ServerTest {

    // ── Helper ────────────────────────────────────────────────────────────────

    private fun decode(json: String): Server =
        OpenAPI.Json.decodeFromString(Server.serializer(), json)

    // ── Simple URL ────────────────────────────────────────────────────────────

    @Test
    fun `simple url deserializes`() {
        val json = """{"url": "https://api.example.com/v1"}"""
        val server = decode(json)
        assertEquals("https://api.example.com/v1", server.url)
        assertNull(server.description)
        assertNull(server.variables)
    }

    @Test
    fun `server with description deserializes`() {
        val json = """
            {
              "url": "https://api.example.com/v1",
              "description": "Production server"
            }
        """.trimIndent()
        val server = decode(json)
        assertEquals("https://api.example.com/v1", server.url)
        assertEquals("Production server", server.description)
        assertNull(server.variables)
    }

    @Test
    fun `relative url deserializes`() {
        val json = """{"url": "/v1"}"""
        val server = decode(json)
        assertEquals("/v1", server.url)
    }

    // ── Server Variables ──────────────────────────────────────────────────────

    @Test
    fun `server variable with default only deserializes`() {
        val json = """
            {
              "url": "https://{environment}.api.example.com/v1",
              "variables": {
                "environment": {
                  "default": "production"
                }
              }
            }
        """.trimIndent()
        val server = decode(json)
        assertEquals("https://{environment}.api.example.com/v1", server.url)
        val variables = assertNotNull(server.variables)
        assertEquals(1, variables.size)
        val env = assertNotNull(variables["environment"])
        assertEquals("production", env.default)
        assertNull(env.enum)
        assertNull(env.description)
    }

    @Test
    fun `server variable with enum and default deserializes`() {
        val json = """
            {
              "url": "https://{environment}.api.example.com/v1",
              "variables": {
                "environment": {
                  "enum": ["production", "staging", "development"],
                  "default": "production",
                  "description": "The deployment environment"
                }
              }
            }
        """.trimIndent()
        val server = decode(json)
        val variables = assertNotNull(server.variables)
        val env = assertNotNull(variables["environment"])
        assertEquals(listOf("production", "staging", "development"), env.enum)
        assertEquals("production", env.default)
        assertEquals("The deployment environment", env.description)
    }

    @Test
    fun `server with multiple variables deserializes`() {
        val json = """
            {
              "url": "https://{username}.{region}.example.com",
              "variables": {
                "username": {
                  "default": "alice"
                },
                "region": {
                  "enum": ["us-east", "eu-west"],
                  "default": "us-east"
                }
              }
            }
        """.trimIndent()
        val server = decode(json)
        val variables = assertNotNull(server.variables)
        assertEquals(2, variables.size)

        val username = assertNotNull(variables["username"])
        assertEquals("alice", username.default)
        assertNull(username.enum)

        val region = assertNotNull(variables["region"])
        assertEquals("us-east", region.default)
        assertEquals(listOf("us-east", "eu-west"), region.enum)
    }

    // ── Multiple Servers ──────────────────────────────────────────────────────

    @Test
    fun `list of multiple servers deserializes`() {
        val json = """
            [
              {
                "url": "https://api.example.com/v1",
                "description": "Production"
              },
              {
                "url": "https://staging.api.example.com/v1",
                "description": "Staging"
              },
              {
                "url": "http://localhost:8080/v1",
                "description": "Local development"
              }
            ]
        """.trimIndent()
        val servers = OpenAPI.Json.decodeFromString(
            kotlinx.serialization.builtins.ListSerializer(Server.serializer()),
            json,
        )
        assertEquals(3, servers.size)
        assertEquals("https://api.example.com/v1", servers[0].url)
        assertEquals("Production", servers[0].description)
        assertEquals("https://staging.api.example.com/v1", servers[1].url)
        assertEquals("Staging", servers[1].description)
        assertEquals("http://localhost:8080/v1", servers[2].url)
        assertEquals("Local development", servers[2].description)
    }

    // ── Unknown fields ────────────────────────────────────────────────────────

    @Test
    fun `unknown fields are silently ignored`() {
        val json = """
            {
              "url": "https://api.example.com",
              "unknownField": "ignored",
              "anotherUnknown": 99
            }
        """.trimIndent()
        val server = decode(json)
        assertEquals("https://api.example.com", server.url)
    }

    // ── YAML ──────────────────────────────────────────────────────────────────

    @Test
    fun `yaml simple server deserializes`() {
        val yaml = """
            url: https://api.example.com/v1
            description: Production server
        """.trimIndent()
        val server = OpenAPI.Yaml.decodeFromString(Server.serializer(), yaml)
        assertEquals("https://api.example.com/v1", server.url)
        assertEquals("Production server", server.description)
        assertNull(server.variables)
    }

    @Test
    fun `yaml server with variables enum and default deserializes`() {
        val yaml = """
            url: "https://{environment}.api.example.com/v1"
            variables:
              environment:
                enum:
                  - production
                  - staging
                  - development
                default: production
                description: The deployment environment
        """.trimIndent()
        val server = OpenAPI.Yaml.decodeFromString(Server.serializer(), yaml)
        assertEquals("https://{environment}.api.example.com/v1", server.url)
        val variables = assertNotNull(server.variables)
        val env = assertNotNull(variables["environment"])
        assertEquals(listOf("production", "staging", "development"), env.enum)
        assertEquals("production", env.default)
        assertEquals("The deployment environment", env.description)
    }
}
