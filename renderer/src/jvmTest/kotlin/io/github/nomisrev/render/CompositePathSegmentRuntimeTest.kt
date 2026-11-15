package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.composite.path.segments.Api
import io.github.nomisrev.render.test.client.composite.path.segments.Reports
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
        api.media.id("42").extension("mp3").get()

        assertEquals(HttpMethod.Get, request.method)
        assertEquals("/media/42.mp3", request.url.encodedPath)
    }

    @Test
    fun aParameterFollowedByText() = runBlocking {
        // `dotJson`, not `json`: the spec also has `/hackathons/{id}/json`, which keeps the plain name.
        api.hackathons.id("42").dotJson.get()

        assertEquals("/hackathons/42.json", request.url.encodedPath)
    }

    @Test
    fun theSegmentWithoutTheTextIsAnotherNode() = runBlocking {
        api.hackathons.id("42").get()
        assertEquals("/hackathons/42", request.url.encodedPath)

        api.hackathons.id("42").dotJson.get()
        assertEquals("/hackathons/42.json", request.url.encodedPath)
    }

    @Test
    fun textFollowedByAParameter() = runBlocking {
        api.lists.format("json").get()

        assertEquals("/lists.json", request.url.encodedPath)
    }

    @Test
    fun aCustomVerbAfterAParameterEncodesTheValueOnly() = runBlocking {
        api.v1.name("operations/7").cancel.post()

        assertEquals(HttpMethod.Post, request.method)
        assertEquals("/v1/operations%2F7:cancel", request.url.encodedPath)
    }

    @Test
    fun aParameterAfterTextHasChildren() = runBlocking {
        api.v.version("2").x.get()

        assertEquals("/v2/x", request.url.encodedPath)
    }

    @Test
    fun eachParameterIsEncodedOnItsOwn() = runBlocking {
        api.media.id("a b/c").extension("m p3").get()

        assertEquals("/media/a%20b%2Fc.m%20p3", request.url.encodedPath)
    }

    @Test
    fun aNumberIsWrittenAsIs() = runBlocking {
        api.ranges.lo(1).hi(20).get()

        assertEquals("/ranges/1-20", request.url.encodedPath)
    }

    @Test
    fun theFragmentOfAPathKeyIsNotSent() = runBlocking {
        api.tags.arn("arn:aws:x/1").tagKeys.delete()

        assertEquals(HttpMethod.Delete, request.method)
        assertEquals("/tags/arn%3Aaws%3Ax%2F1", request.url.encodedPath)
        assertEquals("", request.url.fragment)
    }

    @Test
    fun theTextAfterAParameterAndTheSegmentAfterItAreTwoPaths() = runBlocking {
        api.hackathons.id("42").dotJson.get()
        assertEquals("/hackathons/42.json", request.url.encodedPath)

        api.hackathons.id("42").json.get()
        assertEquals("/hackathons/42/json", request.url.encodedPath)
    }

    @Test
    fun aPathAndTheSameWithATextParameterShareTheirNode() = runBlocking {
        api.lists.get()
        assertEquals("/lists", request.url.encodedPath)

        api.lists.format("json").get()
        assertEquals("/lists.json", request.url.encodedPath)
    }

    @Test
    fun thePathWithoutTheFragmentIsTheNodeBeforeIt() = runBlocking {
        api.tags.arn("a").get()

        assertEquals("/tags/a", request.url.encodedPath)
    }

    @Test
    fun anEnumParameterIsAStepPerValue() = runBlocking {
        api.articles.xml.get()
        assertEquals("/articles.xml", request.url.encodedPath)

        api.articles.json.get()
        assertEquals("/articles.json", request.url.encodedPath)
    }

    @Test
    fun anEnumParameterBeforeTextIsAStepPerValueWithTheTextBelowIt() = runBlocking {
        api.orders.status.paid.json.get()
        assertEquals("/orders/status/Paid.json", request.url.encodedPath)

        // The value is sent as declared, encoded: only its name is made an identifier.
        api.orders.status.pendingPayment.json.get()
        assertEquals("/orders/status/Pending%20Payment.json", request.url.encodedPath)
    }

    @Test
    fun twoEnumParametersOfOneSegmentAreAStepEach() = runBlocking {
        api.news.home.json.get()
        assertEquals("/news/home.json", request.url.encodedPath)

        api.news.world.jsonp.get()
        assertEquals("/news/world.jsonp", request.url.encodedPath)
    }

    @Test
    fun anEnumParameterBeforeAFragmentIsAStepPerValue() = runBlocking {
        api.policy.agentPermissions.revisionId.get()

        assertEquals("/policy/agentPermissions", request.url.encodedPath)
    }

    @Test
    fun aUnionParameterIsANavigationFunctionPerCase() = runBlocking {
        api.reports.period(7).csv.get()
        assertEquals("/reports/7.csv", request.url.encodedPath)

        api.reports.period(Reports.Period.Weekly).csv.get()
        assertEquals("/reports/weekly.csv", request.url.encodedPath)
    }

    @Test
    fun aStepNamedLikeAnOperationOfItsParentIsNamedWithItsSeparator() = runBlocking {
        // `patch` is the PATCH operation of `/pulls/{index}`, so the value `patch` of `{index}.{diffType}` is `dotPatch`.
        api.pulls.index(7).patch()
        assertEquals(HttpMethod.Patch, request.method)
        assertEquals("/pulls/7", request.url.encodedPath)

        api.pulls.index(7).dotPatch.get()
        assertEquals(HttpMethod.Get, request.method)
        assertEquals("/pulls/7.patch", request.url.encodedPath)

        api.pulls.index(7).diff.get()
        assertEquals("/pulls/7.diff", request.url.encodedPath)
    }
}
