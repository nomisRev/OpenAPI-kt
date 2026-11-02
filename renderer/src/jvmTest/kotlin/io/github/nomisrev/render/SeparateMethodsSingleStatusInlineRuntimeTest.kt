package io.github.nomisrev.render

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
import io.github.nomisrev.render.test.client.response.separate.methods.single.status.`inline`.Pets as InlinePets
import io.github.nomisrev.render.test.client.response.separate.methods.single.status.`inline`.with.error.Pets as InlineWithErrorPets

/**
 * Runtime check for the `response-separate-methods-single-status-inline*` goldens: with one generated
 * method per success content type, a single success status whose `application/json` payload is an inline
 * object must decode into that inline model, not ask the deserializer for the per-content-type wrapper.
 */
class SeparateMethodsSingleStatusInlineRuntimeTest {
    private fun httpClient(
        status: HttpStatusCode,
        responseContentType: ContentType,
        responseBody: String,
        accepts: MutableList<String?>,
    ): HttpClient = HttpClient(MockEngine { request ->
        accepts += request.headers[HttpHeaders.Accept]
        respond(responseBody, status, headersOf(HttpHeaders.ContentType, responseContentType.toString()))
    }) {
        install(ContentNegotiation) { json() }
    }

    @Test
    fun singleStatusJsonMethodDecodesInlineModel() = runBlocking {
        val accepts = mutableListOf<String?>()
        val pets = InlinePets(
            httpClient(HttpStatusCode.OK, ContentType.Application.Json, """{"id":42,"name":"Rex"}""", accepts)
        )

        val result: InlinePets.Get.JsonResponse = pets.get.json()

        assertEquals(InlinePets.Get.JsonResponse(id = 42L, name = "Rex"), result)
        assertEquals(listOf<String?>("application/json"), accepts)
    }

    @Test
    fun singleStatusTextPlainMethodDecodesString() = runBlocking {
        val accepts = mutableListOf<String?>()
        val pets = InlinePets(httpClient(HttpStatusCode.OK, ContentType.Text.Plain, "Rex", accepts))

        val result: String = pets.get.textPlain()

        assertEquals("Rex", result)
        assertEquals(listOf<String?>("text/plain"), accepts)
    }

    @Test
    fun inlineModelWithDeclaredErrorDecodesOkAndError() = runBlocking {
        val accepts = mutableListOf<String?>()
        val ok = InlineWithErrorPets(
            httpClient(HttpStatusCode.OK, ContentType.Application.Json, """{"id":42,"name":"Rex"}""", accepts)
        )
        assertEquals(InlineWithErrorPets.Get.JsonResponse.Ok(id = 42L, name = "Rex"), ok.get.json())

        val notFound = InlineWithErrorPets(
            httpClient(HttpStatusCode.NotFound, ContentType.Application.Json, "7", accepts)
        )
        assertEquals(InlineWithErrorPets.Get.NotFound(7), notFound.get.json())
    }
}
