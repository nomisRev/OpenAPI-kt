package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * The inputs of one operation (its parameters, the properties of a flattened body, the form fields, `body`) are
 * arguments of one `invoke` function, so each needs a Kotlin name of its own; the name of the wire never changes.
 * `ParameterNameCollisionRuntimeTest` checks the requests on the wire.
 */
val parameterNameCollisionSpec by testSuite {
    renderSpec(parameterNameCollisionJson, "client/parameter-name-collisions")
}

internal val parameterNameCollisionJson: String =
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/albums": {
              "put": {
                "parameters": [
                  { "name": "ids", "in": "query", "required": true, "schema": { "type": "string" } }
                ],
                "requestBody": {
                  "content": {
                    "application/json": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "ids": { "type": "array", "items": { "type": "string" } },
                          "position": { "type": "integer", "format": "int32" }
                        }
                      }
                    }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/forms": {
              "post": {
                "parameters": [
                  { "name": "title", "in": "query", "required": true, "schema": { "type": "string" } }
                ],
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/x-www-form-urlencoded": {
                      "schema": {
                        "type": "object",
                        "properties": { "title": { "type": "string" }, "note": { "type": "string" } },
                        "required": ["title"]
                      }
                    }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/uploads": {
              "post": {
                "parameters": [
                  { "name": "name", "in": "query", "required": true, "schema": { "type": "string" } }
                ],
                "requestBody": {
                  "required": true,
                  "content": {
                    "multipart/form-data": {
                      "schema": {
                        "type": "object",
                        "properties": { "name": { "type": "string" }, "file": { "type": "string", "format": "binary" } },
                        "required": ["name", "file"]
                      }
                    }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/prices": {
              "get": {
                "parameters": [
                  { "name": "ValidUntilDate", "in": "query", "schema": { "type": "string" } },
                  { "name": "ValidUntilDate<", "in": "query", "schema": { "type": "string" } },
                  { "name": "ValidUntilDate>", "in": "query", "schema": { "type": "string" } },
                  { "name": "amount>=", "in": "query", "schema": { "type": "integer", "format": "int32" } },
                  { "name": "amount<=", "in": "query", "schema": { "type": "integer", "format": "int32" } },
                  { "name": "amount", "in": "query", "schema": { "type": "integer", "format": "int32" } },
                  { "name": "status!", "in": "query", "schema": { "type": "string" } },
                  { "name": "status", "in": "query", "schema": { "type": "string" } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/segments": {
              "get": {
                "parameters": [
                  { "name": "x[]", "in": "query", "schema": { "type": "array", "items": { "type": "string" } } },
                  { "name": "x", "in": "query", "schema": { "type": "string" } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/things/{id}": {
              "get": {
                "parameters": [
                  { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } },
                  { "name": "id", "in": "query", "required": true, "schema": { "type": "string" } },
                  { "name": "id", "in": "header", "schema": { "type": "string" } },
                  { "name": "id", "in": "cookie", "schema": { "type": "string" } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/limits": {
              "get": {
                "parameters": [
                  { "name": "maxResults", "in": "query", "schema": { "type": "integer", "format": "int64" } },
                  { "name": "MaxResults", "in": "query", "schema": { "type": "string", "default": "all" } },
                  { "name": "max_results", "in": "query", "schema": { "type": "boolean" } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/reports": {
              "get": {
                "parameters": [
                  { "name": "client", "in": "query", "required": true, "schema": { "type": "string" } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/variants": {
              "post": {
                "parameters": [
                  { "name": "requestType", "in": "query", "schema": { "type": "string" } }
                ],
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": { "schema": { "type": "array", "items": { "type": "string" } } },
                    "application/xml": { "schema": { "type": "array", "items": { "type": "string" } } }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/echo": {
              "post": {
                "parameters": [
                  { "name": "body", "in": "query", "required": true, "schema": { "type": "string" } },
                  { "name": "response", "in": "query", "schema": { "type": "string" } }
                ],
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": { "schema": { "type": "array", "items": { "type": "string" } } }
                  }
                },
                "responses": { "200": { "description": "ok", "content": { "text/plain": { "schema": { "type": "string" } } } } }
              }
            }
          }
        }
        """.trimIndent()
