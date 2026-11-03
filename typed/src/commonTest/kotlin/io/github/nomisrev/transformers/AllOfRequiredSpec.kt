package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.SchemaContext
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * An instance validates against `allOf` only when it validates against every member, so a property is required when
 * any member requires it: the merged `required` is the union of every member's `required`, whichever member declares
 * the property and in whichever order the members are listed. Every case is checked for the `allOf` itself (`Merged`)
 * and for an inline `allOf` property (`Holder.p`), in both member orders. A `required` naming a property no member
 * declares is ignored, as it is for a standalone object. Next to a property `$ref` (`RefProperty.p`) a `required`-only
 * declaration keeps the `$ref` when the referenced schema already requires those properties, and is otherwise the
 * inline merge of the referenced schema, since the generated type of the `$ref` would leave them optional.
 */
val allOfRequiredSpec by testSuite {
    fun ref(name: String): String = $$"""{ "$ref": "#/components/schemas/$$name" }"""

    val pet = """
        {
          "type": "object",
          "properties": { "id": { "type": "integer" }, "name": { "type": "string" } },
          "required": ["id"]
        }
    """.trimIndent()

    fun document(first: String, second: String): String = """
        {
          "openapi": "3.0.3",
          "info": { "title": "test", "version": "1.0.0" },
          "paths": {},
          "components": {
            "schemas": {
              "Pet": $pet,
              "Merged": { "allOf": [$first, $second] },
              "Holder": { "type": "object", "properties": { "p": { "allOf": [$first, $second] } } },
              "RefProperty": {
                "allOf": [
                  { "type": "object", "properties": { "p": $first } },
                  { "type": "object", "properties": { "p": $second } }
                ]
              }
            }
          }
        }
    """.trimIndent()

    suspend fun model(name: String, first: String, second: String, context: SchemaContext): Model =
        Registry(OpenAPI.fromJson(document(first, second))).use { registry ->
            with(registry) {
                ReferenceOr.schema(name).toModel(NamingContext.reference(name, SchemaContext.Null), context)
            }
        }

    /** The `allOf` of [first] and [second] itself, and as the inline `allOf` property `Holder.p`. */
    suspend fun models(first: String, second: String, context: SchemaContext): List<Model.Object> = listOf(
        assertIs<Model.Object>(model("Merged", first, second, context)),
        assertIs<Model.Object>(
            assertIs<Model.Object>(model("Holder", first, second, context)).properties.getValue("p").model
        ),
    )

    fun Model.Object.required(): Set<String> = properties.filterValues { it.isRequired }.keys

    /** Checks [first] and [second] at the top level and as a property, in both member orders. */
    fun merged(
        label: String,
        first: String,
        second: String,
        context: SchemaContext = SchemaContext.Write,
        check: (Model.Object) -> Unit,
    ) {
        listOf(first to second, second to first).forEachIndexed { index, [a, b] ->
            test("allOf $label${if (index == 1) ", members reversed" else ""}") {
                models(a, b, context).forEach(check)
            }
        }
    }

    val propertyA = """{ "type": "object", "properties": { "a": { "type": "string" } } }"""

    merged("required-only member makes a declared property required", propertyA, """{ "required": ["a"] }""") {
        assertEquals(setOf("a"), it.required())
    }

    merged(
        "required of both members is the union",
        """{ "type": "object", "properties": { "a": { "type": "string" } }, "required": ["a"] }""",
        """{ "type": "object", "properties": { "b": { "type": "string" } }, "required": ["b"] }"""
    ) {
        assertEquals(setOf("a", "b"), it.required())
    }

    merged(
        "required of the member without the property applies to it",
        """{ "properties": { "a": { "type": "string" } } }""",
        """{ "properties": { "b": { "type": "string" } }, "required": ["a", "b"] }"""
    ) {
        assertEquals(setOf("a", "b"), it.required())
    }

    merged(
        "\$ref and an object with required properties requires both",
        ref("Pet"),
        """{ "type": "object", "properties": { "extra": { "type": "string" } }, "required": ["extra"] }"""
    ) {
        assertEquals(setOf("id", "name", "extra"), it.properties.keys)
        assertEquals(setOf("id", "extra"), it.required())
    }

    merged("\$ref and required-only member requires a parent property", ref("Pet"), """{ "required": ["name"] }""") {
        assertEquals(setOf("id", "name"), it.properties.keys)
        assertEquals(setOf("id", "name"), it.required())
    }

    merged(
        "required nullable property is required and nullable",
        """{ "type": "object", "properties": { "a": { "type": "string", "nullable": true } } }""",
        """{ "required": ["a"] }"""
    ) {
        val a = it.properties.getValue("a")
        assertTrue(a.isRequired)
        assertTrue(assertIs<Model.Primitive.String>(a.model).isNullable)
    }

    // As for a standalone object, `required` of a property no member declares adds no property.
    merged("required of an undeclared property is ignored", propertyA, """{ "required": ["a", "missing"] }""") {
        assertEquals(setOf("a"), it.properties.keys)
        assertEquals(setOf("a"), it.required())
    }

    val readOnlyId = """{ "type": "object", "properties": { "id": { "type": "string", "readOnly": true } } }"""
    merged("required readOnly property is required when read", readOnlyId, """{ "required": ["id"] }""", SchemaContext.Read) {
        assertEquals(setOf("id"), it.required())
    }
    merged("required readOnly property is absent when written", readOnlyId, """{ "required": ["id"] }""", SchemaContext.Write) {
        assertEquals(emptySet(), it.properties.keys)
    }

    // A property `$ref` next to a `required`-only declaration.
    fun refProperty(label: String, first: String, second: String, check: (Model) -> Unit) {
        listOf(first to second, second to first).forEachIndexed { index, [a, b] ->
            test("allOf property $label${if (index == 1) ", members reversed" else ""}") {
                val holder = assertIs<Model.Object>(model("RefProperty", a, b, SchemaContext.Write))
                check(holder.properties.getValue("p").model)
            }
        }
    }

    refProperty("\$ref and required of a property it already requires stays the \$ref", ref("Pet"), """{ "required": ["id"] }""") {
        assertEquals("Pet", (assertIs<Model.Reference>(it).context.head as NamingContext.Reference).name)
    }

    refProperty("\$ref and required of a property it leaves optional requires it", ref("Pet"), """{ "required": ["name"] }""") {
        assertEquals(setOf("id", "name"), assertIs<Model.Object>(it).required())
    }
}
