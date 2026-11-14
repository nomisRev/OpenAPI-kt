package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.media.type.without.schema.Api
import io.github.nomisrev.render.test.client.media.type.without.schema.Report
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.toByteArray
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Runtime check for the `media-type-without-schema` golden: a media type declared without `schema` is typed by the
 * media type. A response that is neither JSON nor text is Ktor's `HttpResponse`, passed through unread, with the typed
 * status dispatch of any other response, and a request body that is neither is built by the caller in a block.
 */
class MediaTypeWithoutSchemaRuntimeTest {
    private val requests = mutableListOf<HttpRequestData>()
    private var reply: MockRequestHandleScope.(HttpRequestData) -> HttpResponseData = { error("No reply configured") }

    private val api = Api(
        HttpClient(
            MockEngine { request ->
                requests += request
                reply(this, request)
            }
        ) {
            defaultRequest { url("https://example.com") }
            install(ContentNegotiation) { json() }
        }
    )

    private val request: HttpRequestData
        get() = requests.last()

    private val pdf = "%PDF-1.4 bytes".encodeToByteArray()

    private fun replyWith(
        body: ByteArray,
        contentType: String,
        status: HttpStatusCode = HttpStatusCode.OK,
        vararg headers: Pair<String, String>,
    ) {
        reply = {
            respond(
                content = body,
                status = status,
                headers = Headers.build {
                    append(HttpHeaders.ContentType, contentType)
                    headers.forEach { pair -> append(pair.first, pair.second) }
                },
            )
        }
    }

    private fun sentBytes(): ByteArray = when (val content = request.body) {
        is OutgoingContent.ByteArrayContent -> content.bytes()
        else -> error("Unexpected request body: $content")
    }

    // Responses

    @Test
    fun aPdfResponseWithoutSchemaIsTheHttpResponse() = runBlocking {
        replyWith(pdf, "application/pdf", headers = arrayOf("X-Pages" to "3"))

        val response = api.certificate.get()

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("3", response.headers["X-Pages"])
        assertEquals(ContentType.Application.Pdf, ContentType.parse(response.headers[HttpHeaders.ContentType]!!))
        assertContentEquals(pdf, response.body<ByteArray>())
        assertEquals("/certificate", request.url.encodedPath)
    }

    @Test
    fun theHttpResponseCanBeReadAsAChannel() = runBlocking {
        replyWith(pdf, "application/pdf")

        val channel = api.certificate.get().bodyAsChannel()

        assertContentEquals(pdf, channel.toByteArray())
    }

    @Test
    fun aPdfResponseWithoutSchemaAsksForPdf() = runBlocking {
        replyWith(pdf, "application/pdf")

        api.certificate.get()

        assertTrue(ContentType.Application.Pdf.toString() in request.headers.getAll(HttpHeaders.Accept).orEmpty())
    }

    @Test
    fun anUndeclaredStatusStillThrows() = runBlocking {
        replyWith("nope".encodeToByteArray(), "text/plain", HttpStatusCode.NotFound)

        val failure = assertFailsWith<ResponseException> { api.certificate.get() }

        assertEquals(HttpStatusCode.NotFound, failure.response.status)
    }

    @Test
    fun theSuccessCaseOfASealedResponseHoldsTheHttpResponse() = runBlocking {
        replyWith(pdf, "application/pdf")

        val ok = assertIs<Report.Get.Response.Ok>(api.report.get())

        assertContentEquals(pdf, ok.response.body<ByteArray>())
        assertTrue(ContentType.Application.Pdf.toString() in request.headers.getAll(HttpHeaders.Accept).orEmpty())
    }

    @Test
    fun theDeclaredErrorOfASealedResponseIsStillTyped() = runBlocking {
        replyWith("""{"title":"Missing"}""".encodeToByteArray(), "application/json", HttpStatusCode.NotFound)

        val notFound = assertIs<Report.Get.Response.NotFound>(api.report.get())

        assertEquals("Missing", notFound.value.title)
    }

    @Test
    fun anUndeclaredStatusOfASealedResponseStillThrows() = runBlocking {
        replyWith("oops".encodeToByteArray(), "text/plain", HttpStatusCode.InternalServerError)

        assertFailsWith<ResponseException> { api.report.get() }
        Unit
    }

    @Test
    fun aJsonResponseWithoutSchemaIsAJsonElement() = runBlocking {
        replyWith("""{"id":1}""".encodeToByteArray(), "application/json")

        val data = api.data.get()

        assertEquals(JsonObject(mapOf("id" to JsonPrimitive(1))), data)
    }

    @Test
    fun aTextResponseWithoutSchemaIsAString() = runBlocking {
        replyWith("hello".encodeToByteArray(), "text/plain; charset=utf-8")

        assertEquals("hello", api.note.get())
    }

    @Test
    fun anImageRangeResponseWithoutSchemaIsTheHttpResponse() = runBlocking {
        replyWith(byteArrayOf(1, 2, 3), "image/png")

        val response = api.picture.get()

        assertContentEquals(byteArrayOf(1, 2, 3), response.body<ByteArray>())
        assertTrue("image/*" in request.headers.getAll(HttpHeaders.Accept).orEmpty())
    }

    @Test
    fun aSchemaLessEntryNextToATypedOneGetsAMethodOfItsOwn() = runBlocking {
        replyWith("\"name\"".encodeToByteArray(), "application/json")
        assertEquals("name", api.docs.get.json())
        assertTrue("application/json" in request.headers.getAll(HttpHeaders.Accept).orEmpty())

        replyWith(pdf, "application/pdf")
        val response = api.docs.get.pdf()
        assertContentEquals(pdf, response.body<ByteArray>())
        assertTrue("application/pdf" in request.headers.getAll(HttpHeaders.Accept).orEmpty())
    }

