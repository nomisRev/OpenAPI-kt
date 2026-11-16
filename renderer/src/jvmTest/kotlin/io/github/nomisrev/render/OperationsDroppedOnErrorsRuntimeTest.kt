package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.operations.dropped.on.errors.Diag
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Runtime check for the `operations-dropped-on-errors` golden: the operations next to the ones with an error send the
 * request their declaration describes, and the operation whose form variant is skipped sends its JSON variant.
 */
class OperationsDroppedOnErrorsRuntimeTest {
    private val requests = mutableListOf<HttpRequestData>()

    private val api = Diag(
        HttpClient(
            MockEngine { request ->
                requests += request
                if (request.url.encodedPath == "/items") {
                    respond("\"ok\"", HttpStatusCode.OK, headersOf("Content-Type", ContentType.Application.Json.toString()))
                } else {
                    respond("", HttpStatusCode.NoContent)
                }
            }
        ) {
            defaultRequest { url("https://example.com") }
            install(ContentNegotiation) { json() }
        }
    )

    private val request: HttpRequestData
        get() = requests.last()

    @Test
    fun anOperationNextToOneWithAnErrorSendsItsRequest() = runBlocking {
        assertEquals("ok", api.items.get(limit = 3))

        assertEquals(HttpMethod.Get, request.method)
        assertEquals("/items", request.url.encodedPath)
        assertEquals("limit=3", request.url.encodedQuery)
    }

    @Test
    fun anOperationWithAnUnsupportedFormVariantSendsItsJsonVariant() = runBlocking {
        api.mixed.post(a = "x")

        assertEquals(HttpMethod.Post, request.method)
        assertEquals("/mixed", request.url.encodedPath)
        val body = request.body as TextContent
        assertEquals(ContentType.Application.Json, body.contentType.withoutParameters())
        assertEquals("""{"a":"x"}""", body.text)
    }
}
