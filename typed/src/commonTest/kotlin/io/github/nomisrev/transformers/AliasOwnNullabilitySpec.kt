package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.Route
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.routes.toRoutes
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import kotlin.test.assertTrue

/**
 * A named alias permits `null` when it says so itself (`nullable: true` next to `allOf`/`anyOf`/`oneOf:
 * [{$ref: X}]`, or on a plain typed schema, or `type: [T, "null"]`), when it has a `null` branch, or when its target
 * X does. Its model must then be nullable at every position it is used: the alias's own model, a property `$ref`, an
 * array item, a map value, a request body and a response body. Otherwise `null` on the wire cannot be decoded (or
 * sent) where the alias is resolved rather than referenced, or where it is referenced rather than resolved.
 */
val aliasOwnNullabilitySpec by testSuite {
    // X's typed schema, without braces, per target kind.
    val targets: List<Pair<String, String>> = listOf(
        "object" to """"type": "object", "properties": { "name": { "type": "string" } }, "required": ["name"], "additionalProperties": false""",
        "enum" to """"type": "string", "enum": ["red", "green"]""",
        "primitive" to """"type": "string"""",
        "collection" to """"type": "array", "items": { "type": "string" }""",
    )

    val x = $$"""{ "$ref": "#/components/schemas/X" }"""
    val nullBranch = """{ "type": "null" }"""
    fun typeArrayWithNull(t: String): String = t.replaceFirst(Regex(""""type": "(\w+)""""), """"type": ["$1", "null"]""")

    // label to a function from X's typed schema to (Alias schema, X schema).
    val nullableForms: List<Pair<String, (String) -> Pair<String, String>>> = listOf(
        "allOf: [\$ref X], nullable: true" to { t -> """{ "allOf": [$x], "nullable": true }""" to "{ $t }" },
        "anyOf: [\$ref X], nullable: true" to { t -> """{ "anyOf": [$x], "nullable": true }""" to "{ $t }" },
        "oneOf: [\$ref X], nullable: true" to { t -> """{ "oneOf": [$x], "nullable": true }""" to "{ $t }" },
        "allOf: [\$ref X, null]" to { t -> """{ "allOf": [$x, $nullBranch] }""" to "{ $t }" },
        "anyOf: [\$ref X, null]" to { t -> """{ "anyOf": [$x, $nullBranch] }""" to "{ $t }" },
        "oneOf: [null, \$ref X]" to { t -> """{ "oneOf": [$nullBranch, $x] }""" to "{ $t }" },
        "allOf: [\$ref X] to a nullable: true X" to { t -> """{ "allOf": [$x] }""" to """{ $t, "nullable": true }""" },
        "anyOf: [\$ref X] to a nullable: true X" to { t -> """{ "anyOf": [$x] }""" to """{ $t, "nullable": true }""" },
        "oneOf: [\$ref X] to a nullable: true X" to { t -> """{ "oneOf": [$x] }""" to """{ $t, "nullable": true }""" },
        "allOf: [\$ref X] to a type: [T, null] X" to { t -> """{ "allOf": [$x] }""" to "{ ${typeArrayWithNull(t)} }" },
        "allOf: [\$ref X] to an anyOf: [T, null] X" to { t -> """{ "allOf": [$x] }""" to """{ "anyOf": [{ $t }, $nullBranch] }""" },
        "bare \$ref X to a nullable: true X" to { t -> x to """{ $t, "nullable": true }""" },
        "T, nullable: true" to { t -> """{ $t, "nullable": true }""" to "{ $t }" },
        "type: [T, null]" to { t -> "{ ${typeArrayWithNull(t)} }" to "{ $t }" },
    )

    val nonNullForms: List<Pair<String, (String) -> Pair<String, String>>> = listOf(
        "allOf: [\$ref X]" to { t -> """{ "allOf": [$x] }""" to "{ $t }" },
        "anyOf: [\$ref X]" to { t -> """{ "anyOf": [$x] }""" to "{ $t }" },
        "oneOf: [\$ref X]" to { t -> """{ "oneOf": [$x] }""" to "{ $t }" },
        "bare \$ref X" to { t -> x to "{ $t }" },
        "T" to { t -> "{ $t }" to "{ $t }" },
    )

    fun document(alias: String, target: String): String = $$"""
        {
          "openapi": "3.0.3",
          "info": { "title": "test", "version": "1.0.0" },
          "paths": {
            "/alias": {
              "post": {
                "operationId": "alias",
                "requestBody": { "required": true, "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Alias" } } } },
                "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Alias" } } } } }
              }
            }
          },
          "components": {
            "schemas": {
              "X": $$target,
              "Alias": $$alias,
              "Holder": {
                "type": "object",
                "properties": {
                  "alias": { "$ref": "#/components/schemas/Alias" },
                  "items": { "type": "array", "items": { "$ref": "#/components/schemas/Alias" } },
                  "values": { "type": "object", "additionalProperties": { "$ref": "#/components/schemas/Alias" } }
                },
                "required": ["alias", "items", "values"]
              }
            }
          }
        }
    """.trimIndent()

    suspend fun Registry.component(name: String): Model =
        ReferenceOr.schema(name).toModel(NamingContext.reference(name, SchemaContext.Null), SchemaContext.Write)

    /** The model of `Alias` at every position it is used. */
    suspend fun positions(alias: String, target: String): Map<String, Model> {
        val api = OpenAPI.fromJson(document(alias, target))
        return Registry(api).use { registry ->
            val holder = (registry.component("Holder") as Model.Object).properties
            val route = with(registry) { api.toRoutes() }.single()
            val json = ContentType.Application.Json
            mapOf(
                "definition" to registry.component("Alias"),
                "property" to holder.getValue("alias").model,
                "array item" to (holder.getValue("items").model as Model.Collection).inner,
                "map value" to (holder.getValue("values").model as Model.Dictionary).inner,
                "request body" to (route.body!!.types.getValue(json) as Route.Body.SetBody).type,
                "response body" to route.returns.responses.getValue(HttpStatusCode.OK).types.getValue(json),
            )
        }
    }

    targets.forEach { [kind, target] ->
        nullableForms.forEach { [label, form] ->
            test("Alias $label to $kind is nullable at every position") {
                val [alias, xSchema] = form(target)
                val nonNull = positions(alias, xSchema).filterValues { !it.isNullable }
                assertTrue(nonNull.isEmpty(), "Alias should be nullable, but is non-null at ${nonNull.keys}: $nonNull")
            }
        }

        nonNullForms.forEach { [label, form] ->
            test("Alias $label to $kind is non-null at every position") {
                val [alias, xSchema] = form(target)
                val nullable = positions(alias, xSchema).filterValues { it.isNullable }
                assertTrue(nullable.isEmpty(), "Alias should be non-null, but is nullable at ${nullable.keys}: $nullable")
            }
        }
    }

    // The same alias forms spelled inline, where the `nullable: true` sits next to `allOf`/`anyOf`/`oneOf` at the use site.
    listOf("allOf", "anyOf", "oneOf").forEach { composite ->
        targets.forEach { [kind, target] ->
            test("Inline $composite: [\$ref X], nullable: true to $kind is nullable as a property, array item and map value") {
                val inline = """{ "$composite": [$x], "nullable": true }"""
                val api = OpenAPI.fromJson(
                    document(x, "{ $target }").replace(
                        $$"""{ "$ref": "#/components/schemas/Alias" }""",
                        inline
                    )
                )
                val holder = Registry(api).use { registry ->
                    (registry.component("Holder") as Model.Object).properties
                }
                val models = mapOf(
                    "property" to holder.getValue("alias").model,
                    "array item" to (holder.getValue("items").model as Model.Collection).inner,
                    "map value" to (holder.getValue("values").model as Model.Dictionary).inner,
                )
                val nonNull = models.filterValues { !it.isNullable }
                assertTrue(nonNull.isEmpty(), "should be nullable, but is non-null at ${nonNull.keys}: $nonNull")
            }
        }
    }
}
