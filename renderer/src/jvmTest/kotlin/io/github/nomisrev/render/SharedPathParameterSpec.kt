package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * Operations below one path position share its navigation step (`api.items.id(id)`), though each declares the path
 * parameter itself. Declarations that differ only by validation keywords or a default are one parameter.
 * `SharedPathParameterRuntimeTest` checks the requests.
 */
val sharedPathParameterSpec by testSuite {
    renderSpec(
        """
        {
          "openapi": "3.0.3",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/items/{id}": {
              "get": {
                "parameters": [ { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/items/{id}/parts": {
              "get": {
                "parameters": [ {
                  "name": "id", "in": "path", "required": true,
                  "schema": { "type": "string", "minLength": 34, "maxLength": 34, "pattern": "^HA[0-9a-fA-F]{32}${'$'}" }
                } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/items/{id}/tags": {
              "get": {
                "parameters": [ {
                  "name": "id", "in": "path", "required": true, "schema": { "type": "string", "default": "x" }
                } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/pages/{number}": {
              "get": {
                "parameters": [ {
                  "name": "number", "in": "path", "required": true, "schema": { "type": "integer", "format": "int32" }
                } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/pages/{number}/lines": {
              "get": {
                "parameters": [ {
                  "name": "number", "in": "path", "required": true,
                  "schema": { "type": "integer", "format": "int32", "minimum": 1, "maximum": 99 }
                } ],
                "responses": { "204": { "description": "No Content" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/shared-path-parameter-validation",
    )
}
