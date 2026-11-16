package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * `additionalProperties: false` next to an `allOf` of a string enum (linqr.app's `Icon`), or in a member of one
 * (`Level`), constrains no value of the enum: the schema is the enum. `AllOfAdditionalPropertiesRoundTripTest` decodes
 * this golden.
 */
val allOfAdditionalPropertiesSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.0.3",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/things": {
              "get": {
                "operationId": "getThing",
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Thing" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "Thing": {
                "type": "object",
                "properties": {
                  "uri": {
                    "anyOf": [{ "$ref": "#/components/schemas/Icon" }, { "type": "string", "format": "uri" }]
                  },
                  "level": { "$ref": "#/components/schemas/Level" }
                }
              },
              "Icon": {
                "allOf": [{ "$ref": "#/components/schemas/Icons" }],
                "additionalProperties": false,
                "description": "A predefined icon."
              },
              "Icons": { "type": "string", "enum": ["icon://appstore", "icon://bitcoin"] },
              "Level": {
                "allOf": [{ "type": "string", "enum": ["low", "high"] }, { "additionalProperties": false }]
              }
            }
          }
        }
        """.trimIndent(),
        "client/allof-additional-properties",
    )
}
