package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.alias.own.nullable.AliasOwnNullableHolder
import io.github.nomisrev.render.test.client.alias.own.nullable.AllOfNullableOwner
import io.github.nomisrev.render.test.client.alias.own.nullable.Api
import io.github.nomisrev.render.test.client.alias.own.nullable.NullableAllOfNames
import io.github.nomisrev.render.test.client.alias.own.nullable.NullableAllOfOwner
import io.github.nomisrev.render.test.client.alias.own.nullable.NullableAnyOfColor
import io.github.nomisrev.render.test.client.alias.own.nullable.NullableOneOfString
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
 * An alias that is nullable itself (`allOf`/`anyOf`/`oneOf: [{$ref: X}], nullable: true`), or that aliases a nullable
 * X, accepts `null` on the wire wherever it is used: a property, a list item, a map value, a request body and a
 * response body. Uses the generated goldens under `kotlinTestData/client/alias-own-nullable` directly.
 */
class AliasOwnNullableRoundTripTest {
    private val json = Json

    private val values = """
        {
          "owner": { "name": "a" },
          "owners": [{ "name": "b" }],
          "ownerMap": { "c": { "name": "c" } },
          "color": "red",
          "colors": ["green"],
          "colorMap": { "d": "red" },
          "string": "e",
          "strings": ["f"],
          "stringMap": { "g": "g" },
          "names": ["h"],
          "namesList": [["i"]],
          "namesMap": { "j": ["j"] },
          "nullableTarget": { "name": "k" },
          "nullableTargets": [{ "name": "l" }]
        }
    """.trimIndent()

    private val nulls = """
        {
          "owner": null,
          "owners": [null, { "name": "b" }],
          "ownerMap": { "c": null },
          "color": null,
          "colors": [null, "green"],
          "colorMap": { "d": null },
          "string": null,
          "strings": [null, "f"],
          "stringMap": { "g": null },
          "names": null,
          "namesList": [null, ["i"]],
          "namesMap": { "j": null },
          "nullableTarget": null,
          "nullableTargets": [null]
        }
    """.trimIndent()

    @Test
    fun everyPositionDecodesAndEncodesAValue() {
        val decoded = json.decodeFromString<AliasOwnNullableHolder>(values)
        assertEquals(
            AliasOwnNullableHolder(
                owner = NullableAllOfOwner("a"),
                owners = listOf(NullableAllOfOwner("b")),
                ownerMap = mapOf("c" to NullableAllOfOwner("c")),
                color = NullableAnyOfColor.Red,
                colors = listOf(NullableAnyOfColor.Green),
                colorMap = mapOf("d" to NullableAnyOfColor.Red),
                string = NullableOneOfString("e"),
                strings = listOf(NullableOneOfString("f")),
                stringMap = mapOf("g" to NullableOneOfString("g")),
                names = NullableAllOfNames(listOf("h")),
                namesList = listOf(NullableAllOfNames(listOf("i"))),
                namesMap = mapOf("j" to NullableAllOfNames(listOf("j"))),
                nullableTarget = AllOfNullableOwner("k"),
                nullableTargets = listOf(AllOfNullableOwner("l")),
            ),
            decoded
        )
        assertEquals(json.parseToJsonElement(values), json.parseToJsonElement(json.encodeToString(decoded)))
    }

    @Test
    fun everyPositionDecodesAndEncodesNull() {
        val decoded = json.decodeFromString<AliasOwnNullableHolder>(nulls)
        assertEquals(
            AliasOwnNullableHolder(
                owner = null,
                owners = listOf(null, NullableAllOfOwner("b")),
                ownerMap = mapOf("c" to null),
                color = null,
                colors = listOf(null, NullableAnyOfColor.Green),
                colorMap = mapOf("d" to null),
                string = null,
                strings = listOf(null, NullableOneOfString("f")),
                stringMap = mapOf("g" to null),
                names = null,
                namesList = listOf(null, NullableAllOfNames(listOf("i"))),
                namesMap = mapOf("j" to null),
                nullableTarget = null,
                nullableTargets = listOf(null),
            ),
            decoded
        )
        assertEquals(json.parseToJsonElement(nulls), json.parseToJsonElement(json.encodeToString(decoded)))
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
    fun bodiesSendAndReceiveAValue() = runBlocking {
        val owners = Fixture("\"received\"")
        assertEquals(NullableOneOfString("received"), owners.api.owners.put(NullableAllOfOwner("sent")))
        assertEquals("""{"name":"sent"}""", owners.sent.single())

        val names = Fixture("\"green\"")
        assertEquals(NullableAnyOfColor.Green, names.api.names.put(NullableAllOfNames(listOf("sent"))))
        assertEquals("""["sent"]""", names.sent.single())
    }

    @Test
    fun responseBodiesReceiveNull() = runBlocking {
        assertEquals(null, Fixture("null").api.owners.put(NullableAllOfOwner("sent")))
        assertEquals(null, Fixture("null").api.names.put(NullableAllOfNames(listOf("sent"))))
    }

    @Test
    fun requestBodiesSendNull() = runBlocking {
        listOf(
            Fixture("\"received\"").also { it.api.owners.put(null) },
            Fixture("\"red\"").also { it.api.names.put(null) },
        ).forEach { assertEquals("null", it.sent.single()) }
    }
}
