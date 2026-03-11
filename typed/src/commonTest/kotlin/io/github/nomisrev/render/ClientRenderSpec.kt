package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.Root
import io.github.nomisrev.openapi.render.renderRootFile
import io.github.nomisrev.openapi.render.renderer
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.routes.SchemaContext
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private fun route(
    operationId: String,
    path: String,
    method: HttpMethod = HttpMethod.Get,
    returnModel: Model = Model.Primitive.String(null, null, null, false, null),
    statusCode: HttpStatusCode = HttpStatusCode.OK,
): Route = Route(
    operationId = operationId,
    summary = null,
    path = path,
    method = method,
    body = null,
    parameters = emptyList(),
    returns = Route.Returns(
        default = null,
        responses = mapOf(
            statusCode to Route.ReturnType(
                types = mapOf(ContentType.Application.Json to returnModel),
                extensions = emptyMap()
            )
        ),
        extensions = emptyMap()
    ),
    extensions = emptyMap(),
    deprecated = false,
)

val clientRenderSpec by testSuite {

    test("single parameterless GET endpoint - root interface") {
        val root = Root(
            name = "PetStore",
            operations = listOf(
                route("listPets", "/pets")
            ),
            endpoints = emptyList(),
        )

        val (actual, _) = renderer { root.renderRootFile() }

        val expected = """
            |interface PetStore {
            |    suspend fun listPets(): String
            |}
            |
            |internal class KtorPetStore(private val client: HttpClient) : PetStore {
            |    override suspend fun listPets(): String =
            |        client.get("/pets").body()
            |}
            |
            |fun PetStoreClient(
            |    baseUrl: String,
            |    block: HttpClientConfig<*>.() -> Unit = {},
            |): PetStore {
            |    val client = HttpClient {
            |        install(ContentNegotiation) { json() }
            |        defaultRequest { url(baseUrl) }
            |        block()
            |    }
            |    return KtorPetStore(client)
            |}
        """.trimMargin()

        assertEq(expected, actual)
    }

    test("single GET endpoint returning a reference type") {
        val returnModel = Model.Reference(
            context = NamingContext.reference("ListPets", SchemaContext.Read),
            description = null,
            isNullable = false,
            title = null
        )

        val root = Root(
            name = "PetStore",
            operations = listOf(
                route("listPets", "/pets", returnModel = returnModel)
            ),
            endpoints = emptyList(),
        )

        val (actual, imports) = renderer { root.renderRootFile() }

        val expected = """
            |interface PetStore {
            |    suspend fun listPets(): ListPetsResponse
            |}
            |
            |internal class KtorPetStore(private val client: HttpClient) : PetStore {
            |    override suspend fun listPets(): ListPetsResponse =
            |        client.get("/pets").body()
            |}
            |
            |fun PetStoreClient(
            |    baseUrl: String,
            |    block: HttpClientConfig<*>.() -> Unit = {},
            |): PetStore {
            |    val client = HttpClient {
            |        install(ContentNegotiation) { json() }
            |        defaultRequest { url(baseUrl) }
            |        block()
            |    }
            |    return KtorPetStore(client)
            |}
        """.trimMargin()

        assertEq(expected, actual)

        // Should import the model type (ListPets + SchemaContext.Read -> ListPetsResponse)
        val modelImport = imports.filterIsInstance<io.github.nomisrev.openapi.render.TypeName.Class>()
            .any { it.simpleName == "ListPetsResponse" && it.packageName == "io.github.nomisrev.model" }
        assertEquals(true, modelImport, "Expected import for ListPetsResponse model type. Imports: $imports")
    }

    test("single GET returning Unit for empty response") {
        val root = Root(
            name = "PetStore",
            operations = listOf(
                route(
                    "healthCheck",
                    "/health",
                    returnModel = Model.Primitive.Unit(null, false, null)
                )
            ),
            endpoints = emptyList(),
        )

        val (actual, _) = renderer { root.renderRootFile() }

        val expected = """
            |interface PetStore {
            |    suspend fun healthCheck(): Unit
            |}
            |
            |internal class KtorPetStore(private val client: HttpClient) : PetStore {
            |    override suspend fun healthCheck(): Unit =
            |        client.get("/health").body()
            |}
            |
            |fun PetStoreClient(
            |    baseUrl: String,
            |    block: HttpClientConfig<*>.() -> Unit = {},
            |): PetStore {
            |    val client = HttpClient {
            |        install(ContentNegotiation) { json() }
            |        defaultRequest { url(baseUrl) }
            |        block()
            |    }
            |    return KtorPetStore(client)
            |}
        """.trimMargin()

        assertEq(expected, actual)
    }

    test("empty root generates interface with no members") {
        val root = Root(
            name = "EmptyApi",
            operations = emptyList(),
            endpoints = emptyList(),
        )

        val (actual, _) = renderer { root.renderRootFile() }

        val expected = """
            |interface EmptyApi
            |
            |internal class KtorEmptyApi(private val client: HttpClient) : EmptyApi
            |
            |fun EmptyApiClient(
            |    baseUrl: String,
            |    block: HttpClientConfig<*>.() -> Unit = {},
            |): EmptyApi {
            |    val client = HttpClient {
            |        install(ContentNegotiation) { json() }
            |        defaultRequest { url(baseUrl) }
            |        block()
            |    }
            |    return KtorEmptyApi(client)
            |}
        """.trimMargin()

        assertEq(expected, actual)
    }

    test("read variant type is used for response types") {
        // A reference with SchemaContext.Read should produce the "Response" suffix
        val returnModel = Model.Reference(
            context = NamingContext.reference("Pet", SchemaContext.Read),
            description = null,
            isNullable = false,
            title = null
        )

        val root = Root(
            name = "PetStore",
            operations = listOf(
                route("getPet", "/pet", returnModel = returnModel)
            ),
            endpoints = emptyList(),
        )

        val (actual, _) = renderer { root.renderRootFile() }

        // SchemaContext.Read appends "Response" suffix
        assertTrue(
            actual.contains("suspend fun getPet(): PetResponse"),
            "Expected 'PetResponse' return type (Read variant) in:\n$actual"
        )
    }
}

private fun assertEq(expected: String, actual: String) {
    if (expected != actual) throw AssertionError(expected.diff(actual))
}
