package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A path item that is a `$ref` to a `components.pathItems` entry (here through an alias,
 * `AliasOwners: {$ref: '#/components/pathItems/Owners'}`) renders exactly like the same path item written inline:
 * `/inline` and `/referenced` generate the same operations, with the same path-level parameter.
 * `PathItemRefRoundTripTest` sends and receives through these goldens.
 */
val pathItemRefSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/inline": $$OWNERS_PATH_ITEM,
            "/referenced": { "$ref": "#/components/pathItems/AliasOwners" }
          },
          "components": {
            "schemas": {
              "Owner": {
                "type": "object",
                "additionalProperties": false,
                "properties": { "name": { "type": "string" }, "nickname": { "type": "string" } },
                "required": ["name"]
              }
            },
            "pathItems": {
              "Owners": $$OWNERS_PATH_ITEM,
              "AliasOwners": { "$ref": "#/components/pathItems/Owners" }
            }
          }
        }
        """.trimIndent(),
        "client/path-item-ref"
    )
}

private const val OWNERS_PATH_ITEM = $$"""
{
  "parameters": [{ "name": "X-Trace-Id", "in": "header", "required": true, "schema": { "type": "string" } }],
  "get": {
    "parameters": [{ "name": "limit", "in": "query", "required": true, "schema": { "type": "integer", "format": "int32" } }],
    "responses": {
      "200": { "description": "OK", "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Owner" } } } }
    }
  },
  "put": {
    "requestBody": { "required": true, "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Owner" } } } },
    "responses": { "204": { "description": "No Content" } }
  }
}
"""
