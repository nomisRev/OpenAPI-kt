package io.github.nomisrev.openapi.routes

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.registry.Registry
import io.ktor.http.HttpMethod
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * A Path Item Object may be a `$ref` to a Path Item in `components.pathItems`
 * (`/owners: {$ref: '#/components/pathItems/Owners'}`), and such a component may itself be a `$ref` to another
 * one. The path generates exactly the routes an inline path item with the same operations generates; cyclic aliases
 * fail naming the cycle and references into other documents fail naming the reference.
 *
 * OpenAPI leaves fields declared on both the referencing and the referenced Path Item undefined. The referencing
 * item is the more specific one, so its path-level parameters override the referenced item's parameters with the
 * same `(name, in)`, as operation parameters override path-level ones. An operation declared on both is rejected
 * rather than one of them being dropped.
 */
val PathItemReferenceSpec by testSuite {
    suspend fun routes(paths: String, pathItems: String = ""): List<Route> {
        val api = OpenAPI.fromJson(pathItemDocument(paths, pathItems))
        return Registry(api).use { registry -> with(registry) { api.toRoutes() } }
    }

    val owners = $$"""
        "Owners": {
          "parameters": [{ "name": "X-Trace-Id", "in": "header", "required": true, "schema": { "type": "string" } }],
          "get": {
            "parameters": [{ "name": "limit", "in": "query", "schema": { "type": "integer" } }],
            "responses": { "200": { "description": "OK", "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Owner" } } } } }
          },
          "put": {
            "requestBody": { "required": true, "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Owner" } } } },
            "responses": { "204": { "description": "No Content" } }
          }
        }
    """
    val inlineOwners = owners.substringAfter(":")
    val aliasOwners = $$""""AliasOwners": { "$ref": "#/components/pathItems/Owners" }"""
    val chainOwners = $$""""ChainOwners": { "$ref": "#/components/pathItems/AliasOwners" }"""

    fun reference(name: String, siblings: String = "") =
        $$""""/owners": { "$ref": "#/components/pathItems/$$name"$${if (siblings.isEmpty()) "" else ", $siblings"} }"""

    test("a path item referencing a path item component generates the routes of the inline path item") {
        val inline = routes(""""/owners": $inlineOwners""")
        val referenced = routes(reference("Owners"), owners)
        assertEquals(listOf(HttpMethod.Get, HttpMethod.Put), inline.map { it.method })
        assertEquals(inline, referenced)
    }

    test("a path item component that is a \$ref alias, or a chain of them, resolves like the final path item") {
        val inline = routes(""""/owners": $inlineOwners""")
        assertEquals(inline, routes(reference("AliasOwners"), "$owners, $aliasOwners"))
        assertEquals(inline, routes(reference("ChainOwners"), "$owners, $aliasOwners, $chainOwners"))
    }

    test("cyclic path item aliases fail with a diagnostic naming the cycle") {
        val pathItems = $$"""
            "First": { "$ref": "#/components/pathItems/Second" },
            "Second": { "$ref": "#/components/pathItems/First" }
        """
        val message = assertFailsWith<IllegalStateException> { routes(reference("First"), pathItems) }.message.orEmpty()
        assertTrue("Cyclic" in message, message)
        assertTrue("#/components/pathItems/First" in message, message)
        assertTrue("#/components/pathItems/Second" in message, message)
    }

    test("a path item referencing another document fails naming the reference") {
        val paths = $$""""/owners": { "$ref": "other.json#/components/pathItems/Owners" }"""
        val failure = assertFailsWith<NotImplementedError> { routes(paths, owners) }
        assertTrue("other.json#/components/pathItems/Owners" in failure.message.orEmpty(), "${failure.message}")
    }

    test("a path item component aliasing another document fails naming the reference") {
        val pathItems = $$""""AliasOwners": { "$ref": "other.json#/components/pathItems/Owners" }"""
        val failure = assertFailsWith<NotImplementedError> { routes(reference("AliasOwners"), pathItems) }
        assertTrue("other.json#/components/pathItems/Owners" in failure.message.orEmpty(), "${failure.message}")
    }

    test("a path item referencing a missing path item component fails naming the reference") {
        val message = assertFailsWith<IllegalStateException> { routes(reference("Missing"), owners) }.message.orEmpty()
        assertTrue("#/components/pathItems/Missing" in message, message)
    }

    test("path-level parameters of the referencing path item are added to those of the referenced path item") {
        val siblings = """"parameters": [{ "name": "X-Tenant", "in": "header", "required": true, "schema": { "type": "string" } }]"""
        val get = routes(reference("Owners", siblings), owners).first { it.method == HttpMethod.Get }
        assertEquals(
            listOf("X-Trace-Id" to Parameter.Input.Header, "X-Tenant" to Parameter.Input.Header, "limit" to Parameter.Input.Query),
            get.parameters.map { it.name to it.input },
        )
    }

    test("a path-level parameter of the referencing path item overrides the referenced one with the same name and location") {
        val siblings = """"parameters": [{ "name": "X-Trace-Id", "in": "header", "required": false, "description": "Overridden", "schema": { "type": "string" } }]"""
        val routes = routes(reference("Owners", siblings), owners)
        assertEquals(listOf(HttpMethod.Get, HttpMethod.Put), routes.map { it.method })
        for (route in routes) {
            val traceId = route.parameters.single { it.name == "X-Trace-Id" }
            assertEquals(false, traceId.isRequired)
            assertEquals("Overridden", traceId.description)
        }
    }

    test("an operation of the referencing path item is added to the operations of the referenced path item") {
        val siblings = """"delete": { "responses": { "204": { "description": "No Content" } } }"""
        val routes = routes(reference("Owners", siblings), owners)
        assertEquals(listOf(HttpMethod.Get, HttpMethod.Put, HttpMethod.Delete), routes.map { it.method })
    }

    test("an operation declared on both the referencing and the referenced path item fails naming both") {
        val siblings = """"get": { "responses": { "204": { "description": "No Content" } } }"""
        val message =
            assertFailsWith<IllegalArgumentException> { routes(reference("Owners", siblings), owners) }.message.orEmpty()
        assertTrue("/owners" in message, message)
        assertTrue("get" in message, message)
        assertTrue("#/components/pathItems/Owners" in message, message)
    }
}

private fun pathItemDocument(paths: String, pathItems: String): String = $$"""
{
  "openapi": "3.1.0",
  "info": { "title": "Api", "version": "0.0.1" },
  "paths": { $$paths },
  "components": {
    "schemas": {
      "Owner": {
        "type": "object",
        "additionalProperties": false,
        "properties": { "name": { "type": "string" } },
        "required": ["name"]
      }
    },
    "pathItems": { $$pathItems }
  }
}
""".trimIndent()
