package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.parameter.name.collisions.Api
import io.github.nomisrev.render.test.client.parameter.name.collisions.Variants
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteChannel
import io.ktor.utils.io.readRemaining
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.io.readString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Runtime check for the `parameter-name-collisions` golden: inputs of one operation that want one Kotlin name get
 * names of their own, and each is still sent under its own wire name, where its spec puts it.
 */
class ParameterNameCollisionRuntimeTest {
    private val requests = mutableListOf<HttpRequestData>()

    private val api = Api(
        HttpClient(
            MockEngine { request ->
                requests += request
                if (request.url.encodedPath == "/echo") {
                    respond("echoed", HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "text/plain"))
                } else {
                    respond("", HttpStatusCode.NoContent)
                }
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
    fun aBodyPropertyOfTheNameOfAQueryParameterIsPrefixedAndBothAreSent() = runBlocking {
        api.albums.put(ids = "q", bodyIds = listOf("a", "b"))

        assertEquals("/albums", request.url.encodedPath)
        assertEquals("ids=q", request.url.encodedQuery)
        assertEquals("""{"ids":["a","b"]}""", body())
    }

    @Test
    fun anAbsentOptionalBodyPropertyOfTheNameOfAParameterIsLeftOut() = runBlocking {
        api.albums.put(ids = "q", position = 3)

        assertEquals("ids=q", request.url.encodedQuery)
        assertEquals("""{"position":3}""", body())
    }

    @Test
    fun aFormFieldOfTheNameOfAQueryParameterIsPrefixedAndBothAreSent() = runBlocking {
        api.forms.post(title = "q q", bodyTitle = "t t", note = "n")

        assertEquals("title=q%20q", request.url.encodedQuery)
        assertEquals("title=t+t&note=n", body())
    }

    @Test
    fun aMultipartFieldOfTheNameOfAQueryParameterIsPrefixedAndBothAreSent() = runBlocking {
        api.uploads.post(name = "q", bodyName = "the name", file = byteArrayOf(1, 2))

        assertEquals("name=q", request.url.encodedQuery)
        val content = request.body as OutgoingContent.WriteChannelContent
        val channel = ByteChannel()
        launch {
            content.writeTo(channel)
            channel.close()
        }
        val text = channel.readRemaining().readString()
        assertEquals(true, """name="name"""" in text && "the name" in text, text)
        assertEquals(true, """name=file""" in text || """name="file"""" in text, text)
    }

    @Test
    fun parametersThatDifferBySymbolsAreSpelledAndBothAreSent() = runBlocking {
        api.prices.get(
            validUntilDate = "d",
            validUntilDateLt = "<d",
            validUntilDateGt = ">d",
            amountGte = 1,
            amountLte = 2,
            amount = 3,
            statusNot = "x",
            status = "y",
        )

        assertEquals(
            "ValidUntilDate=d&ValidUntilDate%3C=%3Cd&ValidUntilDate%3E=%3Ed&amount%3E%3D=1&amount%3C%3D=2&amount=3" +
                "&status%21=x&status=y",
            request.url.encodedQuery,
        )
    }

    @Test
    fun aParameterOfOneNameAndOneWithBracketsAreToldApart() = runBlocking {
        api.segments.get(xList = listOf("a", "b"), x = "c")

        assertEquals("x%5B%5D=a&x%5B%5D=b&x=c", request.url.encodedQuery)
    }

    @Test
    fun parametersOfOneNameInDifferentLocationsTakeTheirLocation() = runBlocking {
        api.things.id("P").get(idQuery = "q", idHeader = "h", idCookie = "c")

        assertEquals("/things/P", request.url.encodedPath)
        assertEquals("id=q", request.url.encodedQuery)
        assertEquals("h", request.headers["id"])
        assertEquals("id=c", request.headers["Cookie"])
    }

    @Test
    fun anAbsentOptionalParameterOfASharedNameIsLeftOut() = runBlocking {
        api.things.id("P").get(idQuery = "q")

        assertEquals("id=q", request.url.encodedQuery)
        assertNull(request.headers["id"])
        assertNull(request.headers["Cookie"])
    }

    @Test
    fun parametersThatDifferByCaseOrSeparatorsAreNumberedInOrderOfTheSpec() = runBlocking {
        api.limits.get(maxResults = 10, maxResults2 = "a", maxResults3 = true)

        // Every value is sent under its own wire name. Ktor's `Parameters` compare names ignoring case, though, so
        // `url.encodedParameters.append("MaxResults", ...)` is written as `maxResults` next to an earlier `maxResults`
        // (`maxResults=10&maxResults=a`): the name differs from the spec's in case only, and is not decided by the
        // names of the arguments (see issues/parameter-kotlin-name-collisions.md).
        assertEquals(listOf("10", "a"), request.url.parameters.getAll("maxResults"))
        assertEquals(listOf("true"), request.url.parameters.getAll("max_results"))
    }

    @Test
    fun aRenamedParameterKeepsItsDefault() = runBlocking {
        api.limits.get()

        assertEquals("MaxResults=all", request.url.encodedQuery)
    }

    @Test
    fun aQueryParameterNamedClientDoesNotReplaceTheHttpClient() = runBlocking {
        api.reports.get(client = "simple report")

        assertEquals("/reports", request.url.encodedPath)
        assertEquals("client=simple%20report", request.url.encodedQuery)
    }

    @Test
    fun parametersNamedBodyAndResponseAreSentNextToABody() = runBlocking {
        val result = api.echo.post(body = "b", requestBody = listOf("x", "y"), response = "r")

        assertEquals("echoed", result)
        assertEquals("body=b&response=r", request.url.encodedQuery)
        assertEquals("""["x","y"]""", body())
    }

    @Test
    fun aQueryParameterNamedRequestTypeKeepsItsNameAndTheChoiceOfBodyIsPrefixed() = runBlocking {
        api.variants.post(
            body = listOf("x"),
            bodyRequestType = Variants.Post.RequestType.Json,
            requestType = "r",
        )

        assertEquals("requestType=r", request.url.encodedQuery)
        assertEquals("application/json", request.body.contentType?.withoutParameters()?.toString())
        assertEquals("""["x"]""", body())
    }
}
