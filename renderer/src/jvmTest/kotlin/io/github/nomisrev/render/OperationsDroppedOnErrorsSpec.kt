package io.github.nomisrev.render

import com.squareup.kotlinpoet.FileSpec
import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.ApiTree
import io.github.nomisrev.openapi.GenerationDiagnosticSeverity
import io.github.nomisrev.openapi.PathNode
import io.github.nomisrev.openapi.renderableTree
import io.github.nomisrev.openapi.routes.Route
import java.io.File
import io.github.nomisrev.openapi.GenerationResult
import io.github.nomisrev.openapi.KmpTarget
import io.github.nomisrev.openapi.RenderConfig
import io.github.nomisrev.openapi.generateClientWithDiagnostics
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.render
import io.github.nomisrev.openapi.renderWithDiagnostics
import io.github.nomisrev.openapi.toApiTree
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

private val unionFields = (1..6).joinToString(", ") { """"field$it": { "oneOf": [{ "type": "string" }, { "type": "integer" }] }""" }
private val unionRequired = (1..6).joinToString(", ") { "\"field$it\"" }

/**
 * Every way an operation gets an `Error` diagnostic, next to operations that are fine:
 * - `GET /items/{id}/tags`: a query parameter holding an array of objects, which no `form` serialization exists for;
 * - `POST /forms`: a required form body whose only variant has such a field;
 * - `POST /search`: a JSON body that would need more than 32 flattened overloads.
 * `POST /mixed` has the same unsupported form variant, but also a JSON one: only that variant is skipped, with a warning.
 */
internal val operationsWithErrorsSpec = """
    { "openapi": "3.1.0", "info": { "title": "Diag", "version": "1" },
      "paths": {
        "/items": { "get": { "operationId": "listItems",
          "parameters": [{ "name": "limit", "in": "query", "schema": { "type": "integer", "format": "int32" } }],
          "responses": { "200": { "description": "OK", "content": { "application/json": { "schema": { "type": "string" } } } } } } },
        "/items/{id}/tags": { "get": { "operationId": "listTags",
          "parameters": [
            { "name": "id", "in": "path", "required": true, "schema": { "type": "string" } },
            { "name": "Tags", "in": "query", "schema": { "type": "array", "items": { "type": "object", "properties": { "a": { "type": "string" } } } } }
          ],
          "responses": { "200": { "description": "OK" } } } },
        "/forms": { "post": { "operationId": "postForm",
          "requestBody": { "required": true, "content": { "application/x-www-form-urlencoded": { "schema": {
            "type": "object", "required": ["Parameters"],
            "properties": { "Parameters": { "type": "array", "items": { "type": "object", "properties": { "a": { "type": "string" } } } },
                            "name": { "type": "string" } } } } } },
          "responses": { "200": { "description": "OK" } } } },
        "/mixed": { "post": { "operationId": "postMixed",
          "requestBody": { "required": true, "content": {
            "application/x-www-form-urlencoded": { "schema": { "type": "object",
              "properties": { "Parameters": { "type": "array", "items": { "type": "object", "properties": { "a": { "type": "string" } } } } } } },
            "application/json": { "schema": { "type": "object", "required": ["a"], "properties": { "a": { "type": "string" } } } } } },
          "responses": { "204": { "description": "No content" } } } },
        "/search": { "post": { "operationId": "search",
          "requestBody": { "required": true, "content": { "application/json": { "schema": {
            "type": "object", "required": [$unionRequired], "properties": { $unionFields } } } } },
          "responses": { "200": { "description": "OK" } } } }
      } }
""".trimIndent()

private fun document(path: String, operation: String) = """
    { "openapi": "3.1.0", "info": { "title": "Diag", "version": "1" },
      "paths": {
        "$path": { $operation },
        "/ok": { "get": { "responses": { "204": { "description": "No content" } } } }
      } }
""".trimIndent()

private fun body(required: Boolean, contentType: String, schema: String) = """
    "post": { "requestBody": { "required": $required, "content": { "$contentType": { "schema": $schema } } },
              "responses": { "204": { "description": "No content" } } }
""".trimIndent()

private const val ARRAY_OF_OBJECTS_FIELD =
    """{ "type": "object", "properties": { "Parameters": { "type": "array", "items": { "type": "object", "properties": { "a": { "type": "string" } } } } } }"""
private const val FLAT_OBJECT = """{ "type": "object", "properties": { "a": { "type": "string" } } }"""

private val config = RenderConfig(modelPackage = "test.model", apiPackage = "test.api", targets = setOf(KmpTarget.JVM))

private suspend fun renderWithDiagnostics(json: String): GenerationResult =
    OpenAPI.fromJson(json).toApiTree().renderWithDiagnostics(config)

private fun GenerationResult.file(name: String): FileSpec? = files.singleOrNull { it.name == name }
private fun GenerationResult.errors() = diagnostics.filter { it.severity == GenerationDiagnosticSeverity.Error }.map { it.message }
private fun GenerationResult.warnings() = diagnostics.filter { it.severity == GenerationDiagnosticSeverity.Warning }.map { it.message }

/** Every operation of the tree, as `METHOD /path`, the way diagnostics name it. */
private fun ApiTree.operationKeys(): Set<String> {
    fun keys(operations: Map<*, Route>) = operations.values.map { "${it.method.value} ${it.path}" }
    fun PathNode.keys(): List<String> = keys(operations) + children.flatMap { it.keys() }
    return (keys(operations) + children.flatMap { it.keys() }).toSet()
}

