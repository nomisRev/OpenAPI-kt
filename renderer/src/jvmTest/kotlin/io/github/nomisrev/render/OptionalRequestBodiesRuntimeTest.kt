package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.optional.request.bodies.Api
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Runtime check for the `optional-request-bodies` golden: an operation whose request body is not `required` can be
 * called without one, which sends neither a body nor a `Content-Type`; the overload taking the body's fields always
 * sends the body, even when every field is absent.
 */
class OptionalRequestBodiesRuntimeTest {
    private class Recorded(val query: String, val contentType: String?, val body: String?)

    private val requests = mutableListOf<Recorded>()

    private val api = Api(
        HttpClient(
            MockEngine { request ->
                requests += Recorded(
                    query = request.url.encodedQuery,
                    contentType = (request.body.contentType ?: request.headers[HttpHeaders.ContentType]?.let {
                        error("Content-Type header outside the body: $it")
                    })?.withoutParameters()?.toString(),
                    body = if (request.body is OutgoingContent.NoContent) null else request.body.toByteArray().decodeToString(),
                )
                respond("", HttpStatusCode.NoContent)
            }
        ) {
            defaultRequest { url("https://example.com") }
            install(ContentNegotiation) { json() }
        }
    )

    private fun request(): Recorded = requests.single().also { requests.clear() }

    private fun assertNoBody(request: Recorded) {
        assertNull(request.contentType, "Content-Type")
        assertNull(request.body, "body")
    }

    @Test
    fun optionalFormIsLeftOut() = runBlocking {
        api.form.post()
        assertNoBody(request())
    }

    @Test
    fun optionalFormIsSentWithItsRequiredFields() = runBlocking {
        api.form.post("Ada")
        request().let {
            assertEquals("application/x-www-form-urlencoded", it.contentType)
            assertEquals("name=Ada", it.body)
        }

        api.form.put("Ada", note = "hi")
        request().let {
            assertEquals("application/x-www-form-urlencoded", it.contentType)
            assertEquals("name=Ada&note=hi", it.body)
        }
    }

    @Test
    fun optionalFormOfOptionalFieldsIsLeftOutOrSentEmpty() = runBlocking {
        api.formOptionalFields.post(q = "x")
        request().let {
            assertEquals("q=x", it.query)
            assertNoBody(it)
        }

        api.formOptionalFields.post(name = null)
        request().let {
            assertEquals("application/x-www-form-urlencoded", it.contentType)
            assertEquals("", it.body)
        }

        api.formOptionalFields.post(q = "x", note = "hi")
        request().let {
            assertEquals("q=x", it.query)
            assertEquals("note=hi", it.body)
        }
    }

    @Test
    fun optionalInlineJsonIsLeftOut() = runBlocking {
        api.json.post()
        assertNoBody(request())
    }

    @Test
    fun optionalInlineJsonIsSentWithItsRequiredFields() = runBlocking {
        api.json.post("Ada")
        request().let {
            assertEquals("application/json", it.contentType)
            assertEquals("""{"name":"Ada"}""", it.body)
        }

        api.json.put("Ada", note = "hi")
        request().let {
            assertEquals("application/json", it.contentType)
            assertEquals("""{"name":"Ada","note":"hi"}""", it.body)
        }
    }

    @Test
    fun optionalInlineJsonOfOptionalFieldsIsLeftOutOrSentEmpty() = runBlocking {
        api.jsonOptionalFields.post()
        assertNoBody(request())

        api.jsonOptionalFields.post(q = "x")
        request().let {
            assertEquals("q=x", it.query)
            assertNoBody(it)
        }

        api.jsonOptionalFields.post(name = null)
        request().let {
            assertEquals("application/json", it.contentType)
            assertEquals("{}", it.body)
        }

        api.jsonOptionalFields.post(note = "hi")
        assertEquals("""{"note":"hi"}""", request().body)
    }

    @Test
    fun optionalMultipartIsLeftOut() = runBlocking {
        api.multipart.post()
        assertNoBody(request())
    }

    @Test
    fun optionalMultipartIsSentWithItsRequiredParts() = runBlocking {
        api.multipart.post("abc".encodeToByteArray())
        request().let {
            assertEquals("multipart/form-data", it.contentType)
            val body = requireNotNull(it.body)
            assertTrue("name=\"file\"" in body && "abc" in body && "name=\"note\"" !in body, body)
        }

        api.multipart.put("abc".encodeToByteArray(), note = "hi")
        request().let {
            assertEquals("multipart/form-data", it.contentType)
            val body = requireNotNull(it.body)
            assertTrue("name=\"file\"" in body && "name=\"note\"" in body && "hi" in body, body)
        }
    }

    @Test
    fun optionalFormOrMultipartIsLeftOut() = runBlocking {
        api.formOrMultipart.post()
        assertNoBody(request())
    }
}
