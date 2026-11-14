package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.GenerationDiagnosticSeverity
import io.github.nomisrev.openapi.KmpTarget
import io.github.nomisrev.openapi.RenderConfig
import io.github.nomisrev.openapi.generateClientWithDiagnostics
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.toApiTree
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * A literal path segment is a valid Kotlin name for every step, whatever characters it has on the wire. A `:` splits
 * it into a chain (`/v1/entities:search` is `api.v1.entities.search`), as it does the text of `{name}:cancel`; any other
 * character that is not a letter or a digit is a word break (`a@b` is `AB`); a segment without a letter or digit is
 * spelled (`-` is `dash`). `LiteralPathSegmentRuntimeTest` checks the request path.
 */
val literalPathSegmentSpec by testSuite {
    val document = """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/v1/entities": {
              "get": { "responses": { "204": { "description": "No Content" } } }
            },
            "/v1/entities:search": {
              "post": { "responses": { "204": { "description": "No Content" } } }
            },
            "/v1/items/search": {
              "get": { "responses": { "204": { "description": "No Content" } } }
            },
            "/v1/items:search": {
              "get": { "responses": { "204": { "description": "No Content" } } }
            },
            "/apps/-/config": {
              "get": { "responses": { "204": { "description": "No Content" } } }
            },
            "/handles/a@b": {
              "get": { "responses": { "204": { "description": "No Content" } } }
            },
            "/filters/a=b": {
              "get": { "responses": { "204": { "description": "No Content" } } }
            },
            "/pairs/x@y": {
              "get": { "responses": { "204": { "description": "No Content" } } }
            },
            "/pairs/x=y": {
              "get": { "responses": { "204": { "description": "No Content" } } }
            },
            "/things/~batch": {
              "get": { "responses": { "204": { "description": "No Content" } } }
            },
            "/things/~batch:run": {
              "post": { "responses": { "204": { "description": "No Content" } } }
            },
            "/ops/a:b:c": {
              "get": { "responses": { "204": { "description": "No Content" } } }
            },
            "/ops/_/x": {
              "get": { "responses": { "204": { "description": "No Content" } } }
            }
          }
        }
        """.trimIndent()

    renderSpec(document, "client/literal-path-segments")

    test("steps that get one name are told apart, with a warning each") {
        val result = OpenAPI.fromJson(document).toApiTree().generateClientWithDiagnostics(
            RenderConfig(modelPackage = "test", apiPackage = "test", targets = setOf(KmpTarget.JVM))
        )
        assertEquals(
            listOf(
                "Path '/v1/items:search' is generated as 'colonSearch' (class ColonSearch): another path segment " +
                    "has the same Kotlin name, so this one is named with the text that joins it (':').",
                "Path '/pairs/x=y' is generated as 'xY2' (class XY2): an earlier path segment has the same Kotlin name.",
            ),
            result.diagnostics.map { it.message },
        )
        assertTrue(result.diagnostics.all { it.severity == GenerationDiagnosticSeverity.Warning })
    }
}
