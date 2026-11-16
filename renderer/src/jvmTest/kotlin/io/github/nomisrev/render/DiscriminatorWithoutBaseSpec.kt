package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite

/**
 * A discriminator whose mapping lists the subtypes but not the base, the polymorphism example of the OpenAPI
 * specification (viator, twitter, microsoft graph, rebilly and just-eat): the sealed interface has the subtypes as its
 * only cases. The tag `feline` selects `Cat`, so an operation that returns `Cat` returns that case; `Dog` is not in the
 * mapping and is selected by its name; `Bird` extends the base and `Wings` and declares `canFly` next to its `allOf`.
 * `DiscriminatorWithoutBaseRuntimeTest` decodes each tag, and fails on one the mapping does not select.
 */
val discriminatorWithoutBaseSpec by testSuite {
    renderSpec(
        $$"""
        {
          "openapi": "3.0.3",
          "info": { "title": "Pets", "version": "1" },
          "paths": {
            "/pets": {
              "get": {
                "responses": { "200": { "description": "ok", "content": { "application/json": {
                  "schema": { "type": "array", "items": { "$ref": "#/components/schemas/Pet" } } } } } }
              }
            },
            "/cats/{id}": {
              "get": {
                "parameters": [ { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } } ],
                "responses": { "200": { "description": "ok", "content": { "application/json": {
                  "schema": { "$ref": "#/components/schemas/Cat" } } } } }
              }
            }
          },
          "components": { "schemas": {
            "Pet": {
              "type": "object",
              "required": ["petType", "name"],
              "properties": { "petType": { "type": "string" }, "name": { "type": "string" } },
              "discriminator": { "propertyName": "petType",
                "mapping": { "feline": "#/components/schemas/Cat", "bird": "#/components/schemas/Bird" } }
            },
            "Cat": { "allOf": [ { "$ref": "#/components/schemas/Pet" },
              { "type": "object", "properties": { "huntingSkill": { "type": "string" } } } ] },
            "Dog": { "allOf": [ { "$ref": "#/components/schemas/Pet" },
              { "type": "object", "properties": { "packSize": { "type": "integer" } } } ] },
            "Wings": { "type": "object", "required": ["span"], "properties": { "span": { "type": "number" } } },
            "Bird": {
              "allOf": [ { "$ref": "#/components/schemas/Pet" }, { "$ref": "#/components/schemas/Wings" } ],
              "properties": { "canFly": { "type": "boolean" } }
            }
          } }
        }
        """.trimIndent(),
        "client/discriminator-without-base",
    )
}
