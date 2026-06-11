package io.github.nomisrev.openapi.routes

import de.infix.testBalloon.framework.core.TestConfig
import de.infix.testBalloon.framework.core.disable
import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.parser.Info
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Operation
import io.github.nomisrev.openapi.parser.PathItem
import io.github.nomisrev.openapi.parser.Responses
import io.github.nomisrev.openapi.parser.Response
import io.github.nomisrev.openapi.parser.MediaType
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.registry.Registry
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlin.test.assertIs

val ReturnSpec by testSuite(testConfig = TestConfig.disable()) {
    test("resolveReturns handles null schema with ByteArray for audio/wav") {
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
                val model = returnType.types[ContentType.parse("audio/wav")]!!
                assertIs<Model.ByteArray>(model)
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
}
