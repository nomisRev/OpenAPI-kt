package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.allof.ref.alias.AllOfRefAliasHolder
import io.github.nomisrev.render.test.client.allof.ref.alias.Api
import io.github.nomisrev.render.test.client.allof.ref.alias.ListAllOf
import io.github.nomisrev.render.test.client.allof.ref.alias.MyString
import io.github.nomisrev.render.test.client.allof.ref.alias.Names
import io.github.nomisrev.render.test.client.allof.ref.alias.NullableListAllOf
import io.github.nomisrev.render.test.client.allof.ref.alias.NullablePrimAllOf
import io.github.nomisrev.render.test.client.allof.ref.alias.PrimAllOf
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
 * A named `allOf: [{$ref: X}]` alias of a primitive or collection X has exactly X's wire format, as a property,
 * array item, map value, request body and response body, and `nullable: true` on the alias accepts `null`.
 * Uses the generated goldens under `kotlinTestData/client/allof-ref-alias` directly.
 */
class AllOfRefAliasRoundTripTest {
    private val json = Json

    private val wire = """
        {
          "primAllOf": "a",
          "optionalPrimAllOf": "b",
          "primAllOfs": ["c", "d"],
          "primAllOfMap": { "e": "f" },
          "listAllOf": ["g", "h"],
          "listAllOfs": [["i"], []],
          "nullablePrimAllOf": "j",
          "nullableListAllOf": ["k"],
          "inlinePrimAllOf": "l",
          "inlineListAllOf": ["m"]
        }
    """.trimIndent()

    private val values = AllOfRefAliasHolder(
        primAllOf = PrimAllOf("a"),
        optionalPrimAllOf = PrimAllOf("b"),
        primAllOfs = listOf(PrimAllOf("c"), PrimAllOf("d")),
        primAllOfMap = mapOf("e" to PrimAllOf("f")),
        listAllOf = ListAllOf(listOf("g", "h")),
        listAllOfs = listOf(ListAllOf(listOf("i")), ListAllOf(emptyList())),
        nullablePrimAllOf = NullablePrimAllOf("j"),
        nullableListAllOf = NullableListAllOf(listOf("k")),
        inlinePrimAllOf = MyString("l"),
        inlineListAllOf = Names(listOf("m")),
    )

    @Test
    fun everyAliasDecodesAndEncodesItsTargetsWireFormat() {
        val decoded = json.decodeFromString<AllOfRefAliasHolder>(wire)
        assertEquals(values, decoded)
        assertEquals(json.parseToJsonElement(wire), json.parseToJsonElement(json.encodeToString(decoded)))
    }

    @Test
    fun nullableAliasesAcceptNull() {
        val wire = """
            {
              "primAllOf": "a",
              "primAllOfs": [],
              "primAllOfMap": {},
              "listAllOf": [],
              "listAllOfs": [],
              "nullablePrimAllOf": null,
              "nullableListAllOf": null,
              "inlinePrimAllOf": "l",
              "inlineListAllOf": []
            }
        """.trimIndent()
        val decoded = json.decodeFromString<AllOfRefAliasHolder>(wire)
        assertEquals(null, decoded.nullablePrimAllOf)
        assertEquals(null, decoded.nullableListAllOf)
        assertEquals(null, decoded.optionalPrimAllOf)
        assertEquals(json.parseToJsonElement(wire), json.parseToJsonElement(json.encodeToString(decoded)))
    }

    @Test
    fun bodiesSendAndReceiveTheTargetsWireFormat() = runBlocking {
        val sent = mutableListOf<String>()
        val api = Api(HttpClient(MockEngine { request ->
            sent += (request.body as OutgoingContent.ByteArrayContent).bytes().decodeToString()
            respond("""["x","y"]""", HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
        }) {
            install(ContentNegotiation) { json() }
        })
        assertEquals(ListAllOf(listOf("x", "y")), api.aliases.put(PrimAllOf("sent")))
        assertEquals("\"sent\"", sent.single())
    }
}
