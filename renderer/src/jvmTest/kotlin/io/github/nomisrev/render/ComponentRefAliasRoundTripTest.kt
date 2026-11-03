package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.component.ref.alias.Api
import io.github.nomisrev.render.test.client.component.ref.alias.Owner
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
 * An operation using aliased (and chained) parameter, request body and response components sends and receives
 * exactly what the operation using the components directly does.
 * Uses the generated goldens under `kotlinTestData/client/component-ref-alias` directly.
 */
class ComponentRefAliasRoundTripTest {
    private data class Sent(val path: String, val traceId: String?, val limit: String?, val body: String)

    @Test
    fun aliasedComponentsSendAndReceiveLikeTheirTargets() = runBlocking {
        val sent = mutableListOf<Sent>()
        val api = Api(HttpClient(MockEngine { request ->
            sent += Sent(
                path = request.url.encodedPath,
                traceId = request.headers["X-Trace-Id"],
                limit = request.url.parameters["limit"],
                body = (request.body as OutgoingContent.ByteArrayContent).bytes().decodeToString(),
            )
            respond(
                """{"name":"received","nickname":"r"}""",
                HttpStatusCode.OK,
                headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }) {
            install(ContentNegotiation) { json() }
        })
        val received = Owner("received", "r")
        assertEquals(received, api.direct.put(xTraceId = "trace", limit = 5, body = Owner("sent")))
        assertEquals(received, api.alias.put(xTraceId = "trace", limit = 5, body = Owner("sent")))
        assertEquals(
            listOf(
                Sent("/direct", "trace", "5", """{"name":"sent"}"""),
                Sent("/alias", "trace", "5", """{"name":"sent"}"""),
            ),
            sent,
        )
    }
}
