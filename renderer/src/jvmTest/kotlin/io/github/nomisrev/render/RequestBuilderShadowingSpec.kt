package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * The request lambda of an operation runs with an `HttpRequestBuilder` as its receiver, and a parameter of the
 * operation is a name closer than that receiver. Parameters named after what the generated request code relies on
 * (`url`, `headers`, `method`, `parameter`, `setBody`, ...) must not change what that code refers to.
 * `RequestBuilderShadowingRuntimeTest` checks the request on the wire.
 */
val requestBuilderShadowingSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/search": {
              "get": {
                "parameters": [
                  { "name": "url", "in": "query", "required": true, "schema": { "type": "string" } },
                  { "name": "x", "in": "query", "required": true, "schema": { "type": "integer", "format": "int32" } },
                  { "name": "parameter", "in": "query", "schema": { "type": "string" } },
                  { "name": "method", "in": "query", "schema": { "type": "string" } },
                  { "name": "headers", "in": "query", "schema": { "type": "string" } },
                  { "name": "header", "in": "header", "schema": { "type": "string" } },
                  { "name": "cookie", "in": "cookie", "schema": { "type": "string" } },
                  { "name": "append", "in": "query", "schema": { "type": "array", "items": { "type": "string" } } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/notes": {
              "post": {
                "parameters": [
                  { "name": "url", "in": "query", "required": true, "schema": { "type": "string" } },
                  { "name": "setBody", "in": "query", "schema": { "type": "string" } },
                  { "name": "contentType", "in": "query", "schema": { "type": "string" } },
                  { "name": "accept", "in": "query", "schema": { "type": "string" } }
                ],
                "requestBody": {
                  "required": true,
                  "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Note" } } }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/forms": {
              "post": {
                "parameters": [
                  { "name": "url", "in": "query", "required": true, "schema": { "type": "string" } },
                  { "name": "headers", "in": "header", "schema": { "type": "string" } }
                ],
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/x-www-form-urlencoded": {
                      "schema": {
                        "type": "object",
                        "properties": { "title": { "type": "string" }, "names": { "type": "string" } },
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
                  { "name": "url", "in": "query", "required": true, "schema": { "type": "string" } },
                  { "name": "formData", "in": "query", "schema": { "type": "string" } }
                ],
                "requestBody": {
                  "required": true,
                  "content": {
                    "multipart/form-data": {
                      "schema": {
                        "type": "object",
                        "properties": { "file": { "type": "string", "format": "binary" }, "append": { "type": "string" } },
                        "required": ["file"]
                      }
                    }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            }
          },
          "components": {
            "schemas": {
              "Note": {
                "type": "object",
                "properties": { "text": { "type": "string" } },
                "required": ["text"]
              }
            }
          }
        }
        """.trimIndent(),
        "client/request-builder-shadowing",
    )
}
