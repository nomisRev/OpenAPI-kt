package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.parameter.styles.Api
import io.github.nomisrev.render.test.client.parameter.styles.Code
import io.github.nomisrev.render.test.client.parameter.styles.Items
import io.github.nomisrev.render.test.client.parameter.styles.Metadata
import io.github.nomisrev.render.test.client.parameter.styles.Status
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Instant

/**
 * Runtime check for the `parameter-styles` golden: every parameter is serialized by its `style` and `explode`
 * (OpenAPI 3.1.1, 4.8.12.4; RFC 6570), defaulting to `form` with `explode: true` for query and cookie parameters and
 * to `simple` for path and header parameters.
 *
 * - A scalar is its string form; an enum its wire value (`in-progress`, not `InProgress`), a scalar wrapper the value
 *   it wraps, a date-time its ISO form.
 * - Query values are percent-encoded as RFC 6570 does (a space is `%20`), while the delimiters of `form`
 *   `explode: false` (`,`) stay literal and those of `spaceDelimited` (`%20`) and `pipeDelimited` (`%7C`) are
 *   encoded, as the Style Examples show: a server splits a value by its delimiter before decoding it.
 * - An object, or a map, is its properties as keys (`form`, `explode: true`), `name[key]` keys (`deepObject`), or
 *   one value of keys and values (`explode: false`).
 * - A header is `simple`: an array is `1,2`, an exploded object `x=1,y=2`.
 * - A cookie is `form`: an exploded array is one cookie per item; a non-exploded array is joined by `,`, which Ktor
 *   percent-encodes as a cookie value cannot hold a `,` (RFC 6265).
 * - A path parameter is `simple` (`1,2`), `label` (`.1,2`, exploded `.1.2`) or `matrix` (`;ids=1,2`, exploded
 *   `;ids=1;ids=2`).
 * - A `null` parameter is left out, and so is an empty array: RFC 6570 treats an empty list as undefined.
 */
class ParameterStylesRuntimeTest {
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
    fun queryArraysFollowTheirStyleAndExplode() = runBlocking {
        api.items.get(
            ids = listOf(1, 2),
            tags = listOf("a,b", "c"),
            words = listOf("a", "b"),
            pipes = listOf("a", "b"),
        )

        assertEquals("ids=1&ids=2&tags=a%2Cb,c&words=a%20b&pipes=a%7Cb", request.url.encodedQuery)
    }

    @Test
    fun queryScalarsArePercentEncodedStringForms() = runBlocking {
        api.items.get(
            ids = listOf(1),
            q = "a b&c=d+e",
            `when` = Instant.parse("2024-01-31T10:00:00Z"),
            status = Status.InProgress,
            code = "x/y",
        )

        assertEquals(
            "ids=1&q=a%20b%26c%3Dd%2Be&when=2024-01-31T10%3A00%3A00Z&status=in-progress&code=x%2Fy",
            request.url.encodedQuery,
        )
    }

    @Test
    fun queryEnumsAndScalarWrappersInArraysAreTheirWireValues() = runBlocking {
        api.items.get(
            ids = listOf(1),
            statuses = listOf(Status.Open, Status.InProgress),
            codes = listOf(Code("a"), Code("b c")),
        )

        assertEquals("ids=1&statuses=open,in-progress&codes=a&codes=b%20c", request.url.encodedQuery)
    }

    @Test
    fun queryObjectsAreDeepObjectKeysPropertyKeysOrOneValue() = runBlocking {
        api.items.get(
            ids = listOf(1),
            filter = Items.Get.Filter(status = Status.InProgress, limit = 5),
            range = Items.Get.Range(min = 1),
            color = Items.Get.Color(r = 100, g = 200),
        )

        assertEquals(
            "ids=1&filter%5Bstatus%5D=in-progress&filter%5Blimit%5D=5&min=1&color=R,100,G,200",
            request.url.encodedQuery,
        )
    }

