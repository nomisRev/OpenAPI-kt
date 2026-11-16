package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.reference.siblings.Amount
import io.github.nomisrev.render.test.client.reference.siblings.Api
import io.github.nomisrev.render.test.client.reference.siblings.Optional
import io.github.nomisrev.render.test.client.reference.siblings.Request
import io.github.nomisrev.render.test.client.reference.siblings.TaxComponent
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
import kotlinx.serialization.MissingFieldException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

/** Runtime check for the `client/reference-siblings` golden: keywords next to a `$ref` decide what is sent and accepted. */
class ReferenceSiblingsRuntimeTest {
    private val requests = mutableListOf<HttpRequestData>()

    private fun client(status: HttpStatusCode, body: String) = HttpClient(
        MockEngine { request ->
            requests += request
            respond(body, status, headersOf(HttpHeaders.ContentType, "application/json"))
        }
    ) {
        defaultRequest { url("https://example.com") }
        install(ContentNegotiation) { json() }
    }

    private fun sent() = (requests.last().body as OutgoingContent.ByteArrayContent).bytes().decodeToString()

    @Test
    fun aRequiredSiblingOfAnAllOfMemberIsRequiredOnTheWire() = runBlocking {
        assertEquals(TaxComponent(name = "n"), Api(client(HttpStatusCode.OK, """{"name":"n"}""")).tax.get())

        val error = assertFails { Api(client(HttpStatusCode.OK, """{"rate":1.0}""")).tax.get() }
        assertEquals(true, generateSequence(error) { it.cause }.any { it is MissingFieldException || "name" in it.message.orEmpty() }, error.toString())
    }

    @Test
    fun aNullableSiblingSendsAnExplicitNullOrNothing() = runBlocking {
        val api = Api(client(HttpStatusCode.NoContent, ""))
        val base = Request(amount = Amount(1), strict = Request.Strict(2))

        api.check.post(base)
        assertEquals("""{"amount":{"value":1},"strict":{"value":2}}""", sent())

        api.check.post(base.copy(maybe = Optional.Null, nullableRef = Optional.Value(Amount(3))))
        assertEquals("""{"amount":{"value":1},"maybe":null,"nullableRef":{"value":3},"strict":{"value":2}}""", sent())
    }
}
