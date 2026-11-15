package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A path step can have the name of an operation of its parent: `/standards/get` next to `GET /standards`. The two are
 * one member of the parent, which is called for the operation (`api.standards.get()`) and navigated for the step
 * (`api.standards.get.post()`), so both read as their paths do and neither is renamed.
 * `OperationNamedStepRuntimeTest` checks the requests.
 */
val operationNamedStepSpec by testSuite {
    renderSpec(
        """
        {
          "openapi": "3.0.3",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/standards": {
              "get": {
                "parameters": [ { "name": "limit", "in": "query", "schema": { "type": "integer", "format": "int32" } } ],
                "responses": { "200": { "description": "OK", "content": { "application/json": { "schema": {
                  "type": "object", "required": ["count"], "properties": { "count": { "type": "integer", "format": "int32" } }
                } } } } }
              }
            },
            "/standards/get": {
              "post": {
                "requestBody": { "required": true, "content": { "application/json": { "schema": {
                  "type": "object", "required": ["ids"], "properties": { "ids": { "type": "array", "items": { "type": "string" } } }
                } } } },
                "responses": { "200": { "description": "OK", "content": { "application/json": { "schema": {
                  "type": "object", "required": ["names"], "properties": { "names": { "type": "array", "items": { "type": "string" } } }
                } } } } }
              }
            },
            "/members": {
              "get": { "responses": { "204": { "description": "No Content" } } },
              "post": { "responses": { "204": { "description": "No Content" } } }
            },
            "/members/get": {
              "post": { "responses": { "204": { "description": "No Content" } } }
            },
            "/members/delete": {
              "post": { "responses": { "204": { "description": "No Content" } } }
            },
            "/tokens": {
              "get": { "responses": { "204": { "description": "No Content" } } }
            },
            "/tokens/get/{uuid}": {
              "get": {
                "parameters": [ { "name": "uuid", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/detector/{detectorId}/member": {
              "get": {
                "parameters": [ { "name": "detectorId", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/detector/{detectorId}/member/get": {
              "get": {
                "parameters": [ { "name": "detectorId", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              },
              "post": {
                "parameters": [ { "name": "detectorId", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "204": { "description": "No Content" } }
              }
            },
            "/docs": {
              "get": {
                "responses": { "200": { "description": "OK", "content": {
                  "application/json": { "schema": { "type": "string" } },
                  "text/plain": { "schema": { "type": "string" } }
                } } }
              }
            },
            "/docs/get": {
              "delete": { "responses": { "204": { "description": "No Content" } } }
            }
          }
        }
        """.trimIndent(),
        "client/operation-named-step",
    )
}
