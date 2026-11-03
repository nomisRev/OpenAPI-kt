package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A property is required when any `allOf` member requires it, whichever member declares the property: `PetWithName`
 * makes `Pet`'s optional `name` required, `PetWithExtra` requires `Pet`'s `id` and its own `extra`, and `Required`
 * requires `a` and the nullable `b` that its first member declares and its second requires. `PetWithName` is its own
 * class, it cannot be `Pet`, whose `name` is optional. `AllOfRequiredRoundTripTest` decodes these goldens.
 */
val allOfRequiredSpec by testSuite {
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
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Pets" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "Pet": {
                "type": "object",
                "properties": { "id": { "type": "integer" }, "name": { "type": "string" } },
                "required": ["id"]
              },
              "PetWithName": { "allOf": [{ "$ref": "#/components/schemas/Pet" }, { "required": ["name"] }] },
              "PetWithExtra": {
                "allOf": [
                  { "$ref": "#/components/schemas/Pet" },
                  { "type": "object", "properties": { "extra": { "type": "string" } }, "required": ["extra"] }
                ]
              },
              "Required": {
                "allOf": [
                  {
                    "type": "object",
                    "properties": {
                      "a": { "type": "string" },
                      "b": { "type": "string", "nullable": true },
                      "c": { "type": "string" }
                    }
                  },
                  { "required": ["a", "b"] }
                ]
              },
              "Pets": {
                "type": "object",
                "properties": {
                  "pet": { "$ref": "#/components/schemas/Pet" },
                  "withName": { "$ref": "#/components/schemas/PetWithName" },
                  "withExtra": { "$ref": "#/components/schemas/PetWithExtra" },
                  "required": { "$ref": "#/components/schemas/Required" }
                },
                "required": ["pet", "withName", "withExtra", "required"]
              }
            }
          }
        }
        """.trimIndent(),
        "client/allof-required"
    )
}
