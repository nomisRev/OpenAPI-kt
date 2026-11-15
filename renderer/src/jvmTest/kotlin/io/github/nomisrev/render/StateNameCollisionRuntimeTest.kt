package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.client.path.parameter.Api as ClientParameterApi
import io.github.nomisrev.render.test.client.state.name.collision.Api
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
 * Runtime check for the `state-name-collision` golden: a literal segment `client`, a literal segment named like a path
 * parameter above it, and a path parameter `{client}` each keep their own navigation member.
 */
class StateNameCollisionRuntimeTest {
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
    fun aLiteralSegmentNamedClientIsReachable() = runBlocking {
        api.client.register.post()

        assertEquals(HttpMethod.Post, request.method)
        assertEquals("/client/register", request.url.encodedPath)
    }

    @Test
    fun aLiteralSegmentNamedClientBelowTheRootIsReachable() = runBlocking {
        api.realms.client.get()

        assertEquals(HttpMethod.Get, request.method)
        assertEquals("/realms/client", request.url.encodedPath)
    }

    @Test
    fun aLiteralSegmentNamedLikeAnAncestorParameterIsReachable() = runBlocking {
        api.realm("r").roles.realm.get()

        assertEquals(HttpMethod.Get, request.method)
        assertEquals("/r/roles/realm", request.url.encodedPath)
    }

    @Test
    fun aPathParameterNamedClientNextToALiteralSegmentIsSuffixed() = runBlocking {
        api.client2("c").things.get()

        assertEquals(HttpMethod.Get, request.method)
        assertEquals("/c/things", request.url.encodedPath)
    }

    @Test
    fun aPathParameterNamedClientIsReachable() = runBlocking {
        val parameterApi = ClientParameterApi(
            HttpClient(
                MockEngine { request ->
                    requests += request
                    respond("", HttpStatusCode.NoContent)
                }
            ) {
                defaultRequest { url("https://example.com") }
            }
        )

        parameterApi.client("c").things.get()

        assertEquals(HttpMethod.Get, request.method)
        assertEquals("/c/things", request.url.encodedPath)
    }
}
