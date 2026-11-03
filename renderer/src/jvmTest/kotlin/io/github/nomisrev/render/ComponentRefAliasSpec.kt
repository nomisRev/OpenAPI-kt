package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A request body, response or parameter component that is itself a `$ref` to another component of the same kind
 * (`AliasBody: {$ref: '#/components/requestBodies/OwnerBody'}`), or a chain of them, renders exactly like
 * referencing the final component directly: `/direct` and `/alias` generate the same operation, for an operation
 * parameter, a path item parameter, the request body and the response.
 * `ComponentRefAliasRoundTripTest` sends and receives through these goldens.
 */
val componentRefAliasSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/direct": {
              "parameters": [{ "$ref": "#/components/parameters/TraceId" }],
              "put": {
                "parameters": [{ "$ref": "#/components/parameters/Limit" }],
                "requestBody": { "$ref": "#/components/requestBodies/OwnerBody" },
                "responses": { "200": { "$ref": "#/components/responses/OwnerResponse" } }
              }
            },
            "/alias": {
              "parameters": [{ "$ref": "#/components/parameters/ChainTraceId" }],
              "put": {
                "parameters": [{ "$ref": "#/components/parameters/ChainLimit" }],
                "requestBody": { "$ref": "#/components/requestBodies/ChainBody" },
                "responses": { "200": { "$ref": "#/components/responses/ChainResponse" } }
              }
            }
          },
          "components": {
            "schemas": {
              "Owner": {
                "type": "object",
                "additionalProperties": false,
                "properties": { "name": { "type": "string" }, "nickname": { "type": "string" } },
                "required": ["name"]
              }
            },
            "parameters": {
              "TraceId": { "name": "X-Trace-Id", "in": "header", "required": true, "schema": { "type": "string" } },
              "AliasTraceId": { "$ref": "#/components/parameters/TraceId" },
              "ChainTraceId": { "$ref": "#/components/parameters/AliasTraceId" },
              "Limit": { "name": "limit", "in": "query", "required": true, "schema": { "type": "integer", "format": "int32" } },
              "AliasLimit": { "$ref": "#/components/parameters/Limit" },
              "ChainLimit": { "$ref": "#/components/parameters/AliasLimit" }
            },
            "requestBodies": {
              "OwnerBody": {
                "required": true,
                "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Owner" } } }
              },
              "AliasBody": { "$ref": "#/components/requestBodies/OwnerBody" },
              "ChainBody": { "$ref": "#/components/requestBodies/AliasBody" }
            },
            "responses": {
              "OwnerResponse": {
                "description": "OK",
                "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Owner" } } }
              },
              "AliasResponse": { "$ref": "#/components/responses/OwnerResponse" },
              "ChainResponse": { "$ref": "#/components/responses/AliasResponse" }
            }
          }
        }
        """.trimIndent(),
        "client/component-ref-alias"
    )
}
