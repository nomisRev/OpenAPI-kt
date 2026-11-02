package io.github.nomisrev.render

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import io.github.nomisrev.render.test.client.json.string.bodies.Notes as JsonStringNotes
import io.github.nomisrev.render.test.client.operations.`inline`.method.uniqueness.Widgets as CreatedWidgets
import io.github.nomisrev.render.test.client.response.separate.methods.single.status.`inline`.Pets as InlinePets
import io.github.nomisrev.render.test.client.response.separate.methods.single.status.ref.Pet as RefPet
import io.github.nomisrev.render.test.client.response.separate.methods.single.status.ref.Pets as RefPets
import io.github.nomisrev.render.test.client.response.single.Pets as SinglePets
import io.github.nomisrev.render.test.client.response.unit.Pets as UnitPets

/**
 * Runtime check for operations with a single declared status and no declared errors, ranges or default,
 * which return their payload directly instead of a sealed `Response`. Like the sealed path's
 * `else -> throw ResponseException(...)`, any status other than the declared one must throw
 * [ResponseException] before the body is decoded, regardless of the client's `expectSuccess` setting.
 */
class DirectResponseStatusRuntimeTest {
    private fun httpClient(
        status: HttpStatusCode,
        responseBody: String,
        responseContentType: ContentType = ContentType.Application.Json,
        expectSuccess: Boolean = false,
    ): HttpClient = HttpClient(MockEngine {
        respond(responseBody, status, headersOf(HttpHeaders.ContentType, responseContentType.toString()))
    }) {
        this.expectSuccess = expectSuccess
        install(ContentNegotiation) { json() }
    }

    @Test
    fun successStillDecodes() = runBlocking<Unit> {
        assertEquals(listOf("Rex"), SinglePets(httpClient(HttpStatusCode.OK, """["Rex"]""")).get())
        assertEquals(
            RefPet(id = 1L, name = "Rex"),
            RefPets(httpClient(HttpStatusCode.OK, """{"id":1,"name":"Rex"}""")).get.json(),
        )
        assertEquals(
            CreatedWidgets.Post.Response(id = "w1", name = "Widget"),
            CreatedWidgets(httpClient(HttpStatusCode.Created, """{"id":"w1","name":"Widget"}""")).post("Widget"),
        )
        assertEquals("note", JsonStringNotes(httpClient(HttpStatusCode.OK, "\"note\"")).get.json())
        UnitPets(httpClient(HttpStatusCode.NoContent, "")).petId("1").delete()
    }

    @Test
    fun serverErrorWithDecodableBodyThrows() = runBlocking<Unit> {
        val pets = SinglePets(httpClient(HttpStatusCode.InternalServerError, """["Internal error"]"""))
        val error = assertFailsWith<ResponseException> { pets.get() }
        assertEquals(HttpStatusCode.InternalServerError, error.response.status)
    }

    @Test
    fun serverErrorWithErrorBodyThrowsResponseException() = runBlocking<Unit> {
        val pets = RefPets(httpClient(HttpStatusCode.InternalServerError, """{"code":500,"message":"boom"}"""))
        val error = assertFailsWith<ResponseException> { pets.get.json() }
        assertEquals(HttpStatusCode.InternalServerError, error.response.status)
    }

    @Test
    fun perContentTypeMethodsCheckStatus() = runBlocking<Unit> {
        val textPlain = RefPets(httpClient(HttpStatusCode.ServiceUnavailable, "Down", ContentType.Text.Plain))
        assertFailsWith<ResponseException> { textPlain.get.textPlain() }

        val inline = InlinePets(httpClient(HttpStatusCode.NotFound, """{"id":0,"name":"Not found"}"""))
        assertFailsWith<ResponseException> { inline.get.json() }

        val jsonString = JsonStringNotes(httpClient(HttpStatusCode.InternalServerError, "\"boom\""))
        assertFailsWith<ResponseException> { jsonString.get.json() }
    }

    @Test
    fun unitMethodThrowsOnNotFound() = runBlocking<Unit> {
        val pets = UnitPets(httpClient(HttpStatusCode.NotFound, """{"message":"no such pet"}"""))
        val error = assertFailsWith<ResponseException> { pets.petId("1").delete() }
        assertEquals(HttpStatusCode.NotFound, error.response.status)
    }

    @Test
    fun undeclaredSuccessStatusThrows() = runBlocking<Unit> {
        val widgets = CreatedWidgets(httpClient(HttpStatusCode.OK, """{"id":"w1","name":"Widget"}"""))
        val error = assertFailsWith<ResponseException> { widgets.post("Widget") }
        assertEquals(HttpStatusCode.OK, error.response.status)

        val pets = UnitPets(httpClient(HttpStatusCode.OK, ""))
        assertFailsWith<ResponseException> { pets.petId("1").delete() }
    }

    @Test
    fun behavesTheSameWithExpectSuccess() = runBlocking<Unit> {
        assertEquals(
            listOf("Rex"),
            SinglePets(httpClient(HttpStatusCode.OK, """["Rex"]""", expectSuccess = true)).get(),
        )
        val serverError = SinglePets(httpClient(HttpStatusCode.InternalServerError, """["x"]""", expectSuccess = true))
        assertFailsWith<ResponseException> { serverError.get() }

        val notFound = UnitPets(httpClient(HttpStatusCode.NotFound, "", expectSuccess = true))
        assertFailsWith<ResponseException> { notFound.petId("1").delete() }

        val undeclaredSuccess = CreatedWidgets(
            httpClient(HttpStatusCode.OK, """{"id":"w1","name":"Widget"}""", expectSuccess = true)
        )
        assertFailsWith<ResponseException> { undeclaredSuccess.post("Widget") }
    }
}
