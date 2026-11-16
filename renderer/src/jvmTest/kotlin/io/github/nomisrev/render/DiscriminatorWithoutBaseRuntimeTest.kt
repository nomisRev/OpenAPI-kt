package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.discriminator.without.base.Pet
import io.github.nomisrev.render.test.client.discriminator.without.base.PetsApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Runtime check for the `client/discriminator-without-base` golden: each tag decodes to its case (`feline` to the
 * `Cat` schema, `Dog` by its schema name, `bird` with the properties of both its `allOf` members and the one next to
 * them), encodes back with its tag, and a tag the mapping does not select fails to decode instead of becoming a base
 * value, which the mapping does not list.
 */
class DiscriminatorWithoutBaseRuntimeTest {
    private val pets = """
        [
          {"petType":"feline","name":"Tom","huntingSkill":"lazy"},
          {"petType":"Dog","name":"Rex","packSize":3},
          {"petType":"bird","name":"Tweety","span":0.2,"canFly":true}
        ]
    """.trimIndent()

    private fun api(body: String, json: Json = Json) = PetsApi(HttpClient(MockEngine {
        respond(body, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
    }) {
        install(ContentNegotiation) { json(json) }
    })

    @Test
    fun eachTagDecodesToItsCase() = runBlocking {
        assertEquals(
            listOf(
                Pet.Feline(name = "Tom", huntingSkill = "lazy"),
                Pet.Dog(name = "Rex", packSize = 3),
                Pet.Bird(name = "Tweety", span = 0.2, canFly = true),
            ),
            api(pets).pets.get(),
        )
    }

    @Test
    fun anOperationReturningASubtypeReturnsItsCase() = runBlocking {
        // The case's own serializer does not know the tag, so it is ignored here: see
        // issues/discriminated-subtype-serializer-without-tag.md.
        val cat: Pet.Feline = api("""{"petType":"feline","name":"Tom"}""", Json { ignoreUnknownKeys = true })
            .cats.id("1").get()
        assertEquals(Pet.Feline(name = "Tom"), cat)
    }

    @Test
    fun aCaseEncodesWithItsTag() {
        assertEquals(
            """{"petType":"bird","name":"Tweety","span":0.2}""",
            Json.encodeToString<Pet>(Pet.Bird(name = "Tweety", span = 0.2)),
        )
    }

    @Test
    fun aTagTheMappingDoesNotSelectFailsToDecode() {
        assertFailsWith<SerializationException> {
            Json.decodeFromString<Pet>("""{"petType":"Pet","name":"Plain"}""")
        }
        assertFailsWith<SerializationException> {
            Json.decodeFromString<Pet>("""{"petType":"fish","name":"Nemo"}""")
        }
    }
}
