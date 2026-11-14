package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.request.builder.shadowing.Api
import io.github.nomisrev.render.test.client.request.builder.shadowing.Note
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Runtime check for the `request-builder-shadowing` golden: operation parameters named `url`, `parameter`, `method`,
 * `headers`, `header`, `cookie`, `setBody`, `contentType`, `accept`, `append` and `formData` do not change what the
 * generated request code refers to, so each one is sent where its spec puts it.
 */
class RequestBuilderShadowingRuntimeTest {
    private val requests = mutableListOf<HttpRequestData>()

    private val api = Api(
        HttpClient(
            MockEngine { request ->
                requests += request
                respond("", HttpStatusCode.NoContent)
            }
        ) {
            defaultRequest { url("https://example.com") }
            install(ContentNegotiation) { json() }
        }
    )

    private val request: HttpRequestData
        get() = requests.last()

    @Test
    fun aQueryParameterNamedUrlIsSentNextToAnOrdinaryOne() = runBlocking {
        api.search.get(url = "https://example.com/a b", x = 1)

        assertEquals(HttpMethod.Get, request.method)
        assertEquals("url=https%3A%2F%2Fexample.com%2Fa%20b&x=1", request.url.encodedQuery)
        assertEquals("example.com", request.url.host)
        assertEquals("/search", request.url.encodedPath)
    }

    @Test
    fun queryParametersNamedAfterRequestBuilderMembersAreSentAsQueryParameters() = runBlocking {
        api.search.get(
            url = "u",
            x = 2,
            parameter = "p",
            method = "DELETE",
            headers = "h h",
            append = listOf("a", "b&c"),
            header = "H",
            cookie = "C",
        )

        assertEquals(HttpMethod.Get, request.method)
        assertEquals(
            "url=u&x=2&parameter=p&method=DELETE&headers=h%20h&append=a&append=b%26c",
            request.url.encodedQuery,
        )
        assertEquals("H", request.headers["header"])
        assertEquals("cookie=C", request.headers["Cookie"])
    }

    @Test
    fun aJsonBodyIsSentNextToParametersNamedSetBodyContentTypeAndAccept() = runBlocking {
        api.notes.post(
            url = "https://example.com/n",
            body = Note(text = "hi"),
            setBody = "s",
            contentType = "text/plain",
            accept = "a/b",
        )

        assertEquals("url=https%3A%2F%2Fexample.com%2Fn&setBody=s&contentType=text%2Fplain&accept=a%2Fb", request.url.encodedQuery)
        assertEquals("application/json", request.body.contentType?.withoutParameters()?.toString())
        assertEquals("""{"text":"hi"}""", (request.body as OutgoingContent.ByteArrayContent).bytes().decodeToString())
    }

    @Test
    fun aFormBodyIsSentNextToAQueryParameterNamedUrlAndAHeaderNamedHeaders() = runBlocking {
        api.forms.post(url = "https://example.com/f", title = "T t", names = "n", headers = "hv")

        assertEquals("url=https%3A%2F%2Fexample.com%2Ff", request.url.encodedQuery)
        assertEquals("hv", request.headers["headers"])
        assertEquals("title=T+t&names=n", (request.body as OutgoingContent.ByteArrayContent).bytes().decodeToString())
    }

    @Test
    fun aMultipartBodyIsSentNextToAQueryParameterNamedUrlAndOneNamedFormData() = runBlocking {
        api.uploads.post(url = "https://example.com/u", file = byteArrayOf(1, 2), append = "x", formData = "fd")

        assertEquals("url=https%3A%2F%2Fexample.com%2Fu&formData=fd", request.url.encodedQuery)
        assertNull(request.headers["formData"])
    }
}
