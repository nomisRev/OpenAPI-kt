package io.github.nomisrev.openapi.routes

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Parameter
import io.github.nomisrev.openapi.parser.Style
import io.github.nomisrev.openapi.registry.Registry
import kotlin.test.assertEquals

/**
 * A parameter is serialized by its `style` and `explode` (OpenAPI 3.1.1, 4.8.12.4): `style` defaults to `form` for
 * `query` and `cookie` and to `simple` for `path` and `header`, and `explode` defaults to `true` for `form` and to
 * `false` for every other style. A parameter is supported only when the client writes the serialization its type,
 * location, style and explode define; any other combination is reported instead of sending something else.
 */
val ParameterStyleSpec by testSuite {
    suspend fun input(parameter: String, components: String = ""): Route.Input {
        val api = OpenAPI.fromJson(parameterDocument(parameter, components))
        return Registry(api).use { registry -> with(registry) { api.toRoutes() } }
            .single()
            .parameters
            .single { it.name == "p" }
    }

    fun parameter(location: String, schema: String, extra: String = "") =
        """{ "name": "p", "in": "$location", "required": true, "schema": $schema$extra }"""

    val string = """{ "type": "string" }"""
    val array = """{ "type": "array", "items": { "type": "integer", "format": "int32" } }"""
    val obj = """{ "type": "object", "properties": { "R": { "type": "integer" }, "G": { "type": "integer" } } }"""

    val defaults = listOf(
        Parameter.Input.Query to (Style.form to true),
        Parameter.Input.Cookie to (Style.form to true),
        Parameter.Input.Header to (Style.simple to false),
        Parameter.Input.Path to (Style.simple to false),
    )
    for ([location, expected] in defaults) {
        test("a ${location.value} parameter without style or explode is ${expected.first}, explode ${expected.second}") {
            val input = input(parameter(location.value, array))
            assertEquals(expected, input.style to input.explode)
            assertEquals(true, input.isSupported)
        }
    }

    val declared = listOf(
        "query explode false" to (parameter("query", array, """, "explode": false""") to (Style.form to false)),
        "query spaceDelimited" to (parameter("query", array, """, "style": "spaceDelimited"""") to (Style.spaceDelimited to false)),
        "query pipeDelimited" to (parameter("query", array, """, "style": "pipeDelimited"""") to (Style.pipeDelimited to false)),
        "query deepObject" to (parameter("query", obj, """, "style": "deepObject"""") to (Style.deepObject to false)),
        "query form explode true" to (parameter("query", array, """, "style": "form", "explode": true""") to (Style.form to true)),
        "path label" to (parameter("path", array, """, "style": "label"""") to (Style.label to false)),
        "path matrix explode true" to (parameter("path", array, """, "style": "matrix", "explode": true""") to (Style.matrix to true)),
        "header explode true" to (parameter("header", obj, """, "explode": true""") to (Style.simple to true)),
        "cookie explode false" to (parameter("cookie", array, """, "explode": false""") to (Style.form to false)),
    )
    for ([description, case] in declared) {
        val [json, expected] = case
        test("a $description parameter keeps its declared style and explode") {
            val input = input(json)
            assertEquals(expected, input.style to input.explode)
            assertEquals(true, input.isSupported)
        }
    }

    val supported = listOf(
        "a query string" to parameter("query", string),
        "a query string with spaceDelimited (a scalar is its value)" to
            parameter("query", string, """, "style": "spaceDelimited""""),
        "a query array" to parameter("query", array),
        "a query object, form" to parameter("query", obj),
        "a query object, form explode false" to parameter("query", obj, """, "explode": false"""),
        "a query object, pipeDelimited" to parameter("query", obj, """, "style": "pipeDelimited""""),
        "a query object, deepObject explode true" to
            parameter("query", obj, """, "style": "deepObject", "explode": true"""),
        "a query union of a string and an array" to
            parameter("query", """{ "oneOf": [$string, { "type": "array", "items": $string }] }"""),
        "a query array of a referenced enum" to
            parameter("query", $$"""{ "type": "array", "items": { "$ref": "#/components/schemas/Status" } }"""),
        "a query referenced string" to parameter("query", $$"""{ "$ref": "#/components/schemas/Code" }"""),
        "a path array, label explode true" to parameter("path", array, """, "style": "label", "explode": true"""),
        "a query map, deepObject" to parameter(
            "query",
            """{ "type": "object", "additionalProperties": { "type": "string" } }""",
            """, "style": "deepObject", "explode": true""",
        ),
        "a query referenced map that may be null" to parameter("query", $$"""{ "$ref": "#/components/schemas/Metadata" }"""),
        "a header array" to parameter("header", array),
        "a header object" to parameter("header", obj),
        "a cookie array" to parameter("cookie", array),
        "a cookie object, explode false" to parameter("cookie", obj, """, "explode": false"""),
    )
    for ([description, json] in supported) {
        test("$description parameter is supported") {
            assertEquals(true, input(json, StatusAndCode).isSupported)
        }
    }

    val unsupported = listOf(
        // Styles a location does not define.
        "a query array with simple" to parameter("query", array, """, "style": "simple""""),
        "a query array with matrix" to parameter("query", array, """, "style": "matrix""""),
        "a path array with form" to parameter("path", array, """, "style": "form""""),
        "a header array with form" to parameter("header", array, """, "style": "form""""),
        "a cookie object with deepObject" to parameter("cookie", obj, """, "style": "deepObject""""),
        // Combinations OpenAPI leaves undefined (n/a).
        "a query array with deepObject" to parameter("query", array, """, "style": "deepObject", "explode": true"""),
        "a query array with spaceDelimited explode true" to
            parameter("query", array, """, "style": "spaceDelimited", "explode": true"""),
        "a query array with pipeDelimited explode true" to
            parameter("query", array, """, "style": "pipeDelimited", "explode": true"""),
        // Values the styles do not define a serialization for.
        "a query array of objects" to parameter("query", """{ "type": "array", "items": $obj }"""),
        "a query array of nullable items" to
            parameter("query", """{ "type": "array", "items": { "type": ["string", "null"] } }"""),
        "a query object with an array property" to
            parameter("query", """{ "type": "object", "properties": { "a": $array } }"""),
        "a query map of objects" to
            parameter("query", """{ "type": "object", "additionalProperties": $obj }"""),
        "a path map" to
            parameter("path", """{ "type": "object", "additionalProperties": { "type": "string" } }"""),
        "a path object" to parameter("path", obj, """, "style": "matrix""""),
        "a path label enum (its path segments are fixed values)" to
            parameter("path", """{ "type": "string", "enum": ["a", "b"] }""", """, "style": "label""""),
        // A parameter with `content` is its media type, which is only generated for a scalar.
        "a query object with content" to
            """{ "name": "p", "in": "query", "content": { "application/json": { "schema": $obj } } }""",
    )
    for ([description, json] in unsupported) {
        test("$description parameter is not supported") {
            assertEquals(false, input(json, StatusAndCode).isSupported)
        }
    }
}

private const val StatusAndCode = """
    "Status": { "type": "string", "enum": ["open", "in-progress"] },
    "Code": { "type": "string" },
    "Metadata": { "anyOf": [{ "type": "object", "additionalProperties": { "type": "string" } }, { "type": "null" }] }
"""

private fun parameterDocument(parameter: String, schemas: String): String {
    val path = if (""""in": "path"""" in parameter) "/items/{p}" else "/items"
    return """
    {
      "openapi": "3.1.0",
      "info": { "title": "Api", "version": "0.0.1" },
      "paths": {
        "$path": {
          "get": {
            "parameters": [$parameter],
            "responses": { "204": { "description": "No Content" } }
          }
        }
      },
      "components": { "schemas": { $schemas } }
    }
    """.trimIndent()
}
