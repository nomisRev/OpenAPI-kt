package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.discriminator.several.tags.Animal
import io.github.nomisrev.render.test.client.discriminator.several.tags.Invocation
import io.github.nomisrev.render.test.client.discriminator.several.tags.Invocation.AddMediaInvocation
import io.github.nomisrev.render.test.client.discriminator.several.tags.Media
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Runtime check for the `client/discriminator-several-tags` golden: a schema the mapping selects under several tags is
 * one case whose discriminator property is an enum of them, so each tag decodes to that case and is written back as it
 * was read, through the base and through the case's own serializer (an operation typed as the case).
 */
class DiscriminatorSeveralTagsRuntimeTest {
    private fun api(body: String, sent: MutableList<String> = mutableListOf()) = Media(HttpClient(MockEngine { request ->
        (request.body as? TextContent)?.let { sent += it.text }
        respond(body, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
    }) {
        install(ContentNegotiation) { json() }
    })

    @Test
    fun eachTagOfACaseDecodesToItAndIsWrittenBack() {
        for ([tag, method] in listOf(
            "AddMedia.confirm" to AddMediaInvocation.Method.AddMediaConfirm,
            "AddMedia.handle" to AddMediaInvocation.Method.AddMediaHandle,
        )) {
            val json = """{"method":"$tag","item":"song"}"""
            val value = AddMediaInvocation(method = method, item = "song")
            assertEquals(value, Json.decodeFromString<Invocation>(json))
            assertEquals(value, Json.decodeFromString<AddMediaInvocation>(json))
            assertEquals(json, Json.encodeToString<Invocation>(value))
            assertEquals(json, Json.encodeToString(value))
        }
    }

    @Test
    fun aCaseOfOneTagIsUnchanged() {
        val json = """{"method":"PlayMedia.handle","shuffle":true}"""
        val value = Invocation.PlayMediaHandle(shuffle = true)
        assertEquals(value, Json.decodeFromString<Invocation>(json))
        assertEquals(json, Json.encodeToString<Invocation>(value))
    }

    @Test
    fun theBaseUnderSeveralTagsIsOneCaseWithItsTag() {
        val json = """[{"kind":"beast","name":"Rex"},{"kind":"animal"},{"kind":"bird","span":0.3}]"""
        val animals = listOf(
            Animal.Default(kind = Animal.Default.Kind.Beast, name = "Rex"),
            Animal.Default(kind = Animal.Default.Kind.Animal),
            Animal.Bird(span = 0.3),
        )
        assertEquals(animals, Json.decodeFromString<List<Animal>>(json))
        assertEquals(json, Json.encodeToString(animals))
    }

    @Test
    fun anOperationTypedAsTheCaseDecodesEachOfItsTags() = runBlocking {
        val value: AddMediaInvocation = api("""{"method":"AddMedia.handle"}""").addMedia.id("1").get()
        assertEquals(AddMediaInvocation(method = AddMediaInvocation.Method.AddMediaHandle), value)
    }

    @Test
    fun aRequestBodyIsSentWithItsOwnTag() = runBlocking {
        val sent = mutableListOf<String>()
        api("""{"method":"AddMedia.confirm"}""", sent).invoke.post(AddMediaInvocation(method = AddMediaInvocation.Method.AddMediaConfirm))
        assertEquals(listOf("""{"method":"AddMedia.confirm"}"""), sent)
    }

    @Test
    fun aTagOfAnotherCaseIsAnError() {
        assertFailsWith<SerializationException> {
            Json.decodeFromString<AddMediaInvocation>("""{"method":"PlayMedia.handle"}""")
        }
        assertFailsWith<SerializationException> {
            Json.decodeFromString<Invocation>("""{"method":"AddMediaInvocation"}""")
        }
    }
}
