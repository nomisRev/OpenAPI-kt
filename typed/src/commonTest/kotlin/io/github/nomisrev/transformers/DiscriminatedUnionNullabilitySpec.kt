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
 * A discriminated `oneOf`/`anyOf` (`discriminator: { propertyName, mapping }`) that permits `null` is nullable at every
 * position it is used (its own model, a property `$ref`, an array item, a map value, a request body and a response
 * body), while its cases stay exactly the discriminated cases it has without the `null`: `null` carries no
 * discriminator, so it can only be the union's own nullability, never a case. That holds whether the `null` is a
 * branch of the union, the union's own `nullable: true` or `type: [object, null]`, or a member the mapping points at
 * that permits `null` itself (`oneOf`/`anyOf: [$ref Cat, null]`, `allOf: [$ref Cat], nullable: true`,
 * `type: [object, null]`). Without the latter the union was non-null everywhere, and a `oneOf: [$ref Cat, null]`
 * member became a case wrapping `Cat?` that encoded the discriminator twice.
 */
val discriminatedUnionNullabilitySpec by testSuite {
    val cat = $$"""{ "$ref": "#/components/schemas/Cat" }"""
    val dog = $$"""{ "$ref": "#/components/schemas/Dog" }"""
    val nullBranch = """{ "type": "null" }"""
    fun discriminator(catRef: String = "Cat", tagged: Boolean = false): String {
        val kitty = if (tagged) $$""", "kitty": "#/components/schemas/$$catRef"""" else ""
        return $$""""discriminator": { "propertyName": "petType", "mapping": { "cat": "#/components/schemas/$$catRef"$$kitty, "dog": "#/components/schemas/Dog" } }"""
    }
    val catObject =
        """"type": "object", "additionalProperties": false, "properties": { "petType": { "type": "string" }, "meow": { "type": "string" } }, "required": ["petType", "meow"]"""
    val catTypeArrayWithNull = catObject.replace(""""type": "object"""", """"type": ["object", "null"]""")
    val nullableCat = $$"""{ "$ref": "#/components/schemas/NullableCat" }"""

    data class Form(val version: String, val pet: String, val nullableCat: String? = null)

    fun baseline(composite: String, tagged: Boolean = false) =
        Form("3.1.0", """{ "$composite": [$cat, $dog], ${discriminator(tagged = tagged)} }""")

    // label to (form, the non-null form whose definition it must equal once its nullability is dropped).
    val nullableForms: List<Pair<String, Pair<Form, Form>>> = listOf(
        "oneOf: [Cat, Dog, null]" to (Form("3.1.0", """{ "oneOf": [$cat, $dog, $nullBranch], ${discriminator()} }""") to baseline("oneOf")),
        "oneOf: [null, Cat, Dog]" to (Form("3.1.0", """{ "oneOf": [$nullBranch, $cat, $dog], ${discriminator()} }""") to baseline("oneOf")),
        "anyOf: [Cat, Dog, null]" to (Form("3.1.0", """{ "anyOf": [$cat, $dog, $nullBranch], ${discriminator()} }""") to baseline("anyOf")),
        "oneOf: [Cat, Dog], nullable: true" to (Form("3.0.3", """{ "oneOf": [$cat, $dog], "nullable": true, ${discriminator()} }""") to baseline("oneOf")),
        "anyOf: [Cat, Dog], nullable: true" to (Form("3.0.3", """{ "anyOf": [$cat, $dog], "nullable": true, ${discriminator()} }""") to baseline("anyOf")),
        "type: [object, null], oneOf: [Cat, Dog]" to (Form("3.1.0", """{ "type": ["object", "null"], "oneOf": [$cat, $dog], ${discriminator()} }""") to baseline("oneOf")),
        "tagged oneOf: [Cat, Dog, null]" to (Form("3.1.0", """{ "oneOf": [$cat, $dog, $nullBranch], ${discriminator(tagged = true)} }""") to baseline("oneOf", tagged = true)),
        "oneOf: [NullableCat, Dog], NullableCat: oneOf: [Cat, null]" to (Form(
            "3.1.0",
            """{ "oneOf": [$nullableCat, $dog], ${discriminator("NullableCat")} }""",
            """{ "oneOf": [$cat, $nullBranch] }"""
        ) to baseline("oneOf")),
        "oneOf: [NullableCat, Dog], NullableCat: oneOf: [null, Cat]" to (Form(
            "3.1.0",
            """{ "oneOf": [$nullableCat, $dog], ${discriminator("NullableCat")} }""",
            """{ "oneOf": [$nullBranch, $cat] }"""
        ) to baseline("oneOf")),
        "anyOf: [NullableCat, Dog], NullableCat: anyOf: [Cat, null]" to (Form(
            "3.1.0",
            """{ "anyOf": [$nullableCat, $dog], ${discriminator("NullableCat")} }""",
            """{ "anyOf": [$cat, $nullBranch] }"""
        ) to baseline("anyOf")),
        "oneOf: [NullableCat, Dog], NullableCat: allOf: [Cat], nullable: true" to (Form(
            "3.0.3",
            """{ "oneOf": [$nullableCat, $dog], ${discriminator("NullableCat")} }""",
            """{ "allOf": [$cat], "nullable": true }"""
        ) to baseline("oneOf")),
        "oneOf: [NullableCat, Dog], NullableCat: object, nullable: true" to (Form(
            "3.0.3",
            """{ "oneOf": [$nullableCat, $dog], ${discriminator("NullableCat")} }""",
            """{ $catObject, "nullable": true }"""
        ) to baseline("oneOf")),
        "oneOf: [NullableCat, Dog], NullableCat: type: [object, null]" to (Form(
            "3.1.0",
            """{ "oneOf": [$nullableCat, $dog], ${discriminator("NullableCat")} }""",
            "{ $catTypeArrayWithNull }"
        ) to baseline("oneOf")),
    )

    val taggedNullableMember = Form(
        "3.1.0",
        """{ "oneOf": [$nullableCat, $dog], ${discriminator("NullableCat", tagged = true)} }""",
        """{ "oneOf": [$cat, $nullBranch] }"""
    )

    val nonNullForms: List<Pair<String, Form>> = listOf(
        "oneOf: [Cat, Dog]" to baseline("oneOf"),
        "anyOf: [Cat, Dog]" to baseline("anyOf"),
        "tagged oneOf: [Cat, Dog]" to baseline("oneOf", tagged = true),
        "oneOf: [NonNullCat, Dog], NonNullCat: allOf: [Cat]" to Form(
            "3.1.0",
            """{ "oneOf": [$nullableCat, $dog], ${discriminator("NullableCat")} }""",
            """{ "allOf": [$cat] }"""
        ),
    )

    fun document(form: Form): String = $$"""
        {
          "openapi": "$${form.version}",
          "info": { "title": "test", "version": "1.0.0" },
          "paths": {
            "/pet": {
              "post": {
                "operationId": "pet",
                "requestBody": { "required": true, "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Pet" } } } },
                "responses": { "200": { "description": "ok", "content": { "application/json": { "schema": { "$ref": "#/components/schemas/Pet" } } } } }
              }
            }
          },
          "components": {
            "schemas": {
              "Pet": $${form.pet},
              "Cat": { $$catObject },
              "Dog": { "type": "object", "additionalProperties": false, "properties": { "petType": { "type": "string" }, "bark": { "type": "string" } }, "required": ["petType", "bark"] },
              $${form.nullableCat?.let { "\"NullableCat\": $it," }.orEmpty()}
              "Holder": {
                "type": "object",
                "properties": {
                  "pet": { "$ref": "#/components/schemas/Pet" },
                  "pets": { "type": "array", "items": { "$ref": "#/components/schemas/Pet" } },
                  "petMap": { "type": "object", "additionalProperties": { "$ref": "#/components/schemas/Pet" } }
                },
                "required": ["pet", "pets", "petMap"]
              }
            }
          }
        }
    """.trimIndent()

    suspend fun Registry.component(name: String): Model =
        ReferenceOr.schema(name).toModel(NamingContext.reference(name, SchemaContext.Null), SchemaContext.Write)

    /** The model of `Pet` at every position it is used. */
    suspend fun positions(form: Form): Map<String, Model> {
        val api = OpenAPI.fromJson(document(form))
        return Registry(api).use { registry ->
            val holder = (registry.component("Holder") as Model.Object).properties
            val route = with(registry) { api.toRoutes() }.single()
            val json = ContentType.Application.Json
            mapOf(
                "definition" to registry.component("Pet"),
                "property" to holder.getValue("pet").model,
                "array item" to (holder.getValue("pets").model as Model.Collection).inner,
                "map value" to (holder.getValue("petMap").model as Model.Dictionary).inner,
                "request body" to (route.body!!.types.getValue(json) as Route.Body.SetBody).type,
                "response body" to route.returns.responses.getValue(HttpStatusCode.OK).types.getValue(json),
            )
        }
    }

    nullableForms.forEach { [label, forms] ->
        val [form, nonNull] = forms
        test("Discriminated $label is nullable at every position") {
            val models = positions(form)
            val nonNullPositions = models.filterValues { !it.isNullable }
            assertTrue(nonNullPositions.isEmpty(), "Pet should be nullable, but is non-null at ${nonNullPositions.keys}: $nonNullPositions")
        }

        test("Discriminated $label keeps the cases of its non-null form") {
            val expected = positions(nonNull).getValue("definition")
            val actual = positions(form).getValue("definition")
            assertEquals(expected, actual.with(isNullable = false))
        }
    }

    test("Tagged discriminated oneOf: [NullableCat, Dog], NullableCat: oneOf: [Cat, null] is nullable with non-null object cases") {
        val models = positions(taggedNullableMember)
        val nonNullPositions = models.filterValues { !it.isNullable }
        assertTrue(nonNullPositions.isEmpty(), "Pet should be nullable, but is non-null at ${nonNullPositions.keys}: $nonNullPositions")
        val definition = models.getValue("definition") as Model.OneOf
        assertEquals(UnionDispatch.TaggedCustom("petType"), definition.dispatch)
        assertEquals(listOf(setOf("cat", "kitty"), setOf("dog")), definition.cases.map { it.discriminatorValues })
        definition.cases.forEach { case ->
            assertTrue(case.model is Model.Object && !case.model.isNullable, "Case should be a non-null object: ${case.model}")
        }
    }

    nonNullForms.forEach { [label, form] ->
        test("Discriminated $label is non-null at every position") {
            val nullable = positions(form).filterValues { it.isNullable }
            assertTrue(nullable.isEmpty(), "Pet should be non-null, but is nullable at ${nullable.keys}: $nullable")
        }
    }
}
