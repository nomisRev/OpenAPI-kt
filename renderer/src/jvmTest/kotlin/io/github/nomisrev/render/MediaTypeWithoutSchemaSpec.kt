package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A media type declared without `schema` is typed by the media type alone. As a response, JSON is any JSON
 * (`JsonElement`), text is a `String`, and anything else is Ktor's `HttpResponse`, passed through unread so the caller
 * chooses how to read it. As a request body, JSON and text take a `JsonElement` and a `String`, and anything else is
 * built by the caller in a request builder block. `MediaTypeWithoutSchemaRuntimeTest` checks the requests and
 * responses on the wire.
 */
val mediaTypeWithoutSchemaSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.0.3",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/certificate": {
              "get": {
                "responses": {
                  "200": {
                    "description": "The certificate as a PDF",
                    "content": { "application/pdf": { "example": "pdf bytes" } }
                  }
                }
              }
            },
            "/report": {
              "get": {
                "responses": {
                  "200": {
                    "description": "The report as a PDF",
                    "content": { "application/pdf": {} }
                  },
                  "404": {
                    "description": "Not found",
                    "content": {
                      "application/json": { "schema": { "$ref": "#/components/schemas/Problem" } }
                    }
                  }
                }
              }
            },
            "/data": {
              "get": {
                "responses": {
                  "200": {
                    "description": "Any JSON",
                    "content": {
                      "application/json": { "examples": { "one": { "value": { "id": 1 } } } }
                    }
                  }
                }
              }
            },
            "/note": {
              "get": {
                "responses": {
                  "200": { "description": "Plain text", "content": { "text/plain": {} } }
                }
              }
            },
            "/picture": {
              "get": {
                "responses": {
                  "200": { "description": "Any image", "content": { "image/*": {} } }
                }
              }
            },
            "/docs": {
              "get": {
                "responses": {
                  "200": {
                    "description": "A name, or the document",
                    "content": {
                      "application/json": { "schema": { "type": "string" } },
                      "application/pdf": {}
                    }
                  }
                }
              }
            },
            "/upload": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": { "application/pdf": {} }
                },
                "responses": { "204": { "description": "Stored" } }
              }
            },
            "/upload-optional": {
              "post": {
                "requestBody": {
                  "content": { "application/pdf": { "example": "pdf bytes" } }
                },
                "responses": { "204": { "description": "Stored" } }
              }
            },
            "/upload-pair": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/json": { "schema": { "type": "string" } },
                    "application/pdf": {}
                  }
                },
                "responses": { "204": { "description": "Stored" } }
              }
            },
            "/upload-pair-optional": {
              "post": {
                "requestBody": {
                  "content": {
                    "application/json": { "schema": { "type": "string" } },
                    "application/pdf": {}
                  }
                },
                "responses": { "204": { "description": "Stored" } }
              }
            },
            "/upload-image": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": { "image/*": {} }
                },
                "responses": { "204": { "description": "Stored" } }
              }
            },
            "/upload-any": {
              "post": {
                "parameters": [
                  { "name": "block", "in": "query", "schema": { "type": "string" } }
                ],
                "requestBody": {
                  "content": { "application/pdf": {}, "image/png": {} }
                },
                "responses": { "204": { "description": "Stored" } }
              }
            },
            "/echo": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": { "application/json": { "examples": { "one": { "value": { "id": 1 } } } } }
                },
                "responses": { "204": { "description": "Stored" } }
              }
            },
            "/echo-text": {
              "post": {
                "requestBody": {
                  "required": true,
                  "content": { "text/plain": {} }
                },
                "responses": { "204": { "description": "Stored" } }
              }
            }
          },
          "components": {
            "schemas": {
              "Problem": {
                "type": "object",
                "properties": { "title": { "type": "string" } },
                "required": ["title"]
              }
            }
          }
        }
        """.trimIndent(),
        "client/media-type-without-schema",
    )
}
