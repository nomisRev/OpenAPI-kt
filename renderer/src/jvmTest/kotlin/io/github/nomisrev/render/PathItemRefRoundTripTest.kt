package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.path.item.ref.Api
import io.github.nomisrev.render.test.client.path.item.ref.Owner
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
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The operations of a path item that is a `$ref` to a `components.pathItems` entry send and receive exactly what
 * the operations of the same path item written inline do.
 * Uses the generated goldens under `kotlinTestData/client/path-item-ref` directly.
 */
class PathItemRefRoundTripTest {
    private data class Sent(val method: String, val path: String, val traceId: String?, val limit: String?, val body: String)

    @Test
    fun referencedPathItemSendsAndReceivesLikeTheInlinePathItem() = runBlocking {
        val sent = mutableListOf<Sent>()
        val api = Api(HttpClient(MockEngine { request ->
            sent += Sent(
                method = request.method.value,
                path = request.url.encodedPath,
                traceId = request.headers["X-Trace-Id"],
                limit = request.url.parameters["limit"],
                body = (request.body as? OutgoingContent.ByteArrayContent)?.bytes()?.decodeToString().orEmpty(),
            )
            if (request.method.value == "GET") {
                respond("""{"name":"received"}""", HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
            } else {
                respond("", HttpStatusCode.NoContent)
            }
        }) {
            install(ContentNegotiation) { json() }
        })
        assertEquals(Owner("received"), api.inline.get(limit = 5, xTraceId = "trace"))
        assertEquals(Owner("received"), api.referenced.get(limit = 5, xTraceId = "trace"))
        api.inline.put(xTraceId = "trace", body = Owner("sent"))
        api.referenced.put(xTraceId = "trace", body = Owner("sent"))
        assertEquals(
            listOf(
                Sent("GET", "/inline", "trace", "5", ""),
                Sent("GET", "/referenced", "trace", "5", ""),
                Sent("PUT", "/inline", "trace", null, """{"name":"sent"}"""),
                Sent("PUT", "/referenced", "trace", null, """{"name":"sent"}"""),
            ),
            sent,
        )
    }
}
