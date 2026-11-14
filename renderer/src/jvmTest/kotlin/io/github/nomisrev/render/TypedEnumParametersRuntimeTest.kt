package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.`enum`.typed.parameters.Api
import io.github.nomisrev.render.test.client.`enum`.typed.parameters.Baudrate
import io.github.nomisrev.render.test.client.`enum`.typed.parameters.Meters
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
 * Runtime check for the `enum-typed-parameters` golden: an integer, number or boolean enum is the canonical text of
 * its value (`300`, `0.5`, `false`, `5000000000`) in a path segment, a query parameter, a header and a form field,
 * even though in a JSON body it is a number or boolean.
 */
class TypedEnumParametersRuntimeTest {
    private val requests = mutableListOf<HttpRequestData>()

    private val api = Api(
        HttpClient(
            MockEngine { request ->
                requests += request
                respond("", HttpStatusCode.OK)
            }
        ) {
            defaultRequest { url("https://example.com") }
            install(ContentNegotiation) { json() }
        }
    )

    private val request: HttpRequestData
        get() = requests.last()

    @Test
    fun pathQueryAndHeaderParametersAreTheNumbersText() = runBlocking {
        api.meters._600.get(
            speed = Baudrate.`1200`,
            scale = Meters.`600`.Get.Scale.`15`,
            on = Meters.`600`.Get.On.False,
            xLevel = Meters.`600`.Get.XLevel.`5000000000`,
        )

        assertEquals("/meters/600", request.url.encodedPath)
        assertEquals("speed=1200&scale=1.5&on=false", request.url.encodedQuery)
        assertEquals("5000000000", request.headers["X-Level"])
    }

    @Test
    fun omittedParametersAreLeftOut() = runBlocking {
        api.meters._300.get()

        assertEquals("/meters/300", request.url.encodedPath)
        assertEquals("", request.url.encodedQuery)
    }

    @Test
    fun formFieldIsTheNumbersText() = runBlocking {
        api.meters.post(rate = Baudrate.`600`)

        val body = requireNotNull((request.body as? OutgoingContent.ByteArrayContent)?.bytes()?.decodeToString())
        assertEquals("rate=600", body)
    }
}
