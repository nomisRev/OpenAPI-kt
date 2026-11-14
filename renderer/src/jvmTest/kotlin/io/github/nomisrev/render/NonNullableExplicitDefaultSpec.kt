package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * An explicit `default: null` on a schema that is not nullable is no default: a required and an optional string and
 * enum property, and a query parameter, generate what they generate without the keyword and compile.
 */
val nonNullableExplicitDefaultSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.0.3",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/settings": {
              "get": {
                "operationId": "getPreferences",
                "parameters": [
                  { "name": "sort", "in": "query", "schema": { "type": "string", "default": null } },
                  { "name": "kind", "in": "query", "schema": { "type": "string", "enum": ["S", "O"], "default": null } }
                ],
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Preferences" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "Preferences": {
                "type": "object",
                "required": ["name", "kind"],
                "properties": {
                  "name": { "type": "string", "default": null },
                  "kind": { "type": "string", "enum": ["S", "O"], "default": null },
                  "appId": { "type": "string", "default": null },
                  "optionalKind": { "type": "string", "enum": ["S", "O"], "default": null },
                  "nullableName": { "type": "string", "nullable": true, "default": null }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/non-nullable-explicit-default"
    )
}
