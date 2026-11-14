package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.literal.path.segments.Api
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
 * Runtime check for the `literal-path-segments` golden: the names of a literal segment are plain identifiers, and the
 * request path is the segment as written.
 */
class LiteralPathSegmentRuntimeTest {
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
    fun aCustomVerbIsAStepBelowItsResource() = runBlocking {
        api.v1.entities.search.post()

        assertEquals(HttpMethod.Post, request.method)
        assertEquals("/v1/entities:search", request.url.encodedPath)
    }

    @Test
    fun theResourceOfACustomVerbIsTheNodeOfThePlainPath() = runBlocking {
        api.v1.entities.get()

        assertEquals("/v1/entities", request.url.encodedPath)
    }

    @Test
    fun aSlashAndAColonAreTwoNodes() = runBlocking {
        api.v1.items.search.get()
        assertEquals("/v1/items/search", request.url.encodedPath)

        api.v1.items.colonSearch.get()
        assertEquals("/v1/items:search", request.url.encodedPath)
    }

    @Test
    fun aSegmentWithoutALetterOrDigitIsSpelled() = runBlocking {
        api.apps.dash.config.get()
        assertEquals("/apps/-/config", request.url.encodedPath)

        api.ops.underscore.x.get()
        assertEquals("/ops/_/x", request.url.encodedPath)
    }

    @Test
    fun otherCharactersAreWordBreaksInTheNameOnly() = runBlocking {
        api.handles.aB.get()
        assertEquals("/handles/a@b", request.url.encodedPath)

        api.filters.aB.get()
        assertEquals("/filters/a=b", request.url.encodedPath)

        api.pairs.xY.get()
        assertEquals("/pairs/x@y", request.url.encodedPath)

        api.pairs.xY2.get()
        assertEquals("/pairs/x=y", request.url.encodedPath)
    }

    @Test
    fun severalColonsAreSeveralSteps() = runBlocking {
        api.ops.a.b.c.get()

        assertEquals("/ops/a:b:c", request.url.encodedPath)
    }

    @Test
    fun aSegmentWithAColonKeepsItsLeadingCharactersInTheName() = runBlocking {
        api.things.batch.get()
        assertEquals("/things/~batch", request.url.encodedPath)

        api.things.batch.run.post()
        assertEquals("/things/~batch:run", request.url.encodedPath)
    }
}
