package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * Two `allOf` members that declare one property with `$ref`s that do not extend one another (nexmo.com:reports,
 * beezup.com): the property is their intersection. One enum under two names is the later `$ref` (`ProductVoice`), an
 * enum next to a subset of it is the subset (`Level`), and two open objects are an inline object with the properties of
 * both (`Thing.Links`). `AllOfPropertyTwoRefsRoundTripTest` decodes this golden.
 */
val allOfPropertyTwoRefsSpec by testSuite {
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
                "allOf": [
                  { "$ref": "#/components/schemas/Base" },
                  {
                    "type": "object",
                    "properties": {
                      "product": { "$ref": "#/components/schemas/ProductVoice" },
                      "level": { "$ref": "#/components/schemas/Level" },
                      "links": { "$ref": "#/components/schemas/DetectedLinks" }
                    }
                  }
                ]
              },
              "Base": {
                "type": "object",
                "properties": {
                  "id": { "type": "string" },
                  "product": { "$ref": "#/components/schemas/Product" },
                  "level": { "$ref": "#/components/schemas/AnyLevel" },
                  "links": { "$ref": "#/components/schemas/ColumnLinks" }
                },
                "required": ["id"]
              },
              "Product": { "type": "string", "enum": ["SMS", "VOICE-CALL"], "example": "SMS" },
              "ProductVoice": { "type": "string", "enum": ["SMS", "VOICE-CALL"], "example": "VOICE-CALL" },
              "AnyLevel": { "type": "string", "enum": ["low", "high", "critical"] },
              "Level": { "type": "string", "enum": ["low", "high"] },
              "ColumnLinks": { "type": "object", "properties": { "rename": { "type": "string" } } },
              "DetectedLinks": {
                "type": "object",
                "properties": { "configure": { "type": "string" }, "ignore": { "type": "string" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/allof-property-two-refs",
    )
}
