package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A response case is named after its status code: `Ok`, `NotFound` for the codes Ktor names, the IANA name for a
 * registered code Ktor lacks (`UnavailableForLegalReasons`), `MultiStatus` for the hyphenated 207, and `Status480`
 * for a code without any name. Two vendor codes of one operation must not share a case.
 * `StatusCodeCaseNamesRuntimeTest` checks the dispatch.
 */
val statusCodeCaseNamesSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.0.3",
          "info": { "title": "Status", "version": "1" },
          "paths": {
            "/ping": {
              "get": {
                "responses": {
                  "200": { "description": "ok", "content": { "application/json": { "schema": { "type": "string" } } } },
                  "480": { "description": "first vendor error", "content": { "application/json": { "schema": { "type": "object", "properties": { "a": { "type": "string" } } } } } },
                  "481": { "description": "second vendor error", "content": { "application/json": { "schema": { "type": "object", "properties": { "b": { "type": "string" } } } } } }
                }
              }
            },
            "/legal": {
              "get": {
                "responses": {
                  "200": { "description": "ok", "content": { "application/json": { "schema": { "type": "string" } } } },
                  "207": { "description": "multi", "content": { "application/json": { "schema": { "type": "object", "properties": { "items": { "type": "string" } } } } } },
                  "451": { "description": "legal", "content": { "application/json": { "schema": { "type": "object", "properties": { "reason": { "type": "string" } } } } } }
                }
              }
            },
            "/negotiated": {
              "get": {
                "responses": {
                  "200": {
                    "description": "ok",
                    "content": {
                      "application/json": { "schema": { "type": "object", "properties": { "id": { "type": "string" } } } },
                      "text/plain": { "schema": { "type": "string" } }
                    }
                  },
                  "482": {
                    "description": "vendor error",
                    "content": {
                      "application/json": { "schema": { "type": "object", "properties": { "code": { "type": "string" } } } },
                      "text/plain": { "schema": { "type": "string" } }
                    }
                  },
                  "483": {
                    "description": "other vendor error",
                    "content": {
                      "application/json": { "schema": { "type": "object", "properties": { "detail": { "type": "string" } } } },
                      "text/plain": { "schema": { "type": "string" } }
                    }
                  }
                }
              }
            },
            "/failing": {
              "get": {
                "responses": {
                  "204": { "description": "No Content" },
                  "404": { "description": "missing", "content": { "application/json": { "schema": { "type": "object", "properties": { "what": { "type": "string" } } } } } },
                  "484": { "description": "vendor error", "content": { "application/json": { "schema": { "type": "object", "properties": { "x": { "type": "string" } } } } } },
                  "485": { "description": "other vendor error" }
                }
              }
            }
          }
        }
        """.trimIndent(),
        "client/status-code-case-names",
    )
}
