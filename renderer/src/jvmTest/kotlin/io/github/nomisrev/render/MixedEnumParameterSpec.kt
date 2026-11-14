package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * An enum without a `type` whose members are of different JSON types (`[true, false, 0, 1]`) is a
 * `JsonElement` in a body or property. As a query, header, cookie or path parameter, or a form field, it is a
 * `JsonPrimitive`, sent as its `content` (`true`, `0`, `a`), which `MixedEnumParameterRuntimeTest` asserts on the wire.
 */
val mixedEnumParameterSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.0.3",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/sms/{mode}": {
              "post": {
                "operationId": "postSms",
                "parameters": [
                  { "$ref": "#/components/parameters/delivered" },
                  { "name": "mode", "in": "path", "required": true, "schema": { "enum": ["fast", 1] } },
                  { "name": "X-Delivered", "in": "header", "schema": { "enum": [true, 0] } },
                  { "name": "delivered-cookie", "in": "cookie", "schema": { "enum": [true, 0] } }
                ],
                "requestBody": {
                  "content": {
                    "application/x-www-form-urlencoded": {
                      "schema": {
                        "type": "object",
                        "required": ["flag"],
                        "properties": {
                          "flag": { "enum": [true, false, 0, 1] },
                          "label": { "enum": ["a", 1] }
                        }
                      }
                    }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            }
          },
          "components": {
            "parameters": {
              "delivered": {
                "name": "delivered",
                "in": "query",
                "required": true,
                "schema": { "enum": [true, false, 0, 1] }
              }
            }
          }
        }
        """.trimIndent(),
        "client/mixed-enum-parameter"
    )
}
