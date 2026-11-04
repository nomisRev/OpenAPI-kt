package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.form.urlencoded.encoding.defaults.Api
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Runtime check for the `form-urlencoded-encoding-defaults` golden: an Encoding Object that declares neither `style`
 * nor `explode` is `style: form` with `explode: true` (OpenAPI 3.1.1, 4.8.15.2), so an array field is repeated keys,
 * also when the encoding only declares a `contentType` or `allowReserved`; `explode: false` alone is `form` joined by
 * `,`.
 */
class FormUrlEncodedEncodingDefaultsRuntimeTest {
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

    private fun body(): String =
        requireNotNull((requests.last().body as? OutgoingContent.ByteArrayContent)?.bytes()?.decodeToString())

    @Test
    fun anArrayWhoseEncodingDeclaresNeitherStyleNorExplodeIsRepeatedKeys() = runBlocking {
        api.tags.post(tags = listOf("a", "b"))

        assertEquals("tags=a&tags=b", body())
    }

    @Test
    fun explodeFalseWithoutStyleIsJoinedByComma() = runBlocking {
        api.tags.post(tags = listOf("a"), labels = listOf("x", "y"), ids = listOf(1, 2))

        assertEquals("tags=a&labels=x&labels=y&ids=1%2C2", body())
    }
}
