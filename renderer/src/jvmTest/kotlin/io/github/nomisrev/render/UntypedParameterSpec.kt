package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A schema without a `type` (`{}`, `items: {}`) admits any JSON value, but a query, header, cookie or path parameter
 * and a form field is a string on the wire. Such a value is a `JsonPrimitive` and is sent as its `content`; a
 * free-form object (`type: object` without properties) is a map of them. `UntypedParameterRuntimeTest` asserts the
 * wire.
 */
val untypedParameterSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.0.3",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/lookup/{key}": {
              "get": {
                "operationId": "lookup",
                "parameters": [
                  { "name": "key", "in": "path", "required": true, "schema": {} },
                  { "name": "any", "in": "query", "schema": {} },
                  { "name": "required", "in": "query", "required": true, "schema": {} },
                  { "name": "ids", "in": "query", "explode": false, "schema": { "type": "array", "items": {} } },
                  { "name": "tags", "in": "query", "schema": { "type": "array", "items": {} } },
                  { "name": "AddOns", "in": "query", "schema": { "type": "object" } },
                  { "name": "Flat", "in": "query", "explode": false, "schema": { "type": "object" } },
                  { "name": "X-Any", "in": "header", "schema": {} },
                  { "name": "X-Ids", "in": "header", "schema": { "type": "array", "items": {} } },
                  { "name": "any-cookie", "in": "cookie", "schema": {} }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/engagements": {
              "post": {
                "operationId": "createEngagement",
                "requestBody": {
                  "content": {
                    "application/x-www-form-urlencoded": {
                      "schema": {
                        "type": "object",
                        "required": ["To"],
                        "properties": {
                          "To": { "type": "string" },
                          "Parameters": { "description": "A JSON string" },
                          "Tags": { "type": "array", "items": {} }
                        }
                      }
                    }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/untyped-parameter"
    )
}
