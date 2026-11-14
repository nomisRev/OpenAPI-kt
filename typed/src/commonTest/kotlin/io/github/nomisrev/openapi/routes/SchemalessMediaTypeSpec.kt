package io.github.nomisrev.openapi.routes

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.parser.Info
import io.github.nomisrev.openapi.parser.MediaType
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Operation
import io.github.nomisrev.openapi.parser.PathItem
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.RequestBody
import io.github.nomisrev.openapi.parser.Response
import io.github.nomisrev.openapi.parser.Responses
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.StatusCodeOrRange
import io.github.nomisrev.openapi.registry.Registry
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

/**
 * A media type declared without `schema` (and without `itemSchema`) is typed by the media type alone
 * (`example` and `examples` never influence it): JSON is any JSON, text is a string, and every other media type is the
 * raw response, or a request body the caller builds.
 */
private suspend fun routeOf(
    responses: Map<String, MediaType> = emptyMap(),
    requestBody: RequestBody? = null,
    errorResponses: Map<StatusCodeOrRange, ReferenceOr<Response>> = emptyMap(),
): Route {
    val openAPI = OpenAPI(
        info = Info(title = "Test", version = "1.0.0"),
        paths = [
            "/test" to PathItem(
                post = Operation(
                    requestBody = requestBody?.let { ReferenceOr.Value(it) },
                    responses = Responses(
                        responses = buildMap {
                            if (responses.isNotEmpty()) {
                                put(
                                    StatusCodeOrRange.Code(200),
                                    ReferenceOr.Value(Response(description = "OK", content = responses)),
                                )
                            }
                            putAll(errorResponses)
                        },
                    ),
                ),
            ),
        ],
    )
    return Registry(openAPI).use { registry -> with(registry) { openAPI.toRoutes().single() } }
}

private fun Route.ok(): Map<ContentType, Model> = returns.responses.getValue(HttpStatusCode.OK).types

val SchemalessMediaTypeSpec by testSuite {
    test("the media type alone classifies JSON, text and everything else") {
        assertEquals(MediaTypeKind.Json, ContentType.Application.Json.mediaTypeKind())
        assertEquals(MediaTypeKind.Json, ContentType.parse("application/problem+json").mediaTypeKind())
        assertEquals(MediaTypeKind.Json, ContentType.parse("application/json; charset=utf-8").mediaTypeKind())
        assertEquals(MediaTypeKind.Text, ContentType.Text.Plain.mediaTypeKind())
        assertEquals(MediaTypeKind.Text, ContentType.Text.CSV.mediaTypeKind())
        assertEquals(MediaTypeKind.Text, ContentType.Text.Html.mediaTypeKind())
        assertEquals(MediaTypeKind.Text, ContentType.parse("text/*").mediaTypeKind())
        assertEquals(MediaTypeKind.Other, ContentType.Application.Pdf.mediaTypeKind())
        assertEquals(MediaTypeKind.Other, ContentType.Application.Xml.mediaTypeKind())
        assertEquals(MediaTypeKind.Other, ContentType.Application.OctetStream.mediaTypeKind())
        assertEquals(MediaTypeKind.Other, ContentType.parse("image/*").mediaTypeKind())
        assertEquals(MediaTypeKind.Other, ContentType.parse("*/*").mediaTypeKind())
        assertEquals(MediaTypeKind.Other, ContentType.parse("application/*").mediaTypeKind())
    }

    test("a response media type without schema is typed by its media type") {
        val route = routeOf(
            responses = mapOf(
                "application/json" to MediaType(examples = emptyMap(), example = JsonPrimitive("x")),
            ),
        )
        assertIs<Model.FreeFormJson>(route.ok().getValue(ContentType.Application.Json))

        val text = routeOf(responses = mapOf("text/plain" to MediaType()))
        assertIs<Model.Primitive.String>(text.ok().getValue(ContentType.Text.Plain))
    }

    test("a response media type without schema that is neither JSON nor text is the raw response") {
        val route = routeOf(
            responses = mapOf(
                "application/pdf" to MediaType(example = JsonPrimitive("pdf bytes")),
                "image/*" to MediaType(),
                "*/*" to MediaType(),
                "application/xml" to MediaType(),
            ),
        )
        val types = route.ok()
        assertEquals(
            setOf("application/pdf", "image/*", "*/*", "application/xml"),
            types.keys.map(ContentType::toString).toSet(),
        )
        types.values.forEach { assertIs<Model.RawResponse>(it) }
    }

    test("a declared binary string stays bytes") {
        val route = routeOf(
            responses = mapOf(
                "application/pdf" to MediaType(
                    schema = ReferenceOr.Value(Schema(type = Schema.Type.Basic.String, format = "binary")),
                ),
            ),
        )
        assertIs<Model.ByteArray>(route.ok().getValue(ContentType.Application.Pdf))
    }

    test("a schema-less entry sits next to a typed one for the same status") {
        val route = routeOf(
            responses = mapOf(
                "application/json" to MediaType(schema = ReferenceOr.Value(Schema.string)),
                "application/pdf" to MediaType(),
            ),
        )
        assertIs<Model.Primitive.String>(route.ok().getValue(ContentType.Application.Json))
        assertIs<Model.RawResponse>(route.ok().getValue(ContentType.Application.Pdf))
    }

    test("a request media type without schema is typed by its media type") {
        val json = routeOf(
            requestBody = RequestBody(content = mapOf("application/json" to MediaType()), required = true),
        )
        val jsonBody = assertIs<Route.Body.SetBody>(json.body!!.types.getValue(ContentType.Application.Json))
        assertIs<Model.FreeFormJson>(jsonBody.type)

        val text = routeOf(
            requestBody = RequestBody(content = mapOf("text/plain" to MediaType()), required = true),
        )
        val textBody = assertIs<Route.Body.SetBody>(text.body!!.types.getValue(ContentType.Text.Plain))
        assertIs<Model.Primitive.String>(textBody.type)
    }

    test("a request media type without schema that is neither JSON nor text is built by the caller") {
        val optional = routeOf(
            requestBody = RequestBody(
                content = mapOf("application/pdf" to MediaType(example = JsonPrimitive("pdf bytes"))),
                required = false,
            ),
        )
        val body = assertNotNull(optional.body)
        assertEquals(false, body.required)
        val callerSet = assertIs<Route.Body.CallerSet>(body.types.getValue(ContentType.Application.Pdf))
        assertEquals(ContentType.Application.Pdf, callerSet.contentType)

        val required = routeOf(
            requestBody = RequestBody(
                content = mapOf(
                    "application/json" to MediaType(schema = ReferenceOr.Value(Schema.string)),
                    "application/pdf" to MediaType(),
                ),
                required = true,
            ),
        )
        val bodies = required.body!!.types
        assertIs<Route.Body.SetBody>(bodies.getValue(ContentType.Application.Json))
        assertIs<Route.Body.CallerSet>(bodies.getValue(ContentType.Application.Pdf))
    }

    test("an operation with a schema-less response and request still lists the models it needs") {
        val route = routeOf(
            responses = mapOf("application/pdf" to MediaType()),
            requestBody = RequestBody(content = mapOf("application/pdf" to MediaType()), required = true),
            errorResponses = mapOf(
                StatusCodeOrRange.Code(404) to ReferenceOr.Value(
                    Response(
                        description = "Missing",
                        content = mapOf("application/json" to MediaType(schema = ReferenceOr.Value(Schema.string))),
                    ),
                ),
            ),
        )
        assertEquals(setOf(HttpStatusCode.OK, HttpStatusCode.NotFound), route.returns.responses.keys)
    }
}
