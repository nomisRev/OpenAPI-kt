package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.ignored.`header`.parameters.Api
import io.github.nomisrev.render.test.client.ignored.`header`.parameters.Note
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
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Runtime check for the `ignored-header-parameters` golden. The `Accept`, `Content-Type` and `Authorization` header
 * parameters a spec declares are ignored: `Content-Type` goes out as the media type of the body (the caller's, for a
 * binary body under the range of every `image` subtype), `Accept` as the media type of the per-media-type method, and
 * no `Authorization` header is sent. A query parameter and a cookie with such a name are still sent.
 */
class IgnoredHeaderParametersRuntimeTest {
    private class Recorded(
        val headers: Map<String, List<String>>,
        val contentType: String?,
        val bytes: ByteArray?,
        val url: String,
    )

    private fun HttpRequestData.record(): Recorded = Recorded(
        headers = headers.entries().associate { it.key to it.value },
        contentType = body.contentType?.toString(),
        bytes = when (val content = body) {
            is OutgoingContent.ByteArrayContent -> content.bytes()
            is OutgoingContent.NoContent -> null
            else -> error("Unexpected request body: $content")
        },
        url = url.toString(),
    )

    private fun engine(requests: MutableList<Recorded>) = MockEngine { request ->
        requests += request.record()
        when (request.url.encodedPath) {
            "/reports" -> {
                val accept = request.headers[HttpHeaders.Accept].orEmpty()
                if (accept.startsWith("text/csv")) {
                    respond("a,b", HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "text/csv"))
                } else {
                    respond("\"report\"", HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
                }
            }
            else -> respond("", HttpStatusCode.NoContent)
        }
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

    private fun bothClients(block: suspend (Api, MutableList<Recorded>) -> Unit) = runBlocking {
        for (client in listOf(::defaultClient, ::userClient)) {
            val requests = mutableListOf<Recorded>()
            block(Api(client(requests)), requests)
        }
    }

    private fun Recorded.header(name: String): List<String>? =
        headers.entries.singleOrNull { it.key.equals(name, ignoreCase = true) }?.value

    private val png = byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A)

    @Test
    fun imageUploadIsSentAsTheCallersMediaTypeWithoutAuthorization() = bothClients { api, requests ->
        api.avatars.put(png, ContentType.Image.JPEG, xRequestId = "r-1")

        val request = requests.single()
        assertEquals("image/jpeg", request.contentType)
        assertContentEquals(png, request.bytes)
        assertNull(request.header(HttpHeaders.ContentType), "Content-Type is the body's, not a request header")
        assertNull(request.header(HttpHeaders.Authorization))
        assertEquals(listOf("r-1"), request.header("X-Request-Id"))
    }

    @Test
    fun jsonBodyIsSentAsJsonWithoutAContentTypeArgument() = bothClients { api, requests ->
        api.notes.post(Note(text = "hello"))

        val request = requests.single()
        assertEquals("application/json", request.contentType)
        assertEquals("""{"text":"hello"}""", request.bytes?.decodeToString())
        assertNull(request.header(HttpHeaders.ContentType))
    }

    @Test
    fun acceptIsOnlyTheMediaTypeOfThePerMediaTypeMethod() = bothClients { api, requests ->
        assertEquals("report", api.reports.get.json())
        assertEquals("a,b", api.reports.get.textCsv())

        // The generated Accept is the method's media type; ContentNegotiation then appends the media type of its
        // registered converter, application/json, which it does for any request without it.
        assertEquals(
            listOf(listOf("application/json"), listOf("text/csv", "application/json")),
            requests.map { it.header(HttpHeaders.Accept) },
        )
        requests.forEach { assertNull(it.header(HttpHeaders.Authorization)) }
    }

    @Test
    fun queryAndCookieParametersWithAnIgnoredHeaderNameAreSent() = bothClients { api, requests ->
        api.reports.get.json(contentType = "csv", authorization = "session-1")

        val request = requests.single()
        assertTrue(request.url.endsWith("/reports?Content-Type=csv"), request.url)
        assertEquals(listOf("Authorization=session-1"), request.header(HttpHeaders.Cookie))
        assertNull(request.header(HttpHeaders.Authorization))
        assertEquals(listOf("application/json"), request.header(HttpHeaders.Accept))
    }
}
