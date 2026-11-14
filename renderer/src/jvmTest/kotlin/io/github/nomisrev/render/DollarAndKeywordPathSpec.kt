package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A `$` in a literal path segment (`$count`, `$links`) is text of the path, not a Kotlin string template, and a path
 * parameter named like a Kotlin keyword (`object`, `package`, `in`) is read and passed escaped.
 * `DollarAndKeywordPathRuntimeTest` checks the request path on the wire.
 */
val dollarAndKeywordPathSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/members/$count": {
              "get": { "responses": { "204": { "description": "No Content" } } }
            },
            "/users/{id}/$links": {
              "get": {
                "parameters": [
                  { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/storage/{object}": {
              "get": {
                "parameters": [
                  { "name": "object", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/packages/{package}/in/{in}": {
              "get": {
                "parameters": [
                  { "name": "package", "in": "path", "required": true, "schema": { "type": "string" } },
                  { "name": "in", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/dollar-and-keyword-paths",
    )
}
