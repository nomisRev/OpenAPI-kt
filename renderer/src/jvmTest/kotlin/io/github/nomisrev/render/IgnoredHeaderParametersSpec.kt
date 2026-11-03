package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.GenerationDiagnostic
import io.github.nomisrev.openapi.GenerationDiagnosticSeverity
import io.github.nomisrev.openapi.KmpTarget
import io.github.nomisrev.openapi.RenderConfig
import io.github.nomisrev.openapi.generateClientWithDiagnostics
import io.github.nomisrev.openapi.generateWithDiagnostics
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.toApiTree
import kotlin.test.assertEquals

/**
 * OpenAPI ignores a header parameter named `Accept`, `Content-Type` or `Authorization` (in any case), wherever it is
 * declared. Specs commonly use one to express the media type of an upload; the client sends `Content-Type` from the
 * request body and `Accept` from the declared responses instead, so neither becomes an argument, and a binary body
 * under the range of every `image` subtype keeps its plain `contentType: ContentType` argument. A query or cookie
 * parameter with such a name is an ordinary parameter. `IgnoredHeaderParametersRuntimeTest` checks the headers on the
 * wire.
 */
val ignoredHeaderParametersSpec by testSuite {
    renderSpec(
        IGNORED_HEADER_PARAMETERS_API.trimIndent(),
        "client/ignored-header-parameters"
    )

    // The spec mandates ignoring these definitions, so generation succeeds, but what they declared (the media types an
    // upload accepts, that a request is authorized) is not acted on: each one is reported as a warning.
    val expectedDiagnostics = listOf(
        "Ignoring header parameter Authorization of PUT /avatars: OpenAPI ignores a header parameter named Accept, " +
            "Content-Type or Authorization; authorization is described by security schemes and configured on the " +
            "HttpClient.",
        "Ignoring header parameter Content-Type (declared values: image/png, image/jpeg) of PUT /avatars: OpenAPI " +
            "ignores a header parameter named Accept, Content-Type or Authorization; the Content-Type header is sent " +
            "from the request body.",
        "Ignoring header parameter content-type of POST /notes: OpenAPI ignores a header parameter named Accept, " +
            "Content-Type or Authorization; the Content-Type header is sent from the request body.",
        "Ignoring header parameter ACCEPT (declared values: application/json, text/csv) of GET /reports: OpenAPI " +
            "ignores a header parameter named Accept, Content-Type or Authorization; the client chooses the Accept " +
            "header from the declared responses.",
    ).map { GenerationDiagnostic(GenerationDiagnosticSeverity.Warning, it) }

    val config = RenderConfig(
        modelPackage = "io.github.nomisrev.render.test.ignored.headers",
        apiPackage = "io.github.nomisrev.render.test.ignored.headers",
        targets = setOf(KmpTarget.JVM),
    )

    test("every ignored header parameter is reported as a warning by client generation") {
        val result = OpenAPI.fromJson(IGNORED_HEADER_PARAMETERS_API).toApiTree().generateClientWithDiagnostics(config)
        assertEquals(expectedDiagnostics.sortedBy { it.message }, result.diagnostics.sortedBy { it.message })
    }

    test("every ignored header parameter is reported as a warning by generation from the document") {
        val result = OpenAPI.fromJson(IGNORED_HEADER_PARAMETERS_API).generateWithDiagnostics(config)
        assertEquals(expectedDiagnostics.sortedBy { it.message }, result.diagnostics.sortedBy { it.message })
    }
}

private const val IGNORED_HEADER_PARAMETERS_API = $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/avatars": {
              "parameters": [ { "$ref": "#/components/parameters/AliasAuthorization" } ],
              "put": {
                "parameters": [
                  {
                    "name": "Content-Type",
                    "in": "header",
                    "required": true,
                    "schema": { "type": "string", "enum": ["image/png", "image/jpeg"] }
                  },
                  { "name": "X-Request-Id", "in": "header", "schema": { "type": "string" } }
                ],
                "requestBody": {
                  "required": true,
                  "content": { "image/*": { "schema": { "type": "string", "format": "binary" } } }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/notes": {
              "post": {
                "parameters": [
                  { "name": "content-type", "in": "header", "required": true, "schema": { "type": "string" } }
                ],
                "requestBody": {
                  "required": true,
                  "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Note" } } }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/reports": {
              "get": {
                "parameters": [
                  {
                    "name": "ACCEPT",
                    "in": "header",
                    "required": true,
                    "schema": { "type": "string", "enum": ["application/json", "text/csv"] }
                  },
                  { "name": "Content-Type", "in": "query", "schema": { "type": "string" } },
                  { "name": "Authorization", "in": "cookie", "schema": { "type": "string" } }
                ],
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": { "schema": { "type": "string" } },
                      "text/csv": { "schema": { "type": "string" } }
                    }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "Note": {
                "type": "object",
                "properties": { "text": { "type": "string" } },
                "required": ["text"]
              }
            },
            "parameters": {
              "Authorization": { "name": "Authorization", "in": "header", "required": true, "schema": { "type": "string" } },
              "AliasAuthorization": { "$ref": "#/components/parameters/Authorization" }
            }
          }
        }
        """
