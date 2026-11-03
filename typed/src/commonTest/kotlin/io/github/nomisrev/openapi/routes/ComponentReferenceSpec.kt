package io.github.nomisrev.openapi.routes

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.registry.Registry
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * An operation can reference `#/components/requestBodies/X`, `#/components/responses/X` and
 * `#/components/parameters/X`, and such a component may itself be a `$ref` to another component of the same kind
 * (an alias, `AliasBody: {$ref: '#/components/requestBodies/OwnerBody'}`). Aliases and alias chains resolve to the
 * final component, so they generate exactly what referencing that component directly generates; cyclic aliases fail
 * with a diagnostic naming the cycle, and references into other documents fail naming the reference.
 */
val ComponentReferenceSpec by testSuite {
    suspend fun route(
        operation: String,
        components: String,
        pathParameters: String = "[]",
    ): Route {
        val api = OpenAPI.fromJson(componentDocument(operation, components, pathParameters))
        return Registry(api).use { registry -> with(registry) { api.toRoutes() } }.single()
    }

    // Request bodies

    val ownerBody = $$"""
        "OwnerBody": {
          "required": true,
          "description": "The owner",
          "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Owner" } } }
        }
    """
    val aliasBody = $$""""AliasBody": { "$ref": "#/components/requestBodies/OwnerBody" }"""
    val chainBody = $$""""ChainBody": { "$ref": "#/components/requestBodies/AliasBody" }"""

    fun bodyOperation(name: String) =
        $$""""requestBody": { "$ref": "#/components/requestBodies/$$name" }, "responses": { "204": { "description": "No Content" } }"""

    fun requestBodies(vararg entries: String) = """"requestBodies": { ${entries.joinToString(", ")} }"""

    test("an operation referencing a request body component uses that request body") {
        val bodies = requireNotNull(route(bodyOperation("OwnerBody"), requestBodies(ownerBody)).body)
        assertTrue(bodies.required)
        val body = assertIs<Route.Body.SetBody>(bodies.types.getValue(ContentType.Application.Json))
        assertEquals("The owner", body.description)
        val model = assertIs<Model.Object>(body.type)
        assertEquals(NamingContext.reference("Owner", SchemaContext.Null), model.context)
    }

    test("a request body component that is a \$ref alias resolves like the request body it aliases") {
        val direct = route(bodyOperation("OwnerBody"), requestBodies(ownerBody)).body
        val alias = route(bodyOperation("AliasBody"), requestBodies(ownerBody, aliasBody)).body
        assertEquals(direct, alias)
    }

    test("a request body alias chain resolves like the final request body") {
        val direct = route(bodyOperation("OwnerBody"), requestBodies(ownerBody)).body
        val chain = route(bodyOperation("ChainBody"), requestBodies(ownerBody, aliasBody, chainBody)).body
        assertEquals(direct, chain)
    }

    test("cyclic request body aliases fail with a diagnostic naming the cycle") {
        val components = requestBodies(
            $$""""First": { "$ref": "#/components/requestBodies/Second" }""",
            $$""""Second": { "$ref": "#/components/requestBodies/First" }""",
        )
        val failure = assertFailsWith<IllegalStateException> { route(bodyOperation("First"), components) }
        assertCycle(failure, "requestBodies", "First", "Second")
    }

    test("a request body referencing another document fails naming the reference") {
        val operation =
            $$""""requestBody": { "$ref": "other.json#/components/requestBodies/OwnerBody" }, "responses": { "204": { "description": "No Content" } }"""
        val failure = assertFailsWith<NotImplementedError> { route(operation, requestBodies(ownerBody)) }
        assertTrue("other.json#/components/requestBodies/OwnerBody" in failure.message.orEmpty(), "${failure.message}")
    }

    test("a request body component aliasing another document fails naming the reference") {
        val components = requestBodies($$""""AliasBody": { "$ref": "other.json#/components/requestBodies/OwnerBody" }""")
        val failure = assertFailsWith<NotImplementedError> { route(bodyOperation("AliasBody"), components) }
        assertTrue("other.json#/components/requestBodies/OwnerBody" in failure.message.orEmpty(), "${failure.message}")
    }

    // Responses

    val ownerResponse = $$"""
        "OwnerResponse": {
          "description": "OK",
          "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Owner" } } }
        }
    """
    val aliasResponse = $$""""AliasResponse": { "$ref": "#/components/responses/OwnerResponse" }"""
    val chainResponse = $$""""ChainResponse": { "$ref": "#/components/responses/AliasResponse" }"""

    fun responseOperation(name: String) = $$"""
        "responses": {
          "200": { "$ref": "#/components/responses/$$name" },
          "4XX": { "$ref": "#/components/responses/$$name" },
          "default": { "$ref": "#/components/responses/$$name" }
        }
    """

    fun responses(vararg entries: String) = """"responses": { ${entries.joinToString(", ")} }"""

    test("an operation referencing a response component uses that response for codes, ranges and default") {
        val returns = route(responseOperation("OwnerResponse"), responses(ownerResponse)).returns
        val ok = returns.responses.getValue(HttpStatusCode.OK)
        val model = assertIs<Model.Object>(ok.types.getValue(ContentType.Application.Json))
        assertEquals(NamingContext.reference("Owner", SchemaContext.Null), model.context)
        assertEquals(ok, returns.responseRanges.values.single())
        assertEquals(ok, returns.default)
    }

    test("a response component that is a \$ref alias resolves like the response it aliases") {
        val direct = route(responseOperation("OwnerResponse"), responses(ownerResponse)).returns
        val alias = route(responseOperation("AliasResponse"), responses(ownerResponse, aliasResponse)).returns
        assertEquals(direct, alias)
    }

    test("a response alias chain resolves like the final response") {
        val direct = route(responseOperation("OwnerResponse"), responses(ownerResponse)).returns
        val chain =
            route(responseOperation("ChainResponse"), responses(ownerResponse, aliasResponse, chainResponse)).returns
        assertEquals(direct, chain)
    }

    test("cyclic response aliases fail with a diagnostic naming the cycle") {
        val components = responses(
            $$""""First": { "$ref": "#/components/responses/Second" }""",
            $$""""Second": { "$ref": "#/components/responses/First" }""",
        )
        val failure = assertFailsWith<IllegalStateException> { route(responseOperation("First"), components) }
        assertCycle(failure, "responses", "First", "Second")
    }

    test("a response component aliasing another document fails naming the reference") {
        val components = responses($$""""AliasResponse": { "$ref": "other.json#/components/responses/OwnerResponse" }""")
        val failure = assertFailsWith<NotImplementedError> { route(responseOperation("AliasResponse"), components) }
        assertTrue("other.json#/components/responses/OwnerResponse" in failure.message.orEmpty(), "${failure.message}")
    }

    // Parameters

    val limit = """"Limit": { "name": "limit", "in": "query", "required": true, "schema": { "type": "integer" } }"""
    val aliasLimit = $$""""AliasLimit": { "$ref": "#/components/parameters/Limit" }"""
    val chainLimit = $$""""ChainLimit": { "$ref": "#/components/parameters/AliasLimit" }"""
    val noContent = """"responses": { "204": { "description": "No Content" } }"""

    fun parameterOperation(name: String) =
        $$""""parameters": [{ "$ref": "#/components/parameters/$$name" }], $$noContent"""

    fun parameters(vararg entries: String) = """"parameters": { ${entries.joinToString(", ")} }"""

    fun pathParameter(name: String) = $$"""[{ "$ref": "#/components/parameters/$$name" }]"""

    test("an operation referencing a parameter component uses that parameter") {
        val input = route(parameterOperation("Limit"), parameters(limit)).parameters.single()
        assertEquals("limit", input.name)
        assertEquals(Parameter.Input.Query, input.input)
        assertTrue(input.isRequired)
        assertIs<Model.Primitive.Long>(input.type)
    }

    test("a parameter component that is a \$ref alias resolves like the parameter it aliases") {
        val direct = route(parameterOperation("Limit"), parameters(limit)).parameters
        val alias = route(parameterOperation("AliasLimit"), parameters(limit, aliasLimit)).parameters
        assertEquals(direct, alias)
    }

    test("a parameter alias chain resolves like the final parameter") {
        val direct = route(parameterOperation("Limit"), parameters(limit)).parameters
        val chain = route(parameterOperation("ChainLimit"), parameters(limit, aliasLimit, chainLimit)).parameters
        assertEquals(direct, chain)
    }

    test("a path item parameter referencing a parameter component or an alias of one resolves like the parameter") {
        val direct = route(parameterOperation("Limit"), parameters(limit)).parameters
        val pathLevel = route(noContent, parameters(limit), pathParameter("Limit")).parameters
        val pathLevelChain = route(noContent, parameters(limit, aliasLimit, chainLimit), pathParameter("ChainLimit"))
            .parameters
        assertEquals(direct, pathLevel)
        assertEquals(direct, pathLevelChain)
    }

    test("cyclic parameter aliases fail with a diagnostic naming the cycle") {
        val components = parameters(
            $$""""First": { "$ref": "#/components/parameters/Second" }""",
            $$""""Second": { "$ref": "#/components/parameters/First" }""",
        )
        val failure = assertFailsWith<IllegalStateException> { route(parameterOperation("First"), components) }
        assertCycle(failure, "parameters", "First", "Second")
    }

    test("a parameter component aliasing another document fails naming the reference") {
        val components = parameters($$""""AliasLimit": { "$ref": "other.json#/components/parameters/Limit" }""")
        val failure = assertFailsWith<NotImplementedError> { route(parameterOperation("AliasLimit"), components) }
        assertTrue("other.json#/components/parameters/Limit" in failure.message.orEmpty(), "${failure.message}")
    }
}

private fun assertCycle(failure: Throwable, kind: String, first: String, second: String) {
    val message = failure.message.orEmpty()
    assertTrue("Cyclic" in message, message)
    assertTrue("#/components/$kind/$first" in message, message)
    assertTrue("#/components/$kind/$second" in message, message)
}

private fun componentDocument(operation: String, components: String, pathParameters: String): String = $$"""
{
  "openapi": "3.1.0",
  "info": { "title": "Api", "version": "0.0.1" },
  "paths": {
    "/owners": {
      "parameters": $$pathParameters,
      "put": { $$operation }
    }
  },
  "components": {
    "schemas": {
      "Owner": {
        "type": "object",
        "additionalProperties": false,
        "properties": { "name": { "type": "string" } },
        "required": ["name"]
      }
    },
    $$components
  }
}
""".trimIndent()
