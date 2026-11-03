package io.github.nomisrev.render

import io.github.nomisrev.render.test.object_.type.array.nullable.AnyOfNullableAlias
import io.github.nomisrev.render.test.object_.type.array.nullable.NullableAlias
import io.github.nomisrev.render.test.object_.type.array.nullable.TypeArrayNullable
import io.github.nomisrev.render.test.object_.type.array.nullable.optional.OptionalNullableMessage
import io.github.nomisrev.render.test.object_.type.array.nullable.required.RequiredNullableMessage
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Instant

/**
 * OpenAPI 3.1 `type: [X, "null"]` is a nullable `X` on the wire: `"x"` or `null`.
 * Uses the generated goldens under `kotlinTestData/object/type-array-nullable*` directly.
 */
class TypeArrayNullableRoundTripTest {
    private val json = Json

    @Test
    fun requiredNullablePropertyAcceptsNullAndValue() {
        assertEquals(RequiredNullableMessage(null), json.decodeFromString<RequiredNullableMessage>("""{"message":null}"""))
        assertEquals(RequiredNullableMessage("x"), json.decodeFromString<RequiredNullableMessage>("""{"message":"x"}"""))
        assertEquals("""{"message":null}""", json.encodeToString(RequiredNullableMessage(null)))
        assertEquals("""{"message":"x"}""", json.encodeToString(RequiredNullableMessage("x")))
        // Required: the key must be present, even when its value is null.
        assertFailsWith<SerializationException> { json.decodeFromString<RequiredNullableMessage>("""{}""") }
    }

    @Test
    fun optionalNullablePropertyAcceptsNullValueAndAbsence() {
        assertEquals(OptionalNullableMessage(null), json.decodeFromString<OptionalNullableMessage>("""{"message":null}"""))
        assertEquals(OptionalNullableMessage("x"), json.decodeFromString<OptionalNullableMessage>("""{"message":"x"}"""))
        assertEquals(OptionalNullableMessage(null), json.decodeFromString<OptionalNullableMessage>("""{}"""))
        assertEquals("""{"message":"x"}""", json.encodeToString(OptionalNullableMessage("x")))
    }

    private val allValues = """
        {
          "requiredString": "s",
          "optionalString": "o",
          "nullFirst": "n",
          "anyOfRequiredString": "a",
          "anyOfOptionalString": "b",
          "int32": 1,
          "int64": 9007199254740991,
          "number": 1.5,
          "flag": true,
          "createdAt": "2024-01-02T03:04:05Z",
          "status": "active",
          "owner": { "name": "simon" },
          "tags": ["a", "b"],
          "alias": "al",
          "anyOfAlias": "any",
          "single": "one",
          "stringOrInt": "si",
          "stringOrIntOrNull": 2
        }
    """.trimIndent()

    private val nullKeys = listOf(
        "requiredString", "optionalString", "nullFirst", "int32", "int64", "number", "flag",
        "createdAt", "status", "owner", "tags", "alias", "anyOfAlias", "stringOrIntOrNull",
    )

    @Test
    fun everySingleTypeVariantDecodesItsValue() {
        val decoded = json.decodeFromString<TypeArrayNullable>(allValues)
        assertEquals("s", decoded.requiredString)
        assertEquals("n", decoded.nullFirst)
        assertEquals(1, decoded.int32)
        assertEquals(9007199254740991L, decoded.int64)
        assertEquals(1.5, decoded.number)
        assertEquals(true, decoded.flag)
        assertEquals(Instant.parse("2024-01-02T03:04:05Z"), decoded.createdAt)
        assertEquals(TypeArrayNullable.Status.Active, decoded.status)
        assertEquals(TypeArrayNullable.Owner("simon"), decoded.owner)
        assertEquals(listOf("a", "b"), decoded.tags)
        assertEquals(NullableAlias("al"), decoded.alias)
        assertEquals(AnyOfNullableAlias("any"), decoded.anyOfAlias)
        assertEquals("one", decoded.single)
        assertEquals(json.decodeFromString<TypeArrayNullable>(json.encodeToString(decoded)), decoded)
    }

    @Test
    fun everyNullableVariantDecodesNull() {
        val withNulls = JsonObject(
            json.parseToJsonElement(allValues).jsonObject + nullKeys.associateWith { kotlinx.serialization.json.JsonNull }
        )
        val decoded = json.decodeFromJsonElement(TypeArrayNullable.serializer(), withNulls)
        assertEquals(
            listOf(null, null, null, null, null, null, null, null, null, null, null, null, null, null),
            listOf(
                decoded.requiredString, decoded.optionalString, decoded.nullFirst, decoded.int32, decoded.int64,
                decoded.number, decoded.flag, decoded.createdAt, decoded.status, decoded.owner, decoded.tags,
                decoded.alias, decoded.anyOfAlias, decoded.stringOrIntOrNull,
            )
        )
        assertEquals(JsonPrimitive("one"), json.encodeToJsonElement(TypeArrayNullable.serializer(), decoded).jsonObject["single"])
    }
}
