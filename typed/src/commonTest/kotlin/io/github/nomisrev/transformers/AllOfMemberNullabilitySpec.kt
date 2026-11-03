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
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * An instance validates against `allOf` only when it validates against every member, so a multi-member `allOf`
 * permits `null` only when every member does (`nullable: true`, `type: [T, "null"]`, a `null` branch, or a `$ref` to
 * such a schema), or when the `allOf` schema says so itself. A member that only annotates (`description`, or a
 * keyword such as `maxLength` that does not apply to `null`) does not exclude `null`. The merged model must be
 * nullable exactly then, at every position it is used: its own model, a property `$ref`, an array item, a map value,
 * a request body and a response body.
 */
val allOfMemberNullabilitySpec by testSuite {
    fun ref(name: String): String = $$"""{ "$ref": "#/components/schemas/$$name" }"""

    val extraObject = """{ "type": "object", "properties": { "extra": { "type": "string" } } }"""
    val nullableExtraObject = """{ "type": "object", "nullable": true, "properties": { "extra": { "type": "string" } } }"""
    val typeArrayExtraObject = """{ "type": ["object", "null"], "properties": { "extra": { "type": "string" } } }"""

    // Components every document has; members reference them.
    val components = mapOf(
        "Owner" to """{ "type": "object", "properties": { "name": { "type": "string" } }, "required": ["name"] }""",
        "NullableOwner" to """{ "type": "object", "nullable": true, "properties": { "name": { "type": "string" } }, "required": ["name"] }""",
        "TypeArrayOwner" to """{ "type": ["object", "null"], "properties": { "name": { "type": "string" } }, "required": ["name"] }""",
        "AnyOfNullOwner" to """{ "anyOf": [${ref("Owner")}, { "type": "null" }] }""",
        "AllOfNullableAliasOwner" to """{ "allOf": [${ref("Owner")}], "nullable": true }""",
        "MyString" to """{ "type": "string" }""",
        "NullableString" to """{ "type": "string", "nullable": true }""",
    )

    // label to the `Merged` schema.
    val nullableForms: List<Pair<String, String>> = listOf(
        "[\$ref nullable, \$ref nullable]" to """{ "allOf": [${ref("NullableOwner")}, ${ref("AllOfNullableAliasOwner")}] }""",
        "[\$ref nullable, inline nullable]" to """{ "allOf": [${ref("NullableOwner")}, $nullableExtraObject] }""",
        "[\$ref type array, inline type array]" to """{ "allOf": [${ref("TypeArrayOwner")}, $typeArrayExtraObject] }""",
        "[\$ref anyOf [X, null], \$ref nullable]" to """{ "allOf": [${ref("AnyOfNullOwner")}, ${ref("NullableOwner")}] }""",
        "[inline nullable, \$ref allOf [X] nullable alias]" to """{ "allOf": [$nullableExtraObject, ${ref("AllOfNullableAliasOwner")}] }""",
        "[\$ref nullable, description-only]" to """{ "allOf": [${ref("NullableOwner")}, { "description": "An owner" }] }""",
        "[\$ref nullable string, maxLength-only]" to """{ "allOf": [${ref("NullableString")}, { "maxLength": 3 }] }""",
        "[\$ref nullable string, inline nullable string]" to """{ "allOf": [${ref("NullableString")}, { "type": "string", "nullable": true, "maxLength": 3 }] }""",
        "non-null members, own nullable: true" to """{ "allOf": [${ref("Owner")}, $extraObject], "nullable": true }""",
        "some nullable members, own nullable: true" to """{ "allOf": [${ref("NullableOwner")}, $extraObject], "nullable": true }""",
        "non-null members, own type: [object, null]" to """{ "type": ["object", "null"], "allOf": [${ref("Owner")}, $extraObject] }""",
    )

    val nonNullForms: List<Pair<String, String>> = listOf(
        "[\$ref nullable, inline non-null]" to """{ "allOf": [${ref("NullableOwner")}, $extraObject] }""",
        "[inline non-null, \$ref nullable]" to """{ "allOf": [$extraObject, ${ref("NullableOwner")}] }""",
        "[inline nullable, \$ref non-null]" to """{ "allOf": [$nullableExtraObject, ${ref("Owner")}] }""",
        "[\$ref type array, inline non-null]" to """{ "allOf": [${ref("TypeArrayOwner")}, $extraObject] }""",
        "[inline type array, \$ref non-null]" to """{ "allOf": [$typeArrayExtraObject, ${ref("Owner")}] }""",
        "[\$ref anyOf [X, null], inline non-null]" to """{ "allOf": [${ref("AnyOfNullOwner")}, $extraObject] }""",
        "[\$ref allOf [X] nullable alias, inline non-null]" to """{ "allOf": [${ref("AllOfNullableAliasOwner")}, $extraObject] }""",
        "[\$ref nullable, \$ref non-null]" to """{ "allOf": [${ref("NullableOwner")}, ${ref("Owner")}] }""",
        "[\$ref nullable string, \$ref non-null string]" to """{ "allOf": [${ref("NullableString")}, ${ref("MyString")}] }""",
        "[\$ref nullable string, inline non-null string]" to """{ "allOf": [${ref("NullableString")}, { "type": "string", "maxLength": 3 }] }""",
        "[\$ref non-null, inline non-null]" to """{ "allOf": [${ref("Owner")}, $extraObject] }""",
        "[\$ref non-null, description-only]" to """{ "allOf": [${ref("Owner")}, { "description": "An owner" }] }""",
    )

    fun document(merged: String, extraComponents: Map<String, String> = emptyMap()): String {
        val schemas = (components + extraComponents + ("Merged" to merged))
            .entries.joinToString(",\n") { [name, schema] -> "\"$name\": $schema" }
        return $$"""
        {
          "openapi": "3.0.3",
          "info": { "title": "test", "version": "1.0.0" },
          "paths": {
            "/merged": {
              "post": {
                "operationId": "merged",
                "requestBody": { "required": true, "content": { "application/json": { "schema": $${ref("Merged")} } } },
                "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": $${ref("Merged")} } } } }
              }
            }
          },
          "components": {
            "schemas": {
              $$schemas,
              "Holder": {
                "type": "object",
                "properties": {
                  "merged": $${ref("Merged")},
                  "items": { "type": "array", "items": $${ref("Merged")} },
                  "values": { "type": "object", "additionalProperties": $${ref("Merged")} }
                },
                "required": ["merged", "items", "values"]
              }
            }
          }
        }
        """.trimIndent()
    }

    suspend fun Registry.component(name: String): Model =
        ReferenceOr.schema(name).toModel(NamingContext.reference(name, SchemaContext.Null), SchemaContext.Write)

    /** The model of `Merged` at every position it is used. */
    suspend fun positions(merged: String): Map<String, Model> {
        val api = OpenAPI.fromJson(document(merged))
        return Registry(api).use { registry ->
            val holder = (registry.component("Holder") as Model.Object).properties
            val route = with(registry) { api.toRoutes() }.single()
            val json = ContentType.Application.Json
            mapOf(
                "definition" to registry.component("Merged"),
                "property" to holder.getValue("merged").model,
                "array item" to (holder.getValue("items").model as Model.Collection).inner,
                "map value" to (holder.getValue("values").model as Model.Dictionary).inner,
                "request body" to (route.body!!.types.getValue(json) as Route.Body.SetBody).type,
                "response body" to route.returns.responses.getValue(HttpStatusCode.OK).types.getValue(json),
            )
        }
    }

    nullableForms.forEach { [label, merged] ->
        test("allOf $label is nullable at every position") {
            val nonNull = positions(merged).filterValues { !it.isNullable }
            assertTrue(nonNull.isEmpty(), "Merged should be nullable, but is non-null at ${nonNull.keys}: $nonNull")
        }
    }

    nonNullForms.forEach { [label, merged] ->
        test("allOf $label is non-null at every position") {
            val nullable = positions(merged).filterValues { it.isNullable }
            assertTrue(nullable.isEmpty(), "Merged should be non-null, but is nullable at ${nullable.keys}: $nullable")
        }
    }

    test("allOf of a nullable and a non-null member keeps every member's properties") {
        val api = OpenAPI.fromJson(document("""{ "allOf": [${ref("NullableOwner")}, $extraObject] }"""))
        val merged = Registry(api).use { registry -> registry.component("Merged") as Model.Object }
        assertEquals(setOf("name", "extra"), merged.properties.keys)
        assertTrue(merged.properties.getValue("name").isRequired)
    }

    // A property declared by several members must satisfy each member's property schema, which are merged.
    fun propertyMember(nullable: Boolean): String {
        val nullability = if (nullable) """, "nullable": true""" else ""
        return """{ "type": "object", "properties": { "p": { "type": "object", "properties": { "q": { "type": "string" } }$nullability } } }"""
    }

    listOf(
        Triple(true, true, true),
        Triple(true, false, false),
        Triple(false, true, false),
        Triple(false, false, false),
    ).forEach { [left, right, expected] ->
        test("allOf property declared nullable=$left and nullable=$right is ${if (expected) "nullable" else "non-null"}") {
            val api = OpenAPI.fromJson(
                document("""{ "allOf": [${propertyMember(left)}, ${propertyMember(right)}] }""")
            )
            val merged = Registry(api).use { registry -> registry.component("Merged") as Model.Object }
            assertEquals(expected, merged.properties.getValue("p").model.isNullable, "$merged")
        }
    }
}
