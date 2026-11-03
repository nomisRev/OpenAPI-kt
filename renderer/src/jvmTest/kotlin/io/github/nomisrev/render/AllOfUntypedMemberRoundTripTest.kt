package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.allof.untyped.member.Api
import io.github.nomisrev.render.test.client.allof.untyped.member.UntypedMerge
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Instant

/**
 * An `allOf` member without `type` that shapes values decides the wire format together with the typed member next to
 * it: an array of the untyped member's `items`, an object with its `properties`, a map of its `additionalProperties`,
 * its `enum` values and its `date-time` format, also for a response body. Uses the generated goldens under
 * `kotlinTestData/client/allof-untyped-member` directly.
 */
class AllOfUntypedMemberRoundTripTest {
    private val json = Json

    private val valid = """
        {
          "tags": ["a", "b"],
          "scores": [1, 2],
          "meta": { "a": "x" },
          "extras": { "k": 3 },
          "status": "off",
          "createdAt": "2024-01-02T03:04:05Z",
          "either": ["c"],
          "note": "n"
        }
    """.trimIndent()

    @Test
    fun decodesAndEncodesTheMergedTypes() {
        val decoded = json.decodeFromString<UntypedMerge>(valid)
        assertEquals(
            UntypedMerge(
                tags = listOf("a", "b"),
                scores = listOf(1, 2),
                meta = UntypedMerge.Meta(a = "x"),
                extras = mapOf("k" to 3L),
                status = UntypedMerge.Status.Off,
                createdAt = Instant.parse("2024-01-02T03:04:05Z"),
                either = listOf("c"),
                note = "n",
            ),
            decoded
        )
        assertEquals(json.parseToJsonElement(valid), json.parseToJsonElement(json.encodeToString(decoded)))
    }

    @Test
    fun rejectsValuesTheUntypedMemberExcludes() {
        listOf(
            valid.replace(""""tags": ["a", "b"]""", """"tags": [1, 2]"""),
            valid.replace(""""scores": [1, 2]""", """"scores": ["x"]"""),
            valid.replace(""""extras": { "k": 3 }""", """"extras": { "k": "v" }"""),
            valid.replace(""""status": "off"""", """"status": "maybe""""),
            valid.replace(""""createdAt": "2024-01-02T03:04:05Z"""", """"createdAt": "yesterday""""),
            valid.replace(""""either": ["c"]""", """"either": "c""""),
        ).forEach { invalid ->
            // SerializationException, or IllegalArgumentException for a malformed Instant.
            assertFailsWith<IllegalArgumentException>(invalid) { json.decodeFromString<UntypedMerge>(invalid) }
        }
    }

    private fun api(responseBody: String) = Api(HttpClient(MockEngine {
        respond(responseBody, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
    }) {
        install(ContentNegotiation) { json() }
    })

    @Test
    fun receivesTheArrayOfTheUntypedItems() = runBlocking {
        assertEquals(listOf("a", "b"), api("""["a","b"]""").tags.get())
        // SerializationException (an IllegalArgumentException), possibly wrapped by Ktor's body transformation.
        assertFailsWith<Exception> { api("""{"a":"b"}""").tags.get() }
        assertEquals(UntypedMerge.Status.On, api(valid.replace(""""off"""", """"on"""")).holder.get().status)
    }
}
