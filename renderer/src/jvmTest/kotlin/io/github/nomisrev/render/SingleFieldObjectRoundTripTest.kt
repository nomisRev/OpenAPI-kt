package io.github.nomisrev.render

import io.github.nomisrev.render.test.collection.basic.Tags
import io.github.nomisrev.render.test.discriminated.basic.User
import io.github.nomisrev.render.test.object_.neutral.leaf.no.split.Enabled
import io.github.nomisrev.render.test.object_.single.`field`.optional.SingleOptional
import io.github.nomisrev.render.test.object_.single.`field`.renamed.SingleRenamed
import io.github.nomisrev.render.test.object_.single.`field`.required.SingleRequired
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * An OpenAPI `type: object` schema with exactly one property is a JSON object on the wire,
 * so its generated model must encode to and decode from `{"message":"x"}`, not the bare `"x"`.
 * Uses the generated goldens under `kotlinTestData/` directly.
 */
class SingleFieldObjectRoundTripTest {
    private val json = Json

    @Test
    fun requiredSinglePropertyIsAJsonObject() {
        assertEquals("""{"message":"x"}""", json.encodeToString(SingleRequired("x")))
        assertEquals(SingleRequired("x"), json.decodeFromString<SingleRequired>("""{"message":"x"}"""))
    }

    @Test
    fun optionalSinglePropertyIsAJsonObject() {
        assertEquals("""{"message":"x"}""", json.encodeToString(SingleOptional("x")))
        assertEquals(SingleOptional("x"), json.decodeFromString<SingleOptional>("""{"message":"x"}"""))
        assertEquals(SingleOptional(null), json.decodeFromString<SingleOptional>("""{}"""))
    }

    @Test
    fun renamedSinglePropertyUsesItsSerialName() {
        assertEquals("""{"error_message":"x"}""", json.encodeToString(SingleRenamed("x")))
        assertEquals(SingleRenamed("x"), json.decodeFromString<SingleRenamed>("""{"error_message":"x"}"""))
    }

    @Test
    fun singlePropertyDiscriminatedSubtypeIsAJsonObject() {
        val anonymous: User = User.Anonymous(1)
        assertEquals("""{"type":"anonymous","id":1}""", json.encodeToString<User>(anonymous))
        assertEquals(anonymous, json.decodeFromString<User>("""{"type":"anonymous","id":1}"""))
    }

    // Synthetic wrappers around non-object schemas stay value classes: their wire format IS the bare value.
    @Test
    fun arrayAndScalarSchemaWrappersAreTheBareValue() {
        assertEquals("""["a","b"]""", json.encodeToString(Tags(listOf("a", "b"))))
        assertEquals(Tags(listOf("a", "b")), json.decodeFromString<Tags>("""["a","b"]"""))
        assertEquals("true", json.encodeToString(Enabled(true)))
        assertEquals(Enabled(true), json.decodeFromString<Enabled>("true"))
    }
}
