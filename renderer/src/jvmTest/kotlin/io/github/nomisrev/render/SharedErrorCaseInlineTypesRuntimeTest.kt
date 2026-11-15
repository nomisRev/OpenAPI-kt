package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.shared.error.case.`inline`.types.Api
import io.github.nomisrev.render.test.client.shared.error.case.`inline`.types.Certificate.Post
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * Runtime check for the `shared-error-case-inline-types` golden: an error status of an operation with several success
 * media types is one class, with its inline types nested in it, and is decoded by the method of every media type.
 */
class SharedErrorCaseInlineTypesRuntimeTest {
    private fun api(status: HttpStatusCode, contentType: String, body: String) = Api(
        HttpClient(
            MockEngine { respond(body, status, headersOf(HttpHeaders.ContentType, contentType)) }
        ) {
            defaultRequest { url("https://example.com") }
            install(ContentNegotiation) { json() }
        }
    )

    private val badRequestJson = """
        {
          "error": "invalid_parameter",
          "error_description": "Invalid format",
          "details": { "field": "txnId", "reason": "malformed" },
          "causes": [ { "code": 7 } ]
        }
    """.trimIndent()

    private val badRequest = Post.BadRequest(
        error = Post.BadRequest.Error.InvalidParameter,
        errorDescription = Post.BadRequest.ErrorDescription.InvalidFormat,
        details = Post.BadRequest.Details(field = "txnId", reason = Post.BadRequest.Details.Reason.Malformed),
        causes = listOf(Post.BadRequest.Causes(code = 7)),
    )

    @Test
    fun theErrorIsDecodedByTheJsonMethod() = runBlocking {
        val result: Post.JsonResponse = api(HttpStatusCode.BadRequest, "application/json", badRequestJson).certificate.post.json()

        assertEquals(badRequest, result)
    }

    @Test
    fun theSameErrorIsDecodedByThePdfMethod() = runBlocking {
        val result: Post.PdfResponse = api(HttpStatusCode.BadRequest, "application/json", badRequestJson).certificate.post.pdf()

        assertEquals(badRequest, result)
    }

    @Test
    fun anotherErrorStatusHasItsOwnInlineEnum() = runBlocking {
        val result = api(HttpStatusCode.NotFound, "application/json", """{"error":"url_not_found"}""").certificate.post.pdf()

        assertEquals(Post.NotFound(error = Post.NotFound.Error.UrlNotFound), result)
    }

    @Test
    fun theSuccessCasesAreStillTheirOwn() = runBlocking {
        val json = api(HttpStatusCode.OK, "application/json", """{"id":"42"}""").certificate.post.json()
        assertEquals(Post.JsonResponse.Ok(id = "42"), json)

        val pdf = assertIs<Post.PdfResponse.Ok>(api(HttpStatusCode.OK, "application/pdf", "%PDF").certificate.post.pdf())
        assertContentEquals("%PDF".encodeToByteArray(), pdf.response.body<ByteArray>())
    }
}
