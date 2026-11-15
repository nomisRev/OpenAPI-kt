package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.render

private const val D = "$"

private fun getOp(schema: String) =
    """"get": { "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": $schema } } } } }"""

private fun postOp(schema: String) =
    """"post": { "requestBody": { "required": true, "content": { "application/json": { "schema": $schema } } }, "responses": { "204": { "description": "ok" } } }"""

private fun pathOf(path: String, vararg operations: String) = """"$path": { ${operations.joinToString(", ")} }"""

private fun ref(name: String) = """{ "${D}ref": "#/components/schemas/$name" }"""

private fun spec(paths: String, schemas: String) = """
    { "openapi": "3.0.3", "info": { "title": "Api", "version": "0.0.1" },
      "paths": { $paths },
      "components": { "schemas": { $schemas } } }
""".trimIndent()

private const val PERSON = """"Person": { "type": "object", "properties": {
    "name": { "type": "string" },
    "id": { "type": "string", "readOnly": true },
    "password": { "type": "string", "writeOnly": true } } }"""

private val SUMMARY = """"Summary": { "type": "object", "properties": { "owner": ${ref("Person")} } }"""

/**
 * The context a schema is used in flows down through inline schemas and `$ref`s, so that one component never gets a
 * third, context-free class and the variants of a name that are equal are one class.
 * `ContextVariantsRuntimeTest` checks what is sent and read.
 */
val contextVariantsSpec by testSuite {
    // A: two uses of one readOnly schema (github `alert-created-at`) give one `AlertCreatedAt`.
    renderSpec(
        spec(
            pathOf("/alerts", getOp(ref("alert"))) + "," + pathOf("/summaries", getOp(ref("summary"))),
            """
            "alert": { "type": "object", "properties": {
              "id": { "type": "integer", "readOnly": true },
              "created_at": ${ref("alert-created-at")} } },
            "summary": { "type": "object", "properties": { "created_at": ${ref("alert-created-at")} } },
            "alert-created-at": { "type": "string", "format": "date-time", "readOnly": true }
            """.trimIndent(),
        ),
        "client/context-equal-variants",
    ) { apiTree, config -> apiTree.render(config) }

    // B and D: a ref under an inline wrapper, and a plain schema holding a context-specific one, used in a response.
    renderSpec(
        spec(
            pathOf(
                "/people",
                getOp("""{ "type": "object", "properties": { "person": ${ref("Person")} } }"""),
                postOp(ref("Person")),
            ) + "," + pathOf("/summaries", getOp(ref("Summary"))),
            "$PERSON, $SUMMARY",
        ),
        "client/context-variants",
    ) { apiTree, config -> apiTree.render(config) }

    // The same, with the summary also sent: SummaryRead holds a PersonRead, SummaryWrite a PersonWrite.
    renderSpec(
        spec(
            pathOf("/summaries", getOp(ref("Summary")), postOp(ref("Summary"))),
            "$PERSON, $SUMMARY",
        ),
        "client/context-variants-both",
    ) { apiTree, config -> apiTree.render(config) }

    // C: googleapis retail: a readOnly property, read under an inline wrapper and sent as a body.
    renderSpec(
        spec(
            pathOf(
                "/attributes",
                getOp("""{ "type": "object", "properties": { "attribute": ${ref("Attribute")} } }"""),
                postOp(ref("Attribute")),
            ),
            """"Attribute": { "type": "object", "properties": {
              "key": { "type": "string" }, "inUse": { "type": "boolean", "readOnly": true } } }""",
        ),
        "client/context-retail",
    ) { apiTree, config -> apiTree.render(config) }
}
