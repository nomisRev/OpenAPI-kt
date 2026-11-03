package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.single.ref.alias.AnyOfColor
import io.github.nomisrev.render.test.client.single.ref.alias.AnyOfNames
import io.github.nomisrev.render.test.client.single.ref.alias.AnyOfOwner
import io.github.nomisrev.render.test.client.single.ref.alias.Api
import io.github.nomisrev.render.test.client.single.ref.alias.OneOfLabel
import io.github.nomisrev.render.test.client.single.ref.alias.OneOfOwner
import io.github.nomisrev.render.test.client.single.ref.alias.Owner
import io.github.nomisrev.render.test.client.single.ref.alias.SingleRefAliasHolder
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
 * A named single-`$ref` alias (`anyOf`/`oneOf: [{$ref: X}]`) has exactly X's wire format, as a property, array item,
 * map value, request body and response body, for an object, enum, primitive and collection X.
 * Uses the generated goldens under `kotlinTestData/client/single-ref-alias` directly.
 */
class SingleRefAliasRoundTripTest {
    private val json = Json

    private val wire = """
        {
          "anyOfOwner": { "name": "a" },
          "oneOfOwner": { "name": "b" },
          "optionalAnyOfOwner": { "name": "c" },
          "anyOfOwners": [{ "name": "d" }],
          "oneOfOwnerMap": { "e": { "name": "e" } },
          "anyOfColor": "green",
          "oneOfLabel": "label",
          "anyOfNames": ["x", "y"],
          "inlineAnyOfOwner": { "name": "f" },
          "owner": { "name": "owner" }
        }
    """.trimIndent()

    private val values = SingleRefAliasHolder(
        anyOfOwner = AnyOfOwner("a"),
        oneOfOwner = OneOfOwner("b"),
        optionalAnyOfOwner = AnyOfOwner("c"),
        anyOfOwners = listOf(AnyOfOwner("d")),
        oneOfOwnerMap = mapOf("e" to OneOfOwner("e")),
        anyOfColor = AnyOfColor.Green,
        oneOfLabel = OneOfLabel("label"),
        anyOfNames = AnyOfNames(listOf("x", "y")),
        inlineAnyOfOwner = Owner("f"),
        owner = Owner("owner"),
    )

    @Test
    fun everyAliasDecodesAndEncodesItsTargetsWireFormat() {
        val decoded = json.decodeFromString<SingleRefAliasHolder>(wire)
        assertEquals(values, decoded)
        assertEquals(json.parseToJsonElement(wire), json.parseToJsonElement(json.encodeToString(decoded)))
    }

    @Test
    fun bodiesSendAndReceiveTheTargetsWireFormat() = runBlocking {
        val sent = mutableListOf<String>()
        val api = Api(HttpClient(MockEngine { request ->
            sent += (request.body as OutgoingContent.ByteArrayContent).bytes().decodeToString()
            respond("""{"name":"received"}""", HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
        }) {
            install(ContentNegotiation) { json() }
        })
        assertEquals(OneOfOwner("received"), api.owners.put(AnyOfOwner("sent")))
        assertEquals("""{"name":"sent"}""", sent.single())
    }
}
