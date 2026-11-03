package io.github.nomisrev

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.routes.isJsonLike
import io.ktor.http.ContentType
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue

/** [Route.Bodies.defaultOrNull] selects the request body an operation renders when it has a single signature. */
val requestBodySelectionSpec by testSuite {
    fun setBody(contentType: String): Route.Body.SetBody = Route.Body.SetBody(
        contentType = ContentType.parse(contentType),
        type = Model.Primitive.String(
            default = null,
            description = null,
            constraint = null,
            isNullable = false,
            title = null,
        ),
        description = null,
        extensions = emptyMap(),
    )

    fun bodies(vararg types: Route.Body): Route.Bodies = Route.Bodies(
        required = true,
        types = types.associateBy { body ->
            when (body) {
                is Route.Body.SetBody -> body.contentType
                is Route.Body.OverloadedBody -> body.contentType
                is Route.Body.FormUrlEncoded -> ContentType.Application.FormUrlEncoded
                is Route.Body.Multipart -> ContentType.MultiPart.FormData
            }
        },
        extensions = emptyMap(),
    )

    for (contentType in listOf(
        "application/merge-patch+json",
        "application/json-patch+json",
        "application/vnd.api+json",
        "application/problem+json",
    )) {
        test("a body declared only under $contentType is selected") {
            val body = setBody(contentType)
            assertSame(body, bodies(body).defaultOrNull())
        }
    }

    for (contentType in listOf(
        "application/json; charset=utf-8",
        "text/csv",
        "image/png",
        "application/pdf",
        "application/atom+xml",
        "application/x-ndjson",
    )) {
        test("a body declared only under $contentType is selected instead of dropped") {
            val body = setBody(contentType)
            assertSame(body, bodies(body).defaultOrNull())
        }
    }

    test("a +json body is preferred over a body without a dedicated rendering") {
        val json = setBody("application/merge-patch+json")
        assertSame(json, bodies(setBody("text/csv"), json).defaultOrNull())
    }

    test("declaration order decides between JSON-like and text/plain bodies") {
        val text = setBody("text/plain")
        assertSame(text, bodies(text, setBody("application/merge-patch+json")).defaultOrNull())
    }

    test("form and multipart bodies are preferred over a body without a dedicated rendering") {
        val form = Route.Body.FormUrlEncoded(
            parameters = emptyList(),
            description = null,
            extensions = emptyMap(),
        )
        assertIs<Route.Body.FormUrlEncoded>(bodies(setBody("text/csv"), form).defaultOrNull())

        val multipart = Route.Body.Multipart.Ref(
            value = Model.ByteArray(description = null, isNullable = false, title = null),
            description = null,
            extensions = emptyMap(),
        )
        assertIs<Route.Body.Multipart.Ref>(bodies(setBody("text/csv"), multipart).defaultOrNull())
    }

    test("isJsonLike matches application/json and every +json structured syntax suffix") {
        assertTrue(ContentType.Application.Json.isJsonLike())
        assertTrue(ContentType.parse("application/json; charset=utf-8").isJsonLike())
        assertTrue(ContentType.parse("application/merge-patch+json").isJsonLike())
        assertTrue(ContentType.parse("application/problem+json").isJsonLike())
        assertFalse(ContentType.Text.Plain.isJsonLike())
        assertFalse(ContentType.parse("application/atom+xml").isJsonLike())
        assertFalse(ContentType.parse("application/jsonl").isJsonLike())
        assertFalse(ContentType.parse("text/csv").isJsonLike())
    }
}
