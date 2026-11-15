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
 * `{name}:cancel`, OpenAPI 3.1.1 path templating): it is navigated one step per part, a navigation function for each
 * expression and a property for the text (`api.hackathons.id(id).json`), like the steps of `/`-separated segments.
 * A parameter that is a fixed enum is a property per value, as it is for a whole segment
 * (`api.orders.status.paid.json`), and a union of scalars a navigation function per case.
 * `CompositePathSegmentRuntimeTest` checks the request path.
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
            "/lists": {
              "get": { "responses": { "204": { "description": "No Content" } } }
            },
            "/lists.{format}": {
              "get": {
                "parameters": [ { "name": "format", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/hackathons/{id}/json": {
              "get": {
                "parameters": [ { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } } ],
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
            "/orders/status/{status}.json": {
              "get": {
                "parameters": [ { "name": "status", "in": "path", "required": true, "schema": { "type": "string", "enum": ["Abandoned", "Pending Payment", "Paid"] } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/news/{section}.{format}": {
              "get": {
                "parameters": [ { "name": "section", "in": "path", "required": true, "schema": { "type": "string", "enum": ["home", "world"] } }, { "name": "format", "in": "path", "required": true, "schema": { "type": "string", "enum": ["json", "jsonp"] } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/policy/{actionGroup}#revisionId": {
              "get": {
                "parameters": [ { "name": "actionGroup", "in": "path", "required": true, "schema": { "type": "string", "enum": ["agentPermissions"] } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/pulls/{index}": {
              "patch": {
                "parameters": [ { "name": "index", "in": "path", "required": true, "schema": { "type": "integer", "format": "int64" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/pulls/{index}.{diffType}": {
              "get": {
                "parameters": [
                  { "name": "index", "in": "path", "required": true, "schema": { "type": "integer", "format": "int64" } },
                  { "name": "diffType", "in": "path", "required": true, "schema": { "type": "string", "enum": ["diff", "patch"] } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/reports/{period}.csv": {
              "get": {
                "parameters": [ { "name": "period", "in": "path", "required": true, "schema": { "oneOf": [ { "type": "string", "enum": ["daily", "weekly"] }, { "type": "integer", "format": "int32" } ] } } ],
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

    test("a step joined without a slash that has the name of a sibling is named with its separator, with a warning") {
        fun operation(path: String) = """
            "$path": {
              "get": {
                "parameters": [ { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            }
        """.trimIndent()
        suspend fun generate(vararg paths: String) = OpenAPI.fromJson(
            """{"openapi":"3.1.0","info":{"title":"Api","version":"0.0.1"},"paths":{${paths.joinToString(",") { operation(it) }}}}"""
        ).toApiTree().generateClientWithDiagnostics(
            RenderConfig(modelPackage = "test", apiPackage = "test", targets = setOf(KmpTarget.JVM))
        )

        val warning = "Path '/hackathons/{id}.json' is generated as 'dotJson' (class DotJson): another path segment " +
            "or an operation of its parent has the same Kotlin name, so this one is named with the text that joins it ('.')."
        val result = generate("/hackathons/{id}/json", "/hackathons/{id}.json")
        assertEquals(listOf(warning), result.diagnostics.map { it.message })
        val hackathons = result.files.single { it.name == "Hackathons" }.toString()
        assertTrue("public val json: Json = Json(__client, _id)" in hackathons, hackathons)
        assertTrue("public val dotJson: DotJson = DotJson(__client, _id)" in hackathons, hackathons)
        assertTrue("__client.get(\"hackathons/${'$'}{this._id.encodeURLParameter()}/json\")" in hackathons, hackathons)
        assertTrue("__client.get(\"hackathons/${'$'}{this._id.encodeURLParameter()}.json\")" in hackathons, hackathons)

        // The order of the spec does not matter: the segment after a slash keeps the plain name.
        val reversed = generate("/hackathons/{id}.json", "/hackathons/{id}/json")
        assertEquals(listOf(warning), reversed.diagnostics.map { it.message })
        assertTrue(
            "public val dotJson: DotJson = DotJson(__client, _id)" in reversed.files.single { it.name == "Hackathons" }.toString()
        )

        // A parameter step and other separators.
        val others = generate("/lists/{id}", "/lists.{id}", "/v1/{id}/cancel", "/v1/{id}:cancel")
        val lists = others.files.single { it.name == "Lists" }.toString()
        assertTrue("public fun id(id: String): IdPath = IdPath(__client, id)" in lists, lists)
        assertTrue("public fun dotId(id: String): DotIdPath = DotIdPath(__client, id)" in lists, lists)
        val v1 = others.files.single { it.name == "V1" }.toString()
        assertTrue("public val colonCancel: ColonCancel = ColonCancel(__client, _id)" in v1, v1)

        // Two joined steps of one name and no slash step: both are named with their separator.
        val joined = generate("/hackathons/{id}.json", "/hackathons/{id}:json").files.single { it.name == "Hackathons" }.toString()
        assertTrue("public val dotJson: DotJson" in joined && "public val colonJson: ColonJson" in joined, joined)
    }
}
