package io.github.nomisrev.openapi.registry

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.toApiTree
import kotlin.test.assertEquals
import kotlin.test.assertFalse

/**
 * The context a schema is used in (response: Read, request: Write) flows down through inline schemas and through
 * `$ref`s. A schema is "Null" (one shape) only when nothing beneath it is `readOnly` or `writeOnly`.
 */
val ContextFlowSpec by testSuite {
    val d = "${'$'}"

    fun spec(paths: String) = """
        {
          "openapi": "3.0.3",
          "info": { "title": "Api", "version": "1" },
          "paths": { $paths },
          "components": { "schemas": {
            "Summary": { "type": "object", "properties": { "owner": { "${d}ref": "#/components/schemas/Person" } } },
            "Person": { "type": "object", "properties": {
              "name": { "type": "string" },
              "id": { "type": "string", "readOnly": true },
              "password": { "type": "string", "writeOnly": true } } },
            "Plain": { "type": "object", "properties": { "label": { "type": "string" } } }
          } }
        }
    """.trimIndent()

    fun get(path: String, schema: String) =
        """ "$path": { "get": { "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": $schema } } } } } } """

    fun post(path: String, schema: String) =
        """ "$path": { "post": { "requestBody": { "content": { "application/json": { "schema": $schema } } }, "responses": { "204": { "description": "ok" } } } } """

    fun getAndPost(path: String, schema: String) =
        """ "$path": {
          "get": { "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": $schema } } } } },
          "post": { "requestBody": { "content": { "application/json": { "schema": $schema } } }, "responses": { "204": { "description": "ok" } } } } """

    fun ref(name: String) = """{ "${d}ref": "#/components/schemas/$name" }"""

    suspend fun shapes(paths: String): Map<Pair<String, SchemaContext>, Set<String>> =
        OpenAPI.fromJson(spec(paths)).toApiTree().models
            .filterIsInstance<Model.Object>()
            .mapNotNull { model ->
                val reference = model.context.head as? NamingContext.Reference ?: return@mapNotNull null
                (reference.name to reference.context) to model.properties.keys
            }
            .toMap()

    test("a ref under an inline response wrapper takes the context of the response") {
        val wrapper = """{ "type": "object", "properties": { "person": ${ref("Person")} } }"""
        val shapes = shapes(get("/people", wrapper))
        assertEquals(setOf("name", "id"), shapes["Person" to SchemaContext.Read])
        assertFalse(("Person" to SchemaContext.Null) in shapes, "Person must not be resolved as a third, context-free class")
    }

    test("a ref under an inline request wrapper takes the context of the request") {
        val wrapper = """{ "type": "object", "properties": { "person": ${ref("Person")} } }"""
        val shapes = shapes(post("/people", wrapper))
        assertEquals(setOf("name", "password"), shapes["Person" to SchemaContext.Write])
        assertFalse(("Person" to SchemaContext.Null) in shapes)
    }

    test("a schema that refers to a context-specific schema is itself context-specific") {
        val shapes = shapes(get("/summaries", ref("Summary")))
        assertEquals(setOf("owner"), shapes["Summary" to SchemaContext.Read])
        assertEquals(setOf("name", "id"), shapes["Person" to SchemaContext.Read])
        assertFalse(("Person" to SchemaContext.Write) in shapes, "the owner of a response is not a request shape")
        assertFalse(("Summary" to SchemaContext.Null) in shapes)
    }

    test("used in both directions, both the schema and what it refers to have a request and a response shape") {
        val shapes = shapes(getAndPost("/summaries", ref("Summary")))
        assertEquals(setOf("owner"), shapes["Summary" to SchemaContext.Read])
        assertEquals(setOf("owner"), shapes["Summary" to SchemaContext.Write])
        assertEquals(setOf("name", "id"), shapes["Person" to SchemaContext.Read])
        assertEquals(setOf("name", "password"), shapes["Person" to SchemaContext.Write])
    }

    test("a schema with nothing context-specific beneath it stays one shape") {
        val shapes = shapes(getAndPost("/plain", ref("Plain")))
        assertEquals(setOf("label"), shapes["Plain" to SchemaContext.Null])
        assertEquals(1, shapes.size)
    }
}
