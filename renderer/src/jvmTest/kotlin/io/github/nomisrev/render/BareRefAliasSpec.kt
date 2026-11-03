package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A bare `$ref` component, `BareOwner: {$ref: Owner}`, is its own type named after the component, exactly like
 * `allOf: [{$ref: Owner}]` (`client/allof-ref-alias`): a data class for an object, an enum class for an enum, and a
 * value class for a primitive or collection, at every position including JSON and multipart request bodies.
 * An alias chain `ChainOwner -> BareOwner -> Owner` is Owner's shape named `ChainOwner`, and an alias of a nullable
 * alias (`NullableChainOwner -> anyOf: [$ref Owner, null]`) is nullable wherever it is used.
 * `BareRefAliasRoundTripTest` decodes these goldens.
 */
val bareRefAliasSpec by testSuite {
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
                  "content": { "application/json": { "schema": { "$ref": "#/components/schemas/BareOwner" } } }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/BareOwner" } } }
                  }
                }
              }
            },
            "/uploads": {
              "post": {
                "operationId": "upload",
                "requestBody": {
                  "required": true,
                  "content": { "multipart/form-data": { "schema": { "$ref": "#/components/schemas/BareOwner" } } }
                },
                "responses": { "200": { "description": "OK" } }
              }
            },
            "/chain": {
              "put": {
                "operationId": "putChain",
                "requestBody": {
                  "required": true,
                  "content": { "application/json": { "schema": { "$ref": "#/components/schemas/ChainOwner" } } }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/NullableChainOwner" } } }
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
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/BareRefAliasHolder" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "BareRefAliasHolder": {
                "type": "object",
                "additionalProperties": false,
                "properties": {
                  "bareOwner": { "$ref": "#/components/schemas/BareOwner" },
                  "optionalBareOwner": { "$ref": "#/components/schemas/BareOwner" },
                  "bareOwners": { "type": "array", "items": { "$ref": "#/components/schemas/BareOwner" } },
                  "bareOwnerMap": { "type": "object", "additionalProperties": { "$ref": "#/components/schemas/BareOwner" } },
                  "bareColor": { "$ref": "#/components/schemas/BareColor" },
                  "bareLabel": { "$ref": "#/components/schemas/BareLabel" },
                  "bareNames": { "$ref": "#/components/schemas/BareNames" },
                  "chainOwner": { "$ref": "#/components/schemas/ChainOwner" },
                  "nullableChainOwner": { "$ref": "#/components/schemas/NullableChainOwner" },
                  "owner": { "$ref": "#/components/schemas/Owner" }
                },
                "required": [
                  "bareOwner",
                  "bareOwners",
                  "bareOwnerMap",
                  "bareColor",
                  "bareLabel",
                  "bareNames",
                  "chainOwner",
                  "nullableChainOwner",
                  "owner"
                ]
              },
              "BareOwner": { "$ref": "#/components/schemas/Owner" },
              "BareColor": { "$ref": "#/components/schemas/Color" },
              "BareLabel": { "$ref": "#/components/schemas/Label" },
              "BareNames": { "$ref": "#/components/schemas/Names" },
              "ChainOwner": { "$ref": "#/components/schemas/BareOwner" },
              "NullableChainOwner": { "$ref": "#/components/schemas/NullableOwner" },
              "NullableOwner": { "anyOf": [{ "$ref": "#/components/schemas/Owner" }, { "type": "null" }] },
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
        "client/bare-ref-alias"
    )
}
