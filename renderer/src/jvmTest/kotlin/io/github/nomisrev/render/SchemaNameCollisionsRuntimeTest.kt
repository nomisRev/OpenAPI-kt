package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.schema.names.same.Api
import io.github.nomisrev.render.test.client.schema.names.same.Either
import io.github.nomisrev.render.test.client.schema.names.same.Implicit
import io.github.nomisrev.render.test.client.schema.names.same.Metadata
import io.github.nomisrev.render.test.client.schema.names.same.Metadata2
import io.github.nomisrev.render.test.client.schema.names.same.Thing
import io.github.nomisrev.render.test.client.schema.names.suffix.Api as SuffixApi
import io.github.nomisrev.render.test.client.schema.names.suffix.AvatarRead
import io.github.nomisrev.render.test.client.schema.names.suffix.AvatarRead2
import io.github.nomisrev.render.test.client.schema.names.suffix.AvatarWrite
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * Runtime check for the `schema-names-*` goldens: schemas that map to one Kotlin name are different classes, each
 * decoding its own shape, and the wire values of a discriminated union are still the schema names and mapping keys.
 */
class SchemaNameCollisionsRuntimeTest {
    private val requests = mutableListOf<HttpRequestData>()

    private fun client(status: HttpStatusCode, body: String) = HttpClient(
        MockEngine { request ->
            requests += request
            respond(body, status, headersOf(HttpHeaders.ContentType, "application/json"))
        }
    ) {
        defaultRequest { url("https://example.com") }
        install(ContentNegotiation) { json() }
    }

    private fun sent() = (requests.last().body as OutgoingContent.ByteArrayContent).bytes().decodeToString()

    @Test
    fun eachSchemaOfOneKotlinNameDecodesItsOwnShape() = runBlocking {
        val plain: Metadata = Api(client(HttpStatusCode.OK, """{"kind":"k","a":"x"}""")).plain.get()
        assertEquals(Metadata(kind = "k", a = "x"), plain)

        val meta: Metadata2 = Api(client(HttpStatusCode.OK, """{"kind":"k","b":"y","level":"high"}""")).meta.get()
        assertEquals(Metadata2(kind = "k", b = "y", level = Metadata2.Level.High), meta)
    }

    @Test
    fun theMappingKeysOfADiscriminatedUnionStayTheWireValues() = runBlocking {
        val plain = Api(client(HttpStatusCode.OK, """{"kind":"plain","a":"x"}""")).things.get()
        assertIs<Thing.Plain>(plain)
        assertEquals("x", plain.a)

        val meta = Api(client(HttpStatusCode.OK, """{"kind":"meta","b":"y"}""")).things.get()
        assertIs<Thing.Meta>(meta)
        assertEquals("y", meta.b)
    }

    @Test
    fun theSchemaNamesWithoutAMappingStayTheWireValues() = runBlocking {
        val other = Api(client(HttpStatusCode.OK, """{"kind":"Other","o":"z"}""")).byName.get()
        assertIs<Implicit.Other>(other)
        assertEquals("z", other.o)

        val meta = Api(client(HttpStatusCode.OK, """{"kind":"metadata","b":"y"}""")).byName.get()
        assertIs<Implicit.Metadata>(meta)
        assertEquals("y", meta.b)
    }

    @Test
    fun aUnionCaseDecodesTheRenamedSchema() = runBlocking {
        val either = Api(client(HttpStatusCode.OK, """{"kind":"k","b":"y"}""")).pick.get()

        assertEquals(Either.CaseMetadata2(Metadata2(kind = "k", b = "y")), either)
    }

    @Test
    fun aVariantNamedLikeAComponentIsADifferentClass() = runBlocking {
        val read: AvatarRead2 = SuffixApi(client(HttpStatusCode.OK, """{"id":"1"}""")).avatars.get()
        assertEquals(AvatarRead2(id = "1"), read)

        val component: AvatarRead = SuffixApi(client(HttpStatusCode.OK, """{"b":"y"}""")).legacyAvatars.get()
        assertEquals(AvatarRead(b = "y"), component)

        SuffixApi(client(HttpStatusCode.NoContent, "")).avatars.post(AvatarWrite(secret = "s"))
        assertEquals("""{"secret":"s"}""", sent())
    }
}
