package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.discriminated.base.context.ItemRead
import io.github.nomisrev.render.test.client.discriminated.base.context.ItemWrite
import io.github.nomisrev.render.test.client.discriminated.base.context.ResultRead
import io.github.nomisrev.render.test.client.discriminated.base.context.ResultWrite
import io.github.nomisrev.render.test.client.discriminated.base.context.ResultsApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Runtime check for the `client/discriminated-base-context` golden: a discriminated base whose case alone leads to
 * something `readOnly` has a response shape, whose case reads the `readOnly` `id`, and a request shape, whose case has
 * no `id` to send.
 */
class DiscriminatedBaseContextRuntimeTest {
    private fun api(status: HttpStatusCode, body: String, sent: MutableList<String> = mutableListOf()) =
        ResultsApi(HttpClient(MockEngine { request ->
            (request.body as? TextContent)?.let { sent += it.text }
            respond(body, status, headersOf(HttpHeaders.ContentType, "application/json"))
        }) {
            install(ContentNegotiation) { json() }
        })

    @Test
    fun theResponseShapeReadsTheReadOnlyProperty() = runBlocking {
        val result: ResultRead = api(HttpStatusCode.OK, """{"class":"item","item":{"id":"1","name":"song"}}""").results.get()
        assertEquals(ResultRead.Item(item = ItemRead(id = "1", name = "song")), result)
    }

    @Test
    fun theRequestShapeHasNoReadOnlyPropertyToSend() = runBlocking {
        val sent = mutableListOf<String>()
        api(HttpStatusCode.NoContent, "", sent).results.post(ResultWrite.Item(item = ItemWrite(name = "song")))
        assertEquals(listOf("""{"class":"item","item":{"name":"song"}}"""), sent)
    }
}
