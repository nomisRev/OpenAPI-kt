package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.`inline`.json.body.overloads.Api
import io.github.nomisrev.render.test.client.`inline`.json.body.overloads.Form
import io.github.nomisrev.render.test.client.`inline`.json.body.overloads.Multipart
import io.github.nomisrev.render.test.client.`inline`.json.body.overloads.Optional
import io.github.nomisrev.render.test.client.`inline`.json.body.overloads.Patch
import io.github.nomisrev.render.test.client.`inline`.json.body.overloads.Text
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Runtime check for the `inline-json-body-overloads` golden: the inline JSON `Body` of an operation that also declares
 * a form, multipart, plain-text or second JSON-like body is public, and the overload taking it sends it as JSON with
 * its media type, nested inline types included.
 */
class InlineJsonBodyOverloadsRuntimeTest {
    private class Recorded(val contentType: String?, val body: String?)

    private fun HttpRequestData.record(): Recorded = Recorded(
        contentType = body.contentType?.withoutParameters()?.toString(),
        body = when (val content = body) {
            is OutgoingContent.ByteArrayContent -> content.bytes().decodeToString()
            is OutgoingContent.NoContent -> null
            else -> error("Unexpected request body: $content")
        },
    )

    private fun engine(requests: MutableList<Recorded>) = MockEngine { request ->
        requests += request.record()
        respond("", HttpStatusCode.NoContent)
    }

    /** Configured like the generated `Api(baseUrl)` constructor. */
    private fun defaultClient(requests: MutableList<Recorded>): HttpClient =
        HttpClient(engine(requests)) {
            defaultRequest { url("https://example.com") }
            install(ContentNegotiation) { json() }
        }

    /** A user-supplied client with its own `Json` registered explicitly for `application/json` only. */
    private fun userClient(requests: MutableList<Recorded>): HttpClient =
        HttpClient(engine(requests)) {
            install(ContentNegotiation) {
                json(Json { encodeDefaults = true }, contentType = ContentType.Application.Json)
            }
        }

    private fun bothClients(block: suspend (Api, () -> Recorded) -> Unit) = runBlocking {
        for (client in listOf(::defaultClient, ::userClient)) {
            val requests = mutableListOf<Recorded>()
            block(Api(client(requests))) { requests.single() }
        }
    }

    @Test
    fun inlineJsonBodyNextToAFormIsSentAsJson() = bothClients { api, request ->
        api.form.post(Form.Post.Body("Ada", Form.Post.Body.Kind.Large, Form.Post.Body.Address("Ghent")))

        assertEquals("application/json", request().contentType)
        assertEquals("""{"name":"Ada","kind":"large","address":{"city":"Ghent"}}""", request().body)
    }

    @Test
    fun inlineJsonBodyLeavesOutAnAbsentOptionalNestedObject() = bothClients { api, request ->
        api.form.post(Form.Post.Body("Ada", Form.Post.Body.Kind.Small))

        assertEquals("application/json", request().contentType)
        assertEquals("""{"name":"Ada","kind":"small"}""", request().body)
    }

    @Test
    fun formNextToAnInlineJsonBodyIsSentAsFormUrlEncoded() = bothClients { api, request ->
        api.form.post("Ada")

        assertEquals("application/x-www-form-urlencoded", request().contentType)
        assertEquals("name=Ada", request().body)
    }

    @Test
    fun inlineJsonBodyNextToAMultipartBodyIsSentAsJson() = bothClients { api, request ->
        api.multipart.post(Multipart.Post.Body("Ada"))

        assertEquals("application/json", request().contentType)
        assertEquals("""{"name":"Ada"}""", request().body)
    }

    @Test
    fun inlineJsonBodyNextToAPlainTextBodyIsSentAsJson() = bothClients { api, request ->
        api.text.post(Text.Post.Body("Ada"))

        assertEquals("application/json", request().contentType)
        assertEquals("""{"name":"Ada"}""", request().body)
    }

    @Test
    fun inlineJsonBodySharedByTwoJsonMediaTypesIsSentAsTheChosenOne() = bothClients { api, request ->
        api.patch.post(Patch.Post.Body("Ada"), Patch.Post.RequestType.MergePatchJson)

        assertEquals("application/merge-patch+json", request().contentType)
        assertEquals("""{"name":"Ada"}""", request().body)
    }

    @Test
    fun optionalInlineJsonBodyNextToAFormIsSentAsJson() = bothClients { api, request ->
        api.optional.post(Optional.Post.Body("Ada"))

        assertEquals("application/json", request().contentType)
        assertEquals("""{"name":"Ada"}""", request().body)
    }
}
