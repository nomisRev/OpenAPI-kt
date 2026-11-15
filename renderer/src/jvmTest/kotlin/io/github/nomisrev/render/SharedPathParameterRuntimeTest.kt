package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.shared.path.parameter.validation.Api
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Runtime check for the `shared-path-parameter-validation` golden: operations that declare one path parameter with
 * different validation keywords or defaults are reached through one navigation step.
 */
class SharedPathParameterRuntimeTest {
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

    private val path: String
        get() = requests.last().url.encodedPath

    @Test
    fun everyOperationBelowTheParameterIsReachedThroughOneStep() = runBlocking {
        val item = api.items.id("HA1")

        item.get()
        assertEquals("/items/HA1", path)

        item.parts.get()
        assertEquals("/items/HA1/parts", path)

        item.tags.get()
        assertEquals("/items/HA1/tags", path)
    }

    @Test
    fun aNumberParameterWithBoundsOnOneOperationIsOneStep() = runBlocking {
        val page = api.pages.number(7)

        page.get()
        assertEquals("/pages/7", path)

        page.lines.get()
        assertEquals("/pages/7/lines", path)
    }
}
