package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * An `allOf` that extends a closed object (`additionalProperties: false`, as C# and Java generators emit for every
 * class) has the properties of both members (`ApiException`, tl-api.azurewebsites.net), and a closed object next to a
 * typed `additionalProperties` stays closed, without a map of additional properties (`Tagged`).
 * `AllOfClosedObjectRoundTripTest` decodes this golden.
 */
val allOfClosedObjectSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.0.3",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/errors": {
              "get": {
                "operationId": "getError",
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/ApiException" } } }
                  }
                }
              }
            },
            "/tags": {
              "get": {
                "operationId": "getTagged",
                "responses": {
                  "200": {
                    "description": "OK",
                    "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Tagged" } } }
                  }
                }
              }
            }
          },
          "components": {
            "schemas": {
              "Exception": {
                "type": "object",
                "properties": { "message": { "type": "string" } },
                "additionalProperties": false
              },
              "ApiException": {
                "allOf": [
                  { "$ref": "#/components/schemas/Exception" },
                  {
                    "type": "object",
                    "properties": { "statusCode": { "type": "integer", "format": "int32" } },
                    "required": ["statusCode"],
                    "additionalProperties": false
                  }
                ]
              },
              "Tagged": {
                "allOf": [
                  { "type": "object", "properties": { "tag": { "type": "string" } }, "additionalProperties": false },
                  { "type": "object", "additionalProperties": { "type": "string" } }
                ]
              }
            }
          }
        }
        """.trimIndent(),
        "client/allof-closed-object",
    )
}
