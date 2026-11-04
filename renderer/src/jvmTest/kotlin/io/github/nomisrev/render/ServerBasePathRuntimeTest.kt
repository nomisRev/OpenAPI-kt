package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.server.base.path.Api
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientEngineContainer
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import java.util.Collections
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The engine `HttpClient {}` finds on the test classpath (`META-INF/services`), so the generated public constructors,
 * which build their own client, can be exercised: it records each request URL and responds `204`.
 */
class RecordingEngineContainer : HttpClientEngineContainer {
    override val factory: HttpClientEngineFactory<MockEngineConfig> = Factory

    private object Factory : HttpClientEngineFactory<MockEngineConfig> {
        override fun create(block: MockEngineConfig.() -> Unit): HttpClientEngine = MockEngine.create {
            addHandler { request ->
                requests += request.url.toString()
                respond("", HttpStatusCode.NoContent)
            }
            block()
        }
    }

    companion object {
        val requests: MutableList<String> = Collections.synchronizedList(mutableListOf())
    }
}

/**
 * Runtime check for the `server-base-path` golden: a path template is appended to the server URL (OpenAPI 3.1.1,
 * 4.8.1: "The path is appended to the URL from the Server Object"), so `https://api.example.com/v1` and `/owners/{id}`
 * are `https://api.example.com/v1/owners/{id}`, with or without a trailing slash on the base URL.
 *
 * Ktor's `defaultRequest` resolves a request path against the base URL as RFC 3986 does: an absolute path (`/owners`)
 * replaces the base path, and a relative one (`owners`) replaces its last segment, which is empty only when the base
 * path ends with `/`. A path is sent relative, and the generated constructors make the base path end with `/`, also
 * when the `block` sets its own `defaultRequest` URL.
 */
class ServerBasePathRuntimeTest {
    private val requests = RecordingEngineContainer.requests

    private val expected = listOf(
        "https://api.example.com" to "https://api.example.com/",
        "https://api.example.com/" to "https://api.example.com/",
        "https://api.example.com/v1" to "https://api.example.com/v1/",
        "https://api.example.com/v1/" to "https://api.example.com/v1/",
        "https://api.example.com/v1/beta/" to "https://api.example.com/v1/beta/",
    )

    private suspend fun Api.assertPaths(base: String, prefix: String, suffix: String = "") {
        get()
        assertEquals("$prefix$suffix", requests.last(), "GET / with $base")
        owners.get()
        assertEquals("${prefix}owners$suffix", requests.last(), "GET /owners with $base")
        owners.id("a/b c").get()
        assertEquals("${prefix}owners/a%2Fb%20c$suffix", requests.last(), "GET /owners/{id} with $base")
    }

    @Test
    fun theDefaultClientAppendsThePathToTheBaseUrl() = runBlocking {
        for ([base, prefix] in expected) Api(base).use { it.assertPaths(base, prefix) }
    }

    @Test
    fun aConfiguredClientAppendsThePathToTheBaseUrl() = runBlocking {
        for ([base, prefix] in expected) Api(base) {}.use { it.assertPaths(base, prefix) }
    }

    @Test
    fun aClientWithItsOwnDefaultRequestAppendsThePathToItsUrl() = runBlocking {
        for ([base, prefix] in expected) {
            Api("https://other.example.com/v2") { defaultRequest { url(base) } }.use { it.assertPaths(base, prefix) }
        }
    }

    @Test
    fun theBaseUrlQueryIsKept() = runBlocking {
        Api("https://api.example.com/v1?key=k").use { it.assertPaths("a query", "https://api.example.com/v1/", "?key=k") }
    }

    @Test
    fun aSuppliedClientWhoseBasePathEndsWithASlashAppendsThePath() = runBlocking {
        // The client is used as configured: a relative path resolves against a base path that ends with `/`.
        for ([base, prefix] in expected.filter { [base, _] -> base.endsWith("/") }) {
            val recorded = mutableListOf<String>()
            val client = HttpClient(MockEngine { request ->
                recorded += request.url.toString()
                respond("", HttpStatusCode.NoContent)
            }) { defaultRequest { url(base) } }
            Api(client).use {
                it.get()
                it.owners.get()
                it.owners.id("a/b c").get()
            }
            assertEquals(listOf(prefix, "${prefix}owners", "${prefix}owners/a%2Fb%20c"), recorded, base)
        }
    }
}
