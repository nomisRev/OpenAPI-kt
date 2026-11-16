package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * An `allOf` of a union and another member is the union of the branches merged with it. `code` keeps only the
 * string branch, the others admit no string (each reported); `shape` keeps both object branches, each with the
 * other member's property. `AllOfUnionMergeRoundTripTest` decodes this golden.
 */
val allOfUnionMergeSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.0.3",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/things": {
              "get": {
                "operationId": "getThing",
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Thing" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "Thing": {
                "type": "object",
                "properties": {
                  "code": {
                    "allOf": [
                      { "oneOf": [{ "type": "string" }, { "type": "integer" }, { "type": "boolean" }] },
                      { "type": "string", "maxLength": 3 }
                    ]
                  },
                  "shape": {
                    "allOf": [
                      {
                        "oneOf": [
                          { "type": "object", "properties": { "radius": { "type": "number" } }, "required": ["radius"] },
                          { "type": "object", "properties": { "side": { "type": "number" } }, "required": ["side"] }
                        ]
                      },
                      { "type": "object", "properties": { "color": { "type": "string" } } }
                    ]
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/allof-union-merge",
    )
}
