package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.form.urlencoded.body.Api
import io.github.nomisrev.render.test.client.form.urlencoded.body.Tokens
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.parseUrlEncodedParameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Runtime check for the `form-urlencoded-body` golden: a form body goes out as `application/x-www-form-urlencoded`,
 * with the parameters the spec declares on the media type, and its fields are encoded by the
 * `application/x-www-form-urlencoded` serializer: UTF-8 bytes, a space as `+`, and every byte outside the unreserved
 * set (`&`, `=`, `/`, `?`, non-ASCII) percent-encoded, so a value that contains a separator decodes back to itself.
 */
class FormUrlEncodedBodyRuntimeTest {
    private class Recorded(val contentType: String?, val body: String?)

    private fun HttpRequestData.record(): Recorded = Recorded(
        contentType = body.contentType?.toString(),
        body = (body as? OutgoingContent.ByteArrayContent)?.bytes()?.decodeToString(),
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
            block(Api(client(requests))) { requests.last() }
        }
    }

    private fun decode(body: String?): Map<String, List<String>> =
        requireNotNull(body).parseUrlEncodedParameters(plusIsSpace = true).entries().associate { it.key to it.value }

    @Test
    fun formBodyIsSentAsFormUrlEncoded() = bothClients { api, request ->
        api.tokens.post(Tokens.Post.Body.GrantType.AuthorizationCode, "a b&c=d", "https://example.com/cb?x=é")

        assertEquals("application/x-www-form-urlencoded", request().contentType)
        assertEquals(
            "grant_type=authorization_code&code=a+b%26c%3Dd&redirect_uri=https%3A%2F%2Fexample.com%2Fcb%3Fx%3D%C3%A9",
            request().body,
        )
        assertEquals(
            mapOf(
                "grant_type" to listOf("authorization_code"),
                "code" to listOf("a b&c=d"),
                "redirect_uri" to listOf("https://example.com/cb?x=é"),
            ),
            decode(request().body),
        )
    }

    @Test
    fun formBodyIsSentWithItsDeclaredCharset() = bothClients { api, request ->
        api.notes.post("Zoë + café")

        assertEquals("application/x-www-form-urlencoded; charset=utf-8", request().contentType)
        assertEquals("text=Zo%C3%AB+%2B+caf%C3%A9", request().body)
        assertEquals(mapOf("text" to listOf("Zoë + café")), decode(request().body))
    }

    @Test
    fun formOverloadNextToAJsonBodyIsSentAsFormUrlEncoded() = bothClients { api, request ->
        api.search.post("kotlin coroutines")

        assertEquals("application/x-www-form-urlencoded", request().contentType)
        assertEquals("query=kotlin+coroutines", request().body)
    }
}
