package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.GenerationDiagnostic
import io.github.nomisrev.openapi.GenerationDiagnosticSeverity
import io.github.nomisrev.openapi.KmpTarget
import io.github.nomisrev.openapi.RenderConfig
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.render
import io.github.nomisrev.openapi.renderWithDiagnostics
import io.github.nomisrev.openapi.toApiTree
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val D = "$"

/** adyen.com:BalancePlatformTransferNotification-v3, reduced: a document with webhooks only. */
internal val webhooksOnlySpec = """
    { "openapi": "3.1.0", "info": { "title": "Repro", "version": "1" },
      "paths": {},
      "components": { "schemas": { "TransferEvent": { "type": "object", "required": ["type"],
        "properties": { "type": { "type": "string" }, "amount": { "type": "integer" } } } } },
      "webhooks": { "transfer.created": { "post": {
        "operationId": "onTransfer",
        "requestBody": { "content": { "application/json": { "schema": { "${D}ref": "#/components/schemas/TransferEvent" } } } },
        "responses": { "200": { "description": "ack" } } } } } }
""".trimIndent()

private suspend fun result(json: String) = OpenAPI.fromJson(json).toApiTree().renderWithDiagnostics(
    RenderConfig(modelPackage = "test.model", apiPackage = "test.api", targets = setOf(KmpTarget.JVM))
)

val webhookModelsRenderSpec by testSuite {
    renderSpec(webhooksOnlySpec, "client/webhooks-only") { apiTree, config -> apiTree.render(config) }

    test("a document that generates no files says so") {
        val result = result("""{ "openapi": "3.0.3", "info": { "title": "Empty", "version": "1" }, "paths": {} }""")
        assertEquals(emptyList(), result.files)
        assertEquals(
            listOf(
                GenerationDiagnostic(
                    GenerationDiagnosticSeverity.Warning,
                    "The document has no operations, and no webhooks or schemas they refer to: no files are generated.",
                )
            ),
            result.diagnostics,
        )
    }

    test("a document with files has no such warning") {
        assertTrue(result(webhooksOnlySpec).diagnostics.none { "no files" in it.message })
    }
}
