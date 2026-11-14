package io.github.nomisrev.render

import io.github.nomisrev.render.test.object_.optional.encoding.OptionalEncoding
import io.github.nomisrev.render.test.object_.optional.encoding.additional.OptionalEncodingAdditional
import io.github.nomisrev.render.test.object_.optional.encoding.Optional
import io.github.nomisrev.render.test.object_.optional.encoding.OptionalSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Encodes the `object/optional-encoding*` goldens with the `Json` configurations a generated client meets:
 * Ktor's `ContentNegotiation { json() }` uses `DefaultJson` (`encodeDefaults = true`, `explicitNulls = true`),
 * and a plain `Json {}` (`encodeDefaults = false`). The generated client never owns the `Json` instance,
 * so what goes on the wire must not depend on it where the spec is unambiguous.
 */
class OptionalPropertyEncodingTest {
    private val ktorDefaultJson = Json {
        encodeDefaults = true
        explicitNulls = true
    }
    private val plainJson = Json {}
    private val noExplicitNullsJson = Json { explicitNulls = false }
    private val jsonConfigs = listOf(
        "Ktor DefaultJson" to ktorDefaultJson,
        "Json {}" to plainJson,
    )
    private val configs = jsonConfigs + ("Json { explicitNulls = false }" to noExplicitNullsJson)

    private val unset = OptionalEncoding(
        requiredNotNullable = "x",
        requiredNullable = null,
        requiredNullableWithDefault = null,
    )

    private fun Json.encode(value: OptionalEncoding): JsonObject =
        encodeToJsonElement(OptionalEncoding.serializer(), value).jsonObject

    /** Optional, not nullable: `null` is not a valid value, so an unset property must be omitted. */
    @Test
    fun optionalNotNullablePropertyIsOmittedWhenUnset() = configs.forEach { [name, json] ->
        val encoded = json.encode(unset)
        listOf("optionalNotNullable", "optionalNotNullableWithDefault", "nested", "choice").forEach { key ->
            assertEquals(false, key in encoded, "$name encoded unset optional non-nullable '$key': $encoded")
        }
    }

    @Test
    fun optionalNotNullablePropertyIsEncodedWhenSet() = configs.forEach { [name, json] ->
        val encoded = json.encode(
            unset.copy(
                optionalNotNullable = "o",
                optionalNotNullableWithDefault = 10,
                nested = OptionalEncoding.Nested("n"),
                choice = OptionalEncoding.Choice.KindAndLabel(OptionalEncoding.Choice.KindAndLabel.Kind.A),
            )
        )
        assertEquals(JsonPrimitive("o"), encoded["optionalNotNullable"], name)
        // A value equal to the spec default is still sent: the Kotlin default is `null`, not the spec default.
        assertEquals(JsonPrimitive(10), encoded["optionalNotNullableWithDefault"], name)
        // Nested objects and union case objects follow the same rule for their own optional properties.
        assertEquals(json.parseToJsonElement("""{"id":"n"}"""), encoded["nested"], name)
        assertEquals(json.parseToJsonElement("""{"kind":"a"}"""), encoded["choice"], name)
    }

    /** Required and nullable: the key must always be present, `null` included. */
    @Test
    fun requiredNullablePropertyIsAlwaysEncoded() = jsonConfigs.forEach { [name, json] ->
        val encoded = json.encode(unset)
        assertEquals(JsonPrimitive("x"), encoded["requiredNotNullable"], name)
        assertEquals(JsonNull, encoded["requiredNullable"], "$name: $encoded")
        assertEquals(JsonNull, encoded["requiredNullableWithDefault"], "$name: $encoded")
        assertEquals(JsonPrimitive("r"), json.encode(unset.copy(requiredNullable = "r"))["requiredNullable"], name)
    }

    /**
     * Optional and nullable: absent, `null` and a value are three different states, whatever the `Json`
     * configuration (e.g. PATCH "clear" vs "leave unchanged").
     */
    @Test
    fun optionalNullablePropertySendsItsStateWhateverTheJsonConfiguration() = configs.forEach { [name, json] ->
        val absent = json.encode(unset)
        assertEquals(false, "optionalNullable" in absent, "$name unset: $absent")
        val explicitNull = json.encode(unset.copy(optionalNullable = Optional.Null))
        assertEquals(JsonNull, explicitNull["optionalNullable"], "$name Null: $explicitNull")
        assertEquals(
            JsonPrimitive("v"),
            json.encode(unset.copy(optionalNullable = Optional.Value("v")))["optionalNullable"],
            name,
        )
    }

    @Test
    fun optionalNullablePropertyDecodesToItsState() = configs.forEach { [name, json] ->
        fun decode(body: String) =
            json.decodeFromString(OptionalEncoding.serializer(), """{"requiredNotNullable":"x","requiredNullable":null,"requiredNullableWithDefault":null$body}""")
        assertEquals(Optional.Absent, decode("").optionalNullable, name)
        assertEquals(Optional.Null, decode(""","optionalNullable":null""").optionalNullable, name)
        assertEquals(Optional.Value("v"), decode(""","optionalNullable":"v"""").optionalNullable, name)
    }

    @Test
    fun optionalNullablePropertyDecodesToNullEvenWhenCoercingInputValues() {
        val json = Json { coerceInputValues = true }
        val decoded = json.decodeFromString(
            OptionalEncoding.serializer(),
            """{"requiredNotNullable":"x","requiredNullable":null,"requiredNullableWithDefault":null,"optionalNullable":null}""",
        )
        assertEquals(Optional.Null, decoded.optionalNullable)
    }

    @Test
    fun nestedOptionalNullablePropertyFollowsTheSameContract() = configs.forEach { [name, json] ->
        fun encode(nested: OptionalEncoding.Nested) =
            json.encodeToString(OptionalEncoding.Nested.serializer(), nested)
        assertEquals("""{"id":"n"}""", encode(OptionalEncoding.Nested("n")), name)
        assertEquals("""{"id":"n","maybe":null}""", encode(OptionalEncoding.Nested("n", maybe = Optional.Null)), name)
        assertEquals("""{"id":"n","maybe":"m"}""", encode(OptionalEncoding.Nested("n", maybe = Optional.Value("m"))), name)
    }

    @Test
    fun absentIsNotEncodableOutsideAProperty() {
        assertFailsWith<SerializationException> {
            ktorDefaultJson.encodeToString(OptionalSerializer(String.serializer()), Optional.Absent)
        }
    }

    @Test
    fun optionalNotNullablePropertyIsOmittedWithAdditionalProperties() = configs.forEach { [name, json] ->
        val value = OptionalEncodingAdditional(id = "1", additional = mapOf("extra" to "e"))
        assertEquals(
            """{"id":"1","extra":"e"}""",
            json.encodeToString(OptionalEncodingAdditional.serializer(), value),
            name,
        )
    }
}
