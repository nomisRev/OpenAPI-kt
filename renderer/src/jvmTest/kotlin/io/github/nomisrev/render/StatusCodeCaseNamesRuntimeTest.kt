package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.status.code.case.names.Legal
import io.github.nomisrev.render.test.client.status.code.case.names.Ping
import io.github.nomisrev.render.test.client.status.code.case.names.UnavailableForLegalReasons
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Runtime check for the `status-code-case-names` golden: every declared status code decodes to its own case, also
 * the codes Ktor has no name for.
 */
class StatusCodeCaseNamesRuntimeTest {
    private fun client(status: HttpStatusCode, body: String) = HttpClient(MockEngine {
        respond(body, status, headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()))
    }) {
        install(ContentNegotiation) { json() }
    }

    private fun ping(code: Int, body: String) = runBlocking {
        Ping(client(HttpStatusCode(code, "Vendor"), body)).get()
    }

    private fun legal(status: HttpStatusCode, body: String) = runBlocking {
        Legal(client(status, body)).get()
    }

    @Test
    fun twoVendorCodesDecodeToTheirOwnCases() {
        assertEquals(Ping.Get.Response.Status480(a = "x"), ping(480, """{"a":"x"}"""))
        assertEquals(Ping.Get.Response.Status481(b = "y"), ping(481, """{"b":"y"}"""))
    }

    @Test
    fun theCodeKtorDoesNotNameDecodesToItsIanaCase() {
        assertEquals(
            Legal.Get.Response.UnavailableForLegalReasons(reason = "court order"),
            legal(HttpStatusCode.UnavailableForLegalReasons, """{"reason":"court order"}"""),
        )
    }

    @Test
    fun theHyphenatedKtorCodeDecodesToAnIdentifierCase() {
        assertEquals(
            Legal.Get.Response.MultiStatus(items = "a"),
            legal(HttpStatusCode.MultiStatus, """{"items":"a"}"""),
        )
    }

    @Test
    fun theGeneratedConstantIsTheCodeAndTheIanaReasonPhrase() {
        assertEquals(451, HttpStatusCode.UnavailableForLegalReasons.value)
        assertEquals("Unavailable For Legal Reasons", HttpStatusCode.UnavailableForLegalReasons.description)
    }
}
