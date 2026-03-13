package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.Root
import io.github.nomisrev.openapi.generateClient
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.parser.Server
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.sort
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode

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

private fun queryParam(
    name: String,
    type: Model = Model.Primitive.String(null, null, null, false, null),
    isRequired: Boolean = false
) =
    Route.Input(name = name, type = type, isRequired = isRequired, input = Parameter.Input.Query, description = null)

private fun headerParam(
    name: String,
    type: Model = Model.Primitive.String(null, null, null, false, null),
    isRequired: Boolean = false
) =
    Route.Input(name = name, type = type, isRequired = isRequired, input = Parameter.Input.Header, description = null)

private fun cookieParam(
    name: String,
    type: Model = Model.Primitive.String(null, null, null, false, null),
    isRequired: Boolean = false
) =
    Route.Input(name = name, type = type, isRequired = isRequired, input = Parameter.Input.Cookie, description = null)

private fun server(
    url: String,
    description: String? = null,
    variables: Map<String, Server.Variable>? = null,
): Server = Server(
    url = url,
    description = description,
    variables = variables,
    extensions = emptyMap(),
)

private fun serverVariable(
    default: String,
    enum: List<String>? = null,
): Server.Variable = Server.Variable(
    enum = enum,
    default = default,
    description = null,
    extensions = emptyMap(),
)

private fun goldenPackage(resourceDirectory: String): String =
    "io.github.nomisrev.render.golden.${resourceDirectory.replace('/', '.').replace('-', '_')}"

