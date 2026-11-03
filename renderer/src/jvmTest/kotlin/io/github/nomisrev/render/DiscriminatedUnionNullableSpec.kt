package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A discriminated `oneOf` that permits `null`, through a `null` branch or through a member its mapping points at that
 * permits `null` itself (`NullableCat: oneOf: [$ref Cat, null]`), keeps the sealed hierarchy it has without the
 * `null` (`@JsonClassDiscriminator`, or the tagged serializer when a member has several tags) and is nullable at
 * every use site: property, list item, map value, request body and response body.
 * `DiscriminatedUnionNullableRoundTripTest` decodes these goldens.
 */
val discriminatedUnionNullableSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets": {
              "put": {
                "operationId": "putPet",
                "requestBody": {
                  "required": true,
                  "content": { "application/json": { "schema": { "$ref": "#/components/schemas/NullBranchPet" } } }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/NullableMemberPet" } } }
                  }
                }
              }
            },
            "/tagged": {
              "put": {
                "operationId": "putTagged",
                "requestBody": {
                  "required": true,
                  "content": { "application/json": { "schema": { "$ref": "#/components/schemas/TaggedNullableMemberPet" } } }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/TaggedNullableMemberPet" } } }
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
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/PetHolder" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "PetHolder": {
                "type": "object",
                "additionalProperties": false,
                "properties": {
                  "pet": { "$ref": "#/components/schemas/NullBranchPet" },
                  "optionalPet": { "$ref": "#/components/schemas/NullBranchPet" },
                  "pets": { "type": "array", "items": { "$ref": "#/components/schemas/NullBranchPet" } },
                  "petMap": { "type": "object", "additionalProperties": { "$ref": "#/components/schemas/NullBranchPet" } },
                  "member": { "$ref": "#/components/schemas/NullableMemberPet" },
                  "optionalMember": { "$ref": "#/components/schemas/NullableMemberPet" },
                  "members": { "type": "array", "items": { "$ref": "#/components/schemas/NullableMemberPet" } },
                  "memberMap": { "type": "object", "additionalProperties": { "$ref": "#/components/schemas/NullableMemberPet" } },
                  "tagged": { "$ref": "#/components/schemas/TaggedNullableMemberPet" },
                  "taggedList": { "type": "array", "items": { "$ref": "#/components/schemas/TaggedNullableMemberPet" } }
                },
                "required": ["pet", "pets", "petMap", "member", "members", "memberMap", "tagged", "taggedList"]
              },
              "NullBranchPet": {
                "oneOf": [
                  { "$ref": "#/components/schemas/Cat" },
                  { "$ref": "#/components/schemas/Dog" },
                  { "type": "null" }
                ],
                "discriminator": {
                  "propertyName": "petType",
                  "mapping": { "cat": "#/components/schemas/Cat", "dog": "#/components/schemas/Dog" }
                }
              },
              "NullableMemberPet": {
                "oneOf": [
                  { "$ref": "#/components/schemas/NullableCat" },
                  { "$ref": "#/components/schemas/Dog" }
                ],
                "discriminator": {
                  "propertyName": "petType",
                  "mapping": { "cat": "#/components/schemas/NullableCat", "dog": "#/components/schemas/Dog" }
                }
              },
              "TaggedNullableMemberPet": {
                "oneOf": [
                  { "$ref": "#/components/schemas/NullableCat" },
                  { "$ref": "#/components/schemas/Dog" }
                ],
                "discriminator": {
                  "propertyName": "petType",
                  "mapping": {
                    "cat": "#/components/schemas/NullableCat",
                    "kitty": "#/components/schemas/NullableCat",
                    "dog": "#/components/schemas/Dog"
                  }
                }
              },
              "NullableCat": {
                "oneOf": [{ "$ref": "#/components/schemas/Cat" }, { "type": "null" }]
              },
              "Cat": {
                "type": "object",
                "additionalProperties": false,
                "properties": { "petType": { "type": "string" }, "meow": { "type": "string" } },
                "required": ["petType", "meow"]
              },
              "Dog": {
                "type": "object",
                "additionalProperties": false,
                "properties": { "petType": { "type": "string" }, "bark": { "type": "string" } },
                "required": ["petType", "bark"]
              }
            }
          }
        }
        """.trimIndent(),
        "client/discriminated-union-nullable"
    )
}
