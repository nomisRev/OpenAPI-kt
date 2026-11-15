package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.annotated.reference.Api
import io.github.nomisrev.render.test.client.annotated.reference.ScanRead
import io.github.nomisrev.render.test.client.annotated.reference.ScanWrite
import io.github.nomisrev.render.test.client.annotated.reference.Secret
import io.github.nomisrev.render.test.client.annotated.reference.Status
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Runtime check for the `annotated-reference` golden: a property that is a `readOnly` or `writeOnly` `$ref` is in one
 * shape of its parent only, and the schema it refers to is the one class that is also returned on its own.
 */
class AnnotatedReferenceRuntimeTest {
    private val requests = mutableListOf<HttpRequestData>()

    private fun api(status: HttpStatusCode, body: String) = Api(
        HttpClient(
            MockEngine { request ->
                requests += request
                respond(body, status, headersOf(HttpHeaders.ContentType, "application/json"))
            }
        ) {
            defaultRequest { url("https://example.com") }
            install(ContentNegotiation) { json() }
        }
    )

    @Test
    fun theResponseShapeHasTheReadOnlyProperty() = runBlocking {
        val scan = api(HttpStatusCode.OK, """{"name":"nightly","status":{"code":3}}""").scans.get()

        assertEquals(ScanRead(name = "nightly", status = Status(code = 3)), scan)
    }

    @Test
    fun theRequestShapeHasTheWriteOnlyProperty() = runBlocking {
        api(HttpStatusCode.NoContent, "").scans.post(ScanWrite(name = "nightly", secret = Secret(token = "t")))

        val sent = (requests.last().body as OutgoingContent.ByteArrayContent).bytes().decodeToString()
        assertEquals("""{"name":"nightly","secret":{"token":"t"}}""", sent)
    }

    @Test
    fun theReferencedSchemaIsTheSameClassOnItsOwn() = runBlocking {
        val status: Status = api(HttpStatusCode.OK, """{"code":7}""").health.get()

        assertEquals(Status(code = 7), status)
    }
}
