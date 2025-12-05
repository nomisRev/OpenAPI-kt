package io.github.nomisrev

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Endpoint
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.NamingContext.RouteParam
import io.github.nomisrev.openapi.NamingContext.Path
import io.github.nomisrev.openapi.parser.Operation
import io.github.nomisrev.openapi.parser.Response
import io.github.nomisrev.openapi.parser.Responses
import io.ktor.http.HttpMethod
import kotlin.test.assertEquals

val endpointSpec by testSuite {
    fun endpoint(path: String, operationId: String? = null) = Endpoint(
        path = path,
        method = HttpMethod.Get,
        operation = Operation(
            operationId = operationId,
            responses = Responses(200, Response())
        )
    )

    // Formatting the operationId to a proper class name is for generation
    val operationId = listOf(
        endpoint("/test") to "get",
        endpoint("/test/{user_id}") to "getByUser_id",
        endpoint("/test/{userId}") to "getByUserId",
        endpoint("/test/{user_id}", "customOperationId") to "customOperationId",
    )

    operationId.forEach { (endpoint, expected) ->
        test("Endpoint.getOrCreateOperationId(): $endpoint -> $expected") {
            assertEquals(expected, endpoint.operationId)
        }
    }

    val segments = listOf(
        endpoint("/") to emptyList(),
        endpoint("/test") to listOf("test"),
        endpoint("/test/{user_id}") to listOf("test"),
        endpoint("/test/{userId}/test2") to listOf("test", "test2"),
        endpoint("/test/{userId}/test2/{param2}") to listOf("test", "test2"),
        endpoint("/test/v1/{userId}/test2/{param2}") to listOf("test", "v1", "test2"),
    )

    segments.forEach { (endpoint, expected) ->
        test("Endpoint.pathSegments(): $endpoint -> $expected") {
            assertEquals(expected, endpoint.pathSegments)
        }
    }

    val pathParams = listOf(
        endpoint("/") to emptyList(),
        endpoint("/test") to emptyList(),
        endpoint("/test/{user_id}") to listOf("user_id"),
        endpoint("/test/{userId}/test2") to listOf("userId"),
        endpoint("/test/{userId}/test2/{param2}") to listOf("userId", "param2"),
        endpoint("/test/v1/{userId}/test2/{param2}") to listOf("userId", "param2"),
        // We allow duplicate path parameters but they'll get filtered out for now
        endpoint("/test/{userId}/test2/{userId}/test3") to listOf("userId", "userId"),
    )

    pathParams.forEach { (endpoint, expected) ->
        test("Endpoint.pathParams(): $endpoint -> $expected") {
            assertEquals(expected, endpoint.pathParameters)
        }
    }

    val context = listOf(
        endpoint("/") to RouteParam(name = "param", operationId = "get"),
        endpoint("/test") to Path("test").nest(RouteParam(name = "param", operationId = "get")),
        endpoint("/test/{user_id}") to Path("test").nest(RouteParam(name = "param", operationId = "getByUser_id")),

        endpoint("/test/{userId}/test2") to Path("test").nest(Path("test2"))
            .nest(RouteParam(name = "param", operationId = "get")),

        endpoint("/test/{userId}/test2/{param2}") to Path("test").nest(Path("test2")).nest(RouteParam(name = "param", operationId = "getByParam2")),
    )

    context.forEach { (endpoint, expected) ->
        test("Endpoint.context(): $endpoint -> $expected") {
            assertEquals(expected, endpoint.context(RouteParam("param", endpoint.operationId)))
        }
    }
}