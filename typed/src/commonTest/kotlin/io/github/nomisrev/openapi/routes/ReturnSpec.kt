package io.github.nomisrev.openapi.routes

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.parser.Info
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Operation
import io.github.nomisrev.openapi.parser.PathItem
import io.github.nomisrev.openapi.parser.Responses
import io.github.nomisrev.openapi.parser.Response
import io.github.nomisrev.openapi.parser.MediaType
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.registry.Registry
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlin.test.assertIs
import kotlin.test.assertTrue

val ReturnSpec by testSuite {
    test("resolveReturns handles null schema with raw response for audio/wav") {
        val openAPI = OpenAPI(
            info = Info(title = "Test", version = "1.0.0"),
            paths = mapOf(
                "/test" to PathItem(
                    get = Operation(
                        responses = Responses(
                            responses = mapOf(
                                200 to ReferenceOr.Value(
                                    Response(
                                        description = "OK",
                                        content = mapOf(
                                            "audio/wav" to MediaType(schema = null)
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        Registry(openAPI).use { registry ->
            with(registry) {
                val returns = resolveReturns(
                    path = "/test",
                    segments = emptyList(),
                    method = HttpMethod.Get,
                    operation = openAPI.paths["/test"]!!.get!!
                )
                val returnType = returns.responses[HttpStatusCode.OK]!!
                assertTrue(ContentType.parse("audio/wav") in returnType.rawContentTypes)
            }
        }
    }

    test("resolveReturns keeps explicit binary schema as ByteArray") {
        val openAPI = OpenAPI(
            info = Info(title = "Test", version = "1.0.0"),
            paths = mapOf(
                "/test" to PathItem(
                    get = Operation(
                        responses = Responses(
                            responses = mapOf(
                                200 to ReferenceOr.Value(
                                    Response(
                                        description = "OK",
                                        content = mapOf(
                                            "application/octet-stream" to MediaType(
                                                schema = ReferenceOr.Value(Schema.string.copy(format = "binary"))
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        Registry(openAPI).use { registry ->
            with(registry) {
                val returns = resolveReturns(
                    path = "/test",
                    segments = emptyList(),
                    method = HttpMethod.Get,
                    operation = openAPI.paths["/test"]!!.get!!
                )
                val returnType = returns.responses[HttpStatusCode.OK]!!
                assertIs<Model.ByteArray>(returnType.types[ContentType.Application.OctetStream])
            }
        }
    }

    test("resolveReturns handles null schema with FreeFormJson for application/json") {
        val openAPI = OpenAPI(
            info = Info(title = "Test", version = "1.0.0"),
            paths = mapOf(
                "/test" to PathItem(
                    get = Operation(
                        responses = Responses(
                            responses = mapOf(
                                200 to ReferenceOr.Value(
                                    Response(
                                        description = "OK",
                                        content = mapOf(
                                            "application/json" to MediaType(schema = null)
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        Registry(openAPI).use { registry ->
            with(registry) {
                val returns = resolveReturns(
                    path = "/test",
                    segments = emptyList(),
                    method = HttpMethod.Get,
                    operation = openAPI.paths["/test"]!!.get!!
                )
                val returnType = returns.responses[HttpStatusCode.OK]!!
                val model = returnType.types[ContentType.Application.Json]!!
                assertIs<Model.FreeFormJson>(model)
            }
        }
    }

    test("resolveReturns infers usable models for null schemas by content type") {
        val openAPI = OpenAPI(
            info = Info(title = "Test", version = "1.0.0"),
            paths = mapOf(
                "/test" to PathItem(
                    get = Operation(
                        responses = Responses(
                            responses = mapOf(
                                200 to ReferenceOr.Value(
                                    Response(
                                        description = "OK",
                                        content = mapOf(
                                            "application/problem+json" to MediaType(schema = null),
                                            "application/xml" to MediaType(schema = null),
                                            "application/octet-stream" to MediaType(schema = null),
                                            "application/vnd.example" to MediaType(schema = null),
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        Registry(openAPI).use { registry ->
            with(registry) {
                val returns = resolveReturns(
                    path = "/test",
                    segments = emptyList(),
                    method = HttpMethod.Get,
                    operation = openAPI.paths["/test"]!!.get!!
                )
                val returnType = returns.responses[HttpStatusCode.OK]!!
                assertIs<Model.FreeFormJson>(returnType.types[ContentType.parse("application/problem+json")])
                assertIs<Model.Primitive.String>(returnType.types[ContentType.parse("application/xml")])
                assertTrue(ContentType.Application.OctetStream in returnType.rawContentTypes)
                assertTrue(ContentType.parse("application/vnd.example") in returnType.rawContentTypes)
            }
        }
    }
}
