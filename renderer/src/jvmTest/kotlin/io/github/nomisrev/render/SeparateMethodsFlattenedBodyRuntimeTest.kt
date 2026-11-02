package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.response.separate.methods.flattened.body.Pets
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Runtime check for the `response-separate-methods-flattened-body` golden: a per-response-content-type
 * method whose request body is flattened into parameters must send the flattened body, ask for its own
 * content type, and decode into its own `<ContentType>Response` hierarchy.
 */
class SeparateMethodsFlattenedBodyRuntimeTest {
    private class Recorded(val accept: String?, val contentType: String?, val body: String)

    private fun client(
        status: HttpStatusCode,
        responseContentType: ContentType,
        responseBody: String,
        requests: MutableList<Recorded>,
    ): Pets = Pets(
        HttpClient(MockEngine { request ->
            requests += request.record()
            respond(responseBody, status, headersOf(HttpHeaders.ContentType, responseContentType.toString()))
        }) {
            install(ContentNegotiation) { json() }
        }
    )

    private fun HttpRequestData.record(): Recorded = Recorded(
        accept = headers[HttpHeaders.Accept],
        contentType = body.contentType?.withoutParameters()?.toString(),
        body = when (val content = body) {
            is OutgoingContent.ByteArrayContent -> content.bytes().decodeToString()
            else -> error("Unexpected request body: $content")
        },
    )

    @Test
    fun jsonMethodSendsFlattenedBodyAndReturnsJsonResponse() = runBlocking {
        val requests = mutableListOf<Recorded>()
        val pets = client(HttpStatusCode.Created, ContentType.Application.Json, "42", requests)

        val result: Pets.Post.JsonResponse = pets.post.json(name = "Rex", tag = "dog")

        assertEquals(Pets.Post.JsonResponse.Created(42L), result)
        val request = requests.single()
        assertEquals("application/json", request.accept)
        assertEquals("application/json", request.contentType)
        assertEquals("""{"name":"Rex","tag":"dog"}""", request.body)
    }

    @Test
    fun textPlainMethodSendsFlattenedBodyAndReturnsTextPlainResponse() = runBlocking {
        val requests = mutableListOf<Recorded>()
        val pets = client(HttpStatusCode.Created, ContentType.Text.Plain, "pet-42", requests)

        val result: Pets.Post.TextPlainResponse = pets.post.textPlain(name = "Rex", tag = "dog")

        assertEquals(Pets.Post.TextPlainResponse.Created("pet-42"), result)
        val request = requests.single()
        assertEquals("text/plain", request.accept)
        assertEquals("application/json", request.contentType)
        assertEquals("""{"name":"Rex","tag":"dog"}""", request.body)
    }

    /** `tag` is optional but not nullable: unset, it must be omitted rather than sent as `null`. */
    @Test
    fun unsetOptionalBodyPropertyIsOmittedWithKtorDefaultJson() = runBlocking {
        val requests = mutableListOf<Recorded>()
        val pets = client(HttpStatusCode.Created, ContentType.Application.Json, "42", requests)

        pets.post.json(name = "Rex")
        pets.post.textPlain(name = "Rex")

        assertEquals(listOf("""{"name":"Rex"}""", """{"name":"Rex"}"""), requests.map { it.body })
    }

    @Test
    fun declaredErrorIsReturnedFromBothMethods() = runBlocking {
        val requests = mutableListOf<Recorded>()
        val pets = client(HttpStatusCode.BadRequest, ContentType.Application.Json, "409", requests)

        assertEquals(Pets.Post.BadRequest(409), pets.post.json(name = "Rex"))
        assertEquals(Pets.Post.BadRequest(409), pets.post.textPlain(name = "Rex", tag = "dog"))
    }
}
