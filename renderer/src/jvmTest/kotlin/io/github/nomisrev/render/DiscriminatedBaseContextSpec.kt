package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.render

/**
 * A discriminated base whose cases alone lead to something `readOnly` (`ItemResult.item` is an `Item`, whose `id` is
 * `readOnly`; `Result` itself refers to nothing context-specific). The base is generated with its cases, so it has a
 * request and a response shape: the response is `ResultRead`, whose case has an `ItemRead`, and the request body is
 * `ResultWrite`, whose case has an `ItemWrite` without `id`. With one shape for both, the request body could send the
 * `readOnly` `id` (apple.com:sirikit-cloud-media reached its base both ways and generated two classes of one name).
 * `DiscriminatedBaseContextRuntimeTest` sends and receives both.
 */
val discriminatedBaseContextSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.0.3",
          "info": { "title": "Results", "version": "1" },
          "paths": {
            "/results": {
              "get": {
                "responses": { "200": { "description": "ok", "content": { "application/json": {
                  "schema": { "$ref": "#/components/schemas/Result" } } } } }
              },
              "post": {
                "requestBody": { "required": true, "content": { "application/json": {
                  "schema": { "$ref": "#/components/schemas/Result" } } } },
                "responses": { "204": { "description": "created" } }
              }
            }
          },
          "components": { "schemas": {
            "Item": {
              "type": "object",
              "required": ["name"],
              "properties": { "id": { "type": "string", "readOnly": true }, "name": { "type": "string" } }
            },
            "Result": {
              "type": "object",
              "required": ["class"],
              "properties": { "class": { "type": "string" } },
              "discriminator": { "propertyName": "class", "mapping": { "item": "#/components/schemas/ItemResult" } }
            },
            "ItemResult": {
              "allOf": [ { "$ref": "#/components/schemas/Result" },
                { "type": "object", "properties": { "item": { "$ref": "#/components/schemas/Item" } } } ]
            }
          } }
        }
        """.trimIndent(),
        "client/discriminated-base-context",
    ) { apiTree, config -> apiTree.render(config) }
}
