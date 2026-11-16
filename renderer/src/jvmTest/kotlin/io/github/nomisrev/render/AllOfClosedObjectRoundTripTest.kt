package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.allof.closed.object_.ApiException
import io.github.nomisrev.render.test.client.allof.closed.object_.Tagged
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * An `allOf` extending a closed object decodes what servers send, the properties of both members; a closed object
 * next to a typed `additionalProperties` decodes no other key. Uses the goldens under
 * `kotlinTestData/client/allof-closed-object` directly.
 */
class AllOfClosedObjectRoundTripTest {
    private val json = Json

    @Test
    fun anExtendedClosedObjectHasThePropertiesOfBoth() {
        val text = """{ "message": "Not found", "statusCode": 404 }"""
        val decoded = json.decodeFromString<ApiException>(text)
        assertEquals(ApiException(message = "Not found", statusCode = 404), decoded)
        assertEquals(json.parseToJsonElement(text), json.parseToJsonElement(json.encodeToString(decoded)))
    }

    @Test
    fun aClosedObjectNextToAMapDecodesNoOtherKey() {
        assertEquals(Tagged(tag = "t"), json.decodeFromString<Tagged>("""{ "tag": "t" }"""))
        assertFailsWith<SerializationException> { json.decodeFromString<Tagged>("""{ "tag": "t", "other": "x" }""") }
    }
}
