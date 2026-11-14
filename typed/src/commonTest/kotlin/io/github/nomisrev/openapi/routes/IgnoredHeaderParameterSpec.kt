package io.github.nomisrev.openapi.routes

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.registry.Registry
import kotlin.test.assertEquals

/**
 * OpenAPI Parameter Object: "If `in` is "header" and the `name` field is "Accept", "Content-Type" or
 * "Authorization", the parameter definition SHALL be ignored." Header names are case-insensitive, so the rule matches
 * them in any case. It applies to header parameters only, wherever they are declared (operation, path item, a
 * `components.parameters` reference or an alias of one); a query or cookie parameter with such a name is an ordinary
 * parameter.
 */
val IgnoredHeaderParameterSpec by testSuite {
    suspend fun route(
        operationParameters: String,
        pathParameters: String = "[]",
        components: String = "{}",
    ): Route {
        val api = OpenAPI.fromJson(document(operationParameters, pathParameters, components))
        return Registry(api).use { registry -> with(registry) { api.toRoutes() } }.single()
    }

    fun Route.inputs(): List<Pair<String, Parameter.Input>> = parameters.map { it.name to it.input }

    fun header(name: String) = """{ "name": "$name", "in": "header", "required": true, "schema": { "type": "string" } }"""

    test("Accept, Content-Type and Authorization header parameters of an operation are ignored") {
        val route = route(
            "[${header("Accept")}, ${header("Content-Type")}, ${header("Authorization")}, ${header("X-Request-Id")}]"
        )
        assertEquals(listOf("X-Request-Id" to Parameter.Input.Header), route.inputs())
    }

    test("the ignored header names match in any case") {
        val route = route(
            "[${header("accept")}, ${header("CONTENT-TYPE")}, ${header("content-type")}, ${header("authorization")}]"
        )
        assertEquals(emptyList(), route.inputs())
    }

    test("Accept, Content-Type and Authorization header parameters of a path item are ignored") {
        val route = route(
            operationParameters = "[${header("X-Request-Id")}]",
            pathParameters = "[${header("Content-Type")}, ${header("Authorization")}, ${header("Accept")}]",
        )
        assertEquals(listOf("X-Request-Id" to Parameter.Input.Header), route.inputs())
    }

    test("a referenced Content-Type header parameter, and an alias of one, are ignored") {
        val components = $$"""
            {
              "parameters": {
                "ContentType": $${header("Content-Type")},
                "AliasContentType": { "$ref": "#/components/parameters/ContentType" },
                "Authorization": $${header("Authorization")},
                "AliasAuthorization": { "$ref": "#/components/parameters/Authorization" }
              }
            }
        """
        val route = route(
            operationParameters = $$"""[{ "$ref": "#/components/parameters/ContentType" }, { "$ref": "#/components/parameters/AliasContentType" }]""",
            pathParameters = $$"""[{ "$ref": "#/components/parameters/AliasAuthorization" }]""",
            components = components,
        )
        assertEquals(emptyList(), route.inputs())
    }

    test("the ignored definitions are kept on the route, after resolving references and operation overrides") {
        val components = $$"""
            {
              "parameters": {
                "Authorization": { "name": "Authorization", "in": "header", "description": "Bearer token", "schema": { "type": "string" } },
                "AliasAuthorization": { "$ref": "#/components/parameters/Authorization" }
              }
            }
        """
        val route = route(
            operationParameters =
                """[{ "name": "Content-Type", "in": "header", "required": true, "schema": { "type": "string", "enum": ["image/png"] } }]""",
            pathParameters =
                $$"""[{ "$ref": "#/components/parameters/AliasAuthorization" }, { "name": "Content-Type", "in": "header", "schema": { "type": "string" } }]""",
            components = components,
        )
        assertEquals(emptyList(), route.inputs())
        assertEquals(
            listOf(
                Triple("Authorization", false, "Bearer token"),
                Triple("Content-Type", true, null),
            ),
            route.ignoredParameters.map { Triple(it.name, it.required, it.description) },
        )
        assertEquals(listOf<JsonElement>(JsonPrimitive("image/png")), route.ignoredParameters[1].schema?.valueOrNull()?.enum)
    }

    test("a route without such header parameters ignores none") {
        assertEquals(emptyList(), route("[${header("X-Request-Id")}]").ignoredParameters)
    }

    test("query and cookie parameters named Accept, Content-Type or Authorization are not ignored") {
        fun parameter(name: String, input: String) =
            """{ "name": "$name", "in": "$input", "schema": { "type": "string" } }"""
        val route = route(
            "[${parameter("Content-Type", "query")}, ${parameter("Accept", "query")}, " +
                "${parameter("Authorization", "cookie")}, ${parameter("Accept", "cookie")}]"
        )
        assertEquals(
            listOf(
                "Content-Type" to Parameter.Input.Query,
                "Accept" to Parameter.Input.Query,
                "Authorization" to Parameter.Input.Cookie,
                "Accept" to Parameter.Input.Cookie,
            ),
            route.inputs(),
        )
    }

    test("a header parameter whose name only contains an ignored name is not ignored") {
        val route = route("[${header("X-Content-Type")}, ${header("Accept-Language")}, ${header("Proxy-Authorization")}]")
        assertEquals(
            listOf(
                "X-Content-Type" to Parameter.Input.Header,
                "Accept-Language" to Parameter.Input.Header,
                "Proxy-Authorization" to Parameter.Input.Header,
            ),
            route.inputs(),
        )
    }
}

private fun document(operationParameters: String, pathParameters: String, components: String): String = """
{
  "openapi": "3.1.0",
  "info": { "title": "Api", "version": "0.0.1" },
  "paths": {
    "/uploads": {
      "parameters": $pathParameters,
      "put": {
        "parameters": $operationParameters,
        "responses": { "204": { "description": "No Content" } }
      }
    }
  },
  "components": $components
}
""".trimIndent()
