package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A named component with a single `$ref` branch, `anyOf`/`oneOf: [{$ref: X}]`, is its own type named after the
 * component, like `allOf: [{$ref: X}]` and the nullable `anyOf: [{$ref: X}, {type: null}]` (`client/nullable-ref-alias`):
 * a data class for an object, an enum class for an enum, and a value class for a primitive or collection. It must not
 * generate X a second time, nor leave references to the component pointing at a type that was never generated.
 * An inline (unnamed) `anyOf: [{$ref: Owner}]` is still `Owner`. `SingleRefAliasRoundTripTest` decodes these goldens.
 */
val singleRefAliasSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/owners": {
              "put": {
                "operationId": "putOwner",
                "requestBody": {
                  "required": true,
                  "content": { "application/json": { "schema": { "$ref": "#/components/schemas/AnyOfOwner" } } }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/OneOfOwner" } } }
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
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/SingleRefAliasHolder" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "SingleRefAliasHolder": {
                "type": "object",
                "additionalProperties": false,
                "properties": {
                  "anyOfOwner": { "$ref": "#/components/schemas/AnyOfOwner" },
                  "oneOfOwner": { "$ref": "#/components/schemas/OneOfOwner" },
                  "optionalAnyOfOwner": { "$ref": "#/components/schemas/AnyOfOwner" },
                  "anyOfOwners": { "type": "array", "items": { "$ref": "#/components/schemas/AnyOfOwner" } },
                  "oneOfOwnerMap": { "type": "object", "additionalProperties": { "$ref": "#/components/schemas/OneOfOwner" } },
                  "anyOfColor": { "$ref": "#/components/schemas/AnyOfColor" },
                  "oneOfLabel": { "$ref": "#/components/schemas/OneOfLabel" },
                  "anyOfNames": { "$ref": "#/components/schemas/AnyOfNames" },
                  "inlineAnyOfOwner": { "anyOf": [{ "$ref": "#/components/schemas/Owner" }] },
                  "owner": { "$ref": "#/components/schemas/Owner" }
                },
                "required": [
                  "anyOfOwner",
                  "oneOfOwner",
                  "anyOfOwners",
                  "oneOfOwnerMap",
                  "anyOfColor",
                  "oneOfLabel",
                  "anyOfNames",
                  "inlineAnyOfOwner",
                  "owner"
                ]
              },
              "AnyOfOwner": { "anyOf": [{ "$ref": "#/components/schemas/Owner" }] },
              "OneOfOwner": { "oneOf": [{ "$ref": "#/components/schemas/Owner" }] },
              "AnyOfColor": { "anyOf": [{ "$ref": "#/components/schemas/Color" }] },
              "OneOfLabel": { "oneOf": [{ "$ref": "#/components/schemas/Label" }] },
              "AnyOfNames": { "anyOf": [{ "$ref": "#/components/schemas/Names" }] },
              "Owner": {
                "type": "object",
                "additionalProperties": false,
                "properties": { "name": { "type": "string" } },
                "required": ["name"]
              },
              "Color": { "type": "string", "enum": ["red", "green"] },
              "Label": { "type": "string" },
              "Names": { "type": "array", "items": { "type": "string" } }
            }
          }
        }
        """.trimIndent(),
        "client/single-ref-alias"
    )
}
