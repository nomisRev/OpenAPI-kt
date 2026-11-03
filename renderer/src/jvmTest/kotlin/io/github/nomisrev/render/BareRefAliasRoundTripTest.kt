package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.bare.ref.alias.Api
import io.github.nomisrev.render.test.client.bare.ref.alias.BareColor
import io.github.nomisrev.render.test.client.bare.ref.alias.BareLabel
import io.github.nomisrev.render.test.client.bare.ref.alias.BareNames
import io.github.nomisrev.render.test.client.bare.ref.alias.BareOwner
import io.github.nomisrev.render.test.client.bare.ref.alias.BareRefAliasHolder
import io.github.nomisrev.render.test.client.bare.ref.alias.ChainOwner
import io.github.nomisrev.render.test.client.bare.ref.alias.NullableChainOwner
import io.github.nomisrev.render.test.client.bare.ref.alias.Owner
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
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
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * A bare `$ref` alias (`BareOwner: {$ref: Owner}`) and alias chains have exactly their target's wire format, as a
 * property, array item, map value, JSON and multipart request body and response body.
 * Uses the generated goldens under `kotlinTestData/client/bare-ref-alias` directly.
 */
class BareRefAliasRoundTripTest {
    private val json = Json

    private val wire = """
        {
          "bareOwner": { "name": "a" },
          "optionalBareOwner": { "name": "b" },
          "bareOwners": [{ "name": "c" }],
          "bareOwnerMap": { "d": { "name": "d" } },
          "bareColor": "green",
          "bareLabel": "label",
          "bareNames": ["x", "y"],
          "chainOwner": { "name": "e" },
          "nullableChainOwner": { "name": "f" },
          "owner": { "name": "owner" }
        }
    """.trimIndent()

    private val values = BareRefAliasHolder(
        bareOwner = BareOwner("a"),
        optionalBareOwner = BareOwner("b"),
        bareOwners = listOf(BareOwner("c")),
        bareOwnerMap = mapOf("d" to BareOwner("d")),
        bareColor = BareColor.Green,
        bareLabel = BareLabel("label"),
        bareNames = BareNames(listOf("x", "y")),
        chainOwner = ChainOwner("e"),
        nullableChainOwner = NullableChainOwner("f"),
        owner = Owner("owner"),
    )

    @Test
    fun everyAliasDecodesAndEncodesItsTargetsWireFormat() {
        val decoded = json.decodeFromString<BareRefAliasHolder>(wire)
        assertEquals(values, decoded)
        assertEquals(json.parseToJsonElement(wire), json.parseToJsonElement(json.encodeToString(decoded)))
    }

    @Test
    fun anAliasOfANullableAliasDecodesNull() {
        val decoded = json.decodeFromString<BareRefAliasHolder>(wire.replace("""{ "name": "f" }""", "null"))
        assertNull(decoded.nullableChainOwner)
    }

    @Test
    fun jsonBodiesSendAndReceiveTheTargetsWireFormat() = runBlocking {
        val sent = mutableListOf<String>()
        var response = """{"name":"received"}"""
        val api = Api(HttpClient(MockEngine { request ->
            sent += (request.body as OutgoingContent.ByteArrayContent).bytes().decodeToString()
            respond(response, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
        }) {
            install(ContentNegotiation) { json() }
        })
        assertEquals(BareOwner("received"), api.owners.put(BareOwner("sent")))
        assertEquals(NullableChainOwner("received"), api.chain.put(ChainOwner("chain")))
        response = "null"
        assertNull(api.chain.put(ChainOwner("chain")))
        assertEquals(listOf("""{"name":"sent"}""", """{"name":"chain"}""", """{"name":"chain"}"""), sent)
    }

    @Test
    fun multipartBodySendsTheTargetsProperties() = runBlocking {
        val sent = mutableListOf<String>()
        val api = Api(HttpClient(MockEngine { request ->
            sent += request.body.toByteArray().decodeToString()
            respond("", HttpStatusCode.OK)
        }))
        api.uploads.post(name = "uploaded")
        val body = sent.single()
        assertTrue("name=\"name\"" in body && "uploaded" in body, body)
    }
}