    @Test
    fun queryMapsAreKeysDeepObjectKeysOrOneValue() = runBlocking {
        api.items.get(
            ids = listOf(1),
            metadata = Metadata(mapOf("a b" to "c&d")),
            labels = mapOf("k" to "v w"),
            attrs = mapOf("a" to 1, "b,c" to 2),
        )

        assertEquals("ids=1&a%20b=c%26d&labels%5Bk%5D=v%20w&attrs=a,1,b%2Cc,2", request.url.encodedQuery)
    }

    @Test
    fun emptyQueryMapsAreLeftOut() = runBlocking {
        api.items.get(ids = listOf(1), metadata = Metadata(emptyMap()), labels = emptyMap(), attrs = emptyMap(), xMeta = emptyMap())

        assertEquals("ids=1", request.url.encodedQuery)
        assertNull(request.headers["X-Meta"])
    }

    @Test
    fun aQueryUnionIsSerializedAsItsCase() = runBlocking {
        api.items.get(ids = listOf(1), cwe = Items.Get.Cwe.CaseStrings(listOf("79", "89")))
        assertEquals("ids=1&cwe=79&cwe=89", request.url.encodedQuery)

        api.items.get(ids = listOf(1), cwe = Items.Get.Cwe.CaseString("79"))
        assertEquals("ids=1&cwe=79", request.url.encodedQuery)
    }

    @Test
    fun headersAreSimple() = runBlocking {
        api.items.get(
            ids = listOf(1),
            xIds = listOf(1, 2),
            xStatus = Status.InProgress,
            xPoint = Items.Get.XPoint(x = 1, y = 2),
            xMeta = mapOf("a" to "1", "b" to "2"),
        )

        assertEquals("1,2", request.headers["X-Ids"])
        assertEquals("in-progress", request.headers["X-Status"])
        assertEquals("x=1,y=2", request.headers["X-Point"])
        assertEquals("a,1,b,2", request.headers["X-Meta"])
    }

    @Test
    fun cookiesAreForm() = runBlocking {
        api.items.get(
            ids = listOf(1),
            session = "abc",
            prefs = listOf("a", "b"),
            flags = listOf(Status.Open, Status.InProgress),
        )

        assertEquals("session=abc; prefs=a%2Cb; flags=open; flags=in-progress", request.headers[HttpHeaders.Cookie])
    }

    @Test
    fun nullParametersAndEmptyArraysAreLeftOut() = runBlocking {
        api.items.get(ids = emptyList(), tags = emptyList(), statuses = emptyList(), xIds = emptyList(), prefs = emptyList(), flags = emptyList())

        assertEquals("", request.url.encodedQuery)
        assertNull(request.headers["X-Ids"])
        assertNull(request.headers["X-Status"])
        assertNull(request.headers[HttpHeaders.Cookie])
    }

    @Test
    fun pathParametersFollowTheirStyleAndExplode() = runBlocking {
        api.simple.ids(listOf(Status.Open, Status.InProgress)).get()
        assertEquals("/simple/open,in-progress", request.url.encodedPath)

        api.label.ids(listOf(1, 2)).get()
        assertEquals("/label/.1,2", request.url.encodedPath)

        api.labelExploded.ids(listOf(1, 2)).get()
        assertEquals("/label-exploded/.1.2", request.url.encodedPath)

        api.matrix.ids(listOf(1, 2)).get()
        assertEquals("/matrix/;ids=1,2", request.url.encodedPath)

        api.matrixExploded.ids(listOf(1, 2)).get()
        assertEquals("/matrix-exploded/;ids=1;ids=2", request.url.encodedPath)

        api.codes.code("abc").get()
        assertEquals("/codes/;code=abc", request.url.encodedPath)
    }

    @Test
    fun anEmptyPathArrayExpandsToNothing() = runBlocking {
        api.label.ids(emptyList()).get()
        assertEquals("/label/", request.url.encodedPath)

        api.matrix.ids(emptyList()).get()
        assertEquals("/matrix/", request.url.encodedPath)
    }
}
