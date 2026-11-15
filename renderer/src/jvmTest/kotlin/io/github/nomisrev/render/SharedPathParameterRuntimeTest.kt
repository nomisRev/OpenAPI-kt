package io.github.nomisrev.render

import io.github.nomisrev.render.test.client.shared.path.parameter.validation.Api
import io.github.nomisrev.render.test.client.shared.path.parameter.types.Api as TypesApi
import io.github.nomisrev.render.test.client.shared.path.parameter.types.Vaults
import io.ktor.http.HttpMethod
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
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
 * Runtime check for the `shared-path-parameter-validation` golden: operations that declare one path parameter with
 * different validation keywords or defaults are reached through one navigation step. And for the
 * `shared-path-parameter-types` golden: operations that declare it as different types are each reached through the
 * navigation function of their type.
 */
class SharedPathParameterRuntimeTest {
    private val requests = mutableListOf<HttpRequestData>()

    private val client = HttpClient(
        MockEngine { request ->
            requests += request
            respond("", HttpStatusCode.NoContent)
        }
    ) {
        defaultRequest { url("https://example.com") }
    }

    private val api = Api(client)
    private val types = TypesApi(client)

    private val path: String
        get() = requests.last().url.encodedPath

    @Test
    fun everyOperationBelowTheParameterIsReachedThroughOneStep() = runBlocking {
        val item = api.items.id("HA1")

        item.get()
        assertEquals("/items/HA1", path)

        item.parts.get()
        assertEquals("/items/HA1/parts", path)

        item.tags.get()
        assertEquals("/items/HA1/tags", path)
    }

    @Test
    fun aNumberParameterWithBoundsOnOneOperationIsOneStep() = runBlocking {
        val page = api.pages.number(7)

        page.get()
        assertEquals("/pages/7", path)

        page.lines.get()
        assertEquals("/pages/7/lines", path)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun eachTypeOfAParameterLeadsToTheOperationsThatDeclareIt() = runBlocking {
        // The classes hold different operations: `IdStringPath` has no `items`, `IdUuidPath` has no `get`.
        val byString: Vaults.IdStringPath = types.vaults.id("my vault")
        byString.get()
        assertEquals("/vaults/my%20vault", path)

        val uuid = Uuid.parse("123e4567-e89b-12d3-a456-426614174000")
        val byUuid: Vaults.IdUuidPath = types.vaults.id(uuid)
        byUuid.items.get()
        assertEquals("/vaults/123e4567-e89b-12d3-a456-426614174000/items", path)
        byUuid.files.get()
        assertEquals("/vaults/123e4567-e89b-12d3-a456-426614174000/files", path)
    }

    @Test
    fun twoMethodsOfOnePathCanDeclareDifferentTypes() = runBlocking {
        types.pets.petId(7L).get()
        assertEquals(HttpMethod.Get, requests.last().method)
        assertEquals("/pets/7", path)

        types.pets.petId("rex").delete()
        assertEquals(HttpMethod.Delete, requests.last().method)
        assertEquals("/pets/rex", path)
    }

    @Test
    fun anIntAndALongDeclarationAreTwoFunctions() = runBlocking {
        types.pages.number(3).get()
        assertEquals("/pages/3", path)

        types.pages.number(3L).lines.get()
        assertEquals("/pages/3/lines", path)
    }

    @Test
    fun aListAndASingleValueAreTwoFunctions() = runBlocking {
        types.tags.ids(listOf(1, 2)).get()
        assertEquals("/tags/1,2", path)

        types.tags.ids(1).first.get()
        assertEquals("/tags/1/first", path)
    }

    @Test
    fun aNullableDeclarationIsTheSameNotNullableParameter() = runBlocking {
        val note = types.notes.id("n1")

        note.get()
        assertEquals("/notes/n1", path)
        note.text.get()
        assertEquals("/notes/n1/text", path)
    }
}
