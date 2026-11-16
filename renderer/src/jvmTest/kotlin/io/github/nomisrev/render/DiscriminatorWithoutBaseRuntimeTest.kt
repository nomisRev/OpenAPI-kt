package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.discriminator.without.base.Owner
import io.github.nomisrev.render.test.client.discriminator.without.base.Pet
import io.github.nomisrev.render.test.client.discriminator.without.base.PetsApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import io.ktor.serialization.JsonConvertException
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
 *
 * A case used on its own (an operation's body, a property, a list item, a union branch) is serialized by the case's
 * serializer, not the base's, so that serializer writes and checks the tag itself. The default `Json` throughout: a tag
 * the case's serializer does not know is an unknown key there.
 */
class DiscriminatorWithoutBaseRuntimeTest {
    private val pets = """
        [
          {"petType":"feline","name":"Tom","huntingSkill":"lazy"},
          {"petType":"Dog","name":"Rex","packSize":3},
          {"petType":"bird","name":"Tweety","span":0.2,"canFly":true}
        ]
    """.trimIndent()

    private fun api(body: String, status: HttpStatusCode = HttpStatusCode.OK, sent: MutableList<String> = mutableListOf()) =
        PetsApi(HttpClient(MockEngine { request ->
            (request.body as? TextContent)?.let { sent += it.text }
            respond(body, status, headersOf(HttpHeaders.ContentType, "application/json"))
        }) {
            install(ContentNegotiation) { json() }
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
        val cat: Pet.Feline = api("""{"petType":"feline","name":"Tom"}""").cats.id("1").get()
        assertEquals(Pet.Feline(name = "Tom"), cat)
    }

    @Test
    fun aRequestBodyOfASubtypeIsSentWithItsTag() = runBlocking {
        val sent = mutableListOf<String>()
        val created = api("""{"petType":"feline","name":"Tom"}""", HttpStatusCode.Created, sent)
            .cats.post(Pet.Feline(name = "Tom", huntingSkill = "lazy"))
        assertEquals(listOf("""{"petType":"feline","name":"Tom","huntingSkill":"lazy"}"""), sent)
        assertEquals(Pet.Feline(name = "Tom"), created)
    }

    @Test
    fun aBodyOfAnotherCaseIsAnError() {
        assertFailsWith<JsonConvertException> {
            runBlocking { api("""{"petType":"bird","name":"Tweety","span":0.2}""").cats.id("1").get() }
        }
    }

    @Test
    fun aPropertyAListItemAndAUnionBranchOfASubtypeCarryTheTag() = runBlocking {
        val json = """
            {"cat":{"petType":"feline","name":"Tom"},"cats":[{"petType":"feline","name":"Tim"}],"pet":{"petType":"feline","name":"Tam"},"favourite":{"petType":"feline","name":"Tum"}}
        """.trimIndent()
        val owner = Owner(
            cat = Pet.Feline(name = "Tom"),
            cats = listOf(Pet.Feline(name = "Tim")),
            pet = Pet.Feline(name = "Tam"),
            favourite = Owner.Favourite.CaseFeline(Pet.Feline(name = "Tum")),
        )
        assertEquals(owner, api(json).owners.id("1").get())
        assertEquals(json, Json.encodeToString(owner))
    }

    @Test
    fun aCaseEncodesWithItsTagAsItselfAndAsTheBase() {
        val bird = Pet.Bird(name = "Tweety", span = 0.2)
        val expected = """{"petType":"bird","name":"Tweety","span":0.2}"""
        assertEquals(expected, Json.encodeToString<Pet>(bird))
        assertEquals(expected, Json.encodeToString(bird))
    }

    @Test
    fun theTagIsNotAnAdditionalProperty() {
        val dog = Pet.Dog(name = "Rex", packSize = 3, additional = mapOf("toys" to 2))
        val json = """{"petType":"Dog","name":"Rex","packSize":3,"toys":2}"""
        assertEquals(dog, Json.decodeFromString<Pet>(json))
        assertEquals(dog, Json.decodeFromString<Pet.Dog>(json))
        assertEquals(json, Json.encodeToString<Pet>(dog))
        assertEquals(json, Json.encodeToString(dog))
    }

    @Test
    fun aTagTheMappingDoesNotSelectFailsToDecode() {
        assertFailsWith<SerializationException> {
            Json.decodeFromString<Pet>("""{"petType":"Pet","name":"Plain"}""")
        }
        assertFailsWith<SerializationException> {
            Json.decodeFromString<Pet>("""{"petType":"fish","name":"Nemo"}""")
        }
        assertFailsWith<SerializationException> {
            Json.decodeFromString<Pet.Feline>("""{"petType":"bird","name":"Tweety","span":0.2}""")
        }
    }
}
