package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A non-discriminated `oneOf`/`anyOf` decodes a value as the case whose JSON type matches the value's token: a JSON
 * string only as a string-typed case (enum, `date`, `date-time`, plain string), `true`/`false` only as a boolean case,
 * a JSON number only as a numeric case (integer before number), an array only as an array case and an object only as
 * an object case. Members are declared least specific first, so the result cannot depend on declaration order.
 * `UnionTokenKindDispatchRoundTripTest` decodes these goldens under strict and lenient `Json` configurations.
 */
val unionTokenKindDispatchSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/value": {
              "get": {
                "operationId": "getValue",
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/StringOrInteger" } } }
                  }
                }
              }
            },
            "/holder": {
              "get": {
                "operationId": "getHolder",
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/UnionHolder" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "UnionHolder": {
                "type": "object",
                "properties": {
                  "stringOrInteger": { "$ref": "#/components/schemas/StringOrInteger" },
                  "integerOrString": { "$ref": "#/components/schemas/IntegerOrString" },
                  "stringOrBoolean": { "$ref": "#/components/schemas/StringOrBoolean" },
                  "numberOrInteger": { "$ref": "#/components/schemas/NumberOrInteger" },
                  "stringOrEnumOrInteger": { "$ref": "#/components/schemas/StringOrEnumOrInteger" },
                  "stringOrDateTime": { "$ref": "#/components/schemas/StringOrDateTime" },
                  "plainStringOrDate": { "$ref": "#/components/schemas/PlainStringOrDate" },
                  "freeFormOrCat": { "$ref": "#/components/schemas/FreeFormOrCat" },
                  "mapOrList": { "$ref": "#/components/schemas/MapOrList" },
                  "integerOrCat": { "$ref": "#/components/schemas/IntegerOrCat" },
                  "plainStringOrInteger": { "$ref": "#/components/schemas/PlainStringOrInteger" }
                }
              },
              "StringOrInteger": { "oneOf": [{ "type": "string" }, { "type": "integer" }] },
              "IntegerOrString": { "oneOf": [{ "type": "integer" }, { "type": "string" }] },
              "StringOrBoolean": { "oneOf": [{ "type": "string" }, { "type": "boolean" }] },
              "NumberOrInteger": { "oneOf": [{ "type": "number" }, { "type": "integer" }] },
              "StringOrEnumOrInteger": {
                "oneOf": [{ "type": "string" }, { "type": "string", "enum": ["a", "b"] }, { "type": "integer" }]
              },
              "StringOrDateTime": { "oneOf": [{ "type": "string" }, { "type": "string", "format": "date-time" }] },
              "PlainStringOrDate": {
                "oneOf": [{ "$ref": "#/components/schemas/PlainString" }, { "type": "string", "format": "date" }]
              },
              "PlainStringOrInteger": {
                "oneOf": [{ "$ref": "#/components/schemas/PlainString" }, { "type": "integer" }]
              },
              "FreeFormOrCat": { "oneOf": [{}, { "$ref": "#/components/schemas/Cat" }] },
              "MapOrList": {
                "oneOf": [
                  { "type": "object", "additionalProperties": { "type": "string" } },
                  { "type": "array", "items": { "type": "string" } }
                ]
              },
              "IntegerOrCat": { "oneOf": [{ "type": "integer" }, { "$ref": "#/components/schemas/Cat" }] },
              "PlainString": { "type": "string" },
              "Cat": {
                "type": "object",
                "additionalProperties": false,
                "properties": { "meow": { "type": "string" } },
                "required": ["meow"]
              }
            }
          }
        }
        """.trimIndent(),
        "client/union-token-kind-dispatch"
    )
}
