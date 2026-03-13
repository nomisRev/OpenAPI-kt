package io.github.nomisrev.openapi.parser

import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class InfoTest {

    // ── Helper ────────────────────────────────────────────────────────────────

    private fun decode(json: String): Info =
        OpenAPI.Json.decodeFromString(Info.serializer(), json)

    // ── Minimal ───────────────────────────────────────────────────────────────

    @Test
    fun `minimal info with title and version only deserializes`() {
        val json = """{"title": "My API", "version": "1.0.0"}"""
        val info = decode(json)
        assertEquals("My API", info.title)
        assertEquals("1.0.0", info.version)
        assertNull(info.description)
        assertNull(info.termsOfService)
        assertNull(info.license)
        assertTrue(info.extensions.isEmpty())
    }

    @Test
    fun `minimal info has default empty contact`() {
        val json = """{"title": "My API", "version": "1.0.0"}"""
        val info = decode(json)
        // contact defaults to Contact() when absent
        val contact = assertNotNull(info.contact)
        assertNull(contact.name)
        assertNull(contact.url)
        assertNull(contact.email)
        assertTrue(contact.extensions.isEmpty())
    }

    // ── Full info ─────────────────────────────────────────────────────────────

    @Test
    fun `full info with contact and license deserializes`() {
        val json = """
            {
              "title": "Petstore",
              "version": "1.2.3",
              "description": "A sample API",
              "termsOfService": "https://example.com/tos",
              "contact": {
                "name": "API Support",
                "url": "https://example.com/support",
                "email": "support@example.com"
              },
              "license": {
                "name": "Apache 2.0",
                "url": "https://www.apache.org/licenses/LICENSE-2.0"
              }
            }
        """.trimIndent()

        val info = decode(json)
        assertEquals("Petstore", info.title)
        assertEquals("1.2.3", info.version)
        assertEquals("A sample API", info.description)
        assertEquals("https://example.com/tos", info.termsOfService)

        val contact = assertNotNull(info.contact)
        assertEquals("API Support", contact.name)
        assertEquals("https://example.com/support", contact.url)
        assertEquals("support@example.com", contact.email)

        val license = assertNotNull(info.license)
        assertEquals("Apache 2.0", license.name)
        assertEquals("https://www.apache.org/licenses/LICENSE-2.0", license.url)
    }

    // ── Contact ───────────────────────────────────────────────────────────────

    @Test
    fun `contact with name only deserializes`() {
        val json = """
            {
              "title": "API",
              "version": "1.0",
              "contact": {"name": "John Doe"}
            }
        """.trimIndent()

        val info = decode(json)
        val contact = assertNotNull(info.contact)
        assertEquals("John Doe", contact.name)
        assertNull(contact.url)
        assertNull(contact.email)
    }

    @Test
    fun `explicit null contact deserializes to null`() {
        val json = """
            {
              "title": "API",
              "version": "1.0",
              "contact": null
            }
        """.trimIndent()

        val info = decode(json)
        assertNull(info.contact)
    }

    @Test
    fun `contact with extensions deserializes`() {
        val json = """
            {
              "title": "API",
              "version": "1.0",
              "contact": {
                "name": "Support",
                "x-internal-id": "team-42"
              }
            }
        """.trimIndent()

        val info = decode(json)
        val contact = assertNotNull(info.contact)
        assertEquals("Support", contact.name)
        assertEquals(JsonPrimitive("team-42"), contact.extensions["x-internal-id"])
    }

    // ── License ───────────────────────────────────────────────────────────────

    @Test
    fun `license with name only deserializes`() {
        val json = """
            {
              "title": "API",
              "version": "1.0",
              "license": {"name": "MIT"}
            }
        """.trimIndent()

        val info = decode(json)
        val license = assertNotNull(info.license)
        assertEquals("MIT", license.name)
        assertNull(license.url)
        assertTrue(license.extensions.isEmpty())
    }

    @Test
    fun `license with identifier deserializes 3_1_x`() {
        // 3.1.x added the SPDX identifier field as an alternative to url
        val json = """
            {
              "title": "API",
              "version": "1.0",
              "license": {
                "name": "Apache 2.0",
                "identifier": "Apache-2.0"
              }
            }
        """.trimIndent()

        val info = decode(json)
        val license = assertNotNull(info.license)
        assertEquals("Apache 2.0", license.name)
        assertNull(license.url)
        assertEquals("Apache-2.0", license.identifier)
    }

    @Test
    fun `license with extensions deserializes`() {
        val json = """
            {
              "title": "API",
              "version": "1.0",
              "license": {
                "name": "MIT",
                "x-reviewed": true
              }
            }
        """.trimIndent()

        val info = decode(json)
        val license = assertNotNull(info.license)
        assertEquals("MIT", license.name)
        assertEquals(JsonPrimitive(true), license.extensions["x-reviewed"])
    }

    // ── Extensions ────────────────────────────────────────────────────────────

    @Test
    fun `info with extensions deserializes`() {
        val json = """
            {
              "title": "API",
              "version": "1.0",
              "x-logo": "https://example.com/logo.png",
              "x-internal": true
            }
        """.trimIndent()

        val info = decode(json)
        assertEquals("API", info.title)
        assertEquals(JsonPrimitive("https://example.com/logo.png"), info.extensions["x-logo"])
        assertEquals(JsonPrimitive(true), info.extensions["x-internal"])
    }

    @Test
    fun `info with multiple extensions deserializes`() {
        val json = """
            {
              "title": "API",
              "version": "2.0",
              "x-string": "value",
              "x-number": 42,
              "x-bool": false,
              "x-null": null
            }
        """.trimIndent()

        val info = decode(json)
        assertEquals(4, info.extensions.size)
        assertEquals(JsonPrimitive("value"), info.extensions["x-string"])
        assertEquals(JsonPrimitive(42), info.extensions["x-number"])
        assertEquals(JsonPrimitive(false), info.extensions["x-bool"])
        assertFalse(info.extensions.isEmpty())
    }

    // ── Unknown fields ────────────────────────────────────────────────────────

    @Test
    fun `unknown fields are silently ignored`() {
        val json = """
            {
              "title": "API",
              "version": "1.0",
              "unknownField": "ignored",
              "anotherUnknown": 99
            }
        """.trimIndent()

        val info = decode(json)
        assertEquals("API", info.title)
        assertEquals("1.0", info.version)
    }

    // ── YAML ──────────────────────────────────────────────────────────────────

    @Test
    fun `yaml minimal info deserializes`() {
        val yaml = """
            title: My API
            version: 1.0.0
        """.trimIndent()

        val info = OpenAPI.Yaml.decodeFromString(Info.serializer(), yaml)
        assertEquals("My API", info.title)
        assertEquals("1.0.0", info.version)
        assertNull(info.description)
        assertNull(info.license)
    }

    @Test
    fun `yaml full info with contact and license deserializes`() {
        val yaml = """
            title: Petstore
            version: 1.2.3
            description: A sample API
            termsOfService: https://example.com/tos
            contact:
              name: API Support
              url: https://example.com/support
              email: support@example.com
            license:
              name: Apache 2.0
              url: https://www.apache.org/licenses/LICENSE-2.0
        """.trimIndent()

        val info = OpenAPI.Yaml.decodeFromString(Info.serializer(), yaml)
        assertEquals("Petstore", info.title)
        assertEquals("1.2.3", info.version)
        assertEquals("A sample API", info.description)
        assertEquals("https://example.com/tos", info.termsOfService)

        val contact = assertNotNull(info.contact)
        assertEquals("API Support", contact.name)
        assertEquals("https://example.com/support", contact.url)
        assertEquals("support@example.com", contact.email)

        val license = assertNotNull(info.license)
        assertEquals("Apache 2.0", license.name)
        assertEquals("https://www.apache.org/licenses/LICENSE-2.0", license.url)
    }
}
