package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.allof.required.Api
import io.github.nomisrev.render.test.client.allof.required.Pet
import io.github.nomisrev.render.test.client.allof.required.PetWithExtra
import io.github.nomisrev.render.test.client.allof.required.PetWithName
import io.github.nomisrev.render.test.client.allof.required.Pets
import io.github.nomisrev.render.test.client.allof.required.Required
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * A property any `allOf` member requires is required on the wire: a value without it is rejected, whichever member
 * declares the property, also when it is nullable (`Required.b` must be present, `null` or not). Uses the generated
 * goldens under `kotlinTestData/client/allof-required` directly.
 */
class AllOfRequiredRoundTripTest {
    private val json = Json

    private val valid = """
        {
          "pet": { "id": 1 },
          "withName": { "id": 2, "name": "Rex" },
          "withExtra": { "id": 3, "extra": "x" },
          "required": { "a": "a", "b": null }
        }
    """.trimIndent()

    @Test
    fun decodesAndEncodesTheRequiredProperties() {
        val decoded = json.decodeFromString<Pets>(valid)
        assertEquals(
            Pets(
                pet = Pet(id = 1),
                withName = PetWithName(id = 2, name = "Rex"),
                withExtra = PetWithExtra(id = 3, extra = "x"),
                required = Required(a = "a", b = null),
            ),
            decoded
        )
        assertEquals(json.parseToJsonElement(valid), json.parseToJsonElement(json.encodeToString(decoded)))
    }

    @Test
    fun rejectsAMissingRequiredProperty() {
        listOf(
            valid.replace(""""id": 2, "name": "Rex"""", """"id": 2"""),
            valid.replace(""""id": 3, "extra": "x"""", """"id": 3"""),
            valid.replace(""""a": "a", "b": null""", """"b": null"""),
            valid.replace(""""a": "a", "b": null""", """"a": "a""""),
        ).forEach { invalid ->
            assertFailsWith<SerializationException>(invalid) { json.decodeFromString<Pets>(invalid) }
        }
    }

    private fun api(responseBody: String) = Api(HttpClient(MockEngine {
        respond(responseBody, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
    }) {
        install(ContentNegotiation) { json() }
    })

    @Test
    fun receivesTheRequiredProperties() = runBlocking {
        // SerializationException, possibly wrapped by Ktor's body transformation.
        assertFailsWith<Exception> { api(valid.replace(""", "name": "Rex"""", "")).holder.get() }
        assertEquals("Rex", api(valid).holder.get().withName.name)
    }
}
