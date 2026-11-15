package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.context.variants.Api
import io.github.nomisrev.render.test.client.context.variants.People
import io.github.nomisrev.render.test.client.context.variants.PersonRead
import io.github.nomisrev.render.test.client.context.variants.PersonWrite
import io.github.nomisrev.render.test.client.context.variants.both.Api as BothApi
import io.github.nomisrev.render.test.client.context.variants.both.PersonWrite as BothPersonWrite
import io.github.nomisrev.render.test.client.context.variants.both.SummaryRead
import io.github.nomisrev.render.test.client.context.variants.both.SummaryWrite
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
 * Runtime check for the `context-variants` goldens: the context of a use (response: read, request: write) flows
 * through an inline wrapper and through a plain schema that refers to a context-specific one.
 */
class ContextVariantsRuntimeTest {
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
    fun aSchemaThatReferstoAContextSpecificSchemaDecodesTheReadOnlyProperty() = runBlocking {
        val summary = Api(client(HttpStatusCode.OK, """{"owner":{"name":"a","id":"1"}}""")).summaries.get()

        assertEquals("1", summary.owner?.id)
        assertEquals(PersonRead(name = "a", id = "1"), summary.owner)
    }

    @Test
    fun aReferenceUnderAnInlineResponseWrapperDecodesTheReadOnlyProperty() = runBlocking {
        val response: People.Get.Response =
            Api(client(HttpStatusCode.OK, """{"person":{"name":"a","id":"1"}}""")).people.get()

        assertEquals("1", response.person?.id)
    }

    @Test
    fun theRequestBodyIsSentWithoutTheReadOnlyProperty() = runBlocking {
        Api(client(HttpStatusCode.NoContent, "")).people.post(PersonWrite(name = "a", password = "p"))

        assertEquals("""{"name":"a","password":"p"}""", sent())
    }

    @Test
    fun usedInBothDirectionsTheReadShapeIsReadAndTheWriteShapeIsSent() = runBlocking {
        val summary: SummaryRead =
            BothApi(client(HttpStatusCode.OK, """{"owner":{"name":"a","id":"1"}}""")).summaries.get()
        assertEquals("1", summary.owner?.id)

        BothApi(client(HttpStatusCode.NoContent, "")).summaries.post(
            SummaryWrite(owner = BothPersonWrite(name = "a", password = "p"))
        )
        assertEquals("""{"owner":{"name":"a","password":"p"}}""", sent())
    }
}
