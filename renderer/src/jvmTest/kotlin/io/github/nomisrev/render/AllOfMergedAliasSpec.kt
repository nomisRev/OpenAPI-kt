package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A named component whose several `allOf` members merge to a collection, primitive or map, such as
 * `Tags: {allOf: [{type: array, items: {type: string}}, {minItems: 1}]}`, is generated like the component written
 * with the merged keywords directly: a value class named after the component (`Tags(items: List<String>)`), used at
 * every position (property, optional property, list item, map value, union member, request and response body). So
 * is a `$ref` member with a constraint (`NonEmptyNames`), and `nullable: true` on the component (`NullableTags`) or
 * on every member (`NullableCode`) makes the properties referencing it nullable.
 * `AllOfMergedAliasRoundTripTest` decodes these goldens.
 */
val allOfMergedAliasSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.0.3",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/aliases": {
              "put": {
                "operationId": "putTags",
                "requestBody": {
                  "required": true,
                  "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Tags" } } }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Code" } } }
                  }
                }
              }
            },
            "/labelled": {
              "post": {
                "operationId": "postLabels",
                "requestBody": {
                  "required": true,
                  "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Labels" } } }
                },
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/NonEmptyNames" } } }
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
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/MergedAliasHolder" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "MergedAliasHolder": {
                "type": "object",
                "additionalProperties": false,
                "properties": {
                  "tags": { "$ref": "#/components/schemas/Tags" },
                  "optionalTags": { "$ref": "#/components/schemas/Tags" },
                  "tagLists": { "type": "array", "items": { "$ref": "#/components/schemas/Tags" } },
                  "tagMap": { "type": "object", "additionalProperties": { "$ref": "#/components/schemas/Tags" } },
                  "code": { "$ref": "#/components/schemas/Code" },
                  "codes": { "type": "array", "items": { "$ref": "#/components/schemas/Code" } },
                  "labels": { "$ref": "#/components/schemas/Labels" },
                  "names": { "$ref": "#/components/schemas/NonEmptyNames" },
                  "nullableTags": { "$ref": "#/components/schemas/NullableTags" },
                  "nullableCode": { "$ref": "#/components/schemas/NullableCode" },
                  "tagsOrCount": { "$ref": "#/components/schemas/TagsOrCount" }
                },
                "required": [
                  "tags",
                  "tagLists",
                  "tagMap",
                  "code",
                  "codes",
                  "labels",
                  "names",
                  "nullableTags",
                  "nullableCode",
                  "tagsOrCount"
                ]
              },
              "Tags": { "allOf": [{ "type": "array", "items": { "type": "string" } }, { "minItems": 1 }] },
              "Code": { "allOf": [{ "type": "string" }, { "maxLength": 3 }] },
              "Labels": {
                "allOf": [{ "type": "object", "additionalProperties": { "type": "string" } }, { "minProperties": 1 }]
              },
              "NonEmptyNames": { "allOf": [{ "$ref": "#/components/schemas/Names" }, { "minItems": 1 }] },
              "NullableTags": {
                "allOf": [{ "type": "array", "items": { "type": "string" } }, { "minItems": 1 }],
                "nullable": true
              },
              "NullableCode": {
                "allOf": [{ "type": "string", "nullable": true }, { "maxLength": 3, "nullable": true }]
              },
              "TagsOrCount": { "oneOf": [{ "$ref": "#/components/schemas/Tags" }, { "type": "integer" }] },
              "Names": { "type": "array", "items": { "type": "string" } }
            }
          }
        }
        """.trimIndent(),
        "client/allof-merged-alias"
    )
}
