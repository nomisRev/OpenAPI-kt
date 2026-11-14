package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.untyped.parameter.Api
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
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive

/**
 * Runtime check for the `untyped-parameter` golden: a value without a `type` is a `JsonPrimitive` sent as its
 * `content`, and a free-form object a map of them, wherever a parameter or form field is serialized.
 */
class UntypedParameterRuntimeTest {
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

    private suspend fun lookup(
        key: JsonPrimitive = JsonPrimitive("k"),
        required: JsonPrimitive = JsonPrimitive(1),
        any: JsonPrimitive? = null,
        ids: List<JsonPrimitive>? = null,
        tags: List<JsonPrimitive>? = null,
        addOns: Map<String, JsonPrimitive>? = null,
        flat: Map<String, JsonPrimitive>? = null,
        xAny: JsonPrimitive? = null,
        xIds: List<JsonPrimitive>? = null,
        anyCookie: JsonPrimitive? = null,
    ) = api.lookup.key(key).get(required, any, ids, tags, addOns, flat, xAny, xIds, anyCookie)

    @Test
    fun aQueryParameterIsTheContentOfTheJsonPrimitive() = runBlocking {
        lookup(any = JsonPrimitive("a b"))
        assertEquals("any=a%20b&required=1", request.url.encodedQuery)

        lookup(any = JsonPrimitive(true), required = JsonPrimitive("{\"name\":\"Zeke\"}"))
        assertEquals("any=true&required=%7B%22name%22%3A%22Zeke%22%7D", request.url.encodedQuery)
    }

    @Test
    fun aNullParameterIsLeftOutAndAJsonNullIsToo() = runBlocking {
        lookup(any = null, xAny = null, anyCookie = null)
        assertEquals("required=1", request.url.encodedQuery)
        assertNull(request.headers["X-Any"])

        lookup(any = JsonNull, xAny = JsonNull, anyCookie = JsonNull)
        assertEquals("required=1", request.url.encodedQuery)
        assertNull(request.headers["X-Any"])
        assertNull(request.headers[HttpHeaders.Cookie])
    }

    @Test
    fun aRequiredJsonNullIsSentAsItsText() = runBlocking {
        lookup(required = JsonNull)
        assertEquals("required=null", request.url.encodedQuery)
    }

    @Test
    fun anArrayIsItsItemsContent() = runBlocking {
        lookup(ids = listOf(JsonPrimitive("a"), JsonPrimitive("b"), JsonPrimitive(3)), tags = listOf(JsonPrimitive(1), JsonPrimitive(false)))
        assertEquals("required=1&ids=a,b,3&tags=1&tags=false", request.url.encodedQuery)
    }

    @Test
    fun aFreeFormObjectIsOnePairPerEntryWhenExploded() = runBlocking {
        lookup(addOns = mapOf("twilio" to JsonPrimitive("x y"), "n" to JsonPrimitive(2)))
        assertEquals("required=1&twilio=x%20y&n=2", request.url.encodedQuery)
    }

    @Test
    fun aFreeFormObjectIsKeysAndValuesJoinedByCommasWhenNotExploded() = runBlocking {
        lookup(flat = mapOf("k1" to JsonPrimitive("v1"), "k2" to JsonPrimitive(2)))
        assertEquals("required=1&Flat=k1,v1,k2,2", request.url.encodedQuery)
    }

    @Test
    fun aPathHeaderAndCookieParameterAreTheContentOfTheJsonPrimitive() = runBlocking {
        lookup(
            key = JsonPrimitive("a/b"),
            xAny = JsonPrimitive(7),
            xIds = listOf(JsonPrimitive("x"), JsonPrimitive(true)),
            anyCookie = JsonPrimitive("c"),
        )

        assertEquals("/lookup/a%2Fb", request.url.encodedPath)
        assertEquals("7", request.headers["X-Any"])
        assertEquals("x,true", request.headers["X-Ids"])
        assertEquals("any-cookie=c", request.headers[HttpHeaders.Cookie])
    }

    @Test
    fun aFormFieldIsTheContentOfTheJsonPrimitive() = runBlocking {
        api.engagements.post(to = "+1555", parameters = JsonPrimitive("{\"name\":\"Zeke\"}"))

        val body = requireNotNull((request.body as? OutgoingContent.ByteArrayContent)?.bytes()?.decodeToString())
        assertEquals("To=%2B1555&Parameters=%7B%22name%22%3A%22Zeke%22%7D", body)
    }

    @Test
    fun anArrayFormFieldIsRepeatedKeys() = runBlocking {
        api.engagements.post(to = "a", parameters = JsonPrimitive(5), tags = listOf(JsonPrimitive("x"), JsonPrimitive(true)))

        val body = requireNotNull((request.body as? OutgoingContent.ByteArrayContent)?.bytes()?.decodeToString())
        assertEquals("To=a&Parameters=5&Tags=x&Tags=true", body)
    }
}
