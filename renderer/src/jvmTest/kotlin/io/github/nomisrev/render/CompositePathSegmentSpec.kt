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
 * A path segment can be made of literal text and template expressions (`{id}.json`, `lists.{format}`,
 * `{name}:cancel`, OpenAPI 3.1.1 path templating): each expression is an argument of the navigation function, whose
 * name is the camel-cased concatenation of every part. `CompositePathSegmentRuntimeTest` checks the request path.
 */
val compositePathSegmentSpec by testSuite {
    renderSpec(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/media/{id}.{extension}": {
              "get": {
                "parameters": [
                  { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } },
                  { "name": "extension", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/hackathons/{id}": {
              "get": {
                "parameters": [ { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/hackathons/{id}.json": {
              "get": {
                "parameters": [ { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/lists.{format}": {
              "get": {
                "parameters": [ { "name": "format", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/v1/{name}:cancel": {
              "post": {
                "parameters": [ { "name": "name", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/v{version}/x": {
              "get": {
                "parameters": [ { "name": "version", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/ranges/{lo}-{hi}": {
              "get": {
                "parameters": [
                  { "name": "lo", "in": "path", "required": true, "schema": { "type": "integer", "format": "int32" } },
                  { "name": "hi", "in": "path", "required": true, "schema": { "type": "integer", "format": "int32" } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/articles.{media_type_extension}": {
              "get": {
                "parameters": [
                  {
                    "name": "media_type_extension", "in": "path", "required": true,
                    "schema": { "type": "string", "enum": ["json", "xml"] }
                  }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/tags/{arn}": {
              "get": {
                "parameters": [ { "name": "arn", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/tags/{arn}#tagKeys": {
              "delete": {
                "parameters": [ { "name": "arn", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/composite-path-segments",
    )

    // Path keys that are readable but not valid path templates are generated, with a warning each.
    val sloppy = """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/tags/{arn}#tagKeys": {
              "delete": {
                "parameters": [ { "name": "arn", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/tags/{arn}": {
              "get": {
                "parameters": [ { "name": "arn", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/nasa/{id}{.format}": {
              "get": {
                "parameters": [ { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/undeclared/{name}.json": {
              "get": { "responses": { "204": { "description": "No Content" } } }
            },
            "/open/{brace": {
              "get": { "responses": { "204": { "description": "No Content" } } }
            },
            "/unmatched": {
              "get": {
                "parameters": [ { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            }
          }
        }
        """.trimIndent()

    renderSpec(sloppy, "client/sloppy-path-templates")

    test("a path key that is not a valid path template is generated with a warning") {
        // `{insight_id:}` is a whole-segment parameter whose name is not an identifier: it is reported, but the
        // generated client does not compile yet (a separate issue), so it is not part of the golden. A `?` in a path
        // key is sent as written, which Ktor reads as a query (`/search?format=json`): reported, and not part of the
        // golden, whose file would be named after the path.
        val withColon = sloppy.replace(
            "\"paths\": {",
            """
            "paths": {
                "/search?format=json": {
                  "get": { "responses": { "204": { "description": "No Content" } } }
                },
                "/insights/{insight_id:}": {
                  "get": {
                    "parameters": [ { "name": "insight_id:", "in": "path", "required": true, "schema": { "type": "string" } } ],
                    "responses": { "204": { "description": "No Content" } }
                  }
                },
            """.trimIndent()
        )
        val result = OpenAPI.fromJson(withColon).toApiTree().generateClientWithDiagnostics(
            RenderConfig(modelPackage = "test", apiPackage = "test", targets = setOf(KmpTarget.JVM))
        )
        assertEquals(
            listOf(
                "Path '/search?format=json' contains '?', which a path template does not have. It is sent as written.",
                "Path '/insights/{insight_id:}' has the template expression '{insight_id:}', which is not a plain " +
                    "parameter name. It is generated as a path parameter named 'insight_id:'.",
                "Path '/tags/{arn}#tagKeys' has a fragment ('#tagKeys'), which a path template does not have. " +
                    "The fragment is not sent: the request is for 'tags/{arn}'. It stays in the generated names, " +
                    "so paths that differ only by their fragment stay separate.",
                "Path '/nasa/{id}{.format}' has the template expression '{.format}', which is not a plain " +
                    "parameter name. It is generated as a path parameter named '.format'.",
                "Path '/nasa/{id}{.format}' has the template expression '{.format}' without a declared path " +
                    "parameter. It is generated as a required string parameter.",
                "Path '/undeclared/{name}.json' has the template expression '{name}' without a declared path " +
                    "parameter. It is generated as a required string parameter.",
                "Path '/open/{brace' has an unbalanced brace in the segment '{brace'. It is sent as written.",
                "Path '/unmatched' declares the path parameter 'id', which matches no template expression in the " +
                    "path. It is not sent.",
            ),
            result.diagnostics.map { it.message },
        )
        assertTrue(result.diagnostics.all { it.severity == GenerationDiagnosticSeverity.Warning })
    }
}
