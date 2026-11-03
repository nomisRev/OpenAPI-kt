package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.UnionDispatch
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
 * A `oneOf`/`anyOf` without a `discriminator` keyword permits `null` when one of its members does: `null` validates
 * against the member, so it validates against the union. The union is nullable at every position it is used (its own
 * model, a property `$ref`, an inline property, an array item, a map value, a request body and a response body), and
 * its cases are the members' non-null forms: `null` is decoded as the union's own `null` before any case is tried, so
 * a case can never hold it. That holds for a `$ref` to a nullable alias (`anyOf`/`oneOf: [string, null]`,
 * `type: [string, null]`, `nullable: true`), an inline `nullable: true` or `type: [string, null]` member, a `$ref` to
 * `oneOf: [$ref Cat, null]`, and for a union whose discriminator is inferred from its members' single-value `type`
 * property, which now sees through the nullable member. Without it the union was non-null everywhere and the member
 * became a case wrapping `X?`, so JSON `null` decoded as that case (or failed to decode at all).
 */
val unionNullableMemberSpec by testSuite {
    val nullableString = $$"""{ "$ref": "#/components/schemas/NullableString" }"""
    val nullableCat = $$"""{ "$ref": "#/components/schemas/NullableCat" }"""
    val cat = $$"""{ "$ref": "#/components/schemas/Cat" }"""
    val dog = $$"""{ "$ref": "#/components/schemas/Dog" }"""
    val integer = """{ "type": "integer" }"""

    data class Form(
        val version: String,
        val value: String,
        val nullableString: String = """{ "type": "string" }""",
        val nullableCat: String = """{ "allOf": [$cat] }""",
    )

    fun refMember(composite: String, nullableString: String, version: String = "3.1.0") =
        Form(version, """{ "$composite": [$nullableString, $integer] }""", nullableString)

    val stringAlias = """{ "type": "string" }"""

    // label to (form, the non-null form whose definition it must equal once its nullability is dropped).
    val nullableForms: List<Pair<String, Pair<Form, Form>>> = listOf(
        "oneOf: [NullableString, integer], NullableString: anyOf: [string, null]" to
                (refMember("oneOf", """{ "anyOf": [{ "type": "string" }, { "type": "null" }] }""") to
                        refMember("oneOf", stringAlias)),
        "oneOf: [NullableString, integer], NullableString: oneOf: [null, string]" to
                (refMember("oneOf", """{ "oneOf": [{ "type": "null" }, { "type": "string" }] }""") to
                        refMember("oneOf", stringAlias)),
        "oneOf: [NullableString, integer], NullableString: type: [string, null]" to
                (refMember("oneOf", """{ "type": ["string", "null"] }""") to refMember("oneOf", stringAlias)),
        "oneOf: [NullableString, integer], NullableString: string, nullable: true" to
                (refMember("oneOf", """{ "type": "string", "nullable": true }""", "3.0.3") to
                        refMember("oneOf", stringAlias, "3.0.3")),
        "anyOf: [NullableString, integer], NullableString: anyOf: [string, null]" to
                (refMember("anyOf", """{ "anyOf": [{ "type": "string" }, { "type": "null" }] }""") to
                        refMember("anyOf", stringAlias)),
        "oneOf: [{string, nullable: true}, integer]" to
                (Form("3.0.3", """{ "oneOf": [{ "type": "string", "nullable": true }, $integer] }""") to
                        Form("3.0.3", """{ "oneOf": [{ "type": "string" }, $integer] }""")),
        "anyOf: [{type: [string, null]}, integer]" to
                (Form("3.1.0", """{ "anyOf": [{ "type": ["string", "null"] }, $integer] }""") to
                        Form("3.1.0", """{ "anyOf": [{ "type": "string" }, $integer] }""")),
        "oneOf: [NullableString, {type: [integer, null]}] (two members permit null)" to
                (Form(
                    "3.1.0",
                    """{ "oneOf": [$nullableString, { "type": ["integer", "null"] }] }""",
                    """{ "anyOf": [{ "type": "string" }, { "type": "null" }] }"""
                ) to Form("3.1.0", """{ "oneOf": [$nullableString, $integer] }""")),
        "oneOf: [NullableCat, string], NullableCat: oneOf: [Cat, null]" to
                (Form(
                    "3.1.0",
                    """{ "oneOf": [$nullableCat, { "type": "string" }] }""",
                    nullableCat = """{ "oneOf": [$cat, { "type": "null" }] }"""
                ) to Form("3.1.0", """{ "oneOf": [$nullableCat, { "type": "string" }] }""")),
        "inferred discriminator oneOf: [NullableCat, Dog], NullableCat: oneOf: [Cat, null]" to
                (Form(
                    "3.1.0",
                    """{ "oneOf": [$nullableCat, $dog] }""",
                    nullableCat = """{ "oneOf": [$cat, { "type": "null" }] }"""
                ) to Form("3.1.0", """{ "oneOf": [$cat, $dog] }""")),
        "inferred discriminator anyOf: [NullableCat, Dog], NullableCat: anyOf: [null, Cat]" to
                (Form(
                    "3.1.0",
                    """{ "anyOf": [$nullableCat, $dog] }""",
                    nullableCat = """{ "anyOf": [{ "type": "null" }, $cat] }"""
                ) to Form("3.1.0", """{ "anyOf": [$cat, $dog] }""")),
    )

    val nonNullForms: List<Pair<String, Form>> = listOf(
        "oneOf: [string, integer]" to Form("3.1.0", """{ "oneOf": [{ "type": "string" }, $integer] }"""),
        "anyOf: [NonNullString, integer]" to refMember("anyOf", stringAlias),
        "oneOf: [NonNullCat, string], NonNullCat: allOf: [Cat]" to
                Form("3.1.0", """{ "oneOf": [$nullableCat, { "type": "string" }] }"""),
        "inferred discriminator oneOf: [Cat, Dog]" to Form("3.1.0", """{ "oneOf": [$cat, $dog] }"""),
    )

    fun document(form: Form): String = $$"""
        {
          "openapi": "$${form.version}",
          "info": { "title": "test", "version": "1.0.0" },
          "paths": {
            "/value": {
              "post": {
                "operationId": "value",
                "requestBody": { "required": true, "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Value" } } } },
                "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Value" } } } } }
              }
            }
          },
          "components": {
            "schemas": {
              "Value": $${form.value},
              "NullableString": $${form.nullableString},
              "NullableCat": $${form.nullableCat},
              "Cat": { "type": "object", "additionalProperties": false, "properties": { "type": { "type": "string", "enum": ["cat"] }, "meow": { "type": "string" } }, "required": ["type", "meow"] },
              "Dog": { "type": "object", "additionalProperties": false, "properties": { "type": { "type": "string", "enum": ["dog"] }, "bark": { "type": "string" } }, "required": ["type", "bark"] },
              "Holder": {
                "type": "object",
                "properties": {
                  "value": { "$ref": "#/components/schemas/Value" },
                  "inlineValue": $${form.value},
                  "values": { "type": "array", "items": { "$ref": "#/components/schemas/Value" } },
                  "valueMap": { "type": "object", "additionalProperties": { "$ref": "#/components/schemas/Value" } }
                },
                "required": ["value", "inlineValue", "values", "valueMap"]
              }
            }
          }
        }
    """.trimIndent()

    suspend fun Registry.component(name: String): Model =
        ReferenceOr.schema(name).toModel(NamingContext.reference(name, SchemaContext.Null), SchemaContext.Write)

    /** The model of `Value` at every position it is used. */
    suspend fun positions(form: Form): Map<String, Model> {
        val api = OpenAPI.fromJson(document(form))
        return Registry(api).use { registry ->
            val holder = (registry.component("Holder") as Model.Object).properties
            val route = with(registry) { api.toRoutes() }.single()
            val json = ContentType.Application.Json
            mapOf(
                "definition" to registry.component("Value"),
                "property" to holder.getValue("value").model,
                "inline property" to holder.getValue("inlineValue").model,
                "array item" to (holder.getValue("values").model as Model.Collection).inner,
                "map value" to (holder.getValue("valueMap").model as Model.Dictionary).inner,
                "request body" to (route.body!!.types.getValue(json) as Route.Body.SetBody).type,
                "response body" to route.returns.responses.getValue(HttpStatusCode.OK).types.getValue(json),
            )
        }
    }

    nullableForms.forEach { [label, forms] ->
        val [form, nonNull] = forms
        test("Union $label is nullable at every position") {
            val models = positions(form)
            val nonNullPositions = models.filterValues { !it.isNullable }
            assertTrue(
                nonNullPositions.isEmpty(),
                "Value should be nullable, but is non-null at ${nonNullPositions.keys}: $nonNullPositions"
            )
        }

        test("Union $label has the non-null cases of its non-null form") {
            val actual = positions(form).getValue("definition") as Model.Union
            actual.cases.forEach { case ->
                assertTrue(!case.model.isNullable, "Case should be non-null: ${case.model}")
            }
            val expected = positions(nonNull).getValue("definition")
            assertEquals(expected, actual.with(isNullable = false))
        }
    }

    test("Inferred discriminator oneOf: [NullableCat, Dog], NullableCat: oneOf: [Cat, null] dispatches on type") {
        val definition = positions(
            Form("3.1.0", """{ "oneOf": [$nullableCat, $dog] }""", nullableCat = """{ "oneOf": [$cat, { "type": "null" }] }""")
        ).getValue("definition") as Model.OneOf
        assertEquals(UnionDispatch.NativeDiscriminator("type"), definition.dispatch)
        assertEquals(listOf(setOf("cat"), setOf("dog")), definition.cases.map { it.discriminatorValues })
    }

    nonNullForms.forEach { [label, form] ->
        test("Union $label is non-null at every position") {
            val nullable = positions(form).filterValues { it.isNullable }
            assertTrue(nullable.isEmpty(), "Value should be non-null, but is nullable at ${nullable.keys}: $nullable")
        }
    }
}
