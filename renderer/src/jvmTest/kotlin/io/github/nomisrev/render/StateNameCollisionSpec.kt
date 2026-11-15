package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A literal path segment, or a path parameter, named like the private state of a generated class (`client`, or the
 * name of a path parameter above it) is still reachable through its own public member.
 * `StateNameCollisionRuntimeTest` checks the request on the wire.
 */
val stateNameCollisionSpec by testSuite {
    renderSpec(
        """
        {
          "openapi": "3.0.3",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/client/register": {
              "post": { "responses": { "204": { "description": "No Content" } } }
            },
            "/realms/client": {
              "get": { "responses": { "204": { "description": "No Content" } } }
            },
            "/{realm}/roles/realm": {
              "get": {
                "parameters": [
                  { "name": "realm", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/{client}/things": {
              "get": {
                "parameters": [
                  { "name": "client", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/state-name-collision",
    )
    renderSpec(
        """
        {
          "openapi": "3.0.3",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/{client}/things": {
              "get": {
                "parameters": [
                  { "name": "client", "in": "path", "required": true, "schema": { "type": "string" } }
                ],
                "responses": { "204": { "description": "No Content" } }
              }
            }
          }
        }
        """.trimIndent(),
        "client/client-path-parameter",
    )
}
