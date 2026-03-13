package io.github.nomisrev.openapi.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ExternalDocsTest {

    // ── Helper ────────────────────────────────────────────────────────────────

    private fun decode(json: String): ExternalDocs =
        OpenAPI.Json.decodeFromString(ExternalDocs.serializer(), json)

    // ── Layer 2 scenarios ─────────────────────────────────────────────────────

    @Test
    fun `url only deserializes`() {
        val json = """{"url": "https://example.com/docs"}"""
        val externalDocs = decode(json)
        assertEquals("https://example.com/docs", externalDocs.url)
        assertNull(externalDocs.description)
    }

    @Test
    fun `url and description deserializes`() {
        val json = """
            {
              "url": "https://example.com/docs",
              "description": "Extended API documentation"
            }
        """.trimIndent()

        val externalDocs = decode(json)
        assertEquals("https://example.com/docs", externalDocs.url)
        assertEquals("Extended API documentation", externalDocs.description)
    }
}