/** The `/x` operation of [document] is not generated, `/ok` is, and the error is the only diagnostic. */
private fun GenerationResult.assertOnlyXIsDropped(error: String) {
    assertEquals(1, errors().size, "diagnostics: $diagnostics")
    assertTrue(error in errors().single(), "expected '$error' in ${errors().single()}")
    assertEquals(null, file("X"), "the operation with an error is not generated")
    val root = requireNotNull(file("Diag")).toString()
    assertTrue("val ok" in root && "val x" !in root, root)
}

val operationsDroppedOnErrorsSpec by testSuite {
    renderSpec(operationsWithErrorsSpec, "client/operations-dropped-on-errors") { apiTree, config ->
        apiTree.renderWithDiagnostics(config).files
    }

    test("renderWithDiagnostics returns every error once, without throwing") {
        val result = renderWithDiagnostics(operationsWithErrorsSpec)
        assertEquals(3, result.errors().size, "errors: ${result.errors()}")
        assertTrue(result.errors().any { "GET /items/{id}/tags parameter Tags" in it })
        assertTrue(result.errors().any { "POST /forms (application/x-www-form-urlencoded) contains form fields [Parameters]" in it })
        assertTrue(result.errors().any { "POST /search (application/json) would generate more than 32" in it })
        assertEquals(
            1,
            result.warnings().count { "Skipping unsupported form overload for POST /mixed" in it },
            "warnings: ${result.warnings()}",
        )
        assertEquals(result.diagnostics.distinct(), result.diagnostics, "no diagnostic is reported twice")
    }

    test("operations with an error and the path classes left empty are not generated") {
        val result = renderWithDiagnostics(operationsWithErrorsSpec)
        val names = result.files.map { it.name }.toSet()
        assertEquals(null, result.file("Forms"))
        assertEquals(null, result.file("Search"))
        assertTrue("Items" in names && "Mixed" in names, "files: $names")
        val items = requireNotNull(result.file("Items")).toString()
        assertTrue("Tags" !in items && "fun id(" !in items, items)
        val root = requireNotNull(result.file("Diag")).toString()
        assertTrue("val forms" !in root && "val search" !in root, root)
    }

    test("render still fails, and lists every error") {
        val error = assertFailsWith<IllegalArgumentException> {
            OpenAPI.fromJson(operationsWithErrorsSpec).toApiTree().render(config)
        }
        val message = error.message.orEmpty()
        assertTrue("GET /items/{id}/tags parameter Tags" in message, message)
        assertTrue("POST /forms" in message && "POST /search" in message, message)
    }

    test("a required form body with an unsupported field drops the operation, not the body") {
        renderWithDiagnostics(document("/x", body(required = true, "application/x-www-form-urlencoded", ARRAY_OF_OBJECTS_FIELD)))
            .assertOnlyXIsDropped("form fields [Parameters]")
    }

    test("an optional form body with an unsupported field drops the operation, not the body") {
        renderWithDiagnostics(document("/x", body(required = false, "application/x-www-form-urlencoded", ARRAY_OF_OBJECTS_FIELD)))
            .assertOnlyXIsDropped("form fields [Parameters]")
    }

    test("generateClientWithDiagnostics drops the same operations") {
        val result = OpenAPI.fromJson(document("/x", body(required = true, "application/x-www-form-urlencoded", ARRAY_OF_OBJECTS_FIELD)))
            .toApiTree()
            .generateClientWithDiagnostics(config)
        result.assertOnlyXIsDropped("form fields [Parameters]")
    }

    // No operation is dropped silently: every operation of the document that is not rendered is named by an error.
    listOf(
        "operations-with-errors" to { operationsWithErrorsSpec },
        "required-form" to { document("/x", body(required = true, "application/x-www-form-urlencoded", ARRAY_OF_OBJECTS_FIELD)) },
        "optional-form" to { document("/x", body(required = false, "application/x-www-form-urlencoded", ARRAY_OF_OBJECTS_FIELD)) },
        "youtrack.json" to { File("../parser/src/commonTest/resources/specs/youtrack.json").readText() },
        "github.json" to { File("../parser/src/commonTest/resources/specs/github.json").readText() },
        "openai.yaml" to { File("../parser/src/commonTest/resources/specs/openai.yaml").readText() },
    ).forEach { [name, text] ->
        test("$name: every operation that is not rendered is named by an error") {
            val api = if (name.endsWith(".yaml")) OpenAPI.fromYaml(text()) else OpenAPI.fromJson(text())
            val tree = api.toApiTree()
            val result = tree.renderWithDiagnostics(config)
            val errors = result.errors()
            val dropped = tree.operationKeys() - tree.renderableTree().operationKeys()
            dropped.forEach { operation ->
                assertTrue(errors.any { it.startsWith("$operation ") }, "$operation is not rendered, but no error names it: $errors")
            }
            errors.forEach { error ->
                assertTrue(dropped.any { error.startsWith("$it ") }, "an error names an operation that is rendered: $error")
            }
        }
    }

    test("a document whose only operation has an error generates no client, and reports only the error") {
        val result = renderWithDiagnostics(
            """
            { "openapi": "3.1.0", "info": { "title": "Diag", "version": "1" },
              "paths": { "/a/b/c": { "get": {
                "parameters": [{ "name": "v", "in": "query", "schema": { "type": "array", "items": $FLAT_OBJECT } }],
                "responses": { "204": { "description": "No content" } } } } } }
            """.trimIndent()
        )
        assertEquals(emptyList(), result.files.map { it.name })
        assertEquals(1, result.diagnostics.size, "diagnostics: ${result.diagnostics}")
        assertTrue("GET /a/b/c parameter v" in result.errors().single())
    }
}
