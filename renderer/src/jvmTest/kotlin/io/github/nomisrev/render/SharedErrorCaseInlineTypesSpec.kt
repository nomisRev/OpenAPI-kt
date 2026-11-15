package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * An operation with several success media types has one method and one sealed result per media type, and an error
 * status is one class next to them that is a case of each (`BadRequest : JsonResponse, PdfResponse`). The types
 * declared inline in such an error are nested in that class, and are referenced there (`BadRequest.Error`), not under
 * the first result it is a case of. `SharedErrorCaseInlineTypesRuntimeTest` checks that the error is decoded from
 * every method.
 */
val sharedErrorCaseInlineTypesSpec by testSuite {
    renderSpec(
        """
        {
          "openapi": "3.0.3",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/certificate": {
              "post": {
                "responses": {
                  "200": {
                    "description": "The certificate",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "object",
                          "properties": { "id": { "type": "string" } },
                          "required": ["id"]
                        }
                      },
                      "application/pdf": {}
                    }
                  },
                  "400": {
                    "description": "Bad request",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "object",
                          "properties": {
                            "error": { "type": "string", "enum": ["missing_parameter", "invalid_parameter"] },
                            "error_description": { "type": "string", "enum": ["Bad request", "Invalid format"] },
                            "details": {
                              "type": "object",
                              "properties": {
                                "field": { "type": "string" },
                                "reason": { "type": "string", "enum": ["missing", "malformed"] }
                              },
                              "required": ["field"]
                            },
                            "causes": {
                              "type": "array",
                              "items": {
                                "type": "object",
                                "properties": { "code": { "type": "integer", "format": "int32" } },
                                "required": ["code"]
                              }
                            }
                          }
                        }
                      }
                    }
                  },
                  "404": {
                    "description": "Not found",
                    "content": {
                      "application/json": {
                        "schema": {
                          "type": "object",
                          "properties": {
                            "error": { "type": "string", "enum": ["record_not_found", "url_not_found"] }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/shared-error-case-inline-types",
    )
}
