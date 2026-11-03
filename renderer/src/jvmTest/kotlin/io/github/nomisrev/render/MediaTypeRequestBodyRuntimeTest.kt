package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.json.suffix.request.bodies.Api
import io.github.nomisrev.render.test.client.json.suffix.request.bodies.ClaimPatch
import io.github.nomisrev.render.test.client.json.suffix.request.bodies.PatchOperation
import io.github.nomisrev.render.test.client.non.json.request.bodies.Avatars
import io.github.nomisrev.render.test.client.non.json.request.bodies.Feeds
import io.github.nomisrev.render.test.client.non.json.request.bodies.Reports
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

/**
 * Runtime check for the `json-suffix-request-bodies` and `non-json-request-bodies` goldens: a request body declared
 * only under a media type other than `application/json`, `application/xml`, `application/octet-stream` or
 * `text/plain` is sent, with its declared media type as `Content-Type`.
 *
 * A structured-syntax `+json` body goes through the client's JSON converter: Ktor's `ContentNegotiation` matches a
 * converter registered for `application/json` against every `+json` subtype (RFC 6839), so this holds for the
 * generated default client and for a user-supplied client alike, without hard-coding a `Json` instance.
 */
class MediaTypeRequestBodyRuntimeTest {
    private class Recorded(val url: String, val contentType: String?, val bytes: ByteArray?) {
        val body: String? get() = bytes?.decodeToString()
    }

    private fun HttpRequestData.record(): Recorded = Recorded(
        url = url.toString(),
        contentType = body.contentType?.withoutParameters()?.toString(),
        bytes = when (val content = body) {
            is OutgoingContent.ByteArrayContent -> content.bytes()
            is OutgoingContent.NoContent -> null
            else -> error("Unexpected request body: $content")
        },
    )

    private fun engine(requests: MutableList<Recorded>) = MockEngine { request ->
        requests += request.record()
        respond("", HttpStatusCode.NoContent)
    }

    /** Configured like the generated `Api(baseUrl)` constructor. */
    private fun defaultClient(requests: MutableList<Recorded>): HttpClient =
        HttpClient(engine(requests)) {
            defaultRequest { url("https://example.com") }
            install(ContentNegotiation) { json() }
        }

    /** A user-supplied client with its own `Json` registered explicitly for `application/json` only. */
    private fun userClient(requests: MutableList<Recorded>): HttpClient =
        HttpClient(engine(requests)) {
            install(ContentNegotiation) {
                json(Json { encodeDefaults = true }, contentType = ContentType.Application.Json)
            }
        }

    private fun bothClients(block: suspend (Api, () -> Recorded) -> Unit) = runBlocking {
        for (client in listOf(::defaultClient, ::userClient)) {
            val requests = mutableListOf<Recorded>()
            block(Api(client(requests))) { requests.single() }
        }
    }

    @Test
    fun mergePatchObjectBodyIsSentAsJson() = bothClients { api, request ->
        api.claims.patch(ClaimPatch(status = "approved"))

        assertEquals("application/merge-patch+json", request().contentType)
        assertEquals("""{"status":"approved"}""", request().body)
    }

    @Test
    fun jsonPatchCollectionBodyIsSentAsJson() = bothClients { api, request ->
        api.operations.patch(listOf(PatchOperation(op = "remove", path = "/a"), PatchOperation("add", "/b", "x")))

        assertEquals("application/json-patch+json", request().contentType)
        assertEquals("""[{"op":"remove","path":"/a"},{"op":"add","path":"/b","value":"x"}]""", request().body)
    }

    @Test
    fun vendorJsonFlattenedBodyIsSentAsJson() = bothClients { api, request ->
        api.documents.post(title = "Draft \"one\"", draft = true)

        assertEquals("application/vnd.api+json", request().contentType)
        assertEquals("""{"title":"Draft \"one\"","draft":true}""", request().body)
    }

    @Test
    fun mergePatchStringBodyIsSentAsJsonString() = bothClients { api, request ->
        api.notes.put("say \"hi\"")

        assertEquals("application/merge-patch+json", request().contentType)
        assertEquals(""""say \"hi\""""", request().body)
    }

    @Test
    fun absentOptionalMergePatchBodyIsNotSent() = bothClients { api, request ->
        api.notes.put()

        assertEquals(null, request().body)
    }

    @Test
    fun csvStringBodyIsSentRaw() = runBlocking {
        val requests = mutableListOf<Recorded>()
        Reports(defaultClient(requests)).post("id,name\n1,\"Rex\"\n")

        assertEquals("text/csv", requests.single().contentType)
        assertEquals("id,name\n1,\"Rex\"\n", requests.single().body)
    }

    @Test
    fun pngBinaryBodyIsSentAsBytes() = runBlocking {
        val requests = mutableListOf<Recorded>()
        val png = byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47)
        Avatars(defaultClient(requests)).put(png)

        assertEquals("image/png", requests.single().contentType)
        assertContentEquals(png, requests.single().bytes)
    }

    @Test
    fun atomXmlStringBodyIsSentRaw() = runBlocking {
        val requests = mutableListOf<Recorded>()
        Feeds(defaultClient(requests)).post("<feed/>")

        assertEquals("application/atom+xml", requests.single().contentType)
        assertEquals("<feed/>", requests.single().body)
    }
}
