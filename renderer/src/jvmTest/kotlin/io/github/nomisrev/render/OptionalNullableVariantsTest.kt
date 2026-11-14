package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.optional.nullable.variants.Extended
import io.github.nomisrev.render.test.client.optional.nullable.variants.Optional
import io.github.nomisrev.render.test.client.optional.nullable.variants.Pet
import io.github.nomisrev.render.test.client.optional.nullable.variants.WithAdditional
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The tri-state contract of `OptionalPropertyEncodingTest` for the other places a model gets an optional and
 * nullable property: an allOf result, the subtypes of a discriminated object, an object with a hand-written
 * serializer (additional properties) and a spec default (the Kotlin default stays `Absent`, not the spec's `5`).
 */
class OptionalNullableVariantsTest {
    private val configs = listOf(
        "Ktor DefaultJson" to Json { encodeDefaults = true; explicitNulls = true },
        "Json {}" to Json {},
        "Json { explicitNulls = false }" to Json { explicitNulls = false },
    )

    @Test
    fun anAllOfResultFollowsTheContract() = configs.forEach { [name, json] ->
        fun encode(value: Extended) = json.encodeToString(Extended.serializer(), value)
        assertEquals("""{"id":"i"}""", encode(Extended(id = "i")), name)
        assertEquals(
            """{"id":"i","note":null,"limit":5,"extra":null}""",
            encode(Extended(id = "i", note = Optional.Null, limit = Optional.Value(5), extra = Optional.Null)),
            name,
        )
        assertEquals(Optional.Absent, json.decodeFromString(Extended.serializer(), """{"id":"i"}""").limit, name)
        assertEquals(
            Optional.Null,
            json.decodeFromString(Extended.serializer(), """{"id":"i","limit":null}""").limit,
            name,
        )
    }

    @Test
    fun theSubtypesOfADiscriminatedObjectFollowTheContract() = configs.forEach { [name, json] ->
        fun encode(value: Pet) = json.encodeToString(Pet.serializer(), value)
        assertEquals("""{"type":"cat"}""", encode(Pet.Cat()), name)
        assertEquals("""{"type":"cat","nickname":null}""", encode(Pet.Cat(nickname = Optional.Null)), name)
        assertEquals(
            """{"type":"dog","nickname":"n","tricks":null}""",
            encode(Pet.Dog(nickname = Optional.Value("n"), tricks = Optional.Null)),
            name,
        )
        assertEquals(Pet.Cat(), json.decodeFromString(Pet.serializer(), """{"type":"cat"}"""), name)
        assertEquals(
            Pet.Dog(nickname = Optional.Null, tricks = Optional.Value(listOf("sit"))),
            json.decodeFromString(Pet.serializer(), """{"type":"dog","nickname":null,"tricks":["sit"]}"""),
            name,
        )
    }

    @Test
    fun anObjectWithAdditionalPropertiesFollowsTheContract() = configs.forEach { [name, json] ->
        fun encode(value: WithAdditional) = json.encodeToString(WithAdditional.serializer(), value)
        assertEquals("""{"id":"i","x":"y"}""", encode(WithAdditional("i", additional = mapOf("x" to "y"))), name)
        assertEquals("""{"id":"i","note":null}""", encode(WithAdditional("i", note = Optional.Null)), name)
        assertEquals(
            WithAdditional("i", note = Optional.Null, additional = mapOf("x" to "y")),
            json.decodeFromString(WithAdditional.serializer(), """{"id":"i","note":null,"x":"y"}"""),
            name,
        )
        assertEquals(
            Optional.Absent,
            json.decodeFromString(WithAdditional.serializer(), """{"id":"i"}""").note,
            name,
        )
    }
}
