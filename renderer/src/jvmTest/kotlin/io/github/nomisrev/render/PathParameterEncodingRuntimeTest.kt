package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.path.parameter.encoding.Api
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
 * Runtime check for the `path-parameter-encoding` golden: a path value is percent-encoded as a path segment (OpenAPI
 * 3.1.1, 4.8.12.4; RFC 6570 3.2.1). Everything outside the unreserved `A-Z a-z 0-9 - . _ ~` is encoded, a space as
 * `%20` and a non-ASCII character as its UTF-8 bytes, so a `/` cannot add a segment, a `?` or `#` cannot end the path
 * and a `%` is not read as an escape. The `,` that joins `simple` items, the `.` of `label` and the `;` and `=` of
 * `matrix` are written by the style and stay literal, while the same characters inside a value are encoded (`.` is
 * unreserved, so RFC 6570 leaves it as is in a value too). `allowReserved` does not apply to path parameters.
 */
class PathParameterEncodingRuntimeTest {
    private val requests = mutableListOf<HttpRequestData>()

    private fun api(baseUrl: String = "https://example.com") = Api(
        HttpClient(
            MockEngine { request ->
                requests += request
                respond("", HttpStatusCode.NoContent)
            }
        ) {
            defaultRequest { url(baseUrl) }
        }
    )

    private val api = api()

    private val path: String
        get() = requests.last().url.encodedPath

    @Test
    fun aSimpleScalarIsOneEncodedSegment() = runBlocking {
        val cases = listOf(
            "a/b" to "a%2Fb",
            "a b" to "a%20b",
            "a?b" to "a%3Fb",
            "a#b" to "a%23b",
            "%" to "%25",
            "100%25" to "100%2525",
            "ä" to "%C3%A4",
            "a,b" to "a%2Cb",
            "a.b" to "a.b",
            "a;b" to "a%3Bb",
            "a=b" to "a%3Db",
            "a+b&c" to "a%2Bb%26c",
            "AZaz09-._~" to "AZaz09-._~",
        )
        for ([value, encoded] in cases) {
            api.simple.id(value).get()
            assertEquals("/simple/$encoded", path, "simple $value")
        }
    }

    @Test
    fun aLabelScalarEncodesItsValueButNotItsDot() = runBlocking {
        api.label.id("a/b;c").get()
        assertEquals("/label/.a%2Fb%3Bc", path)

        api.label.id("a.b").get()
        assertEquals("/label/.a.b", path)
    }

    @Test
    fun aMatrixScalarEncodesItsValueButNotItsSemicolonAndEquals() = runBlocking {
        api.matrix.id("a;b=c").get()
        assertEquals("/matrix/;id=a%3Bb%3Dc", path)

        api.matrix.id("x/y?z#w").get()
        assertEquals("/matrix/;id=x%2Fy%3Fz%23w", path)
    }

    @Test
    fun simpleArrayItemsAreEncodedAndJoinedByALiteralComma() = runBlocking {
        api.simpleArray.ids(listOf("a,b", "c/d", "e f")).get()
        assertEquals("/simple-array/a%2Cb,c%2Fd,e%20f", path)
    }

    @Test
    fun labelArrayItemsAreEncodedAndJoinedByTheStyle() = runBlocking {
        api.labelArray.ids(listOf("a,b", "c?d")).get()
        assertEquals("/label-array/.a%2Cb,c%3Fd", path)

        api.labelExploded.ids(listOf("a/b", "c#d")).get()
        assertEquals("/label-exploded/.a%2Fb.c%23d", path)
    }

    @Test
    fun matrixArrayItemsAreEncodedAndJoinedByTheStyle() = runBlocking {
        api.matrixArray.ids(listOf("a;b", "c=d", "%")).get()
        assertEquals("/matrix-array/;ids=a%3Bb,c%3Dd,%25", path)

        api.matrixExploded.ids(listOf("a;b", "c=d")).get()
        assertEquals("/matrix-exploded/;ids=a%3Bb;ids=c%3Dd", path)
    }

    @Test
    fun aNumberIsItsStringForm() = runBlocking {
        api.numbers.n(-1L).get()
        assertEquals("/numbers/-1", path)
    }

    @Test
    fun aFixedEnumSegmentIsEncoded() = runBlocking {
        api.kinds.withSpace.get()
        assertEquals("/kinds/with%20space", path)

        api.refs.aB.get()
        assertEquals("/refs/a%2Fb", path)
    }

    @Test
    fun anOverloadedSegmentIsEncoded() = runBlocking {
        api.versions.version("1.0/beta 2").get()
        assertEquals("/versions/1.0%2Fbeta%202", path)

        api.versions.version(2).get()
        assertEquals("/versions/2", path)
    }

    @Test
    fun anEncodedSegmentSurvivesTheBaseUrl() = runBlocking {
        // Ktor's `defaultRequest` resolves the absolute request path against the base URL, so its path prefix (`/v1/`)
        // is replaced rather than prepended, as before; the encoded value is kept as is by the merge.
        api("https://api.example.com/v1/").simple.id("a/b c").get()
        assertEquals("https://api.example.com/simple/a%2Fb%20c", requests.last().url.toString())
    }
}
