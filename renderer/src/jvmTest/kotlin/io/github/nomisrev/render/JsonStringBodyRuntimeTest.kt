package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.json.string.bodies.Claims
import io.github.nomisrev.render.test.client.json.string.bodies.Drafts
import io.github.nomisrev.render.test.client.json.string.bodies.Names
import io.github.nomisrev.render.test.client.json.string.bodies.Notes
import io.github.nomisrev.render.test.client.json.string.bodies.Tags
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.headersOf
import io.ktor.serialization.JsonConvertException
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Runtime check for the `json-string-bodies` golden. Ktor's `ContentNegotiation` treats `String` as an ignored
 * type, so a plain `body<String>()` / `setBody(String)` bypasses the JSON converter. A `type: string` body under a
 * JSON media type must still be a JSON string on the wire: quoted and escaped when sent, unquoted and unescaped when
 * received. `text/plain` stays raw.
 */
class JsonStringBodyRuntimeTest {
    private class Recorded(val contentType: String?, val body: String?)

    private companion object {
        fun HttpRequestData.record(): Recorded = Recorded(
            contentType = body.contentType?.withoutParameters()?.toString(),
            body = when (val content = body) {
                is OutgoingContent.ByteArrayContent -> content.bytes().decodeToString()
                is OutgoingContent.NoContent -> null
                else -> error("Unexpected request body: $content")
            },
        )
    }

    private class Fixture(
        status: HttpStatusCode,
        responseContentType: ContentType,
        responseBody: String,
    ) {
        val requests = mutableListOf<Recorded>()
        val client = HttpClient(MockEngine { request ->
            requests += request.record()
            respond(responseBody, status, headersOf(HttpHeaders.ContentType, responseContentType.toString()))
        }) {
            install(ContentNegotiation) { json() }
        }
        val request: Recorded get() = requests.single()
    }

    private fun json(status: HttpStatusCode, body: String, contentType: ContentType = ContentType.Application.Json) =
        Fixture(status, contentType, body)

    @Test
    fun jsonStringRequestAndResponseAreJsonEncoded() = runBlocking {
        val fixture = json(HttpStatusCode.OK, """"name \"taken\""""")

        val result = Names(fixture.client).post("""name "taken"""")

        assertEquals("""name "taken"""", result)
        assertEquals("application/json", fixture.request.contentType)
        assertEquals(""""name \"taken\""""", fixture.request.body)
    }

    @Test
    fun textPlainStringRequestAndResponseStayRaw() = runBlocking {
        val fixture = Fixture(HttpStatusCode.OK, ContentType.Text.Plain, """name "taken"""")

        val result = Names(fixture.client).put("""name "taken"""")

        assertEquals("""name "taken"""", result)
        assertEquals("text/plain", fixture.request.contentType)
        assertEquals("""name "taken"""", fixture.request.body)
    }

    @Test
    fun nonStringJsonIsRejectedForStringBody() = runBlocking {
        val fixture = json(HttpStatusCode.OK, "42")

        assertFailsWith<JsonConvertException> { Names(fixture.client).post("x") }
        Unit
    }

    @Test
    fun optionalJsonStringRequestBodyIsJsonEncoded() = runBlocking {
        val fixture = json(HttpStatusCode.Created, """"ok"""")

        assertEquals(Claims.Post.Response.Created("ok"), Claims(fixture.client).post("a\nb"))
        assertEquals(""""a\nb"""", fixture.request.body)
    }

    @Test
    fun nullableJsonStringResponseDecodesNull() = runBlocking {
        val fixture = json(HttpStatusCode.Created, "null")

        assertEquals(Claims.Post.Response.Created(null), Claims(fixture.client).post())
    }

    @Test
    fun errorStringBodiesAreJsonDecoded() = runBlocking {
        val problemJson = ContentType("application", "problem+json")
        assertEquals(
            Claims.Post.Response.Conflict("""already "claimed""""),
            Claims(json(HttpStatusCode.Conflict, """"already \"claimed\""""", problemJson).client).post(),
        )
        assertEquals(
            Claims.Post.Response.Status5XX(HttpStatusCode.BadGateway, "upstream \\ down"),
            Claims(json(HttpStatusCode.BadGateway, """"upstream \\ down"""").client).post(),
        )
        assertEquals(
            Claims.Post.Response.Default(HttpStatusCode.NotFound, "missingé"),
            Claims(json(HttpStatusCode.NotFound, """"missingé"""").client).post(),
        )
    }

    @Test
    fun separateContentTypeMethodsDecodeOnlyJsonAsJson() = runBlocking {
        assertEquals("a\"b", Notes(json(HttpStatusCode.OK, """"a\"b"""").client).get.json())
        assertEquals(
            """"a\"b"""",
            Notes(Fixture(HttpStatusCode.OK, ContentType.Text.Plain, """"a\"b"""").client).get.textPlain(),
        )
    }

    @Test
    fun requestTypeChoosesJsonEncodingPerContentType() = runBlocking {
        val jsonFixture = json(HttpStatusCode.NoContent, "")
        Drafts(jsonFixture.client).post("say \"hi\"", Drafts.Post.RequestType.Json)
        assertEquals("application/json", jsonFixture.request.contentType)
        assertEquals(""""say \"hi\""""", jsonFixture.request.body)

        val textFixture = json(HttpStatusCode.NoContent, "")
        Drafts(textFixture.client).post("say \"hi\"", Drafts.Post.RequestType.TextPlain)
        assertEquals("text/plain", textFixture.request.contentType)
        assertEquals("say \"hi\"", textFixture.request.body)
    }

    @Test
    fun overloadedStringCaseIsJsonEncoded() = runBlocking {
        val stringFixture = json(HttpStatusCode.NoContent, "")
        Tags(stringFixture.client).put("tag \"one\"")
        assertEquals(""""tag \"one\""""", stringFixture.request.body)

        val objectFixture = json(HttpStatusCode.NoContent, "")
        Tags(objectFixture.client).put(Tags.Put.Name("one"))
        assertEquals("""{"name":"one"}""", objectFixture.request.body)
    }
}
