package io.github.nomisrev.openapi.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TagTest {

    private fun decode(json: String): Tag =
        OpenAPI.Json.decodeFromString(Tag.serializer(), json)

    @Test
    fun `tag with external docs deserializes`() {
        val json = """
            {
              "name": "pets",
              "description": "Operations about pets",
              "externalDocs": {
                "description": "Find more info here",
                "url": "https://example.com/docs/tags/pets"
              }
            }
        """.trimIndent()

        val tag = decode(json)
        assertEquals("pets", tag.name)
        assertEquals("Operations about pets", tag.description)
        val docs = kotlin.test.assertNotNull(tag.externalDocs)
        assertEquals("Find more info here", docs.description)
        assertEquals("https://example.com/docs/tags/pets", docs.url)
    }

    @Test
    fun `tag name only deserializes`() {
        val tag = decode("""{"name":"billing"}""")
        assertEquals("billing", tag.name)
        assertNull(tag.description)
        assertNull(tag.externalDocs)
    }
}
