package io.github.nomisrev

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.PathSegment
import io.github.nomisrev.openapi.parser.Components
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

    test("inline simple path parameter union becomes overloaded segment") {
        val pathUnionSchema = Schema(
            oneOf = listOf(
                ReferenceOr.value(Schema.integer.copy(format = "int32")),
                ReferenceOr.value(Schema.string.copy(enum = listOf("queued", "in-progress"))),
            )
        )

        val route = routes(
            openAPI(
                path = "/workflows/{workflowId}",
                parameters = listOf(pathParameter("workflowId", pathUnionSchema)),
            )
        ).single()

        val segment = assertIs<PathSegment.OverloadedParameter>(route.segments.last())
        assertEquals("workflowId", segment.name)
        assertEquals(2, segment.cases.size)
    }

    test("inline path parameter union with multiple enum cases becomes overloaded segment") {
        val pathUnionSchema = Schema(
            oneOf = listOf(
                ReferenceOr.value(Schema.string.copy(enum = listOf("queued"))),
                ReferenceOr.value(Schema.string.copy(enum = listOf("in-progress"))),
            )
        )

        val route = routes(
            openAPI(
                path = "/workflows/{workflowId}",
                parameters = listOf(pathParameter("workflowId", pathUnionSchema)),
            )
        ).single()

        val segment = assertIs<PathSegment.OverloadedParameter>(route.segments.last())
        assertEquals("workflowId", segment.name)
        assertEquals(
            listOf(listOf("queued"), listOf("in-progress")),
            segment.cases.map { case -> assertIs<Model.Enum>(case.model).values },
        )
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

    test("discriminated request body union stays set body") {
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
        assertEquals("type", union.discriminator)
        assertTrue(union.context.head is NamingContext.Path)
    }
}
