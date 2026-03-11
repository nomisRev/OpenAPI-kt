package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.Root
import io.github.nomisrev.openapi.generateClient
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.render.renderRootFile
import io.github.nomisrev.openapi.render.renderer
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.sort
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
    body: Route.Bodies? = null,
    parameters: List<Route.Input> = emptyList(),
    deprecated: Boolean = false,
): Route = Route(
    operationId = operationId,
    summary = null,
    path = path,
    method = method,
    body = body,
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

    test("generateClient splits direct root children into separate files") {
        val root = listOf(
            route("createChatCompletion", "/chat/completions", method = HttpMethod.Post),
            route("listModels", "/models"),
            route("retrieveModel", "/models/{model}", parameters = listOf(pathParam("model")))
        ).sort("OpenAI")

        val files = root.generateClient()
        val actual = files.snapshot()
        val expected = """
            |// Chat.kt
            |package io.github.nomisrev.api
            |
            |import io.ktor.client.HttpClient
            |import io.ktor.client.call.body
            |
            |interface Chat {
            |    val completions: Completions
            |
            |    interface Completions {
            |        suspend fun createChatCompletion(): String
            |    }
            |}
            |
            |internal class KtorChat(private val client: HttpClient) : Chat {
            |    override val completions: Chat.Completions = KtorChatCompletions(client)
            |}
            |
            |internal class KtorChatCompletions(private val client: HttpClient) : Chat.Completions {
            |    override suspend fun createChatCompletion(): String =
            |        client.post("/chat/completions").body()
            |}
            |
            |// Models.kt
            |package io.github.nomisrev.api
            |
            |import io.ktor.client.HttpClient
            |import io.ktor.client.call.body
            |
            |interface Models {
            |    suspend fun listModels(): String
            |
            |    suspend fun retrieveModel(
            |        model: String,
            |    ): String
            |}
            |
            |internal class KtorModels(private val client: HttpClient) : Models {
            |    override suspend fun listModels(): String =
            |        client.get("/models").body()
            |
            |    override suspend fun retrieveModel(model: String): String =
            |        client.get("/models/${'$'}model").body()
            |}
            |
            |// OpenAI.kt
            |package io.github.nomisrev.api
            |
            |import io.ktor.client.HttpClient
            |import io.ktor.client.HttpClientConfig
            |import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
            |import io.ktor.serialization.kotlinx.json.json
            |import io.ktor.client.plugins.defaultRequest
            |
            |interface OpenAI {
            |    val chat: Chat
            |
            |    val models: Models
            |}
            |
            |internal class KtorOpenAI(private val client: HttpClient) : OpenAI {
            |    override val chat: Chat = KtorChat(client)
            |    override val models: Models = KtorModels(client)
            |}
            |
            |fun OpenAIClient(
            |    baseUrl: String,
            |    block: HttpClientConfig<*>.() -> Unit = {},
            |): OpenAI {
            |    val client = HttpClient {
            |        install(ContentNegotiation) { json() }
            |        defaultRequest { url(baseUrl) }
            |        block()
            |    }
            |    return KtorOpenAI(client)
            |}
        """.trimMargin()

        assertEq(expected, actual)
    }

    test("deeper nesting is rendered as inner interfaces in the top-level child file") {
        val root = listOf(
            route(
                "listFineTuningEvents",
                "/fine_tuning/jobs/{fine_tuning_job_id}/events",
                parameters = listOf(pathParam("fine_tuning_job_id"))
            )
        ).sort("OpenAI")

        val files = root.generateClient()
        val actual = files.snapshot()
        val expected = """
            |// FineTuning.kt
            |package io.github.nomisrev.api
            |
            |import io.ktor.client.HttpClient
            |import io.ktor.client.call.body
            |
            |interface FineTuning {
            |    val jobs: Jobs
            |
            |    interface Jobs {
            |        val events: Events
            |
            |        interface Events {
            |            suspend fun listFineTuningEvents(
            |                fineTuningJobId: String,
            |            ): String
            |        }
            |    }
            |}
            |
            |internal class KtorFineTuning(private val client: HttpClient) : FineTuning {
            |    override val jobs: FineTuning.Jobs = KtorFineTuningJobs(client)
            |}
            |
            |internal class KtorFineTuningJobs(private val client: HttpClient) : FineTuning.Jobs {
            |    override val events: FineTuning.Jobs.Events = KtorFineTuningJobsEvents(client)
            |}
            |
            |internal class KtorFineTuningJobsEvents(private val client: HttpClient) : FineTuning.Jobs.Events {
            |    override suspend fun listFineTuningEvents(fineTuningJobId: String): String =
            |        client.get("/fine_tuning/jobs/${'$'}fineTuningJobId/events").body()
            |}
            |
            |// OpenAI.kt
            |package io.github.nomisrev.api
            |
            |import io.ktor.client.HttpClient
            |import io.ktor.client.HttpClientConfig
            |import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
            |import io.ktor.serialization.kotlinx.json.json
            |import io.ktor.client.plugins.defaultRequest
            |
            |interface OpenAI {
            |    val fineTuning: FineTuning
            |}
            |
            |internal class KtorOpenAI(private val client: HttpClient) : OpenAI {
            |    override val fineTuning: FineTuning = KtorFineTuning(client)
            |}
            |
            |fun OpenAIClient(
            |    baseUrl: String,
            |    block: HttpClientConfig<*>.() -> Unit = {},
            |): OpenAI {
            |    val client = HttpClient {
            |        install(ContentNegotiation) { json() }
            |        defaultRequest { url(baseUrl) }
            |        block()
            |    }
            |    return KtorOpenAI(client)
            |}
        """.trimMargin()

        assertEq(expected, actual)
    }

    test("operations at root path are generated on the root interface only") {
        val root = listOf(
            route("health", "/"),
            route("listModels", "/models")
        ).sort("Api")

        val files = root.generateClient()
        val actual = files.snapshot()
        val expected = """
            |// Api.kt
            |package io.github.nomisrev.api
            |
            |import io.ktor.client.HttpClient
            |import io.ktor.client.call.body
            |import io.ktor.client.HttpClientConfig
            |import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
            |import io.ktor.serialization.kotlinx.json.json
            |import io.ktor.client.plugins.defaultRequest
            |
            |interface Api {
            |    val models: Models
            |
            |    suspend fun health(): String
            |}
            |
            |internal class KtorApi(private val client: HttpClient) : Api {
            |    override val models: Models = KtorModels(client)
            |
            |    override suspend fun health(): String =
            |        client.get("/").body()
            |}
            |
            |fun ApiClient(
            |    baseUrl: String,
            |    block: HttpClientConfig<*>.() -> Unit = {},
            |): Api {
            |    val client = HttpClient {
            |        install(ContentNegotiation) { json() }
            |        defaultRequest { url(baseUrl) }
            |        block()
            |    }
            |    return KtorApi(client)
            |}
            |
            |// Models.kt
            |package io.github.nomisrev.api
            |
            |import io.ktor.client.HttpClient
            |import io.ktor.client.call.body
            |
            |interface Models {
            |    suspend fun listModels(): String
            |}
            |
            |internal class KtorModels(private val client: HttpClient) : Models {
            |    override suspend fun listModels(): String =
            |        client.get("/models").body()
            |}
        """.trimMargin()

        assertEq(expected, actual)
    }

    test("interface names are PascalCase and child properties are camelCase") {
        val root = listOf(
            route("createFineTuningJob", "/fine_tuning/jobs", method = HttpMethod.Post)
        ).sort("OpenAI")

        val files = root.generateClient()
        val actual = files.snapshot()
        val expected = """
            |// FineTuning.kt
            |package io.github.nomisrev.api
            |
            |import io.ktor.client.HttpClient
            |import io.ktor.client.call.body
            |
            |interface FineTuning {
            |    val jobs: Jobs
            |
            |    interface Jobs {
            |        suspend fun createFineTuningJob(): String
            |    }
            |}
            |
            |internal class KtorFineTuning(private val client: HttpClient) : FineTuning {
            |    override val jobs: FineTuning.Jobs = KtorFineTuningJobs(client)
            |}
            |
            |internal class KtorFineTuningJobs(private val client: HttpClient) : FineTuning.Jobs {
            |    override suspend fun createFineTuningJob(): String =
            |        client.post("/fine_tuning/jobs").body()
            |}
            |
            |// OpenAI.kt
            |package io.github.nomisrev.api
            |
            |import io.ktor.client.HttpClient
            |import io.ktor.client.HttpClientConfig
            |import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
            |import io.ktor.serialization.kotlinx.json.json
            |import io.ktor.client.plugins.defaultRequest
            |
            |interface OpenAI {
            |    val fineTuning: FineTuning
            |}
            |
            |internal class KtorOpenAI(private val client: HttpClient) : OpenAI {
            |    override val fineTuning: FineTuning = KtorFineTuning(client)
            |}
            |
            |fun OpenAIClient(
            |    baseUrl: String,
            |    block: HttpClientConfig<*>.() -> Unit = {},
            |): OpenAI {
            |    val client = HttpClient {
            |        install(ContentNegotiation) { json() }
            |        defaultRequest { url(baseUrl) }
            |        block()
            |    }
            |    return KtorOpenAI(client)
            |}
        """.trimMargin()

        assertEq(expected, actual)
    }

    test("required JSON body is rendered as typed body parameter with placement in request block") {
        val requestModel = Model.Reference(
            context = NamingContext.reference("CreateChatCompletion", SchemaContext.Write),
            description = null,
            isNullable = false,
            title = null
        )
        val requestBody = Route.Bodies(
            required = true,
            types = mapOf(
                ContentType.Application.Json to Route.Body.SetBody(
                    contentType = ContentType.Application.Json,
                    type = requestModel,
                    description = null,
                    extensions = emptyMap()
                )
            ),
            extensions = emptyMap()
        )
        val root = Root(
            name = "Api",
            operations = listOf(
                route(
                    operationId = "createChatCompletion",
                    path = "/chat/completions/{model}",
                    method = HttpMethod.Post,
                    body = requestBody,
                    parameters = listOf(
                        pathParam("model"),
                        queryParam("limit", isRequired = true),
                        headerParam("OpenAI-Organization", isRequired = true),
                        queryParam("after", isRequired = false),
                        headerParam("X-Trace-Id", isRequired = false),
                    )
                )
            ),
            endpoints = emptyList(),
        )

        val (actual, _) = renderer { root.renderRootFile() }

        assertTrue(actual.contains("body: CreateChatCompletionRequest,"), "Expected typed body parameter:\n$actual")
        assertTrue(actual.contains("openAIOrganization: String,"), "Expected required header param:\n$actual")
        assertTrue(actual.contains("after: String? = null,"), "Expected optional query param:\n$actual")
        assertTrue(actual.contains("xTraceId: String? = null,"), "Expected optional header param:\n$actual")
        assertTrue(actual.contains("contentType(ContentType.Application.Json)"), "Expected JSON contentType in impl:\n$actual")
        assertTrue(actual.contains("setBody(body)"), "Expected body placement in impl:\n$actual")
    }

    test("optional JSON body is nullable and conditionally set") {
        val requestModel = Model.Reference(
            context = NamingContext.reference("UpdateSettings", SchemaContext.Write),
            description = null,
            isNullable = false,
            title = null
        )
        val requestBody = Route.Bodies(
            required = false,
            types = mapOf(
                ContentType.Application.Json to Route.Body.SetBody(
                    contentType = ContentType.Application.Json,
                    type = requestModel,
                    description = null,
                    extensions = emptyMap()
                )
            ),
            extensions = emptyMap()
        )
        val root = Root(
            name = "Api",
            operations = listOf(
                route(
                    operationId = "updateSettings",
                    path = "/settings",
                    method = HttpMethod.Patch,
                    body = requestBody,
                )
            ),
            endpoints = emptyList(),
        )

        val (actual, _) = renderer { root.renderRootFile() }

        assertTrue(actual.contains("body: UpdateSettingsRequest? = null"), "Expected optional body parameter:\n$actual")
        assertTrue(actual.contains("body?.let { setBody(it) }"), "Expected conditional setBody for optional body:\n$actual")
    }

    test("multipart inline schema expands into parameters and uses MultiPartFormDataContent") {
        val multipartBody = Route.Bodies(
            required = true,
            types = mapOf(
                ContentType.MultiPart.FormData to Route.Body.Multipart.Value(
                    parameters = listOf(
                        Route.Body.Multipart.FormData("file", Model.ByteArray(null, false, null)),
                        Route.Body.Multipart.FormData("purpose", Model.Primitive.String(null, null, null, false, null))
                    ),
                    description = null,
                    extensions = emptyMap()
                )
            ),
            extensions = emptyMap()
        )
        val root = Root(
            name = "Api",
            operations = listOf(
                route(
                    operationId = "uploadFile",
                    path = "/files",
                    method = HttpMethod.Post,
                    body = multipartBody,
                )
            ),
            endpoints = emptyList(),
        )

        val (actual, _) = renderer { root.renderRootFile() }

        assertTrue(actual.contains("file: ByteArray,"), "Expected file parameter:\n$actual")
        assertTrue(actual.contains("purpose: String,"), "Expected purpose parameter:\n$actual")
        assertTrue(actual.contains("MultiPartFormDataContent("), "Expected multipart body content:\n$actual")
        assertTrue(actual.contains("append(\"file\", file)"), "Expected binary multipart append:\n$actual")
        assertTrue(actual.contains("append(\"purpose\", purpose.toString())"), "Expected multipart string append:\n$actual")
    }

    test("multipart ref schema uses a single typed body parameter") {
        val requestModel = Model.Reference(
            context = NamingContext.reference("UploadFile", SchemaContext.Write),
            description = null,
            isNullable = false,
            title = null
        )
        val multipartBody = Route.Bodies(
            required = true,
            types = mapOf(
                ContentType.MultiPart.FormData to Route.Body.Multipart.Ref(
                    value = requestModel,
                    description = null,
                    extensions = emptyMap()
                )
            ),
            extensions = emptyMap()
        )
        val root = Root(
            name = "Api",
            operations = listOf(
                route(
                    operationId = "uploadFile",
                    path = "/files",
                    method = HttpMethod.Post,
                    body = multipartBody,
                )
            ),
            endpoints = emptyList(),
        )

        val (actual, _) = renderer { root.renderRootFile() }

        assertTrue(actual.contains("body: UploadFileRequest,"), "Expected single multipart body parameter:\n$actual")
        assertTrue(actual.contains("contentType(ContentType.MultiPart.FormData)"), "Expected multipart contentType:\n$actual")
        assertTrue(actual.contains("setBody(body)"), "Expected multipart setBody:\n$actual")
    }

    test("form-urlencoded schema expands properties and encodes Parameters") {
        val formBody = Route.Bodies(
            required = true,
            types = mapOf(
                ContentType.Application.FormUrlEncoded to Route.Body.FormUrlEncoded(
                    parameters = listOf(
                        Route.Body.Multipart.FormData("grant_type", Model.Primitive.String(null, null, null, false, null)),
                        Route.Body.Multipart.FormData("code", Model.Primitive.String(null, null, null, false, null)),
                        Route.Body.Multipart.FormData("redirect_uri", Model.Primitive.String(null, null, null, false, null)),
                    ),
                    description = null,
                    extensions = emptyMap()
                )
            ),
            extensions = emptyMap()
        )
        val root = Root(
            name = "Api",
            operations = listOf(
                route(
                    operationId = "createToken",
                    path = "/oauth/token",
                    method = HttpMethod.Post,
                    body = formBody,
                )
            ),
            endpoints = emptyList(),
        )

        val (actual, _) = renderer { root.renderRootFile() }

        assertTrue(actual.contains("grantType: String,"), "Expected camelCase form parameter:\n$actual")
        assertTrue(actual.contains("contentType(ContentType.Application.FormUrlEncoded)"), "Expected form contentType:\n$actual")
        assertTrue(actual.contains("Parameters.build {"), "Expected Parameters builder:\n$actual")
        assertTrue(actual.contains("append(\"grant_type\", grantType.toString())"), "Expected grant_type append:\n$actual")
        assertTrue(actual.contains("}.formUrlEncode()"), "Expected formUrlEncode call:\n$actual")
    }

    test("body content type preference follows json over multipart and form-urlencoded") {
        val jsonBody = Route.Body.SetBody(
            contentType = ContentType.Application.Json,
            type = Model.Reference(
                context = NamingContext.reference("CreateThing", SchemaContext.Write),
                description = null,
                isNullable = false,
                title = null
            ),
            description = null,
            extensions = emptyMap()
        )
        val multipartBody = Route.Body.Multipart.Value(
            parameters = listOf(Route.Body.Multipart.FormData("file", Model.ByteArray(null, false, null))),
            description = null,
            extensions = emptyMap()
        )
        val formBody = Route.Body.FormUrlEncoded(
            parameters = listOf(Route.Body.Multipart.FormData("grant_type", Model.Primitive.String(null, null, null, false, null))),
            description = null,
            extensions = emptyMap()
        )
        val requestBody = Route.Bodies(
            required = true,
            types = mapOf(
                ContentType.MultiPart.FormData to multipartBody,
                ContentType.Application.FormUrlEncoded to formBody,
                ContentType.Application.Json to jsonBody,
            ),
            extensions = emptyMap()
        )
        val root = Root(
            name = "Api",
            operations = listOf(
                route(
                    operationId = "createThing",
                    path = "/things",
                    method = HttpMethod.Post,
                    body = requestBody,
                )
            ),
            endpoints = emptyList(),
        )

        val (actual, _) = renderer { root.renderRootFile() }

        assertTrue(actual.contains("body: CreateThingRequest,"), "Expected JSON body param to win preference:\n$actual")
        assertTrue(actual.contains("contentType(ContentType.Application.Json)"), "Expected JSON content type to win preference:\n$actual")
        assertTrue(!actual.contains("file: ByteArray"), "Did not expect multipart parameters when JSON is present:\n$actual")
    }
}

private fun assertEq(expected: String, actual: String) {
    if (expected != actual) throw AssertionError(expected.diff(actual))
}

private fun List<io.github.nomisrev.openapi.KFile>.snapshot(): String =
    sortedBy { it.name }.joinToString("\n\n") { file ->
        "// ${file.name}\n${file.content.trimEnd()}"
    }
