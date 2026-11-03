package io.github.nomisrev

import de.infix.testBalloon.framework.core.testSuite
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
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.routes.isJsonLike
import io.github.nomisrev.openapi.routes.toRoutes
import io.ktor.http.ContentType
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs

// A wildcard request body media type (`*/*`, `application/*`, `text/*`, ...) is a range the server accepts, never a
// `Content-Type` a client can send: the body is a plain `setBody` body sent with the concrete media type, within that
// range, that the client produces for its model.
val wildcardMediaTypeSpec by testSuite {
    val objectSchema = Schema(
        type = Schema.Type.Basic.Object,
        properties = mapOf("name" to ReferenceOr.value(Schema.string)),
        required = listOf("name"),
    )
    val binarySchema = Schema.string.copy(format = "binary")

    suspend fun body(contentType: String, schema: Schema): Route.Body {
        val operation = Operation(
            requestBody = ReferenceOr.value(
                RequestBody(required = true, content = mapOf(contentType to MediaType(schema = ReferenceOr.value(schema))))
            ),
            responses = Responses(204, Response()),
        )
        val api = OpenAPI(info = Info("Test", "1.0"), paths = ["/upload" to PathItem(post = operation)])
        val route = Registry(api).use { registry -> with(registry) { api.toRoutes() } }.single()
        return requireNotNull(route.body?.defaultOrNull())
    }

    data class Case(val declared: String, val schema: Schema, val kind: String, val sent: ContentType)

    for (case in listOf(
        Case("*/*", objectSchema, "object", ContentType.Application.Json),
        Case("application/*", objectSchema, "object", ContentType.Application.Json),
        Case("*/*", Schema.string, "string", ContentType.Text.Plain),
        Case("application/*", Schema.string, "string", ContentType.Application.Json),
        Case("text/*", Schema.string, "string", ContentType.Text.Plain),
        Case("*/*", binarySchema, "binary", ContentType.Application.OctetStream),
        Case("application/*", binarySchema, "binary", ContentType.Application.OctetStream),
        Case("text/*", binarySchema, "binary", ContentType.Text.Plain),
        Case("*/*", Schema.integer, "integer", ContentType.Application.Json),
        Case("text/*; charset=iso-8859-1", Schema.string, "string", ContentType.Text.Plain.withParameter("charset", "iso-8859-1")),
        Case("*/*; charset=utf-8", objectSchema, "object", ContentType.Application.Json.withParameter("charset", "utf-8")),
        Case("*/*; charset=utf-8", binarySchema, "binary", ContentType.Application.OctetStream),
        Case("application/json; charset=utf-8", objectSchema, "object", ContentType.Application.Json.withParameter("charset", "utf-8")),
        Case(
            "application/vnd.api+json; ext=\"https://jsonapi.org/ext/atomic\"",
            objectSchema,
            "object",
            ContentType("application", "vnd.api+json").withParameter("ext", "https://jsonapi.org/ext/atomic"),
        ),
    )) {
        test("an ${case.kind} body declared under ${case.declared} is sent as ${case.sent}") {
            val body = assertIs<Route.Body.SetBody>(body(case.declared, case.schema))
            assertEquals(case.sent, body.contentType)
        }
    }

    test("an object body declared under a wildcard no producible media type matches keeps the declared range") {
        val body = assertIs<Route.Body.SetBody>(body("image/*", objectSchema))
        assertEquals(ContentType.Image.Any, body.contentType)
    }

    test("multipart/form-data and multipart/* bodies are multipart") {
        assertIs<Route.Body.Multipart>(body("multipart/form-data", objectSchema))
        assertIs<Route.Body.Multipart>(body("multipart/*", objectSchema))
    }

    test("a form-urlencoded body is a form, media type parameters aside") {
        assertIs<Route.Body.FormUrlEncoded>(body("application/x-www-form-urlencoded", objectSchema))
        assertIs<Route.Body.FormUrlEncoded>(body("application/x-www-form-urlencoded; charset=utf-8", objectSchema))
    }

    test("a wildcard media type is not JSON-like") {
        assertFalse(ContentType.Any.isJsonLike())
        assertFalse(ContentType.Application.Any.isJsonLike())
        assertFalse(ContentType.Text.Any.isJsonLike())
    }
}
