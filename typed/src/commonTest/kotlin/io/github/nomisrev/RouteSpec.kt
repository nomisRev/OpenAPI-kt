package io.github.nomisrev

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.PathSegment
import io.github.nomisrev.openapi.UnionDispatch
import io.github.nomisrev.openapi.parser.Components
import io.github.nomisrev.openapi.parser.Encoding
import io.github.nomisrev.openapi.parser.Info
import io.github.nomisrev.openapi.parser.MediaType
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Operation
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.parser.PathItem
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.RequestBody
import io.github.nomisrev.openapi.parser.Response
import io.github.nomisrev.openapi.parser.Responses
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.routes.toRoutes
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

val routeSpec by testSuite {
    val stringType = Model.Primitive.String(
        default = null,
        description = null,
        constraint = null,
        isNullable = false,
        title = null
    )
    val intType = Model.Primitive.Int(
        default = null,
        description = null,
        constraint = null,
        isNullable = false,
        title = null
    )

    fun pathParameter(name: String, schema: Schema): ReferenceOr<Parameter> = ReferenceOr.value(
        Parameter(
            name = name,
            input = Parameter.Input.Path,
            schema = ReferenceOr.value(schema)
        )
    )

    @Suppress("LongParameterList")
    fun openAPI(
        path: String,
        method: HttpMethod = HttpMethod.Get,
        parameters: List<ReferenceOr<Parameter>> = emptyList(),
        pathParameters: List<ReferenceOr<Parameter>> = emptyList(),
        requestBody: ReferenceOr<RequestBody>? = null,
        components: Components = Components(),
    ): OpenAPI {
        val operation = Operation(
            parameters = parameters,
            requestBody = requestBody,
            responses = Responses(200, Response())
        )
        val pathItem = when (method) {
            HttpMethod.Get -> PathItem(get = operation, parameters = pathParameters)
            HttpMethod.Post -> PathItem(post = operation, parameters = pathParameters)
            HttpMethod.Put -> PathItem(put = operation, parameters = pathParameters)
            else -> error("Unsupported test method: ${method.value}")
        }

        return OpenAPI(
            info = Info("Test", "1.0"),
            paths = mapOf(path to pathItem),
            components = components,
        )
    }

    fun jsonRequestBody(
        schema: ReferenceOr<Schema>,
        required: Boolean = true,
    ): ReferenceOr<RequestBody> = ReferenceOr.value(
        RequestBody(
            required = required,
            content = mapOf(
                ContentType.Application.Json.toString() to MediaType(schema = schema)
            )
        )
    )

    fun formRequestBody(
        schema: ReferenceOr<Schema>,
        required: Boolean = true,
        encoding: Map<String, Encoding> = emptyMap(),
    ): ReferenceOr<RequestBody> = ReferenceOr.value(
        RequestBody(
            required = required,
            content = mapOf(
                ContentType.Application.FormUrlEncoded.toString() to MediaType(
                    schema = schema,
                    encoding = encoding,
                )
            )
        )
    )

    fun realtimeCallRequestBody(): ReferenceOr<RequestBody> = ReferenceOr.value(
        RequestBody(
            required = true,
            content = mapOf(
                ContentType.MultiPart.FormData.toString() to MediaType(
                    schema = ReferenceOr.schema("RealtimeCallCreateRequest"),
                    encoding = mapOf(
                        "sdp" to io.github.nomisrev.openapi.parser.Encoding(contentType = "application/sdp"),
                        "session" to io.github.nomisrev.openapi.parser.Encoding(contentType = "application/json"),
                    ),
                ),
                "application/sdp" to MediaType(
                    schema = ReferenceOr.value(Schema.string),
                ),
            ),
        )
    )

    suspend fun routes(api: OpenAPI) =
        Registry(api).use { registry -> with(registry) { api.toRoutes() } }

    test("NamingContext path uniqueness by method") {
        val segments = listOf(PathSegment.Literal("pets"))
        val get = NamingContext.path(segments, HttpMethod.Get).nest(NamingContext.Response)
        val post = NamingContext.path(segments, HttpMethod.Post).nest(NamingContext.Response)
        assertNotEquals(get, post)
    }

    test("NamingContext path uniqueness by literal path") {
        val pets = NamingContext.path(listOf(PathSegment.Literal("pets")), HttpMethod.Get).nest(NamingContext.Response)
        val users = NamingContext.path(listOf(PathSegment.Literal("users")), HttpMethod.Get).nest(NamingContext.Response)
        assertNotEquals(pets, users)
    }

    test("NamingContext path uniqueness by parameter name") {
        val byPet = NamingContext.path(
            listOf(PathSegment.Literal("resources"), PathSegment.Parameter("petId", stringType)),
            HttpMethod.Get
        ).nest(NamingContext.Response)
        val byUser = NamingContext.path(
            listOf(PathSegment.Literal("resources"), PathSegment.Parameter("userId", stringType)),
            HttpMethod.Get
        ).nest(NamingContext.Response)
        assertNotEquals(byPet, byUser)
    }

    data class RouteSegmentsCase(
        val api: OpenAPI,
        val expected: List<PathSegment>,
    )

    val componentParam = Parameter(
        name = "petId",
        input = Parameter.Input.Path,
        schema = ReferenceOr.value(Schema.integer.copy(format = "int32"))
    )

    listOf(
        RouteSegmentsCase(
            api = openAPI(path = "/pets"),
            expected = listOf(PathSegment.Literal("pets")),
        ),
        RouteSegmentsCase(
            api = openAPI(path = "/pets/{petId}"),
            expected = listOf(
                PathSegment.Literal("pets"),
                PathSegment.Parameter("petId", stringType)
            ),
        ),
        RouteSegmentsCase(
            api = openAPI(
                path = "/pets/{petId}",
                parameters = listOf(pathParameter("petId", Schema.integer.copy(format = "int32"))),
            ),
            expected = listOf(
                PathSegment.Literal("pets"),
                PathSegment.Parameter("petId", intType)
            ),
        ),
        RouteSegmentsCase(
            api = openAPI(
                path = "/pets/{petId}",
                parameters = listOf(ReferenceOr.Reference("#/components/parameters/PetId")),
                components = Components(
                    parameters = mapOf("PetId" to ReferenceOr.value(componentParam))
                ),
            ),
            expected = listOf(
                PathSegment.Literal("pets"),
                PathSegment.Parameter("petId", intType)
            ),
        ),
        RouteSegmentsCase(
            api = openAPI(
                path = "/pets/{petId}",
                pathParameters = listOf(pathParameter("petId", Schema.integer.copy(format = "int32"))),
            ),
            expected = listOf(
                PathSegment.Literal("pets"),
                PathSegment.Parameter("petId", intType)
            ),
        ),
        RouteSegmentsCase(
            api = openAPI(path = "/repos/{owner}/{repo}/collaborators"),
            expected = listOf(
                PathSegment.Literal("repos"),
                PathSegment.Parameter("owner", stringType),
                PathSegment.Parameter("repo", stringType),
                PathSegment.Literal("collaborators")
            ),
        ),
        RouteSegmentsCase(
            api = openAPI(path = "/{owner}/{repo}"),
            expected = listOf(
                PathSegment.Parameter("owner", stringType),
                PathSegment.Parameter("repo", stringType)
            ),
        ),
    ).verifyAll("toRoutes segments") { input ->
        Eq(input.expected, routes(input.api).single().segments)
    }

    test("inline simple path parameter union expands enum cases and keeps one residual path segment") {
        val pathUnionSchema = Schema(
            oneOf = listOf(
                ReferenceOr.value(Schema.integer.copy(format = "int32")),
                ReferenceOr.value(Schema.string.copy(enum = listOf("queued", "in-progress"))),
            )
        )

        val routeEntries = routes(
            openAPI(
                path = "/workflows/{workflowId}",
                parameters = listOf(pathParameter("workflowId", pathUnionSchema)),
            )
        )

        assertEquals(
            listOf("/workflows/queued", "/workflows/in-progress", "/workflows/{workflowId}"),
            routeEntries.map(Route::path),
        )
        val dynamicRoute = routeEntries.single { it.segments.last() is PathSegment.Parameter }
        val segment = assertIs<PathSegment.Parameter>(dynamicRoute.segments.last())
        assertEquals("workflowId", segment.name)
        val dynamicModel = assertIs<Model.Primitive.Int>(segment.model)
        assertEquals(false, dynamicModel.isNullable)
    }

    test("inline path parameter union with multiple enum cases expands only fixed routes") {
        val pathUnionSchema = Schema(
            oneOf = listOf(
                ReferenceOr.value(Schema.string.copy(enum = listOf("queued"))),
                ReferenceOr.value(Schema.string.copy(enum = listOf("in-progress"))),
            )
        )

        val routeEntries = routes(
            openAPI(
                path = "/workflows/{workflowId}",
                parameters = listOf(pathParameter("workflowId", pathUnionSchema)),
            )
        )

        assertEquals(
            listOf("/workflows/queued", "/workflows/in-progress"),
            routeEntries.map(Route::path),
        )
        routeEntries.forEach { route ->
            assertTrue(route.parameters.isEmpty())
            val segment = assertIs<PathSegment.FixedValue>(route.segments.last())
            assertEquals("workflowId", segment.sourceName)
        }
    }

    test("inline closed enum path parameter expands into fixed routes") {
        val routeEntries = routes(
            openAPI(
                path = "/workflows/{workflowId}",
                parameters = listOf(
                    pathParameter("workflowId", Schema.string.copy(enum = listOf("queued", "in-progress"))),
                ),
            )
        )

        assertEquals(
            listOf("/workflows/queued", "/workflows/in-progress"),
            routeEntries.map(Route::path),
        )
        routeEntries.forEach { route ->
            assertTrue(route.parameters.isEmpty())
            val segment = assertIs<PathSegment.FixedValue>(route.segments.last())
            assertEquals("workflowId", segment.sourceName)
        }
    }

    test("referenced closed enum path parameter expands into fixed routes") {
        val routeEntries = routes(
            openAPI(
                path = "/workflows/{workflowId}",
                parameters = listOf(
                    ReferenceOr.value(
                        Parameter(
                            name = "workflowId",
                            input = Parameter.Input.Path,
                            schema = ReferenceOr.schema("WorkflowState"),
                        )
                    )
                ),
                components = Components(
                    schemas = mapOf(
                        "WorkflowState" to ReferenceOr.value(
                            Schema.string.copy(enum = listOf("queued", "in-progress"))
                        )
                    )
                ),
            )
        )

        assertEquals(
            listOf("/workflows/queued", "/workflows/in-progress"),
            routeEntries.map(Route::path),
        )
        routeEntries.forEach { route ->
            assertTrue(route.parameters.isEmpty())
            val segment = assertIs<PathSegment.FixedValue>(route.segments.last())
            assertEquals("workflowId", segment.sourceName)
        }
    }

    test("mixed path parameter union expands enum cases and keeps residual dynamic route") {
        val pathUnionSchema = Schema(
            oneOf = listOf(
                ReferenceOr.value(Schema.integer.copy(format = "int32")),
                ReferenceOr.value(Schema.string.copy(enum = listOf("queued", "in-progress"))),
            )
        )

        val routeEntries = routes(
            openAPI(
                path = "/workflows/{workflowId}/runs",
                parameters = listOf(pathParameter("workflowId", pathUnionSchema)),
            )
        )

        assertEquals(
            listOf(
                "/workflows/queued/runs",
                "/workflows/in-progress/runs",
                "/workflows/{workflowId}/runs",
            ),
            routeEntries.map(Route::path),
        )

        val fixedRoutes = routeEntries.filter { it.segments[1] is PathSegment.FixedValue }
        assertEquals(listOf("queued", "in-progress"), fixedRoutes.map { route ->
            assertIs<PathSegment.FixedValue>(route.segments[1]).wireValue
        })
        fixedRoutes.forEach { route -> assertTrue(route.parameters.isEmpty()) }

        val dynamicRoute = routeEntries.single { it.segments[1] is PathSegment.Parameter }
        val dynamicSegment = assertIs<PathSegment.Parameter>(dynamicRoute.segments[1])
        val dynamicModel = assertIs<Model.Primitive.Int>(dynamicSegment.model)
        assertEquals(listOf("workflowId"), dynamicRoute.parameters.map(Route.Input::name))
        val dynamicInputType = assertIs<Model.Primitive.Int>(dynamicRoute.parameters.single().type)
        assertEquals(dynamicModel, dynamicInputType)
    }

    test("multiple enum path parameters expand as a cartesian product and keep remaining dynamic inputs") {
        val routeEntries = routes(
            openAPI(
                path = "/orgs/{org}/{securityProduct}/{enablement}",
                parameters = listOf(
                    pathParameter("org", Schema.string),
                    pathParameter(
                        "securityProduct",
                        Schema.string.copy(enum = listOf("dependency-graph", "dependency-submission"))
                    ),
                    pathParameter(
                        "enablement",
                        Schema.string.copy(enum = listOf("enable-all", "disable-all"))
                    ),
                ),
            )
        )

        assertEquals(
            listOf(
                "/orgs/{org}/dependency-graph/enable-all",
                "/orgs/{org}/dependency-graph/disable-all",
                "/orgs/{org}/dependency-submission/enable-all",
                "/orgs/{org}/dependency-submission/disable-all",
            ),
            routeEntries.map(Route::path),
        )
        routeEntries.forEach { route ->
            assertEquals(listOf("org"), route.parameters.map(Route.Input::name))
            val orgSegment = assertIs<PathSegment.Parameter>(route.segments[1])
            val securityProductSegment = assertIs<PathSegment.FixedValue>(route.segments[2])
            val enablementSegment = assertIs<PathSegment.FixedValue>(route.segments[3])
            assertEquals("org", orgSegment.name)
            assertEquals("securityProduct", securityProductSegment.sourceName)
            assertEquals("enablement", enablementSegment.sourceName)
        }
    }

    test("inline non-discriminated request body union becomes overloaded body") {
        val bodySchema = Schema(
            oneOf = listOf(
                ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.Object,
                        required = listOf("name"),
                        properties = mapOf(
                            "name" to ReferenceOr.value(Schema.string),
                            "tag" to ReferenceOr.value(Schema.string)
                        )
                    )
                ),
                ReferenceOr.value(Schema.string),
            )
        )

        val route = routes(
            openAPI(
                path = "/pets",
                method = HttpMethod.Post,
                requestBody = jsonRequestBody(ReferenceOr.value(bodySchema)),
            )
        ).single()

        val body = assertIs<Route.Body.OverloadedBody>(route.body?.defaultOrNull())
        assertEquals(ContentType.Application.Json, body.contentType)
        assertEquals(2, body.cases.size)
        assertTrue(body.type.context.head is NamingContext.Path)

        val nestedObject = assertIs<Model.Object>(route.nested.single())
        assertEquals(setOf("name", "tag"), nestedObject.properties.keys)
    }

    test("inline additionalProperties request body preserves object body model") {
        val bodySchema = Schema(
            type = Schema.Type.Basic.Object,
            additionalProperties = io.github.nomisrev.openapi.parser.AdditionalProperties.PSchema(
                ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.Array,
                        items = ReferenceOr.value(
                            Schema(
                                anyOf = listOf(
                                    ReferenceOr.value(Schema.string),
                                    ReferenceOr.value(
                                        Schema(
                                            type = Schema.Type.Basic.Object,
                                            required = listOf("ifAnyMatch"),
                                            additionalProperties = io.github.nomisrev.openapi.parser.AdditionalProperties.Allowed(false),
                                            properties = mapOf(
                                                "ifAnyMatch" to ReferenceOr.value(
                                                    Schema(
                                                        type = Schema.Type.Basic.Array,
                                                        items = ReferenceOr.value(Schema.string),
                                                    )
                                                )
                                            ),
                                        )
                                    ),
                                    ReferenceOr.value(
                                        Schema(
                                            type = Schema.Type.Basic.Object,
                                            required = listOf("ifNoneMatch"),
                                            additionalProperties = io.github.nomisrev.openapi.parser.AdditionalProperties.Allowed(false),
                                            properties = mapOf(
                                                "ifNoneMatch" to ReferenceOr.value(
                                                    Schema(
                                                        type = Schema.Type.Basic.Array,
                                                        items = ReferenceOr.value(Schema.string),
                                                    )
                                                )
                                            ),
                                        )
                                    ),
                                )
                            )
                        ),
                    )
                )
            ),
        )

        val route = routes(
            openAPI(
                path = "/content-exclusions",
                method = HttpMethod.Put,
                requestBody = jsonRequestBody(ReferenceOr.value(bodySchema)),
            )
        ).single()

        val body = assertIs<Route.Body.SetBody>(route.body?.defaultOrNull())
        val bodyModel = assertIs<Model.Object>(body.type)
        assertTrue(bodyModel.properties.isEmpty())
        val additional = assertIs<Model.Object.AdditionalProperties.Schema>(bodyModel.additionalProperties)
        val values = assertIs<Model.Collection>(additional.value)
        val union = assertIs<Model.Union>(values.inner)
        assertEquals(3, union.cases.size)
        assertTrue(union.context.head is NamingContext.Path)
    }

    test("typed object request body with top-level composite stays a set body object") {
        val bodySchema = Schema(
            type = Schema.Type.Basic.Object,
            required = listOf("name"),
            properties = mapOf(
                "name" to ReferenceOr.value(Schema.string),
                "status" to ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.String,
                        enum = listOf("queued", "in_progress", "completed"),
                    )
                ),
                "conclusion" to ReferenceOr.value(Schema.string),
            ),
            oneOf = listOf(
                ReferenceOr.value(
                    Schema(
                        properties = mapOf(
                            "status" to ReferenceOr.value(
                                Schema(
                                    enum = listOf("completed"),
                                )
                            )
                        ),
                        required = listOf("status", "conclusion"),
                    )
                ),
                ReferenceOr.value(
                    Schema(
                        properties = mapOf(
                            "status" to ReferenceOr.value(
                                Schema(
                                    enum = listOf("queued", "in_progress"),
                                )
                            )
                        ),
                    )
                ),
            ),
        )

        val route = routes(
            openAPI(
                path = "/check-runs",
                method = HttpMethod.Post,
                requestBody = jsonRequestBody(ReferenceOr.value(bodySchema)),
            )
        ).single()

        val body = assertIs<Route.Body.SetBody>(route.body?.defaultOrNull())
        val bodyModel = assertIs<Model.Object>(body.type)
        assertEquals(setOf("name", "status", "conclusion"), bodyModel.properties.keys)
    }

    test("typed object request body without top-level properties stays opaque json") {
        val bodySchema = Schema(
            type = Schema.Type.Basic.Object,
            oneOf = listOf(
                ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.Object,
                        properties = mapOf(
                            "subject_digests" to ReferenceOr.value(
                                Schema(
                                    type = Schema.Type.Basic.Array,
                                    items = ReferenceOr.value(Schema.string),
                                )
                            )
                        ),
                        required = listOf("subject_digests"),
                    )
                ),
                ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.Object,
                        properties = mapOf(
                            "attestation_ids" to ReferenceOr.value(
                                Schema(
                                    type = Schema.Type.Basic.Array,
                                    items = ReferenceOr.value(Schema.integer),
                                )
                            )
                        ),
                        required = listOf("attestation_ids"),
                    )
                ),
            ),
        )

        val route = routes(
            openAPI(
                path = "/attestations/delete-request",
                method = HttpMethod.Post,
                requestBody = jsonRequestBody(ReferenceOr.value(bodySchema)),
            )
        ).single()

        val body = assertIs<Route.Body.SetBody>(route.body?.defaultOrNull())
        val bodyModel = assertIs<Model.FreeFormJson>(body.type)
        assertTrue(!bodyModel.isNullable)
    }

    test("referenced request body union stays set body") {
        val unionSchema = Schema(
            oneOf = listOf(
                ReferenceOr.value(Schema.string),
                ReferenceOr.value(Schema.integer)
            )
        )

        val route = routes(
            openAPI(
                path = "/pets",
                method = HttpMethod.Post,
                requestBody = jsonRequestBody(ReferenceOr.schema("PetBody")),
                components = Components(
                    schemas = mapOf("PetBody" to ReferenceOr.value(unionSchema))
                ),
            )
        ).single()

        val body = assertIs<Route.Body.SetBody>(route.body?.defaultOrNull())
        val union = assertIs<Model.Union>(body.type)
        assertTrue(union.context.head is NamingContext.Reference)
    }

    test("explicitly discriminated request body union stays set body when it uses tagged custom dispatch") {
        val bodySchema = Schema(
            oneOf = listOf(
                ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.Object,
                        required = listOf("type"),
                        properties = mapOf("type" to ReferenceOr.value(Schema.string))
                    )
                ),
                ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.Object,
                        required = listOf("type"),
                        properties = mapOf("type" to ReferenceOr.value(Schema.string))
                    )
                ),
            ),
            discriminator = Schema.Discriminator(propertyName = "type")
        )

        val route = routes(
            openAPI(
                path = "/pets",
                method = HttpMethod.Post,
                requestBody = jsonRequestBody(ReferenceOr.value(bodySchema)),
            )
        ).single()

        val body = assertIs<Route.Body.SetBody>(route.body?.defaultOrNull())
        val union = assertIs<Model.Union>(body.type)
        assertEquals(UnionDispatch.TaggedCustom("type"), union.dispatch)
        assertTrue(union.context.head is NamingContext.Path)
    }

    test("multipart request body with encodings flattens referenced schema and preserves part content types") {
        val route = routes(
            openAPI(
                path = "/realtime/calls",
                method = HttpMethod.Post,
                requestBody = realtimeCallRequestBody(),
                components = Components(
                    schemas = mapOf(
                        "RealtimeCallCreateRequest" to ReferenceOr.value(
                            Schema(
                                type = Schema.Type.Basic.Object,
                                required = listOf("sdp", "session"),
                                properties = mapOf(
                                    "sdp" to ReferenceOr.value(Schema.string),
                                    "session" to ReferenceOr.value(
                                        Schema(
                                            type = Schema.Type.Basic.Object,
                                            required = listOf("type", "model"),
                                            properties = mapOf(
                                                "type" to ReferenceOr.value(Schema.string),
                                                "model" to ReferenceOr.value(Schema.string),
                                            ),
                                        )
                                    ),
                                ),
                            )
                        )
                    )
                ),
            )
        ).single()

        val bodies = requireNotNull(route.body)
        assertEquals(2, bodies.types.size)

        val multipart = assertIs<Route.Body.Multipart.Value>(bodies.types.getValue(ContentType.MultiPart.FormData))
        assertEquals(2, multipart.parameters.size)
        val sdp = assertIs<Route.Body.Multipart.FormData>(multipart.parameters.first { it.name == "sdp" })
        val session = assertIs<Route.Body.Multipart.FormData>(multipart.parameters.first { it.name == "session" })
        assertEquals(ContentType.parse("application/sdp"), sdp.contentType)
        assertEquals(ContentType.Application.Json, session.contentType)

        val sdpOnly = assertIs<Route.Body.SetBody>(bodies.types.getValue(ContentType.parse("application/sdp")))
        assertTrue(sdpOnly.type is Model.Primitive.String)
    }

    test("scalar-only form-urlencoded body stays supported") {
        val route = routes(
            openAPI(
                path = "/oauth/token",
                method = HttpMethod.Post,
                requestBody = formRequestBody(
                    ReferenceOr.value(
                        Schema(
                            type = Schema.Type.Basic.Object,
                            required = listOf("grant_type", "code"),
                            properties = mapOf(
                                "grant_type" to ReferenceOr.value(Schema.string),
                                "code" to ReferenceOr.value(Schema.string),
                            ),
                        )
                    )
                ),
            )
        ).single()

        val form = assertIs<Route.Body.FormUrlEncoded>(
            requireNotNull(route.body).types.getValue(ContentType.Application.FormUrlEncoded)
        )
        assertTrue(form.isSupported)
        assertTrue(form.unsupportedFields.isEmpty())
    }

    test("form-urlencoded body with non-scalar fields and no encoding becomes unsupported") {
        val route = routes(
            openAPI(
                path = "/search",
                method = HttpMethod.Post,
                requestBody = formRequestBody(
                    ReferenceOr.value(
                        Schema(
                            type = Schema.Type.Basic.Object,
                            properties = mapOf(
                                "filters" to ReferenceOr.value(
                                    Schema(
                                        type = Schema.Type.Basic.Object,
                                        properties = mapOf(
                                            "status" to ReferenceOr.value(Schema.string),
                                        ),
                                    )
                                ),
                                "tags" to ReferenceOr.value(
                                    Schema(
                                        type = Schema.Type.Basic.Array,
                                        items = ReferenceOr.value(Schema.string),
                                    )
                                ),
                            ),
                        )
                    )
                ),
            )
        ).single()

        val form = assertIs<Route.Body.FormUrlEncoded>(
            requireNotNull(route.body).types.getValue(ContentType.Application.FormUrlEncoded)
        )
        assertEquals(listOf("filters", "tags"), form.unsupportedFields.sorted())
        assertTrue(!form.isSupported)
    }

    test("explicit form field encoding keeps non-scalar field supported") {
        val route = routes(
            openAPI(
                path = "/search",
                method = HttpMethod.Post,
                requestBody = formRequestBody(
                    schema = ReferenceOr.value(
                        Schema(
                            type = Schema.Type.Basic.Object,
                            properties = mapOf(
                                "filters" to ReferenceOr.value(
                                    Schema(
                                        type = Schema.Type.Basic.Object,
                                        properties = mapOf(
                                            "status" to ReferenceOr.value(Schema.string),
                                        ),
                                    )
                                ),
                            ),
                        )
                    ),
                    encoding = mapOf(
                        "filters" to Encoding(style = "deepObject", explode = true),
                    ),
                ),
            )
        ).single()

        val form = assertIs<Route.Body.FormUrlEncoded>(
            requireNotNull(route.body).types.getValue(ContentType.Application.FormUrlEncoded)
        )
        assertTrue(form.isSupported)
        assertTrue(form.unsupportedFields.isEmpty())
    }
}
