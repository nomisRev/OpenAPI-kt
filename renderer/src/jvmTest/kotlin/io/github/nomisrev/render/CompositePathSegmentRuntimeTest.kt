package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.composite.path.segments.Api
import io.github.nomisrev.render.test.client.composite.path.segments.ArticlesMediaTypeExtensionPath
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Runtime check for the `composite-path-segments` golden: a path segment of text and template expressions is requested
 * with each parameter encoded on its own, the text as written, and a `#fragment` of the path key not sent.
 */
class CompositePathSegmentRuntimeTest {
    private val requests = mutableListOf<HttpRequestData>()

    private val api = Api(
        HttpClient(
            MockEngine { request ->
                requests += request
                respond("", HttpStatusCode.NoContent)
            }
        ) {
            defaultRequest { url("https://example.com") }
        }
    )

    private val request: HttpRequestData
        get() = requests.last()

    @Test
    fun twoParametersAreSeparatedByText() = runBlocking {
        api.media.idExtension(id = "42", extension = "mp3").get()

        assertEquals(HttpMethod.Get, request.method)
        assertEquals("/media/42.mp3", request.url.encodedPath)
    }

    @Test
    fun aParameterFollowedByText() = runBlocking {
        api.hackathons.idJson("42").get()

        assertEquals("/hackathons/42.json", request.url.encodedPath)
    }

    @Test
    fun theSegmentWithoutTheTextIsAnotherNode() = runBlocking {
        api.hackathons.id("42").get()
        assertEquals("/hackathons/42", request.url.encodedPath)

        api.hackathons.idJson("42").get()
        assertEquals("/hackathons/42.json", request.url.encodedPath)
    }

    @Test
    fun textFollowedByAParameter() = runBlocking {
        api.listsFormat("json").get()

        assertEquals("/lists.json", request.url.encodedPath)
    }

    @Test
    fun aCustomVerbAfterAParameterEncodesTheValueOnly() = runBlocking {
        api.v1.nameCancel("operations/7").post()

        assertEquals(HttpMethod.Post, request.method)
        assertEquals("/v1/operations%2F7:cancel", request.url.encodedPath)
    }

    @Test
    fun aParameterAfterTextHasChildren() = runBlocking {
        api.vVersion("2").x.get()

        assertEquals("/v2/x", request.url.encodedPath)
    }

    @Test
    fun eachParameterIsEncodedOnItsOwn() = runBlocking {
        api.media.idExtension(id = "a b/c", extension = "m p3").get()

        assertEquals("/media/a%20b%2Fc.m%20p3", request.url.encodedPath)
    }

    @Test
    fun aNumberIsWrittenAsIs() = runBlocking {
        api.ranges.loHi(lo = 1, hi = 20).get()

        assertEquals("/ranges/1-20", request.url.encodedPath)
    }

    @Test
    fun theFragmentOfAPathKeyIsNotSent() = runBlocking {
        api.tags.arnTagKeys("arn:aws:x/1").delete()

        assertEquals(HttpMethod.Delete, request.method)
        assertEquals("/tags/arn%3Aaws%3Ax%2F1", request.url.encodedPath)
        assertEquals("", request.url.fragment)
    }

    @Test
    fun thePathWithoutTheFragmentIsAnotherNode() = runBlocking {
        api.tags.arn("a").get()

        assertEquals("/tags/a", request.url.encodedPath)
    }

    @Test
    fun anEnumParameterIsAPlainParameterOfTheSegment() = runBlocking {
        api.articlesMediaTypeExtension(ArticlesMediaTypeExtensionPath.MediaTypeExtension.Xml).get()

        assertEquals("/articles.xml", request.url.encodedPath)
    }
}
