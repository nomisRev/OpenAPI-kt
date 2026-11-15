package io.github.nomisrev.render

import de.infix.testBalloon.framework.core.testSuite
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

/**
 * `_metadata` and `metadata` are one Kotlin name; the one declared first keeps it. `metadata` has an inline enum, so
 * `Metadata2.Level` is nested under the renamed owner. `Thing` is discriminated over both: the wire values of its
 * mapping are the raw keys, and so are the ones of `Implicit`, which has no mapping (the schema names `Other` and `metadata`). `Either` is
 * a union of an inline object and `metadata`.
 */
internal val sameNameSpec = spec(
    pathOf("/plain", getOp(ref("_metadata"))) + "," + pathOf("/meta", getOp(ref("metadata"))) + "," +
        pathOf("/things", getOp(ref("Thing"))) + "," + pathOf("/pick", getOp(ref("Either"))) + "," +
        pathOf("/by-name", getOp(ref("Implicit"))),
    """
    "_metadata": { "type": "object", "required": ["kind"], "properties": {
      "kind": { "type": "string" }, "a": { "type": "string" } } },
    "metadata": { "type": "object", "required": ["kind"], "properties": {
      "kind": { "type": "string" }, "b": { "type": "string" },
      "level": { "type": "string", "enum": ["low", "high"] } } },
    "Thing": { "oneOf": [ ${ref("_metadata")}, ${ref("metadata")} ],
      "discriminator": { "propertyName": "kind", "mapping": {
        "plain": "#/components/schemas/_metadata", "meta": "#/components/schemas/metadata" } } },
    "Other": { "type": "object", "required": ["kind"], "properties": {
      "kind": { "type": "string" }, "o": { "type": "string" } } },
    "Implicit": { "oneOf": [ ${ref("Other")}, ${ref("metadata")} ],
      "discriminator": { "propertyName": "kind" } },
    "Either": { "oneOf": [
      { "type": "object", "required": ["x"], "properties": { "x": { "type": "integer" } } },
      ${ref("metadata")} ] }
    """.trimIndent(),
)

/** `HTTPHealthCheck` and `HttpHealthCheck` differ by case only, which is one file on a case-insensitive file system. */
internal val caseOnlySpec = spec(
    pathOf("/a", getOp(ref("HTTPHealthCheck"))) + "," + pathOf("/b", getOp(ref("HttpHealthCheck"))),
    """
    "HTTPHealthCheck": { "type": "object", "properties": { "a": { "type": "string" } } },
    "HttpHealthCheck": { "type": "object", "properties": { "b": { "type": "string" } } }
    """.trimIndent(),
)

/** `Avatar` has a `Read` and a `Write` shape, and a component is literally named `AvatarRead`. */
internal val suffixSpec = spec(
    pathOf("/avatars", getOp(ref("Avatar")), postOp(ref("Avatar"))) + "," + pathOf("/legacy-avatars", getOp(ref("AvatarRead"))),
    """
    "Avatar": { "type": "object", "properties": {
      "id": { "type": "string", "readOnly": true }, "secret": { "type": "string", "writeOnly": true } } },
    "AvatarRead": { "type": "object", "properties": { "b": { "type": "string" } } }
    """.trimIndent(),
)

/** `__string` and `String` are one name and one shape: one class, and no diagnostic. */
internal val equalShapesSpec = spec(
    pathOf("/a", getOp(ref("Holder"))),
    """
    "__string": { "type": "string" },
    "String": { "type": "string" },
    "Holder": { "type": "object", "properties": { "a": ${ref("__string")}, "b": ${ref("String")} } }
    """.trimIndent(),
)

private suspend fun diagnostics(json: String) = OpenAPI.fromJson(json).toApiTree().renderWithDiagnostics(
    RenderConfig(modelPackage = "test.model", apiPackage = "test.api", targets = setOf(KmpTarget.JVM))
).diagnostics

val schemaNameCollisionsSpec by testSuite {
    renderSpec(sameNameSpec, "client/schema-names-same") { apiTree, config -> apiTree.render(config) }
    renderSpec(caseOnlySpec, "client/schema-names-case") { apiTree, config -> apiTree.render(config) }
    renderSpec(suffixSpec, "client/schema-names-suffix") { apiTree, config -> apiTree.render(config) }
    renderSpec(equalShapesSpec, "client/schema-names-equal") { apiTree, config -> apiTree.render(config) }

    test("schemas with one Kotlin name are reported, with both schemas and the reason") {
        val messages = diagnostics(sameNameSpec).filter { it.severity == GenerationDiagnosticSeverity.Warning }
            .map { it.message }.filter { it.contains("is generated as") }

        assertEquals(
            listOf("Schema 'metadata' is generated as 'Metadata2': schema '_metadata' has the Kotlin name 'Metadata'"),
            messages,
        )
        assertTrue(diagnostics(sameNameSpec).none { it.severity == GenerationDiagnosticSeverity.Error })
    }

    test("schemas that differ by case only are reported") {
        val diagnostics = diagnostics(caseOnlySpec)

        assertEquals(
            listOf(
                "Schema 'HttpHealthCheck' is generated as 'HttpHealthCheck2': schema 'HTTPHealthCheck' has the " +
                    "Kotlin name 'HTTPHealthCheck', which differs from it by case only (one file on a case-insensitive file system)"
            ),
            diagnostics.map { it.message }.filter { it.contains("is generated as") },
        )
        assertTrue(diagnostics.none { it.severity == GenerationDiagnosticSeverity.Error })
    }

    test("a variant named like a component is reported") {
        val diagnostics = diagnostics(suffixSpec)

        assertEquals(
            listOf("The Read shape of schema 'Avatar' is generated as 'AvatarRead2': schema 'AvatarRead' has that name"),
            diagnostics.map { it.message }.filter { it.contains("is generated as") },
        )
        assertTrue(diagnostics.none { it.severity == GenerationDiagnosticSeverity.Error })
    }

    test("schemas of one name and one shape share a class and are not reported") {
        val diagnostics = diagnostics(equalShapesSpec)

        assertEquals(emptyList(), diagnostics.map { it.message }.filter { it.contains("is generated as") })
        assertTrue(diagnostics.none { it.severity == GenerationDiagnosticSeverity.Error })
    }
}
