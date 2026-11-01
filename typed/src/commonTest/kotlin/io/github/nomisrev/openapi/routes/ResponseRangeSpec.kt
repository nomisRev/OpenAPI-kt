package io.github.nomisrev.openapi.routes

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.toApiTree
import io.github.nomisrev.openapi.parser.Components
import io.github.nomisrev.openapi.parser.Info
import io.github.nomisrev.openapi.parser.MediaType
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Operation
import io.github.nomisrev.openapi.parser.PathItem
import io.github.nomisrev.openapi.parser.Response
import io.github.nomisrev.openapi.parser.Responses
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.StatusCodeOrRange
import io.github.nomisrev.openapi.registry.Registry
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

val ResponseRangeSpec by testSuite {
    test("response ranges resolve exact and referenced responses and retain nested models") {
        val rangePayload = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf(
                "message" to ReferenceOr.Value(Schema.string),
            ),
        )
        val errorPayload = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf(
                "code" to ReferenceOr.Value(Schema.string),
            ),
        )
        val openAPI = OpenAPI(
            info = Info(title = "Test", version = "1.0.0"),
            paths = mapOf(
                "/test" to PathItem(
                    get = Operation(
                        responses = Responses(
                            responses = mapOf(
                                StatusCodeOrRange.Code(200) to ReferenceOr.Value(
                                    Response(
                                        description = "OK",
                                        content = mapOf(
                                            "application/json" to MediaType(schema = ReferenceOr.Value(Schema.string)),
                                        ),
                                    ),
                                ),
                                StatusCodeOrRange.Range("4XX") to ReferenceOr.Reference(
                                    "#/components/responses/ClientError",
                                ),
                                StatusCodeOrRange.Range("5XX") to ReferenceOr.Value(
                                    Response(
                                        description = "Server error",
                                        content = mapOf(
                                            "application/json" to MediaType(schema = ReferenceOr.Value(rangePayload)),
                                        ),
                                        extensions = mapOf("x-range-response" to JsonPrimitive(true)),
                                    ),
                                ),
                            ),
                            extensions = mapOf("x-response-extension" to JsonPrimitive("preserved")),
                        ),
                    ),
                ),
            ),
            components = Components(
                schemas = mapOf("ErrorPayload" to ReferenceOr.Value(errorPayload)),
                responses = mapOf(
                    "ClientError" to ReferenceOr.Value(
                        Response(
                            description = "Client error",
                            extensions = mapOf("x-component-response" to JsonPrimitive("resolved")),
                            content = mapOf(
                                "application/json" to MediaType(
                                    schema = ReferenceOr.Reference("#/components/schemas/ErrorPayload"),
                                ),
                            ),
                        ),
                    ),
                ),
            ),
        )

        val route = Registry(openAPI).use { registry ->
            with(registry) { openAPI.toRoutes().single() }
        }
        assertEquals(setOf(HttpStatusCode.OK), route.returns.responses.keys)
        assertEquals(
            setOf(StatusCodeOrRange.Range("4XX"), StatusCodeOrRange.Range("5XX")),
            route.returns.responseRanges.keys,
        )
        assertEquals(JsonPrimitive("preserved"), route.returns.extensions["x-response-extension"])
        assertEquals(
            JsonPrimitive(true),
            route.returns.responseRanges.getValue(StatusCodeOrRange.Range("5XX")).extensions["x-range-response"],
        )
        assertEquals(
            JsonPrimitive("resolved"),
            route.returns.responseRanges.getValue(StatusCodeOrRange.Range("4XX")).extensions["x-component-response"],
        )
        assertIs<Model.Object>(
            route.returns.responseRanges
                .getValue(StatusCodeOrRange.Range("4XX"))
                .types.getValue(ContentType.Application.Json),
        )
        assertTrue(route.nested.isNotEmpty())
        assertTrue(openAPI.toApiTree().models.any { it is Model.Object })
    }
}