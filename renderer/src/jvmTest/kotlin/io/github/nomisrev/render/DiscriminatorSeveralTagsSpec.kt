package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A discriminator mapping that selects one schema under several tags (apple.com:sirikit-cloud-media selects a handler's
 * schema by each of its JSON-RPC `method` names): the schema is one case, `Invocation.AddMediaInvocation`, whose
 * `method` is an enum of its tags, so a value is written with the tag it was read with. `AddMediaInvocation` repeats
 * that part of the mapping in a discriminator of its own and declares `type: object` next to its `allOf`, as sirikit's
 * do, and is still a case of `Invocation`. `Animal` lists itself under two tags: its own case has the tag as a property
 * the same way. `DiscriminatorSeveralTagsRuntimeTest` decodes each tag and encodes it back.
 */
val discriminatorSeveralTagsSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.0.3",
          "info": { "title": "Media", "version": "1" },
          "paths": {
            "/invoke": {
              "post": {
                "requestBody": { "required": true, "content": { "application/json": {
                  "schema": { "$ref": "#/components/schemas/Invocation" } } } },
                "responses": { "200": { "description": "ok", "content": { "application/json": {
                  "schema": { "$ref": "#/components/schemas/Invocation" } } } } }
              }
            },
            "/add-media/{id}": {
              "get": {
                "parameters": [ { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "200": { "description": "ok", "content": { "application/json": {
                  "schema": { "$ref": "#/components/schemas/AddMediaInvocation" } } } } }
              }
            },
            "/animals": {
              "get": {
                "responses": { "200": { "description": "ok", "content": { "application/json": {
                  "schema": { "type": "array", "items": { "$ref": "#/components/schemas/Animal" } } } } } }
              }
            }
          },
          "components": { "schemas": {
            "Invocation": {
              "type": "object",
              "required": ["method"],
              "properties": { "method": { "type": "string" }, "session": { "type": "string" } },
              "discriminator": { "propertyName": "method", "mapping": {
                "AddMedia.confirm": "#/components/schemas/AddMediaInvocation",
                "AddMedia.handle": "#/components/schemas/AddMediaInvocation",
                "PlayMedia.handle": "#/components/schemas/PlayMediaInvocation" } }
            },
            "AddMediaInvocation": {
              "type": "object",
              "allOf": [ { "$ref": "#/components/schemas/Invocation" } ],
              "properties": {
                "method": { "type": "string", "enum": ["AddMedia.confirm", "AddMedia.handle"] },
                "item": { "type": "string" }
              },
              "discriminator": { "propertyName": "method", "mapping": {
                "AddMedia.confirm": "#/components/schemas/AddMediaInvocation",
                "AddMedia.handle": "#/components/schemas/AddMediaInvocation" } }
            },
            "PlayMediaInvocation": {
              "allOf": [ { "$ref": "#/components/schemas/Invocation" },
                { "type": "object", "properties": { "shuffle": { "type": "boolean" } } } ]
            },
            "Animal": {
              "type": "object",
              "required": ["kind"],
              "properties": { "kind": { "type": "string" }, "name": { "type": "string" } },
              "discriminator": { "propertyName": "kind", "mapping": {
                "animal": "#/components/schemas/Animal",
                "beast": "#/components/schemas/Animal",
                "bird": "#/components/schemas/Bird" } }
            },
            "Bird": { "allOf": [ { "$ref": "#/components/schemas/Animal" },
              { "type": "object", "properties": { "span": { "type": "number" } } } ] }
          } }
        }
        """.trimIndent(),
        "client/discriminator-several-tags",
    )
}
