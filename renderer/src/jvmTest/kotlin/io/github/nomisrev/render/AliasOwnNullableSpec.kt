package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A named alias of a `$ref` that permits `null` itself, `allOf`/`anyOf`/`oneOf: [{$ref: X}]` next to
 * `nullable: true`, is its own type named after the alias (`client/allof-ref-alias`, `client/single-ref-alias`) and
 * is nullable at every use site: property, list item, map value, request body and response body, for an object,
 * enum, primitive and collection X. So is a non-null `allOf: [{$ref: X}]` alias of a nullable X.
 * `AliasOwnNullableRoundTripTest` decodes these goldens.
 */
val aliasOwnNullableSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.0.3",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/owners": {
              "put": {
                "operationId": "putOwner",
                "requestBody": {
                  "required": true,
                  "content": { "application/json": { "schema": { "$ref": "#/components/schemas/NullableAllOfOwner" } } }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/NullableOneOfString" } } }
                  }
                }
              }
            },
            "/names": {
              "put": {
                "operationId": "putNames",
                "requestBody": {
                  "required": true,
                  "content": { "application/json": { "schema": { "$ref": "#/components/schemas/NullableAllOfNames" } } }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/NullableAnyOfColor" } } }
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
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/AliasOwnNullableHolder" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "AliasOwnNullableHolder": {
                "type": "object",
                "additionalProperties": false,
                "properties": {
                  "owner": { "$ref": "#/components/schemas/NullableAllOfOwner" },
                  "owners": { "type": "array", "items": { "$ref": "#/components/schemas/NullableAllOfOwner" } },
                  "ownerMap": { "type": "object", "additionalProperties": { "$ref": "#/components/schemas/NullableAllOfOwner" } },
                  "color": { "$ref": "#/components/schemas/NullableAnyOfColor" },
                  "colors": { "type": "array", "items": { "$ref": "#/components/schemas/NullableAnyOfColor" } },
                  "colorMap": { "type": "object", "additionalProperties": { "$ref": "#/components/schemas/NullableAnyOfColor" } },
                  "string": { "$ref": "#/components/schemas/NullableOneOfString" },
                  "strings": { "type": "array", "items": { "$ref": "#/components/schemas/NullableOneOfString" } },
                  "stringMap": { "type": "object", "additionalProperties": { "$ref": "#/components/schemas/NullableOneOfString" } },
                  "names": { "$ref": "#/components/schemas/NullableAllOfNames" },
                  "namesList": { "type": "array", "items": { "$ref": "#/components/schemas/NullableAllOfNames" } },
                  "namesMap": { "type": "object", "additionalProperties": { "$ref": "#/components/schemas/NullableAllOfNames" } },
                  "nullableTarget": { "$ref": "#/components/schemas/AllOfNullableOwner" },
                  "nullableTargets": { "type": "array", "items": { "$ref": "#/components/schemas/AllOfNullableOwner" } },
                  "optionalOwner": { "$ref": "#/components/schemas/NullableAllOfOwner" }
                },
                "required": [
                  "owner", "owners", "ownerMap",
                  "color", "colors", "colorMap",
                  "string", "strings", "stringMap",
                  "names", "namesList", "namesMap",
                  "nullableTarget", "nullableTargets"
                ]
              },
              "NullableAllOfOwner": { "allOf": [{ "$ref": "#/components/schemas/Owner" }], "nullable": true },
              "NullableAnyOfColor": { "anyOf": [{ "$ref": "#/components/schemas/Color" }], "nullable": true },
              "NullableOneOfString": { "oneOf": [{ "$ref": "#/components/schemas/MyString" }], "nullable": true },
              "NullableAllOfNames": { "allOf": [{ "$ref": "#/components/schemas/Names" }], "nullable": true },
              "AllOfNullableOwner": { "allOf": [{ "$ref": "#/components/schemas/NullableOwner" }] },
              "Owner": {
                "type": "object",
                "additionalProperties": false,
                "properties": { "name": { "type": "string" } },
                "required": ["name"]
              },
              "NullableOwner": {
                "type": "object",
                "nullable": true,
                "additionalProperties": false,
                "properties": { "name": { "type": "string" } },
                "required": ["name"]
              },
              "Color": { "type": "string", "enum": ["red", "green"] },
              "MyString": { "type": "string" },
              "Names": { "type": "array", "items": { "type": "string" } }
            }
          }
        }
        """.trimIndent(),
        "client/alias-own-nullable"
    )
}