val clientRenderSpec by testSuite {

    verifyKotlinFiles(
        name = "single parameterless GET endpoint - root interface",
        resourceDirectory = "client/root/single-parameterless-get"
    ) {
        Root(
            name = "PetStore",
            operations = listOf(
                route("listPets", "/pets")
            ),
            endpoints = emptyList(),
        ).generateClient(goldenPackage("client/root/single-parameterless-get"))
    }

    verifyKotlinFiles(
        name = "single GET endpoint returning a reference type",
        resourceDirectory = "client/root/single-reference-response"
    ) {
        val returnModel = Model.Reference(
            context = NamingContext.reference("ListPets", SchemaContext.Read),
            description = null,
            isNullable = false,
            title = null
        )
        Root(
            name = "PetStore",
            operations = listOf(
                route("listPets", "/pets", returnModel = returnModel)
            ),
            endpoints = emptyList(),
        ).generateClient(goldenPackage("client/root/single-reference-response"))
    }

    verifyKotlinFiles(
        name = "single GET returning Unit for empty response",
        resourceDirectory = "client/root/unit-response"
    ) {
        Root(
            name = "PetStore",
            operations = listOf(
                route(
                    "healthCheck",
                    "/health",
                    returnModel = Model.Primitive.Unit(null, false, null)
                )
            ),
            endpoints = emptyList(),
        ).generateClient(goldenPackage("client/root/unit-response"))
    }

    verifyKotlinFiles(
        name = "empty root generates interface with no members",
        resourceDirectory = "client/root/empty-root"
    ) {
        Root(
            name = "EmptyApi",
            operations = emptyList(),
            endpoints = emptyList(),
        ).generateClient(goldenPackage("client/root/empty-root"))
    }

    verifyKotlinFiles(
        name = "server sealed interface and factory parameter are generated for static servers",
        resourceDirectory = "client/server/static"
    ) {
        Root(
            name = "OpenAI",
            operations = listOf(route("listModels", "/models")),
            endpoints = emptyList(),
            servers = listOf(
                server("https://api.openai.com/v1", description = "Production"),
                server("https://staging.api.openai.com/v1", description = "Staging Server"),
            ),
        ).generateClient(goldenPackage("client/server/static"))
    }

    verifyKotlinFiles(
        name = "server variables render enum and string parameters with interpolated url",
        resourceDirectory = "client/server/variables"
    ) {
        Root(
            name = "Example",
            operations = listOf(route("listData", "/data")),
            endpoints = emptyList(),
            servers = listOf(
                server(
                    url = "https://{environment}.api.example.com/{version}",
                    description = "Multi-environment server",
                    variables = mapOf(
                        "environment" to serverVariable(
                            default = "production",
                            enum = listOf("production", "staging", "dev"),
                        ),
                        "version" to serverVariable(default = "v2"),
                    ),
                ),
            ),
        ).generateClient(goldenPackage("client/server/variables"))
    }

    verifyKotlinFiles(
        name = "server case naming falls back to Default for a single unnamed server",
        resourceDirectory = "client/server/fallback-single"
    ) {
        Root(
            name = "Example",
            operations = listOf(route("listData", "/data")),
            endpoints = emptyList(),
            servers = listOf(server("https://api.example.com/v1")),
        ).generateClient(goldenPackage("client/server/fallback-single"))
    }

    verifyKotlinFiles(
        name = "server case naming falls back to indexed names for multiple unnamed servers",
        resourceDirectory = "client/server/fallback-multi"
    ) {
        Root(
            name = "Example",
            operations = listOf(route("listData", "/data")),
            endpoints = emptyList(),
            servers = listOf(
                server("https://api-1.example.com/v1"),
                server("https://api-2.example.com/v1"),
            ),
        ).generateClient(goldenPackage("client/server/fallback-multi"))
    }

    verifyKotlinFiles(
        name = "read variant type is used for response types",
        resourceDirectory = "client/response/read-variant"
    ) {
        val returnModel1 = Model.Reference(
            context = NamingContext.reference("Pet", SchemaContext.Read),
            description = null,
            isNullable = false,
            title = null
        )
        Root(
            name = "PetStore",
            operations = listOf(
                route("getPet", "/pet", returnModel = returnModel1)
            ),
            endpoints = emptyList(),
        ).generateClient(goldenPackage("client/response/read-variant"))
    }

    verifyKotlinFiles(name = "path parameter interpolation",
        resourceDirectory = "client/params/path-interpolation"
    ) {
        Root(
            name = "PetStore",
            operations = listOf(
                route(
                    "retrieveModel",
                    "/models/{model}",
                    parameters = listOf(pathParam("model"))
                )
            ),
            endpoints = emptyList(),
        ).generateClient(goldenPackage("client/params/path-interpolation"))
    }

    verifyKotlinFiles(name = "required query parameter",
        resourceDirectory = "client/params/required-query"
    ) {
        Root(
            name = "Api",
            operations = listOf(
                route(
                    "search",
                    "/search",
                    parameters = listOf(queryParam("query", isRequired = true))
                )
            ),
            endpoints = emptyList(),
        ).generateClient(goldenPackage("client/params/required-query"))
    }

    verifyKotlinFiles(name = "optional query parameter",
        resourceDirectory = "client/params/optional-query"
    ) {
        Root(
            name = "Api",
            operations = listOf(
                route(
                    "listItems",
                    "/items",
                    parameters = listOf(queryParam("limit", type = Model.Primitive.Int(null, null, null, false, null)))
                )
            ),
            endpoints = emptyList(),
        ).generateClient(goldenPackage("client/params/optional-query"))
    }

    verifyKotlinFiles(name = "header parameter",
        resourceDirectory = "client/params/header"
    ) {
        Root(
            name = "Api",
            operations = listOf(
                route(
                    "getData",
                    "/data",
                    parameters = listOf(headerParam("X-Api-Key", isRequired = true))
                )
            ),
            endpoints = emptyList(),
        ).generateClient(goldenPackage("client/params/header"))
    }

    verifyKotlinFiles(name = "cookie parameter",
        resourceDirectory = "client/params/cookie"
    ) {
        Root(
            name = "Api",
            operations = listOf(
                route(
                    "getData",
                    "/data",
                    parameters = listOf(cookieParam("session_id", isRequired = true))
                )
            ),
            endpoints = emptyList(),
        ).generateClient(goldenPackage("client/params/cookie"))
    }

    verifyKotlinFiles(
        name = "parameter ordering - path then required query then required header then optional query then optional header",
        resourceDirectory = "client/params/ordering"
    ) {
        Root(
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
        ).generateClient(goldenPackage("client/params/ordering"))
    }

    verifyKotlinFiles(
        name = "query parameter with non-null default value",
        resourceDirectory = "client/params/query-default-non-null"
    ) {
        Root(
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
        ).generateClient(goldenPackage("client/params/query-default-non-null"))
    }

    verifyKotlinFiles(
        name = "required parameter with default renders default value without annotation",
        resourceDirectory = "client/params/required-with-default"
    ) {
        Root(
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
        ).generateClient(goldenPackage("client/params/required-with-default"))
    }

    verifyKotlinFiles(
        name = "@Deprecated annotation for deprecated operation",
        resourceDirectory = "client/operation/deprecated"
    ) {
        Root(
            name = "Api",
            operations = listOf(
                route(
                    "legacyEndpoint",
                    "/legacy",
                    deprecated = true
                )
            ),
            endpoints = emptyList(),
        ).generateClient(goldenPackage("client/operation/deprecated"))
    }

    verifyKotlinFiles(
        name = "camelCase conversion for parameter names",
        resourceDirectory = "client/params/camel-case"
    ) {
        Root(
            name = "Api",
            operations = listOf(
                route(
                    "listEvents",
                    "/fine_tuning/jobs/{fine_tuning_job_id}/events",
                    parameters = listOf(pathParam("fine_tuning_job_id"))
                )
            ),
            endpoints = emptyList(),
        ).generateClient(goldenPackage("client/params/camel-case"))
    }

    verifyKotlinFiles(
        name = "generateClient splits direct root children into separate files",
        resourceDirectory = "client/splits-direct-root-children"
    ) {
        listOf(
            route("createChatCompletion", "/chat/completions", method = HttpMethod.Post),
            route("listModels", "/models"),
            route("retrieveModel", "/models/{model}", parameters = listOf(pathParam("model")))
        ).sort("OpenAI").generateClient(goldenPackage("client/splits-direct-root-children"))
    }

    verifyKotlinFiles(
        name = "deeper nesting is rendered as inner interfaces in the top-level child file",
        resourceDirectory = "client/deeper-nesting"
    ) {
        listOf(
            route(
                "listFineTuningEvents",
                "/fine_tuning/jobs/{fine_tuning_job_id}/events",
                parameters = listOf(pathParam("fine_tuning_job_id"))
            )
        ).sort("OpenAI").generateClient(goldenPackage("client/deeper-nesting"))
    }

    verifyKotlinFiles(
        name = "operations at root path are generated on the root interface only",
        resourceDirectory = "client/root-operations"
    ) {
        listOf(
            route("health", "/"),
            route("listModels", "/models")
        ).sort("Api").generateClient(goldenPackage("client/root-operations"))
    }

    verifyKotlinFiles(
        name = "interface names are PascalCase and child properties are camelCase",
        resourceDirectory = "client/pascal-and-camel-case"
    ) {
        listOf(
            route("createFineTuningJob", "/fine_tuning/jobs", method = HttpMethod.Post)
        ).sort("OpenAI").generateClient(goldenPackage("client/pascal-and-camel-case"))
    }

    verifyKotlinFiles(
        name = "required JSON body is rendered as typed body parameter with placement in request block",
        resourceDirectory = "client/body/required-json"
    ) {
        val requestModel1 = Model.Reference(
            context = NamingContext.reference("CreateChatCompletion", SchemaContext.Write),
            description = null,
            isNullable = false,
            title = null
        )
        val requestBody1 = Route.Bodies(
            required = true,
            types = mapOf(
                ContentType.Application.Json to Route.Body.SetBody(
                    contentType = ContentType.Application.Json,
                    type = requestModel1,
                    description = null,
                    extensions = emptyMap()
                )
            ),
            extensions = emptyMap()
        )
        Root(
            name = "Api",
            operations = listOf(
                route(
                    operationId = "createChatCompletion",
                    path = "/chat/completions/{model}",
                    method = HttpMethod.Post,
                    body = requestBody1,
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
        ).generateClient(goldenPackage("client/body/required-json"))
    }

    verifyKotlinFiles(
        name = "optional JSON body is nullable and conditionally set",
        resourceDirectory = "client/body/optional-json"
    ) {
        val requestModel1 = Model.Reference(
            context = NamingContext.reference("UpdateSettings", SchemaContext.Write),
            description = null,
            isNullable = false,
            title = null
        )
        val requestBody1 = Route.Bodies(
            required = false,
            types = mapOf(
                ContentType.Application.Json to Route.Body.SetBody(
                    contentType = ContentType.Application.Json,
                    type = requestModel1,
                    description = null,
                    extensions = emptyMap()
                )
            ),
            extensions = emptyMap()
        )
        Root(
            name = "Api",
            operations = listOf(
                route(
                    operationId = "updateSettings",
                    path = "/settings",
                    method = HttpMethod.Patch,
                    body = requestBody1,
                )
            ),
            endpoints = emptyList(),
        ).generateClient(goldenPackage("client/body/optional-json"))
    }

    verifyKotlinFiles(
        name = "multipart inline schema expands into parameters and uses MultiPartFormDataContent",
        resourceDirectory = "client/body/multipart-inline"
    ) {
        val multipartBody1 = Route.Bodies(
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
        Root(
            name = "Api",
            operations = listOf(
                route(
                    operationId = "uploadFile",
                    path = "/files",
                    method = HttpMethod.Post,
                    body = multipartBody1,
                )
            ),
            endpoints = emptyList(),
        ).generateClient(goldenPackage("client/body/multipart-inline"))
    }

    verifyKotlinFiles(
        name = "multipart ref schema uses a single typed body parameter",
        resourceDirectory = "client/body/multipart-ref"
    ) {
        val requestModel1 = Model.Reference(
            context = NamingContext.reference("UploadFile", SchemaContext.Write),
            description = null,
            isNullable = false,
            title = null
        )
        val multipartBody1 = Route.Bodies(
            required = true,
            types = mapOf(
                ContentType.MultiPart.FormData to Route.Body.Multipart.Ref(
                    value = requestModel1,
                    description = null,
                    extensions = emptyMap()
                )
            ),
            extensions = emptyMap()
        )
        Root(
            name = "Api",
            operations = listOf(
                route(
                    operationId = "uploadFile",
                    path = "/files",
                    method = HttpMethod.Post,
                    body = multipartBody1,
                )
            ),
            endpoints = emptyList(),
        ).generateClient(goldenPackage("client/body/multipart-ref"))
    }

    verifyKotlinFiles(
        name = "form-urlencoded schema expands properties and encodes Parameters",
        resourceDirectory = "client/body/form-urlencoded"
    ) {
        val formBody1 = Route.Bodies(
            required = true,
            types = mapOf(
                ContentType.Application.FormUrlEncoded to Route.Body.FormUrlEncoded(
                    parameters = listOf(
                        Route.Body.Multipart.FormData(
                            "grant_type",
                            Model.Primitive.String(null, null, null, false, null)
                        ),
                        Route.Body.Multipart.FormData("code", Model.Primitive.String(null, null, null, false, null)),
                        Route.Body.Multipart.FormData(
                            "redirect_uri",
                            Model.Primitive.String(null, null, null, false, null)
                        ),
                    ),
                    description = null,
                    extensions = emptyMap()
                )
            ),
            extensions = emptyMap()
        )
        Root(
            name = "Api",
            operations = listOf(
                route(
                    operationId = "createToken",
                    path = "/oauth/token",
                    method = HttpMethod.Post,
                    body = formBody1,
                )
            ),
            endpoints = emptyList(),
        ).generateClient(goldenPackage("client/body/form-urlencoded"))
    }

    verifyKotlinFiles(
        name = "body content type preference follows json over multipart and form-urlencoded",
        resourceDirectory = "client/body/content-type-preference"
    ) {
        val jsonBody1 = Route.Body.SetBody(
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
        val multipartBody1 = Route.Body.Multipart.Value(
            parameters = listOf(Route.Body.Multipart.FormData("file", Model.ByteArray(null, false, null))),
            description = null,
            extensions = emptyMap()
        )
        val formBody1 = Route.Body.FormUrlEncoded(
            parameters = listOf(
                Route.Body.Multipart.FormData(
                    "grant_type",
                    Model.Primitive.String(null, null, null, false, null)
                )
            ),
            description = null,
            extensions = emptyMap()
        )
        val requestBody1 = Route.Bodies(
            required = true,
            types = mapOf(
                ContentType.MultiPart.FormData to multipartBody1,
                ContentType.Application.FormUrlEncoded to formBody1,
                ContentType.Application.Json to jsonBody1,
            ),
            extensions = emptyMap()
        )
        Root(
            name = "Api",
            operations = listOf(
                route(
                    operationId = "createThing",
                    path = "/things",
                    method = HttpMethod.Post,
                    body = requestBody1,
                )
            ),
            endpoints = emptyList(),
        ).generateClient(goldenPackage("client/body/content-type-preference"))
    }

    verifyKotlinFiles(
        name = "multiple responses generate a sealed result and status dispatch in implementation",
        resourceDirectory = "client/responses/multiple"
    ) {
        val returns1 = Route.Returns(
            default = null,
            responses = mapOf(
                HttpStatusCode.OK to Route.ReturnType(
                    types = mapOf(
                        ContentType.Application.Json to Model.Primitive.String(
                            null,
                            null,
                            null,
                            false,
                            null
                        )
                    ),
                    extensions = emptyMap()
                ),
                HttpStatusCode.NotFound to Route.ReturnType(
                    types = mapOf(
                        ContentType.Application.Json to Model.Primitive.String(
                            null,
                            null,
                            null,
                            false,
                            null
                        )
                    ),
                    extensions = emptyMap()
                ),
            ),
            extensions = emptyMap()
        )
        Root(
            name = "Api",
            operations = listOf(
                route(
                    operationId = "retrieveModel",
                    path = "/models/{model}",
                    parameters = listOf(pathParam("model"))
                ).copy(
                    returns = returns1
                )
            ),
            endpoints = emptyList(),
        ).generateClient(goldenPackage("client/responses/multiple"))
    }

    verifyKotlinFiles(
        name = "default response is rendered and used in else branch",
        resourceDirectory = "client/responses/default"
    ) {
        val returns1 = Route.Returns(
            default = Route.ReturnType(
                types = mapOf(ContentType.Application.Json to Model.Primitive.String(null, null, null, false, null)),
                extensions = emptyMap()
            ),
            responses = mapOf(
                HttpStatusCode.OK to Route.ReturnType(
                    types = mapOf(
                        ContentType.Application.Json to Model.Primitive.String(
                            null,
                            null,
                            null,
                            false,
                            null
                        )
                    ),
                    extensions = emptyMap()
                ),
            ),
            extensions = emptyMap()
        )
        Root(
            name = "Api",
            operations = listOf(
                route(operationId = "getModel", path = "/models/{model}", parameters = listOf(pathParam("model"))).copy(
                    returns = returns1
                )
            ),
            endpoints = emptyList(),
        ).generateClient(goldenPackage("client/responses/default"))
    }

    verifyKotlinFiles(
        name = "no-content response renders data object case",
        resourceDirectory = "client/responses/no-content"
    ) {
        val returns1 = Route.Returns(
            default = null,
            responses = mapOf(
                HttpStatusCode.NoContent to Route.ReturnType(
                    types = emptyMap(),
                    extensions = emptyMap()
                ),
                HttpStatusCode.OK to Route.ReturnType(
                    types = mapOf(
                        ContentType.Application.Json to Model.Primitive.String(
                            null,
                            null,
                            null,
                            false,
                            null
                        )
                    ),
                    extensions = emptyMap()
                ),
            ),
            extensions = emptyMap()
        )
        Root(
            name = "Api",
            operations = listOf(
                route(
                    operationId = "deleteModel",
                    path = "/models/{model}",
                    method = HttpMethod.Delete,
                    parameters = listOf(pathParam("model"))
                ).copy(
                    returns = returns1
                )
            ),
            endpoints = emptyList(),
        ).generateClient(goldenPackage("client/responses/no-content"))
    }
}
