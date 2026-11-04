package io.github.nomisrev.render

import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import io.github.nomisrev.render.test.client.path.trailing.slash.Api as TrailingSlashApi
import io.github.nomisrev.render.test.client.path.trailing.slash.collision.Api as CollisionApi

/**
 * Runtime check for the `path-trailing-slash` and `path-trailing-slash-collision` goldens: the request path is the path
 * template as written (OpenAPI 3.1.1, 4.8.1: "The path is appended to the URL from the Server Object"), so a trailing
 * slash (`/owners/`) and an empty segment (`/a//b`) are sent, and `/owners` and `/owners/` are two operations.
 *
 * The [RecordingEngineContainer] answers `204`, so an operation that expects `200` throws after its request is recorded.
 */
class PathTrailingSlashRuntimeTest {
    private val requests = RecordingEngineContainer.requests

    private val bases = listOf(
        "https://api.example.com" to "https://api.example.com/",
        "https://api.example.com/" to "https://api.example.com/",
        "https://api.example.com/v1" to "https://api.example.com/v1/",
        "https://api.example.com/v1/" to "https://api.example.com/v1/",
    )

    private suspend fun expectRequest(expected: String, message: String, call: suspend () -> Unit) {
        call()
        assertEquals(expected, requests.last(), message)
    }

    @Test
    fun aTrailingSlashIsSent() = runBlocking {
        for ([base, prefix] in bases) TrailingSlashApi(base).use { api ->
            expectRequest("${prefix}owners/", "GET /owners/ with $base") {
                assertFailsWith<ResponseException> { api.owners.get() }
            }
            expectRequest("${prefix}owners/", "POST /owners/ with $base") { api.owners.post("x") }
            expectRequest("${prefix}owners/a%2Fb/", "GET /owners/{id}/ with $base") { api.owners.id("a/b").get() }
        }
    }

    @Test
    fun anEmptySegmentIsSent() = runBlocking {
        for ([base, prefix] in bases) TrailingSlashApi(base).use { api ->
            expectRequest("${prefix}a//b", "GET /a//b with $base") { api.a.b.get() }
        }
    }

    @Test
    fun aPathWithAndWithoutATrailingSlashAreTwoOperations() = runBlocking {
        for ([base, prefix] in bases) CollisionApi(base).use { api ->
            expectRequest("${prefix}owners", "GET /owners with $base") {
                assertFailsWith<ResponseException> { api.owners.get() }
            }
            expectRequest("${prefix}owners/", "GET /owners/ with $base") {
                assertFailsWith<ResponseException> { api.owners.slash.get() }
            }
            expectRequest("${prefix}owners/", "POST /owners/ with $base") { api.owners.slash.post() }
            expectRequest("${prefix}owners/a%2Fb", "GET /owners/{id} with $base") { api.owners.id("a/b").get() }
        }
    }
}
