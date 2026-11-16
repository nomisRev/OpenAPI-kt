package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A named component `allOf: [{$ref: X}]` to a primitive or collection X is its own value class named after the
 * component, like `anyOf`/`oneOf: [{$ref: X}]` (`client/single-ref-alias`): the component is generated, and every
 * use site (property, list item, map value, request and response body) uses it. `nullable: true` on the component
 * makes its properties nullable. An inline (unnamed) `allOf: [{$ref: X}]` is X itself, as a plain `$ref: X` is.
 * `AllOfRefAliasRoundTripTest` decodes these goldens.
 */
val allOfRefAliasSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/aliases": {
              "put": {
                "operationId": "putAlias",
                "requestBody": {
                  "required": true,
                  "content": { "application/json": { "schema": { "$ref": "#/components/schemas/PrimAllOf" } } }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/ListAllOf" } } }
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
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/AllOfRefAliasHolder" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "AllOfRefAliasHolder": {
                "type": "object",
                "additionalProperties": false,
                "properties": {
                  "primAllOf": { "$ref": "#/components/schemas/PrimAllOf" },
                  "optionalPrimAllOf": { "$ref": "#/components/schemas/PrimAllOf" },
                  "primAllOfs": { "type": "array", "items": { "$ref": "#/components/schemas/PrimAllOf" } },
                  "primAllOfMap": { "type": "object", "additionalProperties": { "$ref": "#/components/schemas/PrimAllOf" } },
                  "listAllOf": { "$ref": "#/components/schemas/ListAllOf" },
                  "listAllOfs": { "type": "array", "items": { "$ref": "#/components/schemas/ListAllOf" } },
                  "nullablePrimAllOf": { "$ref": "#/components/schemas/NullablePrimAllOf" },
                  "nullableListAllOf": { "$ref": "#/components/schemas/NullableListAllOf" },
                  "inlinePrimAllOf": { "allOf": [{ "$ref": "#/components/schemas/MyString" }] },
                  "inlineListAllOf": { "allOf": [{ "$ref": "#/components/schemas/Names" }] }
                },
                "required": [
                  "primAllOf",
                  "primAllOfs",
                  "primAllOfMap",
                  "listAllOf",
                  "listAllOfs",
                  "nullablePrimAllOf",
                  "nullableListAllOf",
                  "inlinePrimAllOf",
                  "inlineListAllOf"
                ]
              },
              "PrimAllOf": { "allOf": [{ "$ref": "#/components/schemas/MyString" }] },
              "ListAllOf": { "allOf": [{ "$ref": "#/components/schemas/Names" }] },
              "NullablePrimAllOf": { "allOf": [{ "$ref": "#/components/schemas/MyString" }], "nullable": true },
              "NullableListAllOf": { "allOf": [{ "$ref": "#/components/schemas/Names" }], "nullable": true },
              "MyString": { "type": "string" },
              "Names": { "type": "array", "items": { "type": "string" } }
            }
          }
        }
        """.trimIndent(),
        "client/allof-ref-alias"
    )
}
