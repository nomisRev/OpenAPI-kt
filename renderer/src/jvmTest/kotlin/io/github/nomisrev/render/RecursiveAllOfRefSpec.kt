package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * Properties declared as `allOf: [{$ref: X}, {description: ...}]` that lead back to the schema holding them are that
 * schema (`Node.child`), the list of it (`Node.children`, the AWS shape) and the map of it (`Node.byName`, the DynamoDB
 * shape); `Node.owner` is the top-level `Owner`, not a nested copy. An `allOf` that adds a property (`Node.annotated`)
 * is a nested type that refers to itself. `RecursiveAllOfRefRoundTripTest` decodes this golden.
 */
val recursiveAllOfRefSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.0.3",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/nodes": {
              "get": {
                "operationId": "getNode",
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Node" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "Node": {
                "type": "object",
                "properties": {
                  "name": { "type": "string" },
                  "child": { "allOf": [{ "$ref": "#/components/schemas/Node" }, { "description": "The child." }] },
                  "children": { "allOf": [{ "$ref": "#/components/schemas/NodeList" }, { "description": "The children." }] },
                  "byName": { "allOf": [{ "$ref": "#/components/schemas/NodeMap" }, { "description": "By name." }] },
                  "owner": { "allOf": [{ "$ref": "#/components/schemas/Owner" }, { "description": "The owner." }] },
                  "annotated": {
                    "allOf": [
                      { "$ref": "#/components/schemas/Node" },
                      { "properties": { "note": { "type": "string" } } }
                    ]
                  }
                }
              },
              "NodeList": { "type": "array", "items": { "$ref": "#/components/schemas/Node" } },
              "NodeMap": { "type": "object", "additionalProperties": { "$ref": "#/components/schemas/Node" } },
              "Owner": { "type": "object", "properties": { "id": { "type": "integer", "format": "int32" } } }
            }
          }
        }
        """.trimIndent(),
        "client/recursive-allof-ref",
    )
}
