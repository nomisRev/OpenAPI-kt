package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.wildcard.media.types.Api
import io.github.nomisrev.render.test.client.wildcard.media.types.Labels
import io.github.nomisrev.render.test.client.wildcard.media.types.Pet
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

// Runtime check for the `wildcard-media-types` golden. A request body declared under a wildcard media type range
// (`*/*`, `application/*`, `text/*`) goes out with a concrete `Content-Type` in that range: a wildcard is not a valid
// `Content-Type`, and Ktor finds no converter for one. A response declared under a wildcard range is decoded by the
// `Content-Type` the server actually sends.
class WildcardMediaTypeRuntimeTest {
    private class Recorded(val contentType: String?, val bytes: ByteArray?) {
        val body: String? get() = bytes?.decodeToString()
    }

    private fun HttpRequestData.record(): Recorded = Recorded(
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

    private fun respondingApi(contentType: String, body: String): Api =
        Api(HttpClient(MockEngine { respond(body, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, contentType)) }) {
            install(ContentNegotiation) { json() }
        })

    @Test
    fun anyObjectBodyIsSentAsJson() = bothClients { api, request ->
        api.objects.post(Pet(name = "Rex"))

        assertEquals("application/json", request().contentType)
        assertEquals("""{"name":"Rex"}""", request().body)
    }

    @Test
    fun applicationWildcardObjectBodyIsSentAsJson() = bothClients { api, request ->
        api.appObjects.post(Pet(name = "Rex", tag = "dog"))

        assertEquals("application/json", request().contentType)
        assertEquals("""{"name":"Rex","tag":"dog"}""", request().body)
    }

    @Test
    fun anyStringBodyIsSentAsPlainText() = bothClients { api, request ->
        api.texts.post("say \"hi\"")

        assertEquals("text/plain", request().contentType)
        assertEquals("say \"hi\"", request().body)
    }

    @Test
    fun applicationWildcardStringBodyIsSentAsJsonString() = bothClients { api, request ->
        api.appTexts.post("say \"hi\"")

        assertEquals("application/json", request().contentType)
        assertEquals(""""say \"hi\""""", request().body)
    }

    @Test
    fun textWildcardStringBodyIsSentAsPlainText() = bothClients { api, request ->
        api.notes.post("line 1\nline 2")

        assertEquals("text/plain", request().contentType)
        assertEquals("line 1\nline 2", request().body)
    }

    @Test
    fun anyStringBodyChosenAtRuntimeIsSentAsPlainText() = bothClients { api, request ->
        api.labels.post("say \"hi\"", Labels.Post.RequestType.UnnamedUnnamed)

        assertEquals("text/plain", request().contentType)
        assertEquals("say \"hi\"", request().body)
    }

    @Test
    fun applicationWildcardStringBodyChosenAtRuntimeIsSentAsJsonString() = bothClients { api, request ->
        api.labels.post("say \"hi\"", Labels.Post.RequestType.Unnamed)

        assertEquals("application/json", request().contentType)
        assertEquals(""""say \"hi\""""", request().body)
    }

    @Test
    fun anyBinaryBodyIsSentAsOctetStream() = bothClients { api, request ->
        val bytes = byteArrayOf(0, 1, 0x7F, 0x80.toByte(), 0xFF.toByte())
        api.files.post(bytes)

        assertEquals("application/octet-stream", request().contentType)
        assertContentEquals(bytes, request().bytes)
    }

    @Test
    fun applicationWildcardBinaryBodyIsSentAsOctetStream() = bothClients { api, request ->
        val bytes = byteArrayOf(0x25, 0x50, 0x44, 0x46)
        api.appFiles.post(bytes)

        assertEquals("application/octet-stream", request().contentType)
        assertContentEquals(bytes, request().bytes)
    }

    @Test
    fun anyStringResponseSentAsPlainTextIsDecodedRaw() = runBlocking {
        assertEquals("say \"hi\"", respondingApi("text/plain", "say \"hi\"").greeting.get())
    }

    @Test
    fun anyStringResponseSentAsJsonIsDecodedAsJsonString() = runBlocking {
        assertEquals("say \"hi\"", respondingApi("application/json", """"say \"hi\""""").greeting.get())
        assertEquals("hi", respondingApi("application/json; charset=utf-8", "\"hi\"").greeting.get())
    }

    @Test
    fun applicationWildcardStringResponseIsDecodedByItsContentType() = runBlocking {
        assertEquals("hi", respondingApi("application/problem+json", "\"hi\"").appGreeting.get())
        assertEquals("<p>hi</p>", respondingApi("application/xml", "<p>hi</p>").appGreeting.get())
    }

    @Test
    fun textWildcardStringResponseIsDecodedRaw() = runBlocking {
        assertEquals("\"hi\"", respondingApi("text/csv", "\"hi\"").textGreeting.get())
    }

    @Test
    fun anyObjectResponseIsDecodedAsJson() = runBlocking {
        assertEquals(Pet(name = "Rex", tag = "dog"), respondingApi("application/json", """{"name":"Rex","tag":"dog"}""").animal.get())
    }
}
