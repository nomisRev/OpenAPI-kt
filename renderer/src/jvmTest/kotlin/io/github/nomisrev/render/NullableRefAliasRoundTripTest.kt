package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.nullable.ref.alias.Optional
import io.github.nomisrev.render.test.client.nullable.ref.alias.AnyOfRefAlias
import io.github.nomisrev.render.test.client.nullable.ref.alias.Api
import io.github.nomisrev.render.test.client.nullable.ref.alias.NullFirstAnyOfRefAlias
import io.github.nomisrev.render.test.client.nullable.ref.alias.NullFirstOneOfRefAlias
import io.github.nomisrev.render.test.client.nullable.ref.alias.OneOfRefAlias
import io.github.nomisrev.render.test.client.nullable.ref.alias.Owner
import io.github.nomisrev.render.test.client.nullable.ref.alias.RefAliasHolder
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
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * A named nullable alias of a referenced object (`anyOf`/`oneOf: [{$ref: Owner}, {type: null}]`, in any order)
 * accepts both an `Owner`-shaped object and `null` on the wire as a property, array item, map value, request body
 * and response body. Uses the generated goldens under `kotlinTestData/client/nullable-ref-alias` directly.
 */
class NullableRefAliasRoundTripTest {
    private val json = Json

    private val allValues = """
        {
          "anyOfRefAlias": { "name": "a" },
          "nullFirstAnyOfRefAlias": { "name": "b" },
          "oneOfRefAlias": { "name": "c" },
          "nullFirstOneOfRefAlias": { "name": "d" },
          "optionalAnyOfRefAlias": { "name": "e" },
          "anyOfRefAliases": [{ "name": "f" }, null],
          "anyOfRefAliasMap": { "g": { "name": "g" }, "h": null },
          "owner": { "name": "owner" }
        }
    """.trimIndent()

    private val values = RefAliasHolder(
        anyOfRefAlias = AnyOfRefAlias("a"),
        nullFirstAnyOfRefAlias = NullFirstAnyOfRefAlias("b"),
        oneOfRefAlias = OneOfRefAlias("c"),
        nullFirstOneOfRefAlias = NullFirstOneOfRefAlias("d"),
        optionalAnyOfRefAlias = Optional.Value(AnyOfRefAlias("e")),
        anyOfRefAliases = listOf(AnyOfRefAlias("f"), null),
        anyOfRefAliasMap = mapOf("g" to AnyOfRefAlias("g"), "h" to null),
        owner = Owner("owner"),
    )

    @Test
    fun everyAliasDecodesItsValue() {
        val decoded = json.decodeFromString<RefAliasHolder>(allValues)
        assertEquals(values, decoded)
        assertEquals(decoded, json.decodeFromString<RefAliasHolder>(json.encodeToString(decoded)))
    }

    @Test
    fun everyAliasDecodesNull() {
        val nullKeys = listOf(
            "anyOfRefAlias", "nullFirstAnyOfRefAlias", "oneOfRefAlias", "nullFirstOneOfRefAlias", "optionalAnyOfRefAlias",
        )
        val withNulls = JsonObject(json.parseToJsonElement(allValues).jsonObject + nullKeys.associateWith { JsonNull })
        val decoded = json.decodeFromJsonElement(RefAliasHolder.serializer(), withNulls)
        assertEquals(
            values.copy(
                anyOfRefAlias = null,
                nullFirstAnyOfRefAlias = null,
                oneOfRefAlias = null,
                nullFirstOneOfRefAlias = null,
                optionalAnyOfRefAlias = Optional.Null,
            ),
            decoded
        )
        assertEquals(decoded, json.decodeFromString<RefAliasHolder>(json.encodeToString(decoded)))
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
        val fixture = Fixture("""{"name":"received"}""")
        val result = fixture.api.owners.put(AnyOfRefAlias("sent"))
        assertEquals(OneOfRefAlias("received"), result)
        assertEquals("""{"name":"sent"}""", fixture.sent.single())
    }

    @Test
    fun bodiesSendAndReceiveNull() = runBlocking {
        val fixture = Fixture("null")
        val result = fixture.api.owners.put(null)
        assertEquals(null, result)
        assertEquals("null", fixture.sent.single())
    }
}
