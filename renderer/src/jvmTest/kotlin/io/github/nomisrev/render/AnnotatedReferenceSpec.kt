package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.render

/**
 * `readOnly` or `writeOnly` next to the `$ref` of a property leaves the property out of the request or the response
 * shape of the schema that has it (`Scan` and `ScanRead`). The schema it refers to is one class (`Status`), unless
 * its own content has two shapes. `AnnotatedReferenceRuntimeTest` checks what is sent and read.
 */
val annotatedReferenceSpec by testSuite {
    renderSpec(
        """
        {
          "openapi": "3.0.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/scans": {
              "get": {
                "responses": { "200": { "description": "ok",
                  "content": { "application/json": { "schema": { "${'$'}ref": "#/components/schemas/Scan" } } } } }
              },
              "post": {
                "requestBody": { "required": true,
                  "content": { "application/json": { "schema": { "${'$'}ref": "#/components/schemas/Scan" } } } },
                "responses": { "204": { "description": "ok" } }
              }
            },
            "/health": {
              "get": {
                "responses": { "200": { "description": "ok",
                  "content": { "application/json": { "schema": { "${'$'}ref": "#/components/schemas/Status" } } } } }
              }
            }
          },
          "components": { "schemas": {
            "Scan": { "type": "object", "required": ["name"], "properties": {
              "name": { "type": "string" },
              "status": { "${'$'}ref": "#/components/schemas/Status", "description": "Output only.", "readOnly": true },
              "secret": { "${'$'}ref": "#/components/schemas/Secret", "writeOnly": true } } },
            "Status": { "type": "object", "required": ["code"], "properties": {
              "code": { "type": "integer", "format": "int32" } } },
            "Secret": { "type": "object", "required": ["token"], "properties": { "token": { "type": "string" } } }
          } }
        }
        """.trimIndent(),
        "client/annotated-reference",
    ) { apiTree, config -> apiTree.render(config) }
}
