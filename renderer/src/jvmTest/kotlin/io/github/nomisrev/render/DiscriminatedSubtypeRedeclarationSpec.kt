package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.GenerationDiagnostic
import io.github.nomisrev.openapi.GenerationDiagnosticSeverity
import io.github.nomisrev.openapi.RenderConfig
import io.github.nomisrev.openapi.generateClientWithDiagnostics
import io.github.nomisrev.openapi.generateWithDiagnostics
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.toApiTree
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * A discriminated subtype that redeclares an inherited property so that no value satisfies both declarations
 * overrides it, as YouTrack's subtypes do with the `{type: object}` placeholders of `ActivityItem.added` and
 * `IssueCustomField.value`: `CommentActivity.added` is a `List<Comment>`, `CountActivity.count` an `Int`, and the
 * parent's `Default` case keeps its own `count: String`, so `count` is not a property of the sealed interface. Each
 * override is a warning; the same contradiction in an `allOf` that is no discriminated subtype still fails.
 */
val discriminatedSubtypeRedeclarationSpec by testSuite {
    renderSpec(DISCRIMINATED_SUBTYPE_REDECLARATION_API.trimIndent(), "client/discriminated-subtype-redeclaration")

    val expectedDiagnostics = listOf(
        GenerationDiagnostic(
            GenerationDiagnosticSeverity.Warning,
            "Discriminated subtype CommentActivity overrides property 'added' it inherits from Activity: it " +
                """declares {"type":"array","items":{"${'$'}ref":"#/components/schemas/Comment"}}, which """ +
                """contradicts the inherited {"type":"object"} (No value is both of type object and of type """ +
                "array.). CommentActivity's declaration is used.",
        ),
        GenerationDiagnostic(
            GenerationDiagnosticSeverity.Warning,
            "Discriminated subtype CountActivity overrides property 'count' it inherits from Activity: it " +
                """declares {"type":"integer","format":"int32"}, which contradicts the inherited """ +
                """{"type":"string"} (No value is both of type string and of type integer.). CountActivity's """ +
                "declaration is used.",
        ),
    )

    test("a discriminated subtype overriding an inherited property is reported as a warning") {
        val apiTree = OpenAPI.fromJson(DISCRIMINATED_SUBTYPE_REDECLARATION_API.trimIndent()).toApiTree()
        assertEquals(expectedDiagnostics, apiTree.generateClientWithDiagnostics(RenderConfig()).diagnostics)
    }

    test("generateWithDiagnostics reports a discriminated subtype overriding an inherited property") {
        val result = OpenAPI.fromJson(DISCRIMINATED_SUBTYPE_REDECLARATION_API.trimIndent())
            .generateWithDiagnostics(RenderConfig())
        assertEquals(expectedDiagnostics, result.diagnostics)
    }

    test("a contradictory allOf that is no discriminated subtype still fails generation") {
        val error = assertFails { OpenAPI.fromJson(PLAIN_ALLOF_CONTRADICTION_API.trimIndent()).toApiTree() }
        assertIs<IllegalArgumentException>(error)
        assertTrue(
            "allOf members declare property 'result' incompatibly: No value is both of type object and of type array." in
                error.message.orEmpty(),
            error.message,
        )
    }
}

private const val DISCRIMINATED_SUBTYPE_REDECLARATION_API = $$"""
        {
          "openapi": "3.0.3",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/feed": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Activity" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "Activity": {
                "type": "object",
                "properties": {
                  "kind": { "type": "string" },
                  "id": { "type": "string" },
                  "added": { "type": "object" },
                  "count": { "type": "string" }
                },
                "required": ["id"],
                "discriminator": {
                  "propertyName": "kind",
                  "mapping": {
                    "Activity": "#/components/schemas/Activity",
                    "CommentActivity": "#/components/schemas/CommentActivity",
                    "CountActivity": "#/components/schemas/CountActivity"
                  }
                }
              },
              "CommentActivity": {
                "allOf": [
                  { "$ref": "#/components/schemas/Activity" },
                  {
                    "type": "object",
                    "properties": {
                      "id": { "type": "string", "maxLength": 36 },
                      "added": { "type": "array", "items": { "$ref": "#/components/schemas/Comment" } }
                    }
                  }
                ]
              },
              "CountActivity": {
                "allOf": [
                  { "$ref": "#/components/schemas/Activity" },
                  {
                    "type": "object",
                    "properties": { "count": { "type": "integer", "format": "int32" } }
                  }
                ]
              },
              "Comment": {
                "type": "object",
                "properties": { "text": { "type": "string" } },
                "required": ["text"]
              }
            }
          }
        }
        """

private const val PLAIN_ALLOF_CONTRADICTION_API = $$"""
        {
          "openapi": "3.0.3",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/collection": {
              "get": {
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Collection" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "Base": { "type": "object", "properties": { "result": { "type": "object" } } },
              "Collection": {
                "allOf": [
                  { "$ref": "#/components/schemas/Base" },
                  { "type": "object", "properties": { "result": { "type": "array", "items": { "type": "string" } } } }
                ]
              }
            }
          }
        }
        """
