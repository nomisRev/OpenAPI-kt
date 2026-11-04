package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A request body is optional unless `requestBody.required` is `true` (OpenAPI 3.1.1, 4.8.13). A body whose fields are
 * arguments (a form, a multipart form, an inline JSON object) gets an overload without them that sends no body, next
 * to the overload that sends it, whose required fields stay required. `OptionalRequestBodiesRuntimeTest` checks what
 * each overload sends.
 */
val optionalRequestBodiesSpec by testSuite {
    clientTest(OPTIONAL_REQUEST_BODIES_API.trimIndent(), "client/optional-request-bodies")
}

private const val FIELDS = """
  "type": "object",
  "properties": { "name": { "type": "string" }, "note": { "type": "string" } },
  "required": ["name"]
"""

private const val MULTIPART_FIELDS = """
  "type": "object",
  "properties": { "file": { "type": "string", "format": "binary" }, "note": { "type": "string" } },
  "required": ["file"]
"""

private const val OPTIONAL_FIELDS = """
  "type": "object",
  "properties": { "name": { "type": "string" }, "note": { "type": "string" } }
"""

private const val OPTIONAL_REQUEST_BODIES_API = """
{
  "openapi": "3.1.0",
  "info": { "title": "Api", "version": "0.0.1" },
  "paths": {
    "/form": {
      "post": {
        "requestBody": { "content": { "application/x-www-form-urlencoded": { "schema": { $FIELDS } } } },
        "responses": { "204": { "description": "No Content" } }
      },
      "put": {
        "requestBody": {
          "required": true,
          "content": { "application/x-www-form-urlencoded": { "schema": { $FIELDS } } }
        },
        "responses": { "204": { "description": "No Content" } }
      }
    },
    "/form-optional-fields": {
      "post": {
        "parameters": [{ "name": "q", "in": "query", "schema": { "type": "string" } }],
        "requestBody": { "content": { "application/x-www-form-urlencoded": { "schema": { $OPTIONAL_FIELDS } } } },
        "responses": { "204": { "description": "No Content" } }
      }
    },
    "/json": {
      "post": {
        "requestBody": { "content": { "application/json": { "schema": { $FIELDS } } } },
        "responses": { "204": { "description": "No Content" } }
      },
      "put": {
        "requestBody": { "required": true, "content": { "application/json": { "schema": { $FIELDS } } } },
        "responses": { "204": { "description": "No Content" } }
      }
    },
    "/json-optional-fields": {
      "post": {
        "parameters": [{ "name": "q", "in": "query", "schema": { "type": "string" } }],
        "requestBody": { "content": { "application/json": { "schema": { $OPTIONAL_FIELDS } } } },
        "responses": { "204": { "description": "No Content" } }
      }
    },
    "/multipart": {
      "post": {
        "requestBody": { "content": { "multipart/form-data": { "schema": { $MULTIPART_FIELDS } } } },
        "responses": { "204": { "description": "No Content" } }
      },
      "put": {
        "requestBody": { "required": true, "content": { "multipart/form-data": { "schema": { $MULTIPART_FIELDS } } } },
        "responses": { "204": { "description": "No Content" } }
      }
    },
    "/form-or-multipart": {
      "post": {
        "requestBody": {
          "content": {
            "application/x-www-form-urlencoded": { "schema": { $FIELDS } },
            "multipart/form-data": { "schema": { $FIELDS } }
          }
        },
        "responses": { "204": { "description": "No Content" } }
      }
    }
  }
}
"""
