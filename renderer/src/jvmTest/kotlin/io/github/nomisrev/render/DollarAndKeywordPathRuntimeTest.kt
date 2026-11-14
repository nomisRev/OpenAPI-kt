package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.dollar.and.keyword.paths.Api
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
 * Runtime check for the `dollar-and-keyword-paths` golden: a `$` in a literal path segment is sent as written, and a
 * path parameter named like a Kotlin keyword is sent as its value.
 */
class DollarAndKeywordPathRuntimeTest {
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
    fun aDollarInALiteralSegmentIsSentAsWritten() = runBlocking {
        api.members.count.get()

        assertEquals(HttpMethod.Get, request.method)
        assertEquals("/members/\$count", request.url.encodedPath)
    }

    @Test
    fun aDollarInALiteralSegmentAfterAPathParameterIsSentAsWritten() = runBlocking {
        api.users.id("7").links.get()

        assertEquals("/users/7/\$links", request.url.encodedPath)
    }

    @Test
    fun aPathParameterNamedAsAKeywordIsSentAsItsValue() = runBlocking {
        api.storage.`object`("abc").get()

        assertEquals("/storage/abc", request.url.encodedPath)
    }

    @Test
    fun keywordPathParametersAreSentAsTheirValues() = runBlocking {
        api.packages.`package`("p").`in`.`in`("x").get()

        assertEquals("/packages/p/in/x", request.url.encodedPath)
    }
}
