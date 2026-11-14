package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A property that is optional and nullable has three states on the wire: absent, `null`, a value. It is
 * `Optional<T>` (`Absent`, `Null`, `Value`) in a JSON body, also as a parameter of a flattened inline body. The
 * urlencoded form, the query, the header and the cookie have no null: there `Absent` and `Null` are left out.
 * `OptionalNullableWireRuntimeTest` checks the requests on the wire.
 */
val optionalNullableWireSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/items/{id}": {
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
                        "properties": {
                          "name": { "type": ["string", "null"] },
                          "description": { "type": ["string", "null"] },
                          "count": { "type": "integer", "format": "int32" },
                          "settings": {
                            "type": "object",
                            "properties": {
                              "color": { "type": ["string", "null"] }
                            }
                          }
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
                "requestBody": {
                  "required": true,
                  "content": {
                    "application/x-www-form-urlencoded": {
                      "schema": {
                        "type": "object",
                        "properties": {
                          "nickname": { "type": ["string", "null"] },
                          "filter": { "$ref": "#/components/schemas/Filter" }
                        }
                      },
                      "encoding": { "filter": { "style": "deepObject", "explode": true } }
                    }
                  }
                },
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/search": {
              "get": {
                "parameters": [
                  { "name": "filter", "in": "query", "schema": { "$ref": "#/components/schemas/Filter" } },
                  {
                    "name": "deep",
                    "in": "query",
                    "style": "deepObject",
                    "explode": true,
                    "schema": { "$ref": "#/components/schemas/Filter" }
                  },
                  { "name": "X-Filter", "in": "header", "schema": { "$ref": "#/components/schemas/Filter" } },
                  { "name": "filter_cookie", "in": "cookie", "schema": { "$ref": "#/components/schemas/Filter" } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            }
          },
          "components": {
            "schemas": {
              "Filter": {
                "type": "object",
                "properties": {
                  "q": { "type": ["string", "null"] },
                  "limit": { "type": "integer", "format": "int32" }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/optional-nullable-wire",
    )
}
