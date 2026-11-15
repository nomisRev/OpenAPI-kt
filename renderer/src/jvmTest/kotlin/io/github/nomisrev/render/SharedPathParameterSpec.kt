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
 * Operations below one path position share its navigation step (`api.items.id(id)`), though each declares the path
 * parameter itself. Declarations that differ only by validation keywords or a default are one parameter. Declarations
 * as different types are a navigation function per type (`id(id: String)` and `id(id: Long)`), each leading to the
 * operations that declare that type, so no operation is reachable with a type it does not declare.
 * `SharedPathParameterRuntimeTest` checks the requests.
 */
val sharedPathParameterSpec by testSuite {
    renderSpec(
        """
        {
          "openapi": "3.0.3",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/items/{id}": {
              "get": {
                "parameters": [ { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/items/{id}/parts": {
              "get": {
                "parameters": [ {
                  "name": "id", "in": "path", "required": true,
                  "schema": { "type": "string", "minLength": 34, "maxLength": 34, "pattern": "^HA[0-9a-fA-F]{32}${'$'}" }
                } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/items/{id}/tags": {
              "get": {
                "parameters": [ {
                  "name": "id", "in": "path", "required": true, "schema": { "type": "string", "default": "x" }
                } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/pages/{number}": {
              "get": {
                "parameters": [ {
                  "name": "number", "in": "path", "required": true, "schema": { "type": "integer", "format": "int32" }
                } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/pages/{number}/lines": {
              "get": {
                "parameters": [ {
                  "name": "number", "in": "path", "required": true,
                  "schema": { "type": "integer", "format": "int32", "minimum": 1, "maximum": 99 }
                } ],
                "responses": { "204": { "description": "No Content" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/shared-path-parameter-validation",
    )

    val types = """
        {
          "openapi": "3.0.3",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/vaults/{id}": {
              "get": {
                "parameters": [ { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/vaults/{id}/items": {
              "get": {
                "parameters": [ { "name": "id", "in": "path", "required": true, "schema": { "type": "string", "format": "uuid" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/vaults/{id}/files": {
              "get": {
                "parameters": [ { "name": "id", "in": "path", "required": true, "schema": { "type": "string", "format": "uuid" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/pets/{petId}": {
              "get": {
                "parameters": [ { "name": "petId", "in": "path", "required": true, "schema": { "type": "integer", "format": "int64" } } ],
                "responses": { "204": { "description": "No Content" } }
              },
              "delete": {
                "parameters": [ { "name": "petId", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/pages/{number}": {
              "get": {
                "parameters": [ { "name": "number", "in": "path", "required": true, "schema": { "type": "integer", "format": "int32" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/pages/{number}/lines": {
              "get": {
                "parameters": [ { "name": "number", "in": "path", "required": true, "schema": { "type": "integer", "format": "int64" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/tags/{ids}": {
              "get": {
                "parameters": [ { "name": "ids", "in": "path", "required": true, "schema": { "type": "array", "items": { "type": "integer", "format": "int32" } } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/tags/{ids}/first": {
              "get": {
                "parameters": [ { "name": "ids", "in": "path", "required": true, "schema": { "type": "integer", "format": "int32" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/notes/{id}": {
              "get": {
                "parameters": [ { "name": "id", "in": "path", "required": true, "schema": { "type": "string", "nullable": true } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/notes/{id}/text": {
              "get": {
                "parameters": [ { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            }
          }
        }
        """.trimIndent()

    renderSpec(types, "client/shared-path-parameter-types")

    test("a path parameter declared as different types is reported for each navigation function") {
        val result = OpenAPI.fromJson(types).toApiTree().generateClientWithDiagnostics(
            RenderConfig(modelPackage = "test", apiPackage = "test", targets = setOf(KmpTarget.JVM))
        )
        val messages = result.diagnostics.map { it.message }
        fun perType(path: String, className: String, type: String) =
            "Path '$path' is generated as '${path.substringAfter('{').substringBefore('}')}' (class $className): " +
                "operations declare this path parameter as different types, so there is a navigation function for " +
                "each type, and this one leads to the operations that declare it as $type."
        assertEquals(
            setOf(
                perType("/vaults/{id}", "IdStringPath", "String"),
                perType("/vaults/{id}", "IdUuidPath", "Uuid"),
                perType("/pets/{petId}", "PetIdLongPath", "Long"),
                perType("/pets/{petId}", "PetIdStringPath", "String"),
                perType("/pages/{number}", "NumberIntPath", "Int"),
                perType("/pages/{number}", "NumberLongPath", "Long"),
                perType("/tags/{ids}", "IdsIntListPath", "IntList"),
                perType("/tags/{ids}", "IdsIntPath", "Int"),
                "GET /notes/{id} declares the path parameter 'id' as nullable. A path segment cannot be null, so it " +
                    "is generated as not nullable.",
            ),
            messages.toSet(),
        )
        assertEquals(9, result.diagnostics.size, result.diagnostics.joinToString("\n") { it.message })
        assertTrue(result.diagnostics.all { it.severity == GenerationDiagnosticSeverity.Warning })
    }
}
