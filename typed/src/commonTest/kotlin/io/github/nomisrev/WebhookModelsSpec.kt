package io.github.nomisrev

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.toApiTree
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * A webhook is a request the API sends to the consumer: no client call is generated for it, but the schemas of its
 * payload are generated, as received (the request body is read, the response the consumer sends back is written).
 * Component schemas that no operation or webhook refers to are not generated, and are reported.
 */
val WebhookModelsSpec by testSuite {
    fun ref(name: String) = """{ "${'$'}ref": "#/components/schemas/$name" }"""

    suspend fun tree(json: String) = OpenAPI.fromJson(json).toApiTree()

    fun names(models: List<Model>): Set<NamingContext.Reference> =
        models.mapTo(mutableSetOf()) { (it as Model.ContextHolder).context.head as NamingContext.Reference }

    val webhooksOnly = """
        { "openapi": "3.1.0", "info": { "title": "Repro", "version": "1" },
          "paths": {},
          "components": {
            "schemas": {
              "TransferEvent": { "type": "object", "required": ["type"],
                "properties": { "type": { "type": "string" }, "amount": ${ref("Amount")} } },
              "Amount": { "type": "object", "properties": { "value": { "type": "integer" } } },
              "Audited": { "type": "object", "properties": {
                "id": { "type": "string", "readOnly": true }, "note": { "type": "string" } } },
              "Ack": { "type": "object", "properties": {
                "id": { "type": "string", "readOnly": true }, "received": { "type": "boolean" } } },
              "Nested": { "type": "object", "properties": { "n": { "type": "string" } } }
            },
            "pathItems": {
              "audit": { "post": {
                "requestBody": { "content": { "application/json": { "schema": ${ref("Audited")} } } },
                "responses": { "200": { "description": "ack",
                  "content": { "application/json": { "schema": ${ref("Ack")} } } } } } }
            }
          },
          "webhooks": {
            "transfer.created": { "post": {
              "operationId": "onTransfer",
              "requestBody": { "content": { "application/json": { "schema": ${ref("TransferEvent")} } } },
              "responses": { "200": { "description": "ack" } } } },
            "audit": { "${'$'}ref": "#/components/pathItems/audit" },
            "inline": { "post": {
              "requestBody": { "content": { "application/json": { "schema": {
                "type": "object", "properties": { "nested": ${ref("Nested")} } } } } },
              "responses": { "204": { "description": "ok" } } } }
          }
        }
    """.trimIndent()

    test("the schemas a webhook payload refers to are generated, received as read and answered as written") {
        assertEquals(
            setOf(
                NamingContext.Reference("TransferEvent", SchemaContext.Null),
                NamingContext.Reference("Amount", SchemaContext.Null),
                NamingContext.Reference("Audited", SchemaContext.Read),
                NamingContext.Reference("Ack", SchemaContext.Write),
                NamingContext.Reference("Nested", SchemaContext.Null),
            ),
            names(tree(webhooksOnly).models),
        )
    }

    test("an inline webhook payload, which has no name to generate it under, is reported") {
        val warnings = tree(webhooksOnly).warnings
        assertTrue(warnings.any { "Webhook 'inline'" in it && "inline" in it }, warnings.toString())
        assertTrue(warnings.none { "transfer.created" in it || "audit" in it }, warnings.toString())
    }

    val withOrphans = """
        { "openapi": "3.0.3", "info": { "title": "Repro", "version": "1" },
          "paths": { "/ping": { "get": { "responses": { "200": { "description": "ok",
            "content": { "application/json": { "schema": ${ref("Pong")} } } } } } } },
          "components": { "schemas": {
            "Pong": { "oneOf": [ ${ref("Cat")} ], "discriminator": { "propertyName": "kind",
              "mapping": { "cat": "#/components/schemas/Cat", "dog": "Dog" } } },
            "Cat": { "type": "object", "properties": { "kind": { "type": "string" } } },
            "Dog": { "type": "object", "properties": { "kind": { "type": "string" } } },
            "Orphan": { "type": "object", "properties": { "child": ${ref("OrphanChild")} } },
            "OrphanChild": { "type": "string" }
          } }
        }
    """.trimIndent()

    test("component schemas no operation or webhook refers to are reported, also through other unreferenced ones") {
        val warnings = tree(withOrphans).warnings.filter { "not referenced" in it }
        assertEquals(
            listOf("2 component schemas are not referenced by any operation or webhook and are not generated: Orphan, OrphanChild"),
            warnings,
        )
    }

    test("a document whose schemas are all referenced reports none") {
        assertTrue(tree(webhooksOnly).warnings.none { "not referenced" in it }, tree(webhooksOnly).warnings.toString())
    }
}
