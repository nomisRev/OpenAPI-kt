package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.discriminated.union.nullable.Api
import io.github.nomisrev.render.test.client.discriminated.union.nullable.NullBranchPet
import io.github.nomisrev.render.test.client.discriminated.union.nullable.NullableMemberPet
import io.github.nomisrev.render.test.client.discriminated.union.nullable.PetHolder
import io.github.nomisrev.render.test.client.discriminated.union.nullable.TaggedNullableMemberPet
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * A discriminated `oneOf` that permits `null`, through a `null` branch or a member that permits `null` itself, decodes
 * both `null` and a tagged object (`{"petType": "cat", ...}`) wherever it is used, and encodes the tag exactly once.
 * Uses the generated goldens under `kotlinTestData/client/discriminated-union-nullable` directly.
 */
class DiscriminatedUnionNullableRoundTripTest {
    private val json = Json

    private val values = """
        {
          "pet": { "petType": "cat", "meow": "a" },
          "pets": [{ "petType": "dog", "bark": "b" }],
          "petMap": { "c": { "petType": "cat", "meow": "c" } },
          "member": { "petType": "cat", "meow": "d" },
          "members": [{ "petType": "dog", "bark": "e" }],
          "memberMap": { "f": { "petType": "cat", "meow": "f" } },
          "tagged": { "petType": "kitty", "meow": "g" },
          "taggedList": [{ "petType": "dog", "bark": "h" }]
        }
    """.trimIndent()

    private val nulls = """
        {
          "pet": null,
          "pets": [null, { "petType": "dog", "bark": "b" }],
          "petMap": { "c": null },
          "member": null,
          "members": [null, { "petType": "dog", "bark": "e" }],
          "memberMap": { "f": null },
          "tagged": null,
          "taggedList": [null]
        }
    """.trimIndent()

    @Test
    fun everyPositionDecodesAndEncodesATaggedValue() {
        val decoded = json.decodeFromString<PetHolder>(values)
        assertEquals(
            PetHolder(
                pet = NullBranchPet.Cat("a"),
                pets = listOf(NullBranchPet.Dog("b")),
                petMap = mapOf("c" to NullBranchPet.Cat("c")),
                member = NullableMemberPet.Cat("d"),
                members = listOf(NullableMemberPet.Dog("e")),
                memberMap = mapOf("f" to NullableMemberPet.Cat("f")),
                tagged = TaggedNullableMemberPet.NullableCat("kitty", "g"),
                taggedList = listOf(TaggedNullableMemberPet.Dog("dog", "h")),
            ),
            decoded
        )
        assertEquals(json.parseToJsonElement(values), json.parseToJsonElement(json.encodeToString(decoded)))
    }

    @Test
    fun everyPositionDecodesAndEncodesNull() {
        val decoded = json.decodeFromString<PetHolder>(nulls)
        assertEquals(
            PetHolder(
                pet = null,
                pets = listOf(null, NullBranchPet.Dog("b")),
                petMap = mapOf("c" to null),
                member = null,
                members = listOf(null, NullableMemberPet.Dog("e")),
                memberMap = mapOf("f" to null),
                tagged = null,
                taggedList = listOf(null),
            ),
            decoded
        )
        assertEquals(json.parseToJsonElement(nulls), json.parseToJsonElement(json.encodeToString(decoded)))
    }

    @Test
    fun aNullableMemberEncodesItsTagOnce() {
        assertEquals(
            """{"petType":"cat","meow":"m"}""",
            json.encodeToString<NullableMemberPet>(NullableMemberPet.Cat("m"))
        )
    }

    private class Fixture(responseBody: String) {
        val sent = mutableListOf<String>()
        val api = Api(HttpClient(MockEngine { request ->
            sent += (request.body as OutgoingContent.ByteArrayContent).bytes().decodeToString()
            respond(responseBody, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
        }) {
            install(ContentNegotiation) { json() }
        })
    }

    @Test
    fun bodiesSendAndReceiveATaggedValue() = runBlocking {
        val pets = Fixture("""{"petType":"cat","meow":"received"}""")
        assertEquals(NullableMemberPet.Cat("received"), pets.api.pets.put(NullBranchPet.Dog("sent")))
        assertEquals("""{"petType":"dog","bark":"sent"}""", pets.sent.single())

        val tagged = Fixture("""{"petType":"kitty","meow":"received"}""")
        assertEquals(
            TaggedNullableMemberPet.NullableCat("kitty", "received"),
            tagged.api.tagged.put(TaggedNullableMemberPet.Dog("dog", "sent"))
        )
        assertEquals("""{"petType":"dog","bark":"sent"}""", tagged.sent.single())
    }

    @Test
    fun responseBodiesReceiveNull() = runBlocking {
        assertEquals(null, Fixture("null").api.pets.put(NullBranchPet.Dog("sent")))
        assertEquals(null, Fixture("null").api.tagged.put(TaggedNullableMemberPet.Dog("dog", "sent")))
    }

    @Test
    fun requestBodiesSendNull() = runBlocking {
        listOf(
            Fixture("""{"petType":"cat","meow":"received"}""").also { it.api.pets.put(null) },
            Fixture("""{"petType":"dog","bark":"received"}""").also { it.api.tagged.put(null) },
        ).forEach { assertEquals("null", it.sent.single()) }
    }
}
