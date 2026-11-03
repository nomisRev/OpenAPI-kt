package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.allof.member.nullable.AllNullableOwner
import io.github.nomisrev.render.test.client.allof.member.nullable.AllOfMemberNullableHolder
import io.github.nomisrev.render.test.client.allof.member.nullable.Api
import io.github.nomisrev.render.test.client.allof.member.nullable.MixedOwner
import io.github.nomisrev.render.test.client.allof.member.nullable.OwnNullableOwner
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
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * An `allOf` of a nullable `$ref` and a non-null member rejects `null` on the wire, while an `allOf` whose members all
 * permit `null`, or that is `nullable: true` itself, accepts it. Uses the generated goldens under
 * `kotlinTestData/client/allof-member-nullable` directly.
 */
class AllOfMemberNullableRoundTripTest {
    private val json = Json

    private val values = """
        {
          "mixed": { "name": "a", "extra": "x" },
          "mixedList": [{ "name": "b" }],
          "allNullable": { "name": "c" },
          "allNullableList": [{ "name": "d", "extra": "y" }],
          "ownNullable": { "name": "e" }
        }
    """.trimIndent()

    private val nulls = """
        {
          "mixed": { "name": "a" },
          "mixedList": [],
          "allNullable": null,
          "allNullableList": [null, { "name": "d" }],
          "ownNullable": null
        }
    """.trimIndent()

    @Test
    fun everyPositionDecodesAndEncodesAValue() {
        val decoded = json.decodeFromString<AllOfMemberNullableHolder>(values)
        assertEquals(
            AllOfMemberNullableHolder(
                mixed = MixedOwner("a", "x"),
                mixedList = listOf(MixedOwner("b")),
                allNullable = AllNullableOwner("c"),
                allNullableList = listOf(AllNullableOwner("d", "y")),
                ownNullable = OwnNullableOwner("e"),
            ),
            decoded
        )
        assertEquals(json.parseToJsonElement(values), json.parseToJsonElement(json.encodeToString(decoded)))
    }

    @Test
    fun nullablePositionsDecodeAndEncodeNull() {
        val decoded = json.decodeFromString<AllOfMemberNullableHolder>(nulls)
        assertEquals(
            AllOfMemberNullableHolder(
                mixed = MixedOwner("a"),
                mixedList = emptyList(),
                allNullable = null,
                allNullableList = listOf(null, AllNullableOwner("d")),
                ownNullable = null,
            ),
            decoded
        )
        assertEquals(json.parseToJsonElement(nulls), json.parseToJsonElement(json.encodeToString(decoded)))
    }

    @Test
    fun mixedMemberPositionsRejectNull() {
        listOf(
            nulls.replace(""""mixed": { "name": "a" }""", """"mixed": null"""),
            nulls.replace(""""mixedList": []""", """"mixedList": [null]"""),
        ).forEach { invalid ->
            assertFailsWith<SerializationException>(invalid) {
                json.decodeFromString<AllOfMemberNullableHolder>(invalid)
            }
        }
    }

    @Test
    fun optionalMixedMemberIsNotEncodedWhenAbsent() {
        val encoded = json.encodeToString(AllOfMemberNullableHolder(MixedOwner("a"), emptyList(), null, emptyList(), null))
        assertEquals(
            """{"mixed":{"name":"a"},"mixedList":[],"allNullable":null,"allNullableList":[],"ownNullable":null}""",
            encoded
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
    fun bodiesSendAValueAndReceiveAValueOrNull() = runBlocking {
        val value = Fixture("""{"name":"received"}""")
        assertEquals(AllNullableOwner("received"), value.api.owners.put(MixedOwner("sent")))
        assertEquals("""{"name":"sent"}""", value.sent.single())

        assertEquals(null, Fixture("null").api.owners.put(MixedOwner("sent")))
    }
}
