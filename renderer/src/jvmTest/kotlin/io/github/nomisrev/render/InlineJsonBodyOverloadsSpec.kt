package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * An operation with a single inline JSON object body takes its properties as arguments and keeps `Body` internal. Next
 * to another request body (a form, a multipart form, plain text, or a second JSON-like media type sharing its schema)
 * the JSON body is not flattened: its overload takes `Body` itself, so `Body` and the inline types nested in it are
 * public. `InlineJsonBodyOverloadsRuntimeTest` sends each body through that overload.
 */
val inlineJsonBodyOverloadsSpec by testSuite {
    clientTest(INLINE_JSON_BODY_OVERLOADS_API.trimIndent(), "client/inline-json-body-overloads")
}

private const val INLINE_JSON_BODY_OVERLOADS_API = """
{
  "openapi": "3.1.0",
  "info": { "title": "Api", "version": "0.0.1" },
  "paths": {
    "/form": {
      "post": {
        "requestBody": {
          "required": true,
          "content": {
            "application/json": {
              "schema": {
                "type": "object",
                "properties": {
                  "name": { "type": "string" },
                  "kind": { "type": "string", "enum": ["small", "large"] },
                  "address": {
                    "type": "object",
                    "properties": { "city": { "type": "string" } },
                    "required": ["city"]
                  }
                },
                "required": ["name", "kind"]
              }
            },
            "application/x-www-form-urlencoded": {
              "schema": {
                "type": "object",
                "properties": { "name": { "type": "string" } },
                "required": ["name"]
              }
            }
          }
        },
        "responses": { "204": { "description": "No Content" } }
      }
    },
    "/multipart": {
      "post": {
        "requestBody": {
          "required": true,
          "content": {
            "application/json": {
              "schema": {
                "type": "object",
                "properties": { "name": { "type": "string" } },
                "required": ["name"]
              }
            },
            "multipart/form-data": {
              "schema": {
                "type": "object",
                "properties": { "file": { "type": "string", "format": "binary" } },
                "required": ["file"]
              }
            }
          }
        },
        "responses": { "204": { "description": "No Content" } }
      }
    },
    "/text": {
      "post": {
        "requestBody": {
          "required": true,
          "content": {
            "application/json": {
              "schema": {
                "type": "object",
                "properties": { "name": { "type": "string" } },
                "required": ["name"]
              }
            },
            "text/plain": { "schema": { "type": "string" } }
          }
        },
        "responses": { "204": { "description": "No Content" } }
      }
    },
    "/patch": {
      "post": {
        "requestBody": {
          "required": true,
          "content": {
            "application/json": {
              "schema": {
                "type": "object",
                "properties": { "name": { "type": "string" } },
                "required": ["name"]
              }
            },
            "application/merge-patch+json": {
              "schema": {
                "type": "object",
                "properties": { "name": { "type": "string" } },
                "required": ["name"]
              }
            }
          }
        },
        "responses": { "204": { "description": "No Content" } }
      }
    },
    "/optional": {
      "post": {
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "type": "object",
                "properties": { "name": { "type": "string" } },
                "required": ["name"]
              }
            },
            "application/x-www-form-urlencoded": {
              "schema": {
                "type": "object",
                "properties": { "name": { "type": "string" } },
                "required": ["name"]
              }
            }
          }
        },
        "responses": { "204": { "description": "No Content" } }
      }
    }
  }
}
"""
