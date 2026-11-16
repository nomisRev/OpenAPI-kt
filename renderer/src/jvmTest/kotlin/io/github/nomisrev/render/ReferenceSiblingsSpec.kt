package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.render

private const val D = "$"

private fun ref(name: String, siblings: String = "") =
    """{ "${D}ref": "#/components/schemas/$name"${if (siblings.isEmpty()) "" else ", $siblings"} }"""

/**
 * Keywords next to a `$ref` in an OpenAPI 3.1 document: `required` on an `allOf` member makes `TaxComponent.name`
 * required, `type: [object, null]` and `nullable: true` make `maybe` and `nullableRef` nullable `Amount`s (optional and
 * nullable, so `Optional`), `required` next to the `$ref` of `strict` is an inline merge, and `description`,
 * `deprecated`, `default` and a contradicting `type` change nothing.
 */
internal val referenceSiblingsSpec = """
    { "openapi": "3.1.0", "info": { "title": "Api", "version": "1" },
      "paths": {
        "/tax": { "get": { "responses": { "200": { "description": "ok",
          "content": { "application/json": { "schema": ${ref("TaxComponent")} } } } } } },
        "/check": { "post": {
          "requestBody": { "required": true, "content": { "application/json": { "schema": ${ref("Request")} } } },
          "responses": { "204": { "description": "ok" } } } }
      },
      "components": { "schemas": {
        "Dates": { "type": "object", "properties": { "modifiedDate": { "type": "string" }, "name": { "type": "string" } } },
        "Amount": { "type": "object", "properties": { "value": { "type": "integer" } } },
        "TaxComponent": { "allOf": [
          { "type": "object", "properties": { "rate": { "type": "number" } } },
          ${ref("Dates", """"required": ["name"]""")} ] },
        "Request": { "type": "object", "required": ["amount", "strict"], "properties": {
          "amount": ${ref("Amount", """"description": "The amount to check." """)},
          "previous": ${ref("Amount", """"description": "Previous amount", "deprecated": true, "default": { "value": 0 }""")},
          "maybe": ${ref("Amount", """"type": ["object", "null"]""")},
          "nullableRef": ${ref("Amount", """"nullable": true""")},
          "wrongType": ${ref("Amount", """"type": "string"""")},
          "strict": ${ref("Amount", """"required": ["value"]""")} } }
      } } }
""".trimIndent()

val referenceSiblingsRenderSpec by testSuite {
    renderSpec(referenceSiblingsSpec, "client/reference-siblings") { apiTree, config -> apiTree.render(config) }
}
