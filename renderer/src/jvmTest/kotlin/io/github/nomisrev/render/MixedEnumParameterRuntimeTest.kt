package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.mixed.`enum`.parameter.Api
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.serialization.kotlinx.json.json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonPrimitive

/**
 * Runtime check for the `mixed-enum-parameter` golden: an enum without a `type` and with members of different JSON
 * types is sent as the text of its JSON primitive wherever a parameter or form field is serialized, as the
 * `delivered` query parameter of the nexmo.com conversion API (`[true, false, 0, 1]`) is.
 */
class MixedEnumParameterRuntimeTest {
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

    @Test
    fun aQueryParameterIsTheTextOfTheJsonPrimitive() = runBlocking {
        api.sms.mode(JsonPrimitive("fast")).post(delivered = JsonPrimitive(true))
        assertEquals("delivered=true", request.url.encodedQuery)

        api.sms.mode(JsonPrimitive("fast")).post(delivered = JsonPrimitive(0))
        assertEquals("delivered=0", request.url.encodedQuery)
    }

    @Test
    fun aPathHeaderAndCookieParameterAreTheTextOfTheJsonPrimitive() = runBlocking {
        api.sms.mode(JsonPrimitive(1)).post(
            delivered = JsonPrimitive(false),
            xDelivered = JsonPrimitive(true),
            deliveredCookie = JsonPrimitive(0),
        )

        assertEquals("/sms/1", request.url.encodedPath)
        assertEquals("true", request.headers["X-Delivered"])
        assertEquals("delivered-cookie=0", request.headers[HttpHeaders.Cookie])
    }

    @Test
    fun aStringMemberIsSentWithoutJsonQuotes() = runBlocking {
        api.sms.mode(JsonPrimitive("fast")).post(delivered = JsonPrimitive(1))

        assertEquals("/sms/fast", request.url.encodedPath)
    }

    @Test
    fun aFormFieldIsTheTextOfTheJsonPrimitive() = runBlocking {
        api.sms.mode(JsonPrimitive("fast")).post(
            delivered = JsonPrimitive(1),
            flag = JsonPrimitive(false),
            label = JsonPrimitive("a b"),
        )

        val body = requireNotNull((request.body as? OutgoingContent.ByteArrayContent)?.bytes()?.decodeToString())
        assertEquals("flag=false&label=a+b", body)
    }
}
