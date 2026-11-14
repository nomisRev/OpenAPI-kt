package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.path.parameter.shadowing.Api
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

/**
 * Runtime check for the `path-parameter-shadowing` golden: an operation parameter, or a flattened body or form
 * property, with the Kotlin name of a path parameter does not replace it in the request path.
 */
class PathParameterShadowingRuntimeTest {
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

    private fun body(): String = (request.body as OutgoingContent.ByteArrayContent).bytes().decodeToString()

    @Test
    fun aQueryParameterOfTheSameNameAndTypeDoesNotReplaceThePathParameter() = runBlocking {
        api.users.id("A").get(id = "B")

        assertEquals(HttpMethod.Get, request.method)
        assertEquals("/users/A", request.url.encodedPath)
        assertEquals("id=B", request.url.encodedQuery)
    }

    @Test
    fun aQueryParameterOfAnotherTypeDoesNotReplaceThePathParameter() = runBlocking {
        api.accounts.accountId("x y").get(accountId = 7)

        assertEquals("/accounts/x%20y", request.url.encodedPath)
        assertEquals("account_id=7", request.url.encodedQuery)
    }

    @Test
    fun aRequiredBodyPropertyOfTheSameNameDoesNotReplaceThePathParameter() = runBlocking {
        api.users.id("A").post(id = "B", name = "n")

        assertEquals(HttpMethod.Post, request.method)
        assertEquals("/users/A", request.url.encodedPath)
        assertEquals("""{"id":"B","name":"n"}""", body())
    }

    @Test
    fun anOptionalBodyPropertyOfTheSameNameDoesNotReplaceThePathParameter() = runBlocking {
        api.users.id("A").patch(id = "B")

        assertEquals(HttpMethod.Patch, request.method)
        assertEquals("/users/A", request.url.encodedPath)
        assertEquals("""{"id":"B"}""", body())
    }

    @Test
    fun anAbsentOptionalBodyPropertyLeavesThePathParameterAlone() = runBlocking {
        api.users.id("A").patch(id = null)

        assertEquals("/users/A", request.url.encodedPath)
    }

    @Test
    fun aFormFieldOfTheSameNameDoesNotReplaceThePathParameter() = runBlocking {
        api.forms.id("A").post(id = "B", title = "T t")

        assertEquals("/forms/A", request.url.encodedPath)
        assertEquals("id=B&title=T+t", body())
    }
}
