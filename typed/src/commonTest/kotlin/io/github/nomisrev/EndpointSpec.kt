package io.github.nomisrev

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.NamingContext.RouteParam
import io.github.nomisrev.openapi.PathSegment
import io.github.nomisrev.openapi.parser.Components
import io.github.nomisrev.openapi.parser.Info
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Operation
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Response
import io.github.nomisrev.openapi.parser.Responses
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.routes.Endpoint
import io.github.nomisrev.openapi.routes.toRoute
import io.ktor.http.HttpMethod

val endpointSpec by testSuite {
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

    fun endpoint(
        path: String,
        operationId: String? = null,
        parameters: List<ReferenceOr<Parameter>> = emptyList(),
    ) = Endpoint(
        path = path,
        method = HttpMethod.Get,
        operation = Operation(
            operationId = operationId,
            parameters = parameters,
            responses = Responses(200, Response())
        )
    )

    fun openAPI(components: Components = Components()): OpenAPI =
        OpenAPI(info = Info("Test", "1.0"), components = components)

    suspend fun route(endpoint: Endpoint, api: OpenAPI = openAPI()) =
        Registry(api).use { registry -> with(registry) { endpoint.toRoute() } }

    fun pathParameter(
        name: String,
        schema: Schema,
    ): ReferenceOr<Parameter> = ReferenceOr.value(
        Parameter(
            name = name,
            input = Parameter.Input.Path,
            schema = ReferenceOr.value(schema)
        )
    )

    // Formatting the operationId to a proper class name is for generation

    listOf(
        endpoint("/test") to "get",
        endpoint("/test/{user_id}") to "getByUser_id",
        endpoint("/test/{userId}") to "getByUserId",
        endpoint("/test/{user_id}", "customOperationId") to "customOperationId",
    ).verifyAll("Endpoint.getOrCreateOperationId()") { (endpoint, expected) ->
        Eq(expected, endpoint.operationId)
    }

    listOf(
        endpoint("/") to emptyList(),
        endpoint("/test") to listOf("test"),
        endpoint("/test/{user_id}") to listOf("test"),
        endpoint("/test/{userId}/test2") to listOf("test", "test2"),
        endpoint("/test/{userId}/test2/{param2}") to listOf("test", "test2"),
        endpoint("/test/v1/{userId}/test2/{param2}") to listOf("test", "v1", "test2"),
    ).verifyAll("Endpoint.pathSegments()") { (endpoint, expected) ->
        Eq(expected, endpoint.pathSegments)
    }

    listOf(
        endpoint("/") to emptyList(),
        endpoint("/test") to emptyList(),
        endpoint("/test/{user_id}") to listOf("user_id"),
        endpoint("/test/{userId}/test2") to listOf("userId"),
        endpoint("/test/{userId}/test2/{param2}") to listOf("userId", "param2"),
        endpoint("/test/v1/{userId}/test2/{param2}") to listOf("userId", "param2"),
        // We allow duplicate path parameters but they'll get filtered out for now
        endpoint("/test/{userId}/test2/{userId}/test3") to listOf("userId", "userId"),
    ).verifyAll("Endpoint.pathParams()") { (endpoint, expected) ->
        Eq(expected, endpoint.pathParameters)
    }

    listOf(
        endpoint("/") to emptyList(),
        endpoint("/pets") to listOf(PathSegment.Literal("pets")),
        endpoint("/pets/{petId}") to listOf(
            PathSegment.Literal("pets"),
            PathSegment.Parameter("petId", stringType)
        ),
    ).verifyAll("Endpoint.segments()") { (endpoint, expected) ->
        Eq(expected, endpoint.segments)
    }

    listOf(
        endpoint("/") to NamingContext.path(emptyList()).nest(RouteParam(name = "param", operationId = "get")),
        endpoint("/test") to NamingContext.path("test").nest(RouteParam(name = "param", operationId = "get")),
        endpoint("/test/{user_id}") to NamingContext.path("test").nest(RouteParam(name = "param", operationId = "getByUser_id")),
        endpoint("/test/{userId}/test2") to NamingContext.path(listOf("test", "test2"))
            .nest(RouteParam(name = "param", operationId = "get")),
        endpoint("/test/{userId}/test2/{param2}") to NamingContext.path(listOf("test", "test2"))
            .nest(RouteParam(name = "param", operationId = "getByParam2")),
    ).verifyAll("Endpoint.context(..)") { (endpoint, expected) ->
        Eq(expected, endpoint.context(RouteParam("param", endpoint.operationId)))
    }

    data class RouteSegmentsCase(
        val endpoint: Endpoint,
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
            endpoint = endpoint("/pets"),
            api = openAPI(),
            expected = listOf(PathSegment.Literal("pets")),
        ),
        RouteSegmentsCase(
            endpoint = endpoint("/pets/{petId}"),
            api = openAPI(),
            expected = listOf(
                PathSegment.Literal("pets"),
                PathSegment.Parameter("petId", stringType)
            ),
        ),
        RouteSegmentsCase(
            endpoint = endpoint(
                "/pets/{petId}",
                parameters = listOf(pathParameter("petId", Schema.integer.copy(format = "int32")))
            ),
            api = openAPI(),
            expected = listOf(
                PathSegment.Literal("pets"),
                PathSegment.Parameter("petId", intType)
            ),
        ),
        RouteSegmentsCase(
            endpoint = endpoint(
                "/pets/{petId}",
                parameters = listOf(ReferenceOr.Reference("#/components/parameters/PetId"))
            ),
            api = openAPI(
                Components(
                    parameters = mapOf("PetId" to ReferenceOr.value(componentParam))
                )
            ),
            expected = listOf(
                PathSegment.Literal("pets"),
                PathSegment.Parameter("petId", intType)
            ),
        ),
        RouteSegmentsCase(
            endpoint = endpoint("/repos/{owner}/{repo}/collaborators"),
            api = openAPI(),
            expected = listOf(
                PathSegment.Literal("repos"),
                PathSegment.Parameter("owner", stringType),
                PathSegment.Parameter("repo", stringType),
                PathSegment.Literal("collaborators")
            ),
        ),
        RouteSegmentsCase(
            endpoint = endpoint("/{owner}/{repo}"),
            api = openAPI(),
            expected = listOf(
                PathSegment.Parameter("owner", stringType),
                PathSegment.Parameter("repo", stringType)
            ),
        ),
    ).verifyAll("Endpoint.toRoute().segments") { input ->
        Eq(input.expected, route(input.endpoint, input.api).segments)
    }
}
