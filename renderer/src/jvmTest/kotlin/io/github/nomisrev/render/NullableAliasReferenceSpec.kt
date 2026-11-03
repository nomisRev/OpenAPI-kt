package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A named component that only allows `null` next to another schema is a nullable alias, however it is
 * spelled: `anyOf`/`oneOf: [X, {type: null}]` (in any order) or OpenAPI 3.0's `nullable: true` (also
 * next to `allOf: [{$ref: X}]`); `type: [X, "null"]` is covered by `object/type-array-nullable`.
 * A property, array item or map value referencing it must render nullable (`Alias?`), or `null` on
 * the wire fails to decode. `NullableAliasReferenceRoundTripTest` decodes these goldens.
 */
val nullableAliasReferenceSpec by testSuite {
    modelTest(
        """
        |"NullableAliasHolder": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": {
        |    "anyOfAlias": { "${'$'}ref": "#/components/schemas/AnyOfAlias" },
        |    "nullFirstAnyOfAlias": { "${'$'}ref": "#/components/schemas/NullFirstAnyOfAlias" },
        |    "oneOfAlias": { "${'$'}ref": "#/components/schemas/OneOfAlias" },
        |    "nullableAlias": { "${'$'}ref": "#/components/schemas/NullableAlias" },
        |    "allOfNullableAlias": { "${'$'}ref": "#/components/schemas/AllOfNullableAlias" },
        |    "optionalAnyOfAlias": { "${'$'}ref": "#/components/schemas/AnyOfAlias" },
        |    "anyOfAliases": { "type": "array", "items": { "${'$'}ref": "#/components/schemas/AnyOfAlias" } },
        |    "anyOfAliasMap": { "type": "object", "additionalProperties": { "${'$'}ref": "#/components/schemas/AnyOfAlias" } }
        |  },
        |  "required": [
        |    "anyOfAlias",
        |    "nullFirstAnyOfAlias",
        |    "oneOfAlias",
        |    "nullableAlias",
        |    "allOfNullableAlias",
        |    "anyOfAliases",
        |    "anyOfAliasMap"
        |  ]
        |},
        |"AnyOfAlias": { "anyOf": [{ "type": "string" }, { "type": "null" }] },
        |"NullFirstAnyOfAlias": { "anyOf": [{ "type": "null" }, { "type": "string" }] },
        |"OneOfAlias": { "oneOf": [{ "type": "string" }, { "type": "null" }] },
        |"NullableAlias": { "type": "string", "nullable": true },
        |"AllOfNullableAlias": { "allOf": [{ "${'$'}ref": "#/components/schemas/Owner" }], "nullable": true },
        |"Owner": {
        |  "type": "object",
        |  "additionalProperties": false,
        |  "properties": { "name": { "type": "string" } },
        |  "required": ["name"]
        |}
        """.trimMargin(),
        "object/nullable-alias-reference"
    )

    // A named nullable alias of a referenced object, `anyOf`/`oneOf: [{$ref: Owner}, {type: null}]` (in any order),
    // is its own type like the non-null `allOf: [{$ref: Owner}]` alias: a copy of `Owner` named after the alias, used
    // as `Alias?` as a property, array item, map value, request body and response body. It must not generate `Owner`
    // a second time, nor leave references to the alias pointing at a type that was never generated.
    // `NullableRefAliasRoundTripTest` decodes these goldens.
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
                  "content": { "application/json": { "schema": { "$ref": "#/components/schemas/AnyOfRefAlias" } } }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/OneOfRefAlias" } } }
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
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/RefAliasHolder" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "RefAliasHolder": {
                "type": "object",
                "additionalProperties": false,
                "properties": {
                  "anyOfRefAlias": { "$ref": "#/components/schemas/AnyOfRefAlias" },
                  "nullFirstAnyOfRefAlias": { "$ref": "#/components/schemas/NullFirstAnyOfRefAlias" },
                  "oneOfRefAlias": { "$ref": "#/components/schemas/OneOfRefAlias" },
                  "nullFirstOneOfRefAlias": { "$ref": "#/components/schemas/NullFirstOneOfRefAlias" },
                  "optionalAnyOfRefAlias": { "$ref": "#/components/schemas/AnyOfRefAlias" },
                  "anyOfRefAliases": { "type": "array", "items": { "$ref": "#/components/schemas/AnyOfRefAlias" } },
                  "anyOfRefAliasMap": { "type": "object", "additionalProperties": { "$ref": "#/components/schemas/AnyOfRefAlias" } },
                  "owner": { "$ref": "#/components/schemas/Owner" }
                },
                "required": [
                  "anyOfRefAlias",
                  "nullFirstAnyOfRefAlias",
                  "oneOfRefAlias",
                  "nullFirstOneOfRefAlias",
                  "anyOfRefAliases",
                  "anyOfRefAliasMap",
                  "owner"
                ]
              },
              "AnyOfRefAlias": { "anyOf": [{ "$ref": "#/components/schemas/Owner" }, { "type": "null" }] },
              "NullFirstAnyOfRefAlias": { "anyOf": [{ "type": "null" }, { "$ref": "#/components/schemas/Owner" }] },
              "OneOfRefAlias": { "oneOf": [{ "$ref": "#/components/schemas/Owner" }, { "type": "null" }] },
              "NullFirstOneOfRefAlias": { "oneOf": [{ "type": "null" }, { "$ref": "#/components/schemas/Owner" }] },
              "Owner": {
                "type": "object",
                "additionalProperties": false,
                "properties": { "name": { "type": "string" } },
                "required": ["name"]
              }
            }
          }
        }
        """.trimIndent(),
        "client/nullable-ref-alias"
    )
}
