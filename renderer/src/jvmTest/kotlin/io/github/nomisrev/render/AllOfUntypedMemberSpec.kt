package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * An `allOf` member without `type` that shapes values is merged as the type it implies: `items` is an array,
 * `properties`/`additionalProperties` an object, and an `enum` or `format` applies to the typed member next to it.
 * So `{type: array}` and `{items: {type: string}}` is a `List<String>` (as a response body and as a property),
 * a union branch the untyped member is compatible with is kept, and `UntypedMerge` renders every property with the
 * type both declarations agree on. `AllOfUntypedMemberRoundTripTest` decodes these goldens.
 */
val allOfUntypedMemberSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.0.3",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/holder": {
              "get": {
                "operationId": "getHolder",
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/UntypedMerge" } } }
                  }
                }
              }
            },
            "/tags": {
              "get": {
                "operationId": "getTags",
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": {
                      "application/json": {
                        "schema": { "allOf": [{ "type": "array" }, { "items": { "type": "string" } }] }
                      }
                    }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "UntypedMerge": {
                "allOf": [
                  {
                    "type": "object",
                    "properties": {
                      "tags": { "type": "array" },
                      "scores": { "items": { "type": "integer", "format": "int32" }, "minItems": 1 },
                      "meta": { "type": "object" },
                      "extras": { "additionalProperties": { "type": "integer" } },
                      "status": { "enum": ["on", "off"] },
                      "createdAt": { "format": "date-time" },
                      "either": { "oneOf": [{ "type": "array" }, { "type": "string" }] },
                      "note": { "type": "string" }
                    },
                    "required": ["tags", "scores", "meta", "extras", "status", "createdAt", "either"]
                  },
                  {
                    "type": "object",
                    "properties": {
                      "tags": { "items": { "type": "string" } },
                      "scores": { "type": "array", "items": { "type": "integer" } },
                      "meta": { "properties": { "a": { "type": "string" } } },
                      "extras": { "type": "object" },
                      "status": { "type": "string" },
                      "createdAt": { "type": "string" },
                      "either": { "items": { "type": "string" } },
                      "note": { "maxLength": 10 }
                    }
                  }
                ]
              }
            }
          }
        }
        """.trimIndent(),
        "client/allof-untyped-member"
    )
}
