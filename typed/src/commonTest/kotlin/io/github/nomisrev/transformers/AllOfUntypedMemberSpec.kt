package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Constraints
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.SchemaContext
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * An `allOf` member without `type` that still shapes values, such as `{items: {...}}` next to `{type: array}`, is
 * merged as the type the rest of the pipeline models such a schema as: `items` is an array, `properties` or
 * `additionalProperties` an object, and an `enum` or `format` the type of the member next to it. Every case is checked
 * for the `allOf` itself (`Merged`) and for a property `p` that both members of `MergedProperty` declare, in both
 * member orders. Members whose implied types no value has both of are rejected with an error naming the schema or
 * property, instead of crashing with `TODO()`.
 */
val allOfUntypedMemberSpec by testSuite {
    fun member(property: String): String = """{ "type": "object", "properties": { "p": $property } }"""

    fun document(first: String, second: String): String = """
        {
          "openapi": "3.0.3",
          "info": { "title": "test", "version": "1.0.0" },
          "paths": {},
          "components": {
            "schemas": {
              "Merged": { "allOf": [$first, $second] },
              "MergedProperty": { "allOf": [${member(first)}, ${member(second)}] },
              "Names": { "type": "array", "items": { "type": "string", "minLength": 1 } }
            }
          }
        }
    """.trimIndent()

    suspend fun model(name: String, first: String, second: String): Model =
        Registry(OpenAPI.fromJson(document(first, second))).use { registry ->
            with(registry) {
                ReferenceOr.schema(name).toModel(NamingContext.reference(name, SchemaContext.Null), SchemaContext.Write)
            }
        }

    /**
     * The `allOf` of [first] and [second] itself, and as property `p` both members declare. The component `Merged`
     * merged to a collection, primitive or map is its value wrapper, like any named non-object schema: check what it
     * wraps.
     */
    suspend fun models(first: String, second: String): List<Model> = listOf(
        model("Merged", first, second).let { merged ->
            if (merged is Model.Object && merged.isTransparentWrapper) merged.properties.values.single().model else merged
        },
        assertIs<Model.Object>(model("MergedProperty", first, second)).properties.getValue("p").model,
    )

    /** Checks [first] and [second] at the top level and as a property, in both member orders. */
    fun merged(label: String, first: String, second: String, check: (Model) -> Unit) {
        listOf(first to second, second to first).forEachIndexed { index, [a, b] ->
            test("allOf $label${if (index == 1) ", members reversed" else ""}") { models(a, b).forEach(check) }
        }
    }

    fun rejected(label: String, first: String, second: String, vararg reason: String) {
        listOf(first to second, second to first).forEachIndexed { index, [a, b] ->
            listOf("Merged" to listOf("Merged"), "MergedProperty" to listOf("MergedProperty", "'p'")).forEach { [name, names] ->
                test("allOf $label is rejected for $name${if (index == 1) ", members reversed" else ""}") {
                    val error = assertFails { model(name, a, b) }
                    val message = error.message.orEmpty()
                    assertIs<IllegalArgumentException>(error, message)
                    assertTrue(
                        (names + reason).all { it in message },
                        "Expected an error naming ${names + reason}, but got ${error::class.simpleName}: $message"
                    )
                }
            }
        }
    }

    val string = """{ "type": "string" }"""
    val untypedItems = """{ "items": { "type": "string" } }"""

    // items without type is an array.
    merged("array and untyped items is an array of the items", """{ "type": "array" }""", untypedItems) { model ->
        val collection = assertIs<Model.Collection>(model)
        assertIs<Model.Primitive.String>(collection.inner)
    }

    merged(
        "typed array and untyped items intersect their items",
        """{ "type": "array", "items": { "type": "string", "minLength": 1 } }""",
        """{ "items": { "type": "string", "maxLength": 3 } }"""
    ) { model ->
        val inner = assertIs<Model.Primitive.String>(assertIs<Model.Collection>(model).inner)
        assertEquals(Constraints.Text(1, 3, null), inner.constraint)
    }

    merged(
        "two untyped items members is an array with both constraints",
        """{ "items": { "type": "integer", "format": "int32" }, "minItems": 1 }""",
        """{ "items": { "type": "integer" }, "maxItems": 5 }"""
    ) { model ->
        val collection = assertIs<Model.Collection>(model)
        assertIs<Model.Primitive.Int>(collection.inner)
        assertEquals(Constraints.Collection(1, 5, false), collection.constraint)
    }

    merged(
        "\$ref array and untyped items intersect their items",
        $$"""{ "$ref": "#/components/schemas/Names" }""",
        """{ "items": { "type": "string", "maxLength": 3 } }"""
    ) { model ->
        val inner = assertIs<Model.Primitive.String>(assertIs<Model.Collection>(model).inner)
        assertEquals(Constraints.Text(1, 3, null), inner.constraint)
    }

    // A union branch the untyped member is compatible with is kept, instead of every branch being dropped.
    merged(
        "oneOf of array and string with untyped items is the array branch",
        """{ "oneOf": [{ "type": "array" }, { "type": "string" }] }""",
        untypedItems
    ) { model ->
        assertIs<Model.Primitive.String>(assertIs<Model.Collection>(model).inner)
    }

    // properties or additionalProperties without type is an object.
    merged(
        "object and untyped properties is an object with the properties",
        """{ "type": "object" }""",
        """{ "properties": { "a": { "type": "string" } } }"""
    ) { model ->
        assertEquals(setOf("a"), assertIs<Model.Object>(model).properties.keys)
    }

    merged(
        "object and untyped additionalProperties is a map",
        """{ "type": "object" }""",
        """{ "additionalProperties": { "type": "integer" } }"""
    ) { model ->
        assertIs<Model.Primitive.Long>(assertIs<Model.Dictionary>(model).inner)
    }

    // enum or format without type applies to the typed member.
    merged("string and untyped enum is the enum", string, """{ "enum": ["on", "off"] }""") { model ->
        val enum = assertIs<Model.Enum>(model)
        assertIs<Model.Primitive.String>(enum.inner)
        assertEquals(listOf("on", "off"), enum.values.map { it.rawValue })
    }

    merged("string and untyped date-time format is a date-time", string, """{ "format": "date-time" }""") { model ->
        assertIs<Model.DateTime>(model)
    }

    // The implied types no value has both of.
    rejected("string and untyped items", string, untypedItems, "string", "array")
    rejected(
        "array and untyped properties",
        """{ "type": "array", "items": { "type": "string" } }""",
        """{ "properties": { "a": { "type": "string" } } }""",
        "array", "object"
    )
    rejected("object and untyped items", """{ "type": "object" }""", untypedItems, "object", "array")
    rejected(
        "untyped properties and untyped items",
        """{ "properties": { "a": { "type": "string" } } }""",
        untypedItems,
        "object", "array"
    )
}
