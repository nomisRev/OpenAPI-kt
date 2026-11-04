package io.github.nomisrev.render

import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import io.github.nomisrev.render.test.client.path.identifier.collision.Api as IdentifierApi
import io.github.nomisrev.render.test.client.path.separator.collision.Api as SeparatorApi

/**
 * Runtime check for the `path-separator-collision` and `path-identifier-collision` goldens: paths whose literal
 * segments are one Kotlin name (`/secret-scanning` and `/secret_scanning`, `/Users` and `/users`, `/owners/` and
 * `/owners/slash`) are each generated, and every operation requests its own template.
 *
 * The [RecordingEngineContainer] answers `204`, so an operation that expects `200` throws after its request is recorded.
 */
class PathSegmentCollisionRuntimeTest {
    private val requests = RecordingEngineContainer.requests
    private val base = "https://api.example.com/"

    private suspend fun expectRequest(path: String, call: suspend () -> Unit) {
        call()
        assertEquals(base + path, requests.last())
    }

    @Test
    fun separatorEquivalentPathsAreEachGenerated() = runBlocking {
        SeparatorApi(base).use { api ->
            expectRequest("secret-scanning") { api.secretScanning.get() }
            expectRequest("secret_scanning") { api.secretScanning2.get() }
            expectRequest("secret_scanning") { api.secretScanning2.post() }
            expectRequest("x-y/a") { api.xY.a.get() }
            expectRequest("x_y/a") { api.xY2.a.get() }
            expectRequest("x_y/a") { api.xY2.a.delete() }
            // Different methods share one class, and each sends its own path.
            expectRequest("p-q") { api.pQ.get() }
            expectRequest("p_q") { api.pQ.put() }
        }
    }

    @Test
    fun pathsWithOneKotlinNameAreEachGenerated() = runBlocking {
        IdentifierApi(base).use { api ->
            expectRequest("Users") { assertFailsWith<ResponseException> { api.users.get() } }
            expectRequest("users") { assertFailsWith<ResponseException> { api.users2.get() } }
            expectRequest("a.b") { api.aB.get() }
            expectRequest("a-b") { api.aB2.post() }
            expectRequest("owners") { api.owners.get() }
            expectRequest("owners/") { api.owners.slash.get() }
            expectRequest("owners/slash") { api.owners.slash2.get() }
            expectRequest("accounts/a%2Fb") { api.accounts.accountId("a/b").get() }
            expectRequest("accounts/a%2Fb/members") { api.accounts.accountId2("a/b").members.get() }
        }
    }
}
