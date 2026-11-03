package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.media.type.parameters.Api
import io.github.nomisrev.render.test.client.media.type.parameters.Labels
import io.github.nomisrev.render.test.client.media.type.parameters.Pet
import io.github.nomisrev.render.test.client.media.type.parameters.Problem
import io.github.nomisrev.render.test.client.media.type.parameters.Reports
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
import kotlin.test.assertTrue

/**
 * Runtime check for the `media-type-parameters` golden: the parameters a spec declares on a media type reach the wire.
 *
 * A request body goes out with its declared `charset`, and Ktor encodes it in that charset (`TextContent` for a
 * `String`, the kotlinx JSON converter for a serializable value), and with other parameters such as JSON:API's `ext`.
 * The `Accept` header of a per-media-type method carries the declared parameters. A response is matched on its media
 * type whether or not the server labels it with the declared `charset`. A `multipart/form-data` `boundary` declared in
 * the spec is never sent: Ktor generates the boundary it actually writes.
 */
class MediaTypeParametersRuntimeTest {
    private class Recorded(val contentType: String?, val accept: List<String>, val bytes: ByteArray?)

    private fun HttpRequestData.record(): Recorded = Recorded(
        contentType = body.contentType?.toString(),
        accept = headers.getAll(HttpHeaders.Accept).orEmpty(),
        bytes = (body as? OutgoingContent.ByteArrayContent)?.bytes(),
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
            block(Api(client(requests))) { requests.last() }
        }
    }

    /** The recording engine answers `204 No Content`, which an operation declaring a response body rejects. */
    private suspend fun sentIgnoringResponse(call: suspend () -> Any?) {
        runCatching { call() }
    }

    private fun respondingApi(status: HttpStatusCode, contentType: String, body: ByteArray): Api =
        Api(HttpClient(MockEngine { respond(body, status, headersOf(HttpHeaders.ContentType, contentType)) }) {
            install(ContentNegotiation) { json() }
        })

    private fun respondingApi(status: HttpStatusCode, contentType: String, body: String): Api =
        respondingApi(status, contentType, body.encodeToByteArray())

    private val latin1 = Charsets.ISO_8859_1

    @Test
    fun jsonBodyIsSentWithItsDeclaredCharset() = bothClients { api, request ->
        sentIgnoringResponse { api.pets.post(Pet(name = "Zoë")) }

        assertEquals("application/json; charset=utf-8", request().contentType)
        assertContentEquals("""{"name":"Zoë"}""".encodeToByteArray(), request().bytes)
    }

    @Test
    fun textBodyIsEncodedInItsDeclaredCharset() = bothClients { api, request ->
        sentIgnoringResponse { api.notes.post("café") }

        assertEquals("text/plain; charset=iso-8859-1", request().contentType)
        assertContentEquals("café".toByteArray(latin1), request().bytes)
    }

    @Test
    fun textBodyUnderACharsetRangeIsEncodedInThatCharset() = bothClients { api, request ->
        api.latinNotes.post("café")

        assertEquals("text/plain; charset=iso-8859-1", request().contentType)
        assertContentEquals("café".toByteArray(latin1), request().bytes)
    }

    @Test
    fun jsonApiExtensionParameterIsSent() = bothClients { api, request ->
        sentIgnoringResponse { api.operations.post(Pet(name = "Rex")) }

        assertEquals("""application/vnd.api+json; ext="https://jsonapi.org/ext/atomic"""", request().contentType)
        assertEquals("""{"name":"Rex"}""", request().bytes?.decodeToString())
    }

    @Test
    fun bodyChosenAtRuntimeIsSentWithItsDeclaredCharset() = bothClients { api, request ->
        api.labels.post("café", Labels.Post.RequestType.TextCsv)
        assertEquals("text/csv; charset=iso-8859-1", request().contentType)
        assertContentEquals("café".toByteArray(latin1), request().bytes)

        api.labels.post("café", Labels.Post.RequestType.TextPlain)
        assertEquals("text/plain; charset=utf-8", request().contentType)
        assertContentEquals("café".encodeToByteArray(), request().bytes)
    }

    @Test
    fun multipartBoundaryIsGeneratedNotCopiedFromTheSpec() = bothClients { api, request ->
        api.uploads.post("Rex")

        val contentType = ContentType.parse(requireNotNull(request().contentType))
        assertTrue(contentType.match(ContentType.MultiPart.FormData), contentType.toString())
        val boundary = contentType.parameter("boundary")
        assertTrue(!boundary.isNullOrBlank() && boundary != "spec-boundary", contentType.toString())
    }

    @Test
    fun acceptCarriesTheDeclaredParameters() = bothClients { api, request ->
        sentIgnoringResponse { api.reports.get.json() }
        assertEquals("application/json; charset=utf-8", request().accept.first())

        sentIgnoringResponse { api.reports.get.textCsv() }
        assertEquals("text/csv; charset=utf-8", request().accept.first())
    }

    @Test
    fun successResponseDeclaredWithCharsetMatchesWithAndWithoutCharset() = runBlocking {
        val pet = Pet(name = "Rex")
        for (contentType in listOf("application/json", "application/json; charset=utf-8", "application/json; charset=UTF-8")) {
            assertEquals(
                Reports.Get.JsonResponse.Ok(pet),
                respondingApi(HttpStatusCode.OK, contentType, """{"name":"Rex"}""").reports.get.json(),
                contentType,
            )
        }
        for (contentType in listOf("text/csv", "text/csv; charset=utf-8")) {
            assertEquals(
                Reports.Get.TextCsvResponse.Ok("a,b"),
                respondingApi(HttpStatusCode.OK, contentType, "a,b").reports.get.textCsv(),
                contentType,
            )
        }
    }

    @Test
    fun errorResponseDeclaredWithCharsetIsDispatchedWithAndWithoutCharset() = runBlocking {
        for (contentType in listOf("application/problem+json", "application/problem+json; charset=utf-8")) {
            assertEquals(
                Reports.Get.ProblemJsonBadRequest(Problem(title = "bad")),
                respondingApi(HttpStatusCode.BadRequest, contentType, """{"title":"bad"}""").reports.get.json(),
                contentType,
            )
        }
        for (contentType in listOf("text/plain", "text/plain; charset=utf-8")) {
            assertEquals(
                Reports.Get.TextPlainBadRequest("bad"),
                respondingApi(HttpStatusCode.BadRequest, contentType, "bad").reports.get.json(),
                contentType,
            )
        }
    }

    @Test
    fun errorRangeResponseDeclaredWithCharsetIsDispatchedWithAndWithoutCharset() = runBlocking {
        for (contentType in listOf("application/json", "application/json; charset=utf-8")) {
            assertEquals(
                Reports.Get.JsonStatus4XX(HttpStatusCode.NotFound, Problem(title = "gone")),
                respondingApi(HttpStatusCode.NotFound, contentType, """{"title":"gone"}""").reports.get.json(),
                contentType,
            )
        }
        for (contentType in listOf("text/plain", "text/plain; charset=utf-8")) {
            assertEquals(
                Reports.Get.TextPlainStatus4XX(HttpStatusCode.NotFound, "gone"),
                respondingApi(HttpStatusCode.NotFound, contentType, "gone").reports.get.json(),
                contentType,
            )
        }
    }

    @Test
    fun textResponseIsDecodedInTheCharsetTheServerSends() = runBlocking {
        val api = respondingApi(HttpStatusCode.OK, "text/plain; charset=iso-8859-1", "café".toByteArray(latin1))
        assertEquals("café", api.notes.post("x"))
    }

    @Test
    fun jsonApiResponseMatchesWithAndWithoutTheExtensionParameter() = runBlocking {
        for (contentType in listOf("application/vnd.api+json", """application/vnd.api+json; ext="https://jsonapi.org/ext/atomic"""")) {
            assertEquals(
                Pet(name = "Rex"),
                respondingApi(HttpStatusCode.OK, contentType, """{"name":"Rex"}""").operations.post(Pet(name = "x")),
                contentType,
            )
        }
    }
}
