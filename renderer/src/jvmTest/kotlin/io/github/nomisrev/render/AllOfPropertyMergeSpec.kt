package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A property that several `allOf` members declare must satisfy every declaration, so it renders as their
 * intersection, whichever member lists which declaration: non-null unless every declaration permits `null`, the
 * `format` and `enum` of any declaration, the `$ref` when the other declaration only annotates or restates its type,
 * the more specific of two `$ref`s (`Dog` extends `Pet`), and an inline object for a `$ref` object that another
 * declaration adds properties to. `AllOfPropertyMergeRoundTripTest` decodes these goldens.
 */
val allOfPropertyMergeSpec by testSuite {
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
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/PropertyMerge" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "PropertyMerge": {
                "allOf": [
                  {
                    "type": "object",
                    "properties": {
                      "nullableThenNonNull": { "type": "string", "nullable": true },
                      "nonNullThenNullable": { "type": "string" },
                      "bothNullable": { "type": "string", "nullable": true },
                      "createdAt": { "type": "string", "format": "date-time" },
                      "status": { "type": "string", "enum": ["active", "inactive", "banned"], "nullable": true },
                      "pet": { "$ref": "#/components/schemas/Dog" },
                      "owner": { "$ref": "#/components/schemas/NullableOwner" },
                      "name": { "$ref": "#/components/schemas/MyString" },
                      "ownerWithExtra": { "$ref": "#/components/schemas/Owner" },
                      "label": { "type": "string" }
                    },
                    "required": [
                      "nullableThenNonNull", "nonNullThenNullable", "bothNullable", "createdAt", "status", "pet",
                      "owner", "name", "ownerWithExtra"
                    ]
                  },
                  {
                    "type": "object",
                    "properties": {
                      "nullableThenNonNull": { "type": "string", "maxLength": 10 },
                      "nonNullThenNullable": { "type": "string", "nullable": true },
                      "bothNullable": { "type": "string", "nullable": true, "maxLength": 10 },
                      "createdAt": { "type": "string" },
                      "status": { "type": "string", "enum": ["active", "inactive"] },
                      "pet": { "$ref": "#/components/schemas/Pet" },
                      "owner": { "description": "The owner, if any" },
                      "name": { "type": "string", "maxLength": 20 },
                      "ownerWithExtra": { "type": "object", "properties": { "extra": { "type": "string" } } },
                      "label": { "$ref": "#/components/schemas/NullableString" }
                    }
                  }
                ]
              },
              "Pet": {
                "type": "object",
                "properties": { "id": { "type": "integer" }, "name": { "type": "string" } },
                "required": ["id"]
              },
              "Dog": {
                "allOf": [
                  { "$ref": "#/components/schemas/Pet" },
                  { "type": "object", "properties": { "bark": { "type": "boolean" } } }
                ]
              },
              "Owner": {
                "type": "object",
                "properties": { "id": { "type": "integer" }, "name": { "type": "string" } },
                "required": ["id"]
              },
              "NullableOwner": {
                "type": "object",
                "nullable": true,
                "properties": { "id": { "type": "integer" }, "name": { "type": "string" } },
                "required": ["id"]
              },
              "MyString": { "type": "string" },
              "NullableString": { "type": "string", "nullable": true }
            }
          }
        }
        """.trimIndent(),
        "client/allof-property-merge"
    )
}
