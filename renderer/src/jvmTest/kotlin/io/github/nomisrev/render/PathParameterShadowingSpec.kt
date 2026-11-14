package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A path parameter is a property of the operation class and the path template reads it, while the `invoke` function
 * of the operation may declare a parameter of the same Kotlin name: a query parameter, or the property of a flattened
 * inline body or form. The path must still be built from the path parameter.
 * `PathParameterShadowingRuntimeTest` checks the request on the wire.
 */
val pathParameterShadowingSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/accounts/{accountId}": {
              "get": {
                "parameters": [
                  { "name": "accountId", "in": "path", "required": true, "schema": { "type": "string" } },
                  { "name": "account_id", "in": "query", "required": true, "schema": { "type": "integer", "format": "int32" } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/users/{id}": {
              "get": {
                "parameters": [
                  { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } },
                  { "name": "id", "in": "query", "required": true, "schema": { "type": "string" } }
                ],
                "responses": { "204": { "description": "No Content" } }
              },
              "post": {
                "parameters": [
                  { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": {
                        "type": "object",
                        "properties": { "id": { "type": "string" }, "name": { "type": "string" } },
                        "required": ["id"]
                      }
                    }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              },
              "patch": {
                "parameters": [
                  { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": {
                      "schema": {
                        "type": "object",
                        "properties": { "id": { "type": "string" }, "name": { "type": "string" } }
                      }
                    }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/forms/{id}": {
              "post": {
                "parameters": [
                  { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/x-www-form-urlencoded": {
                      "schema": {
                        "type": "object",
                        "properties": { "id": { "type": "string" }, "title": { "type": "string" } },
                        "required": ["id"]
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
        "client/path-parameter-shadowing",
    )
}
