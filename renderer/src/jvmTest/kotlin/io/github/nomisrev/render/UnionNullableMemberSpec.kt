package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A non-discriminated `oneOf`/`anyOf` with a member that permits `null` (a `$ref` to `anyOf: [string, null]`, an
 * inline `nullable: true` or `type: [string, null]` member, a `$ref` to `oneOf: [$ref Cat, null]`) permits `null`
 * itself: it is a nullable union at every use site (property, list item, map value, request body and response body),
 * and its cases are the members' non-null forms. The same holds for a union whose discriminator is inferred from its
 * members' required single-value `type` property. `UnionNullableMemberRoundTripTest` decodes these goldens.
 */
val unionNullableMemberSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/value": {
              "put": {
                "operationId": "putValue",
                "requestBody": {
                  "required": true,
                  "content": { "application/json": { "schema": { "$ref": "#/components/schemas/RefMember" } } }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/AnyOfRefMember" } } }
                  }
                }
              }
            },
            "/pet": {
              "put": {
                "operationId": "putPet",
                "requestBody": {
                  "required": true,
                  "content": { "application/json": { "schema": { "$ref": "#/components/schemas/InferredPet" } } }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/InferredPet" } } }
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
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/ValueHolder" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "ValueHolder": {
                "type": "object",
                "additionalProperties": false,
                "properties": {
                  "ref": { "$ref": "#/components/schemas/RefMember" },
                  "optionalRef": { "$ref": "#/components/schemas/RefMember" },
                  "refs": { "type": "array", "items": { "$ref": "#/components/schemas/RefMember" } },
                  "refMap": { "type": "object", "additionalProperties": { "$ref": "#/components/schemas/RefMember" } },
                  "anyOf": { "$ref": "#/components/schemas/AnyOfRefMember" },
                  "anyOfs": { "type": "array", "items": { "$ref": "#/components/schemas/AnyOfRefMember" } },
                  "anyOfMap": { "type": "object", "additionalProperties": { "$ref": "#/components/schemas/AnyOfRefMember" } },
                  "inline": { "$ref": "#/components/schemas/InlineNullableMember" },
                  "inlines": { "type": "array", "items": { "$ref": "#/components/schemas/InlineNullableMember" } },
                  "inlineMap": { "type": "object", "additionalProperties": { "$ref": "#/components/schemas/InlineNullableMember" } },
                  "typeArray": { "$ref": "#/components/schemas/TypeArrayMember" },
                  "typeArrays": { "type": "array", "items": { "$ref": "#/components/schemas/TypeArrayMember" } },
                  "typeArrayMap": { "type": "object", "additionalProperties": { "$ref": "#/components/schemas/TypeArrayMember" } },
                  "twoNullable": { "$ref": "#/components/schemas/TwoNullableMembers" },
                  "twoNullables": { "type": "array", "items": { "$ref": "#/components/schemas/TwoNullableMembers" } },
                  "object": { "$ref": "#/components/schemas/ObjectMember" },
                  "objects": { "type": "array", "items": { "$ref": "#/components/schemas/ObjectMember" } },
                  "objectMap": { "type": "object", "additionalProperties": { "$ref": "#/components/schemas/ObjectMember" } },
                  "pet": { "$ref": "#/components/schemas/InferredPet" },
                  "pets": { "type": "array", "items": { "$ref": "#/components/schemas/InferredPet" } },
                  "petMap": { "type": "object", "additionalProperties": { "$ref": "#/components/schemas/InferredPet" } }
                },
                "required": [
                  "ref", "refs", "refMap", "anyOf", "anyOfs", "anyOfMap", "inline", "inlines", "inlineMap",
                  "typeArray", "typeArrays", "typeArrayMap", "twoNullable", "twoNullables",
                  "object", "objects", "objectMap", "pet", "pets", "petMap"
                ]
              },
              "RefMember": {
                "oneOf": [{ "$ref": "#/components/schemas/NullableString" }, { "type": "integer" }]
              },
              "AnyOfRefMember": {
                "anyOf": [{ "$ref": "#/components/schemas/NullableString" }, { "type": "integer" }]
              },
              "InlineNullableMember": {
                "oneOf": [{ "type": "string", "nullable": true }, { "type": "integer" }]
              },
              "TypeArrayMember": {
                "oneOf": [{ "type": ["string", "null"] }, { "type": "integer" }]
              },
              "TwoNullableMembers": {
                "oneOf": [{ "$ref": "#/components/schemas/NullableString" }, { "type": ["integer", "null"] }]
              },
              "ObjectMember": {
                "oneOf": [{ "$ref": "#/components/schemas/NullableCat" }, { "type": "string" }]
              },
              "InferredPet": {
                "oneOf": [{ "$ref": "#/components/schemas/NullableCat" }, { "$ref": "#/components/schemas/Dog" }]
              },
              "NullableString": {
                "anyOf": [{ "type": "string" }, { "type": "null" }]
              },
              "NullableCat": {
                "oneOf": [{ "$ref": "#/components/schemas/Cat" }, { "type": "null" }]
              },
              "Cat": {
                "type": "object",
                "additionalProperties": false,
                "properties": { "type": { "type": "string", "enum": ["cat"] }, "meow": { "type": "string" } },
                "required": ["type", "meow"]
              },
              "Dog": {
                "type": "object",
                "additionalProperties": false,
                "properties": { "type": { "type": "string", "enum": ["dog"] }, "bark": { "type": "string" } },
                "required": ["type", "bark"]
              }
            }
          }
        }
        """.trimIndent(),
        "client/union-nullable-member"
    )
}
