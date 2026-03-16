package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

val clientSpec by testSuite {
    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/pets": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              }
            },
            "/users": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/simple-tree"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/repos/{owner}/{repo}": {
              "get": {
                "parameters": [
                  { "name": "owner", "in": "path", "required": true, "schema": { "type": "string" } },
                  { "name": "repo", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/nested-tree"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/repos/{owner}/{repo}/collaborators": {
              "get": {
                "parameters": [
                  { "name": "owner", "in": "path", "required": true, "schema": { "type": "string" } },
                  { "name": "repo", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            },
            "/users": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/mixed-segments"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              }
            },
            "/health": {
              "get": {
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/root-operations"
    )

    clientTest(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/a/b/{c}/d/{e}/f": {
              "get": {
                "parameters": [
                  { "name": "c", "in": "path", "required": true, "schema": { "type": "string" } },
                  { "name": "e", "in": "path", "required": true, "schema": { "type": "integer", "format": "int64" } }
                ],
                "responses": { "200": { "description": "OK" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/deep-nesting"
    )
}
