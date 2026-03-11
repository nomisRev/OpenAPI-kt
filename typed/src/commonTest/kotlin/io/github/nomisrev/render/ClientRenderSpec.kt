package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.Root
import io.github.nomisrev.openapi.parser.Parameter
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
    parameters: List<Route.Input> = emptyList(),
    deprecated: Boolean = false,
): Route = Route(
    operationId = operationId,
    summary = null,
    path = path,
    method = method,
    body = null,
    parameters = parameters,
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
    deprecated = deprecated,
)

private fun pathParam(name: String, type: Model = Model.Primitive.String(null, null, null, false, null)) =
    Route.Input(name = name, type = type, isRequired = true, input = Parameter.Input.Path, description = null)

private fun queryParam(name: String, type: Model = Model.Primitive.String(null, null, null, false, null), isRequired: Boolean = false) =
    Route.Input(name = name, type = type, isRequired = isRequired, input = Parameter.Input.Query, description = null)

private fun headerParam(name: String, type: Model = Model.Primitive.String(null, null, null, false, null), isRequired: Boolean = false) =
    Route.Input(name = name, type = type, isRequired = isRequired, input = Parameter.Input.Header, description = null)

private fun cookieParam(name: String, type: Model = Model.Primitive.String(null, null, null, false, null), isRequired: Boolean = false) =
    Route.Input(name = name, type = type, isRequired = isRequired, input = Parameter.Input.Cookie, description = null)

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

    test("path parameter interpolation") {
        val root = Root(
            name = "PetStore",
            operations = listOf(
                route(
                    "retrieveModel",
                    "/models/{model}",
                    parameters = listOf(pathParam("model"))
                )
            ),
            endpoints = emptyList(),
        )

        val (actual, _) = renderer { root.renderRootFile() }

        assertTrue(actual.contains("suspend fun retrieveModel(\n        model: String,\n    ): String"), "Expected path param in interface:\n$actual")
        assertTrue(actual.contains("override suspend fun retrieveModel(model: String): String ="), "Expected path param in impl:\n$actual")
        assertTrue(actual.contains("\"/models/\$model\""), "Expected path interpolation in impl:\n$actual")
    }

    test("required query parameter") {
        val root = Root(
            name = "Api",
            operations = listOf(
                route(
                    "search",
                    "/search",
                    parameters = listOf(queryParam("query", isRequired = true))
                )
            ),
            endpoints = emptyList(),
        )

        val (actual, _) = renderer { root.renderRootFile() }

        assertTrue(actual.contains("query: String,"), "Expected required query param in interface:\n$actual")
        assertTrue(actual.contains("parameter(\"query\", query)"), "Expected parameter() call in impl:\n$actual")
    }

    test("optional query parameter") {
        val root = Root(
            name = "Api",
            operations = listOf(
                route(
                    "listItems",
                    "/items",
                    parameters = listOf(queryParam("limit", type = Model.Primitive.Int(null, null, null, false, null)))
                )
            ),
            endpoints = emptyList(),
        )

        val (actual, _) = renderer { root.renderRootFile() }

        assertTrue(actual.contains("limit: Int? = null,"), "Expected optional query param in interface:\n$actual")
        assertTrue(actual.contains("limit?.let { parameter(\"limit\", it) }"), "Expected ?.let pattern in impl:\n$actual")
    }

    test("header parameter") {
        val root = Root(
            name = "Api",
            operations = listOf(
                route(
                    "getData",
                    "/data",
                    parameters = listOf(headerParam("X-Api-Key", isRequired = true))
                )
            ),
            endpoints = emptyList(),
        )

        val (actual, _) = renderer { root.renderRootFile() }

        assertTrue(actual.contains("xApiKey: String,"), "Expected camelCase header param in interface:\n$actual")
        assertTrue(actual.contains("header(\"X-Api-Key\", xApiKey)"), "Expected header() call in impl:\n$actual")
    }

    test("cookie parameter") {
        val root = Root(
            name = "Api",
            operations = listOf(
                route(
                    "getData",
                    "/data",
                    parameters = listOf(cookieParam("session_id", isRequired = true))
                )
            ),
            endpoints = emptyList(),
        )

        val (actual, _) = renderer { root.renderRootFile() }

        assertTrue(actual.contains("sessionId: String,"), "Expected camelCase cookie param in interface:\n$actual")
        assertTrue(actual.contains("cookie(\"session_id\", sessionId)"), "Expected cookie() call in impl:\n$actual")
    }

    test("parameter ordering - path then required query then required header then optional query then optional header") {
        val root = Root(
            name = "Api",
            operations = listOf(
                route(
                    "getResource",
                    "/resources/{id}",
                    parameters = listOf(
                        headerParam("X-Optional", isRequired = false),
                        queryParam("optionalQ", isRequired = false),
                        headerParam("X-Required", isRequired = true),
                        queryParam("requiredQ", isRequired = true),
                        pathParam("id"),
                    )
                )
            ),
            endpoints = emptyList(),
        )

        val (actual, _) = renderer { root.renderRootFile() }

        // Extract parameter lines from the interface suspend fun
        val paramSection = actual.substringAfter("suspend fun getResource(").substringBefore("): String")
        val paramNames = paramSection.lines().map { it.trim() }.filter { it.contains(":") }.map { it.substringBefore(":").trim() }

        assertEquals(
            listOf("id", "requiredQ", "xRequired", "optionalQ", "xOptional"),
            paramNames,
            "Parameters should be ordered: path, required query, required header, optional query, optional header.\nActual interface:\n$actual"
        )
    }

    test("query parameter with non-null default value") {
        val root = Root(
            name = "Api",
            operations = listOf(
                route(
                    "listItems",
                    "/items",
                    parameters = listOf(
                        queryParam(
                            "limit",
                            type = Model.Primitive.Int(
                                default = Model.Default.Value(20),
                                description = null,
                                constraint = null,
                                isNullable = false,
                                title = null
                            ),
                            isRequired = false
                        )
                    )
                )
            ),
            endpoints = emptyList(),
        )

        val (actual, _) = renderer { root.renderRootFile() }

        assertTrue(actual.contains("limit: Int = 20,"), "Expected default value in interface:\n$actual")
        // Non-null default means always sent (no ?.let)
        assertTrue(actual.contains("parameter(\"limit\", limit)"), "Expected direct parameter() call (no ?.let) for param with default:\n$actual")
    }

    test("required parameter with default renders default value without annotation") {
        val root = Root(
            name = "Api",
            operations = listOf(
                route(
                    "listItems",
                    "/items",
                    parameters = listOf(
                        queryParam(
                            "limit",
                            type = Model.Primitive.Int(
                                default = Model.Default.Value(20),
                                description = null,
                                constraint = null,
                                isNullable = false,
                                title = null
                            ),
                            isRequired = true
                        )
                    )
                )
            ),
            endpoints = emptyList(),
        )

        val (actual, _) = renderer { root.renderRootFile() }

        assertTrue(actual.contains("limit: Int = 20,"), "Expected default value on required param:\n$actual")
        assertTrue(!actual.contains("@Required"), "Function parameters should not have @Required annotation:\n$actual")
    }

    test("@Deprecated annotation for deprecated operation") {
        val root = Root(
            name = "Api",
            operations = listOf(
                route(
                    "legacyEndpoint",
                    "/legacy",
                    deprecated = true
                )
            ),
            endpoints = emptyList(),
        )

        val (actual, _) = renderer { root.renderRootFile() }

        assertTrue(
            actual.contains("@Deprecated(\"Deprecated by the API provider\")\n    suspend fun legacyEndpoint(): String"),
            "Expected @Deprecated annotation on interface function:\n$actual"
        )
        assertTrue(
            actual.contains("@Deprecated(\"Deprecated by the API provider\")\n    override suspend fun legacyEndpoint(): String"),
            "Expected @Deprecated annotation on impl function:\n$actual"
        )
    }

    test("camelCase conversion for parameter names") {
        val root = Root(
            name = "Api",
            operations = listOf(
                route(
                    "listEvents",
                    "/fine_tuning/jobs/{fine_tuning_job_id}/events",
                    parameters = listOf(pathParam("fine_tuning_job_id"))
                )
            ),
            endpoints = emptyList(),
        )

        val (actual, _) = renderer { root.renderRootFile() }

        assertTrue(actual.contains("fineTuningJobId: String"), "Expected camelCase param name 'fineTuningJobId':\n$actual")
        assertTrue(actual.contains("\$fineTuningJobId"), "Expected camelCase interpolation in URL:\n$actual")
    }
}

private fun assertEq(expected: String, actual: String) {
    if (expected != actual) throw AssertionError(expected.diff(actual))
}
