package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.allof.merged.alias.Api
import io.github.nomisrev.render.test.client.allof.merged.alias.Code
import io.github.nomisrev.render.test.client.allof.merged.alias.Labels
import io.github.nomisrev.render.test.client.allof.merged.alias.MergedAliasHolder
import io.github.nomisrev.render.test.client.allof.merged.alias.NonEmptyNames
import io.github.nomisrev.render.test.client.allof.merged.alias.NullableCode
import io.github.nomisrev.render.test.client.allof.merged.alias.NullableTags
import io.github.nomisrev.render.test.client.allof.merged.alias.Tags
import io.github.nomisrev.render.test.client.allof.merged.alias.TagsOrCount
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
 * A named multi-member `allOf` that merges to a collection, primitive or map is a value class with exactly the merged
 * schema's wire format, as a property, array item, map value, union member, request body and response body, and the
 * nullable forms accept `null`. Uses the generated goldens under `kotlinTestData/client/allof-merged-alias` directly.
 */
class AllOfMergedAliasRoundTripTest {
    private val json = Json

    private val wire = """
        {
          "tags": ["a"],
          "optionalTags": ["b", "c"],
          "tagLists": [["d"], ["e", "f"]],
          "tagMap": { "g": ["h"] },
          "code": "abc",
          "codes": ["x", "yz"],
          "labels": { "k": "v" },
          "names": ["n"],
          "nullableTags": ["t"],
          "nullableCode": "nc",
          "tagsOrCount": ["u"]
        }
    """.trimIndent()

    private val values = MergedAliasHolder(
        tags = Tags(listOf("a")),
        optionalTags = Tags(listOf("b", "c")),
        tagLists = listOf(Tags(listOf("d")), Tags(listOf("e", "f"))),
        tagMap = mapOf("g" to Tags(listOf("h"))),
        code = Code("abc"),
        codes = listOf(Code("x"), Code("yz")),
        labels = Labels(mapOf("k" to "v")),
        names = NonEmptyNames(listOf("n")),
        nullableTags = NullableTags(listOf("t")),
        nullableCode = NullableCode("nc"),
        tagsOrCount = TagsOrCount.CaseTags(Tags(listOf("u"))),
    )

    @Test
    fun everyMergedAliasDecodesAndEncodesTheMergedWireFormat() {
        val decoded = json.decodeFromString<MergedAliasHolder>(wire)
        assertEquals(values, decoded)
        assertEquals(json.parseToJsonElement(wire), json.parseToJsonElement(json.encodeToString(decoded)))
    }

    @Test
    fun nullableMergedAliasesAcceptNullAndTheUnionItsOtherMember() {
        val wire = """
            {
              "tags": [],
              "tagLists": [],
              "tagMap": {},
              "code": "",
              "codes": [],
              "labels": {},
              "names": [],
              "nullableTags": null,
              "nullableCode": null,
              "tagsOrCount": 3
            }
        """.trimIndent()
        val decoded = json.decodeFromString<MergedAliasHolder>(wire)
        assertEquals(null, decoded.nullableTags)
        assertEquals(null, decoded.nullableCode)
        assertEquals(null, decoded.optionalTags)
        assertEquals(TagsOrCount.CaseLong(3), decoded.tagsOrCount)
        assertEquals(json.parseToJsonElement(wire), json.parseToJsonElement(json.encodeToString(decoded)))
    }

    @Test
    fun bodiesSendAndReceiveTheMergedWireFormat() = runBlocking {
        val sent = mutableListOf<String>()
        val api = Api(HttpClient(MockEngine { request ->
            sent += (request.body as OutgoingContent.ByteArrayContent).bytes().decodeToString()
            val body = if (request.url.encodedPath == "/aliases") "\"abc\"" else """["x","y"]"""
            respond(body, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
        }) {
            install(ContentNegotiation) { json() }
        })
        assertEquals(Code("abc"), api.aliases.put(Tags(listOf("sent"))))
        assertEquals(NonEmptyNames(listOf("x", "y")), api.labelled.post(Labels(mapOf("k" to "v"))))
        assertEquals(listOf("""["sent"]""", """{"k":"v"}"""), sent)
    }
}
