package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.media.type.range.uploads.Api
import io.github.nomisrev.render.test.client.media.type.range.uploads.Pet
import io.github.nomisrev.render.test.client.media.type.range.uploads.Scans
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

// Runtime check for the `media-type-range-uploads` golden. A binary body declared under a media type range the client
// cannot pick a concrete media type from (`image/*`, `video/*`, `font/*`) goes out verbatim with the `Content-Type`
// the caller passes, and a media type outside the declared range, or a range itself, is rejected before anything is
// sent.
class MediaTypeRangeUploadRuntimeTest {
    private class Recorded(val contentType: String?, val bytes: ByteArray?, val url: String)

    private fun HttpRequestData.record(): Recorded = Recorded(
        contentType = body.contentType?.toString(),
        bytes = when (val content = body) {
            is OutgoingContent.ByteArrayContent -> content.bytes()
            is OutgoingContent.NoContent -> null
            else -> error("Unexpected request body: $content")
        },
        url = url.toString(),
    )

    private fun engine(requests: MutableList<Recorded>) = MockEngine { request ->
        requests += request.record()
        respond("", HttpStatusCode.NoContent)
    }

    /** Configured like the generated `Api(baseUrl)` constructor. */
    private fun defaultClient(requests: MutableList<Recorded>): HttpClient =
        HttpClient(engine(requests)) {
            defaultRequest { url("https://example.com") }
            install(ContentNegotiation) { json() }
        }

    /** A user-supplied client with its own `Json` registered explicitly for `application/json` only. */
    private fun userClient(requests: MutableList<Recorded>): HttpClient =
        HttpClient(engine(requests)) {
            install(ContentNegotiation) {
                json(Json { encodeDefaults = true }, contentType = ContentType.Application.Json)
            }
        }

    private fun bothClients(block: suspend (Api, MutableList<Recorded>) -> Unit) = runBlocking {
        for (client in listOf(::defaultClient, ::userClient)) {
            val requests = mutableListOf<Recorded>()
            block(Api(client(requests)), requests)
        }
    }

    private val png = byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, 0x00, 0xFF.toByte())

    @Test
    fun imageBodyIsSentAsTheSuppliedMediaType() = bothClients { api, requests ->
        api.avatars.put(png, ContentType.Image.PNG)

        val request = requests.single()
        assertEquals("image/png", request.contentType)
        assertContentEquals(png, request.bytes)
    }

    @Test
    fun suppliedMediaTypeParametersAreSent() = bothClients { api, requests ->
        val svg = "<svg/>".encodeToByteArray()
        api.avatars.put(svg, ContentType.Image.SVG.withParameter("charset", "utf-8"))

        assertEquals("image/svg+xml; charset=utf-8", requests.single().contentType)
        assertContentEquals(svg, requests.single().bytes)
    }

    @Test
    fun mediaTypeOutsideTheDeclaredRangeIsRejectedBeforeSending() = bothClients { api, requests ->
        for (contentType in listOf(ContentType.Application.Json, ContentType.Video.MP4, ContentType.Image.Any, ContentType.Any)) {
            val error = assertFailsWith<IllegalArgumentException> { api.avatars.put(png, contentType) }
            assertEquals("contentType must be a media type in image/*, but was $contentType", error.message)
        }
        assertTrue(requests.isEmpty())
    }

    @Test
    fun optionalImageBodyIsSentAsTheSuppliedMediaType() = bothClients { api, requests ->
        api.thumbnails.post(size = 64, body = png, contentType = ContentType.Image.JPEG)

        val request = requests.single()
        assertEquals("image/jpeg", request.contentType)
        assertContentEquals(png, request.bytes)
        assertTrue(request.url.endsWith("/thumbnails?size=64"), request.url)
    }

    @Test
    fun omittedOptionalImageBodyNeedsNoMediaType() = bothClients { api, requests ->
        api.thumbnails.post()

        assertNull(requests.single().bytes)
        assertNull(requests.single().contentType)
    }

    @Test
    fun optionalImageBodyWithoutMediaTypeIsRejectedBeforeSending() = bothClients { api, requests ->
        val error = assertFailsWith<IllegalArgumentException> { api.thumbnails.post(body = png) }
        assertEquals("contentType must be a media type in image/*, but was null", error.message)
        assertTrue(requests.isEmpty())
    }

    @Test
    fun bodyUnderSeveralRangesAcceptsAMediaTypeInAnyOfThem() = bothClients { api, requests ->
        api.media.post(png, ContentType.Image.PNG)
        api.media.post(png, ContentType.Video.MP4)

        assertEquals(listOf("image/png", "video/mp4"), requests.map { it.contentType })
        requests.forEach { assertContentEquals(png, it.bytes) }

        val error = assertFailsWith<IllegalArgumentException> { api.media.post(png, ContentType.Application.Pdf) }
        assertEquals("contentType must be a media type in image/* or video/*, but was application/pdf", error.message)
        assertEquals(2, requests.size)
    }

    @Test
    fun imageOverloadNextToJsonBody() = bothClients { api, requests ->
        api.documents.post(Pet(name = "Rex"))
        api.documents.post(png, ContentType.Image.GIF)

        assertEquals(listOf("application/json", "image/gif"), requests.map { it.contentType })
        assertEquals("""{"name":"Rex"}""", requests[0].bytes?.decodeToString())
        assertContentEquals(png, requests[1].bytes)
    }

    @Test
    fun imageOverloadNextToClashingBinaryBodies() = bothClients { api, requests ->
        api.scans.post(png, Scans.Post.RequestType.OctetStream)
        api.scans.post(png, Scans.Post.RequestType.Pdf)
        api.scans.post(png, ContentType.Image.PNG)

        assertEquals(listOf("application/octet-stream", "application/pdf", "image/png"), requests.map { it.contentType })
        requests.forEach { assertContentEquals(png, it.bytes) }
    }

    @Test
    fun mediaTypeArgumentIsRenamedNextToAContentTypeParameter() = bothClients { api, requests ->
        val font = byteArrayOf(0x77, 0x4F, 0x46, 0x32)
        api.fonts.post(contentType = "woff2", body = font, bodyContentType = ContentType("font", "woff2"))

        val request = requests.single()
        assertEquals("font/woff2", request.contentType)
        assertContentEquals(font, request.bytes)
        assertTrue(request.url.endsWith("/fonts?contentType=woff2"), request.url)

        assertFailsWith<IllegalArgumentException> {
            api.fonts.post(contentType = "woff2", body = font, bodyContentType = ContentType.Image.PNG)
        }
        assertEquals(1, requests.size)
    }
}