    // Requests

    @Test
    fun aPdfRequestBodyWithoutSchemaIsSetByTheCaller() = runBlocking {
        reply = { respond("", HttpStatusCode.NoContent) }

        api.upload.post { setBody(pdf) }

        assertEquals("/upload", request.url.encodedPath)
        assertEquals(ContentType.Application.Pdf, request.body.contentType)
        assertContentEquals(pdf, sentBytes())
    }

    @Test
    fun theCallerCanOverrideTheDeclaredMediaType() = runBlocking {
        reply = { respond("", HttpStatusCode.NoContent) }

        api.upload.post { setBody(io.ktor.http.content.ByteArrayContent(pdf, ContentType.Application.OctetStream)) }
        assertEquals(ContentType.Application.OctetStream, request.body.contentType)

        api.upload.post {
            contentType(ContentType.Application.OctetStream)
            setBody(pdf)
        }
        assertEquals(ContentType.Application.OctetStream, request.body.contentType)
    }

    @Test
    fun aBlockThatSetsNoBodySendsNoContentType() = runBlocking {
        reply = { respond("", HttpStatusCode.NoContent) }

        api.upload.post { }

        assertIs<OutgoingContent.NoContent>(request.body)
        assertNull(request.headers[HttpHeaders.ContentType])
        assertNull(request.body.contentType)
    }

    @Test
    fun anOptionalPdfRequestBodyCalledWithoutABlockSendsNoBody() = runBlocking {
        reply = { respond("", HttpStatusCode.NoContent) }

        api.uploadOptional.post()

        assertIs<OutgoingContent.NoContent>(request.body)
        assertNull(request.headers[HttpHeaders.ContentType])
        assertNull(request.body.contentType)
    }

    @Test
    fun anOptionalPdfRequestBodyCanBeSetInTheBlock() = runBlocking {
        reply = { respond("", HttpStatusCode.NoContent) }

        api.uploadOptional.post { setBody(pdf) }

        assertEquals(ContentType.Application.Pdf, request.body.contentType)
        assertContentEquals(pdf, sentBytes())
    }

    @Test
    fun aJsonAndPdfPairHasBothOverloads() = runBlocking {
        reply = { respond("", HttpStatusCode.NoContent) }

        api.uploadPair.post("name")
        assertEquals(ContentType.Application.Json, request.body.contentType)
        assertEquals("\"name\"", sentBytes().decodeToString())

        api.uploadPair.post { setBody(pdf) }
        assertEquals(ContentType.Application.Pdf, request.body.contentType)
        assertContentEquals(pdf, sentBytes())
    }

    @Test
    fun anOptionalPairKeepsCallingWithoutArgumentsUnambiguous() = runBlocking {
        reply = { respond("", HttpStatusCode.NoContent) }

        api.uploadPairOptional.post()
        assertIs<OutgoingContent.NoContent>(request.body)

        api.uploadPairOptional.post("name")
        assertEquals("\"name\"", sentBytes().decodeToString())

        api.uploadPairOptional.post { setBody(pdf) }
        assertEquals(ContentType.Application.Pdf, request.body.contentType)
    }

    @Test
    fun aRangeRequestBodyTakesTheMediaTypeFromTheCaller() = runBlocking {
        reply = { respond("", HttpStatusCode.NoContent) }

        api.uploadImage.post(ContentType.Image.PNG) { setBody(byteArrayOf(1, 2)) }

        assertEquals(ContentType.Image.PNG, request.body.contentType)
        assertContentEquals(byteArrayOf(1, 2), sentBytes())
        assertFailsWith<IllegalArgumentException> { api.uploadImage.post(ContentType.Application.Pdf) {} }
        assertFailsWith<IllegalArgumentException> { api.uploadImage.post(ContentType.parse("image/*")) {} }
    }

    @Test
    fun severalSchemaLessMediaTypesAreOneOverloadWhoseMediaTypeTheCallerChooses() = runBlocking {
        reply = { respond("", HttpStatusCode.NoContent) }

        api.uploadAny.post(contentType = ContentType.Image.PNG) { setBody(byteArrayOf(1)) }
        assertEquals(ContentType.Image.PNG, request.body.contentType)

        api.uploadAny.post(block = "q") { setBody(pdf) }
        assertEquals("block=q", request.url.encodedQuery)
        assertNull(request.headers[HttpHeaders.ContentType])

        assertFailsWith<IllegalArgumentException> { api.uploadAny.post(contentType = ContentType.Text.Plain) {} }
        Unit
    }

    @Test
    fun aJsonRequestBodyWithoutSchemaIsAJsonElement() = runBlocking {
        reply = { respond("", HttpStatusCode.NoContent) }

        api.echo.post(JsonObject(mapOf("id" to JsonPrimitive(1))))

        assertEquals(ContentType.Application.Json, request.body.contentType)
        assertEquals("""{"id":1}""", sentBytes().decodeToString())
    }

    @Test
    fun aTextRequestBodyWithoutSchemaIsAString() = runBlocking {
        reply = { respond("", HttpStatusCode.NoContent) }

        api.echoText.post("hello")

        assertEquals(ContentType.Text.Plain, request.body.contentType?.withoutParameters())
        assertEquals("hello", (request.body as OutgoingContent.ByteArrayContent).bytes().decodeToString())
    }
}
