package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * An `allOf` permits `null` only when every member does, or when it says so itself: `MixedOwner`, the `allOf` of a
 * nullable `$ref` and a non-null inline object, is non-null at every use site, `AllNullableOwner`, whose members are
 * both nullable, and `OwnNullableOwner`, `nullable: true` next to non-null members, are nullable.
 * `AllOfMemberNullableRoundTripTest` decodes these goldens.
 */
val allOfMemberNullableSpec by testSuite {
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
                  "content": { "application/json": { "schema": { "$ref": "#/components/schemas/MixedOwner" } } }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/AllNullableOwner" } } }
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
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/AllOfMemberNullableHolder" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "AllOfMemberNullableHolder": {
                "type": "object",
                "properties": {
                  "mixed": { "$ref": "#/components/schemas/MixedOwner" },
                  "mixedList": { "type": "array", "items": { "$ref": "#/components/schemas/MixedOwner" } },
                  "allNullable": { "$ref": "#/components/schemas/AllNullableOwner" },
                  "allNullableList": { "type": "array", "items": { "$ref": "#/components/schemas/AllNullableOwner" } },
                  "ownNullable": { "$ref": "#/components/schemas/OwnNullableOwner" },
                  "optionalMixed": { "$ref": "#/components/schemas/MixedOwner" }
                },
                "required": ["mixed", "mixedList", "allNullable", "allNullableList", "ownNullable"]
              },
              "MixedOwner": {
                "allOf": [
                  { "$ref": "#/components/schemas/NullableOwner" },
                  { "type": "object", "properties": { "extra": { "type": "string" } } }
                ]
              },
              "AllNullableOwner": {
                "allOf": [
                  { "$ref": "#/components/schemas/NullableOwner" },
                  { "type": "object", "nullable": true, "properties": { "extra": { "type": "string" } } }
                ]
              },
              "OwnNullableOwner": {
                "allOf": [
                  { "$ref": "#/components/schemas/Owner" },
                  { "type": "object", "properties": { "extra": { "type": "string" } } }
                ],
                "nullable": true
              },
              "Owner": {
                "type": "object",
                "properties": { "name": { "type": "string" } },
                "required": ["name"]
              },
              "NullableOwner": {
                "type": "object",
                "nullable": true,
                "properties": { "name": { "type": "string" } },
                "required": ["name"]
              }
            }
          }
        }
        """.trimIndent(),
        "client/allof-member-nullable"
    )
}
