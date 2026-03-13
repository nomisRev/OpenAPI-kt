package io.github.nomisrev.openapi.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class XmlTest {

    // ── Helper ────────────────────────────────────────────────────────────────

    private fun decode(json: String): Xml =
        OpenAPI.Json.decodeFromString(Xml.serializer(), json)

    // ── Layer 2 scenarios ─────────────────────────────────────────────────────

    @Test
    fun `all xml properties deserialize`() {
        val json = """
            {
              "name": "book",
              "namespace": "https://example.com/books",
              "prefix": "bk",
              "attribute": true,
              "wrapped": false
            }
        """.trimIndent()

        val xml = decode(json)
        assertEquals("book", xml.name)
        assertEquals("https://example.com/books", xml.namespace)
        assertEquals("bk", xml.prefix)
        assertEquals(true, xml.attribute)
        assertEquals(false, xml.wrapped)
    }

    @Test
    fun `attribute and wrapped combo deserialize`() {
        val json = """
            {
              "name": "item",
              "attribute": true,
              "wrapped": true
            }
        """.trimIndent()

        val xml = decode(json)
        assertEquals("item", xml.name)
        assertEquals(true, xml.attribute)
        assertEquals(true, xml.wrapped)
        assertNull(xml.namespace)
        assertNull(xml.prefix)
    }

    @Test
    fun `yaml xml with attribute and wrapped deserializes`() {
        val yaml = """
            name: item
            attribute: true
            wrapped: true
        """.trimIndent()

        val xml = OpenAPI.Yaml.decodeFromString(Xml.serializer(), yaml)
        assertEquals("item", xml.name)
        assertEquals(true, xml.attribute)
        assertEquals(true, xml.wrapped)
    }
}
