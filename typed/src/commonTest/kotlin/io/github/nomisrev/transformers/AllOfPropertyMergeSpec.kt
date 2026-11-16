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
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * When several `allOf` members declare the same property, the property value must validate against every
 * declaration, so the merged property is the intersection of them: whatever the declarations are (inline primitives,
 * `$ref`s, or a mix) and in whichever order the members list them. It is nullable only when every declaration permits
 * `null`, its constraints intersect, its `format` is kept, a `$ref` is kept when the other declaration only annotates,
 * constrains or restates its type, the more specific of two `$ref`s that extend one another is kept, two `$ref`s that do
 * not extend one another are merged (keeping a `$ref` whose schema is the intersection), and declarations no value can
 * satisfy (`string` and `integer`), or whose intersection no generated type expresses (two recursive or union schemas),
 * are rejected with an error rather than resolved by picking one of them.
 */
val allOfPropertyMergeSpec by testSuite {
    fun Model.Reference.name(): String = (context.head as NamingContext.Reference).name

    fun ref(name: String): String = $$"""{ "$ref": "#/components/schemas/$$name" }"""

    val components = mapOf(
        "Owner" to """{ "type": "object", "properties": { "name": { "type": "string" } }, "required": ["name"] }""",
        "NullableOwner" to """{ "type": "object", "nullable": true, "properties": { "name": { "type": "string" } }, "required": ["name"] }""",
        "MyString" to """{ "type": "string" }""",
        "NullableString" to """{ "type": "string", "nullable": true }""",
        "Pet" to """{ "type": "object", "properties": { "name": { "type": "string" } }, "required": ["name"] }""",
        "Dog" to """{ "allOf": [${ref("Pet")}, { "type": "object", "properties": { "bark": { "type": "boolean" } } }] }""",
        "NullableAliasOwner" to """{ "allOf": [${ref("Owner")}], "nullable": true }""",
        "Puppy" to """{ "allOf": [${ref("Dog")}, { "type": "object", "properties": { "age": { "type": "integer" } } }] }""",
        // nexmo.com:reports: one enum under two names, which differ only in annotations.
        "ProductA" to """{ "type": "string", "enum": ["SMS", "VOICE"], "example": "SMS", "description": "A product" }""",
        "ProductB" to """{ "type": "string", "enum": ["SMS", "VOICE"], "example": "VOICE" }""",
        "EnumAB" to """{ "type": "string", "enum": ["a", "b"] }""",
        "EnumA" to """{ "type": "string", "enum": ["a"] }""",
        "EnumBC" to """{ "type": "string", "enum": ["b", "c"] }""",
        "EnumC" to """{ "type": "string", "enum": ["c"] }""",
        "DefaultA" to """{ "type": "string", "enum": ["a", "b"], "default": "a" }""",
        "DefaultB" to """{ "type": "string", "enum": ["a", "b"], "default": "b" }""",
        // beezup.com: two open objects with different properties.
        "ObjA" to """{ "type": "object", "properties": { "a": { "type": "string" } } }""",
        "ObjB" to """{ "type": "object", "properties": { "b": { "type": "string" } } }""",
        "RecA" to """{ "type": "object", "properties": { "x": { "type": "string" }, "next": ${ref("RecA")} } }""",
        "RecB" to """{ "type": "object", "properties": { "y": { "type": "string" }, "next": ${ref("RecB")} } }""",
        "UnionA" to """{ "oneOf": [${ref("ObjA")}, { "type": "string" }] }""",
        "UnionB" to """{ "oneOf": [${ref("ObjB")}, { "type": "integer" }] }""",
    )

    fun member(property: String, required: Boolean = false): String =
        """{ "type": "object", "properties": { "p": $property }${if (required) """, "required": ["p"]""" else ""} }"""

    fun document(first: String, second: String): String {
        val schemas = (components + ("Merged" to """{ "allOf": [${member(first)}, ${member(second)}] }"""))
            .entries.joinToString(",\n") { [name, schema] -> "\"$name\": $schema" }
        return """
        {
          "openapi": "3.0.3",
          "info": { "title": "test", "version": "1.0.0" },
          "paths": {},
          "components": { "schemas": { $schemas } }
        }
        """.trimIndent()
    }

    /** The model of `Merged`, the `allOf` of a member declaring [first] and one declaring [second], and the warnings. */
    suspend fun merged(first: String, second: String, context: SchemaContext): Pair<Model.Object, List<String>> {
        val api = OpenAPI.fromJson(document(first, second))
        return Registry(api).use { registry ->
            val merged = with(registry) {
                ReferenceOr.schema("Merged").toModel(NamingContext.reference("Merged", SchemaContext.Null), context)
            }
            (merged as Model.Object) to registry.warnings()
        }
    }

    /** The model of property `p` of `Merged`, the `allOf` of a member declaring [first] and one declaring [second]. */
    suspend fun property(first: String, second: String, context: SchemaContext = SchemaContext.Write): Model? =
        merged(first, second, context).first.properties["p"]?.model

    /** Checks [first] and [second] in both member orders: `allOf` is an intersection, the order must not matter. */
    fun bothOrders(label: String, first: String, second: String, check: suspend (Model?) -> Unit) {
        test("allOf property $label") { check(property(first, second)) }
        test("allOf property $label, members reversed") { check(property(second, first)) }
    }

    fun rejected(label: String, first: String, second: String, vararg reason: String) {
        listOf(first to second, second to first).forEachIndexed { index, [a, b] ->
            test("allOf property $label is rejected${if (index == 1) ", members reversed" else ""}") {
                val error = assertFails { property(a, b) }
                val message = error.message.orEmpty()
                assertTrue(
                    reason.all { it in message },
                    "Expected an error naming ${reason.toList()}, but got ${error::class.simpleName}: $message"
                )
            }
        }
    }

    val string = """{ "type": "string" }"""
    val nullableString = """{ "type": "string", "nullable": true }"""

    // Nullability: allOf permits null only when every declaration does.
    bothOrders("nullable string and non-null string is non-null", nullableString, string) { model ->
        assertIs<Model.Primitive.String>(model)
        assertFalse(model.isNullable, "$model")
    }

    test("allOf property nullable string and nullable string is nullable") {
        val model = property(nullableString, """{ "type": "string", "nullable": true, "maxLength": 3 }""")
        assertIs<Model.Primitive.String>(model)
        assertTrue(model.isNullable, "$model")
    }

    bothOrders(
        "nullable enum and non-null string is a non-null enum",
        """{ "type": "string", "enum": ["a", "b"], "nullable": true }""",
        string
    ) { model ->
        assertIs<Model.Enum>(model)
        assertFalse(model.isNullable, "$model")
    }

    bothOrders("nullable \$ref string and non-null string is non-null", ref("NullableString"), string) { model ->
        assertFalse(assertIs<Model>(model).isNullable, "$model")
    }

    // Constraints intersect.
    bothOrders(
        "text constraints intersect",
        """{ "type": "string", "minLength": 1, "maxLength": 5, "pattern": "^[a-z]+$" }""",
        """{ "type": "string", "minLength": 2, "maxLength": 3 }"""
    ) { model ->
        assertEquals(Constraints.Text(2, 3, "^[a-z]+$"), assertIs<Model.Primitive.String>(model).constraint)
    }

    bothOrders(
        "a constraint-only declaration constrains the typed one",
        string,
        """{ "maxLength": 3 }"""
    ) { model ->
        assertEquals(Constraints.Text(0, 3, null), assertIs<Model.Primitive.String>(model).constraint)
    }

    bothOrders(
        "number constraints intersect",
        """{ "type": "integer", "minimum": 1, "maximum": 10 }""",
        """{ "type": "integer", "minimum": 3, "maximum": 20 }"""
    ) { model ->
        val constraint = assertIs<Model.Primitive.Long>(model).constraint
        assertEquals(Constraints.Number.Bound(3.0, false), constraint?.minimum)
        assertEquals(Constraints.Number.Bound(10.0, false), constraint?.maximum)
    }

    bothOrders(
        "enums intersect",
        """{ "type": "string", "enum": ["a", "b", "c"] }""",
        """{ "type": "string", "enum": ["b", "c", "d"] }"""
    ) { model ->
        assertEquals(listOf("b", "c"), assertIs<Model.Enum>(model).values.map { it.rawValue })
    }

    bothOrders(
        "an enum and a plain string is the enum",
        """{ "type": "string", "enum": ["a", "b"] }""",
        string
    ) { model ->
        assertEquals(listOf("a", "b"), assertIs<Model.Enum>(model).values.map { it.rawValue })
    }

    // The format decides the generated type, so it is kept whichever member declares it.
    bothOrders("date-time format and plain string is a date-time", """{ "type": "string", "format": "date-time" }""", string) { model ->
        assertIs<Model.DateTime>(model)
    }

    bothOrders(
        "int64 and int32 is an int32",
        """{ "type": "integer", "format": "int64" }""",
        """{ "type": "integer", "format": "int32" }"""
    ) { model ->
        assertIs<Model.Primitive.Int>(model)
    }

    bothOrders(
        "integer and number is an integer",
        """{ "type": "integer", "maximum": 5 }""",
        """{ "type": "number", "minimum": 1.5 }"""
    ) { model ->
        val constraint = assertIs<Model.Primitive.Long>(model).constraint
        assertEquals(Constraints.Number.Bound(1.5, false), constraint?.minimum)
        assertEquals(Constraints.Number.Bound(5.0, false), constraint?.maximum)
    }

    // readOnly/writeOnly from any declaration apply to the property.
    test("allOf property readOnly in one declaration is not written") {
        assertEquals(null, property("""{ "type": "string", "readOnly": true }""", string, SchemaContext.Write))
        assertEquals(null, property(string, """{ "type": "string", "readOnly": true }""", SchemaContext.Write))
    }

    // A $ref next to a declaration that only annotates, constrains or restates its type stays that $ref.
    bothOrders("\$ref and description is the \$ref", ref("Owner"), """{ "description": "The owner" }""") { model ->
        val reference = assertIs<Model.Reference>(model)
        assertEquals("Owner", reference.name())
        assertFalse(reference.isNullable)
    }

    bothOrders("nullable \$ref and description stays nullable", ref("NullableOwner"), """{ "description": "The owner" }""") { model ->
        val reference = assertIs<Model.Reference>(model)
        assertTrue(reference.isNullable, "$reference")
    }

    bothOrders("\$ref string and constrained string is the \$ref", ref("MyString"), """{ "type": "string", "maxLength": 3 }""") { model ->
        val reference = assertIs<Model.Reference>(model)
        assertEquals("MyString", reference.name())
    }

    bothOrders("\$ref and readOnly is not written", ref("Owner"), """{ "readOnly": true }""") { model ->
        assertEquals(null, model)
    }

    // A $ref next to a declaration that adds structure is their intersection.
    bothOrders(
        "\$ref object and object with an extra property has both properties",
        ref("Owner"),
        """{ "type": "object", "properties": { "extra": { "type": "string" } } }"""
    ) { model ->
        val obj = assertIs<Model.Object>(model)
        assertEquals(setOf("name", "extra"), obj.properties.keys)
        assertTrue(obj.properties.getValue("name").isRequired)
        assertFalse(obj.isNullable)
    }

    // Of two $refs where one extends the other, the more specific one is the intersection.
    listOf("Dog" to "Pet", "Puppy" to "Pet", "Puppy" to "Dog").forEach { [specific, general] ->
        bothOrders("\$ref $specific and \$ref $general is $specific", ref(specific), ref(general)) { model ->
            val reference = assertIs<Model.Reference>(model)
            assertEquals(specific, reference.name())
        }
    }

    // A nullable $ref next to a declaration that excludes null is non-null: its non-null intersection.
    bothOrders("nullable \$ref object and object is a non-null object", ref("NullableOwner"), """{ "type": "object" }""") { model ->
        val obj = assertIs<Model.Object>(model)
        assertEquals(setOf("name"), obj.properties.keys)
        assertFalse(obj.isNullable, "$obj")
    }

    bothOrders("nullable \$ref extending a non-null \$ref is non-null", ref("NullableAliasOwner"), ref("Owner")) { model ->
        assertFalse(assertIs<Model>(model).isNullable, "$model")
    }

    bothOrders("same \$ref twice is that \$ref", ref("Owner"), ref("Owner")) { model ->
        assertEquals("Owner", assertIs<Model.Reference>(model).name())
    }

    // Two $refs that do not extend one another are their intersection: a $ref whose schema it is, or an inline type.
    test("allOf property \$refs to one enum differing in annotations is the later \$ref") {
        assertEquals("ProductB", assertIs<Model.Reference>(property(ref("ProductA"), ref("ProductB"))).name())
        assertEquals("ProductA", assertIs<Model.Reference>(property(ref("ProductB"), ref("ProductA"))).name())
    }

    bothOrders("\$ref enum and \$ref of a subset of it is the subset", ref("EnumAB"), ref("EnumA")) { model ->
        assertEquals("EnumA", assertIs<Model.Reference>(model).name())
    }

    bothOrders("\$refs to overlapping enums is an enum of the shared values", ref("EnumAB"), ref("EnumBC")) { model ->
        assertEquals(listOf("b"), assertIs<Model.Enum>(model).values.map { it.rawValue })
    }

    bothOrders("\$refs to two open objects is an object with the properties of both", ref("ObjA"), ref("ObjB")) { model ->
        assertEquals(setOf("a", "b"), assertIs<Model.Object>(model).properties.keys)
    }

    bothOrders("\$ref object and the same object as nullable is the non-null \$ref", ref("Owner"), ref("NullableOwner")) { model ->
        val reference = assertIs<Model.Reference>(model)
        assertEquals("Owner", reference.name())
        assertFalse(reference.isNullable, "$reference")
    }

    bothOrders(
        "arrays of \$refs to two open objects is an array of an object with the properties of both",
        """{ "type": "array", "items": ${ref("ObjA")} }""",
        """{ "type": "array", "items": ${ref("ObjB")} }""",
    ) { model ->
        val inner = assertIs<Model.Collection>(model).inner
        assertEquals(setOf("a", "b"), assertIs<Model.Object>(inner).properties.keys)
    }

    // A default is generated as a Kotlin default value, so enums that differ in it are not one schema. Their merge
    // keeps the default of the first member, as for inline declarations, so it is the first member's $ref.
    test("allOf property \$refs to one enum with different defaults is the \$ref whose default the merge keeps") {
        assertEquals("DefaultA", assertIs<Model.Reference>(property(ref("DefaultA"), ref("DefaultB"))).name())
        assertEquals("DefaultB", assertIs<Model.Reference>(property(ref("DefaultB"), ref("DefaultA"))).name())
    }

    test("allOf property \$refs merged into an inline type is reported") {
        val warnings = merged(ref("ObjA"), ref("ObjB"), SchemaContext.Write).second
        assertTrue(
            warnings.any { "ObjA" in it && "ObjB" in it && "intersection" in it },
            "Expected a warning naming ObjA and ObjB, got $warnings"
        )
    }

    test("allOf property \$ref kept as the intersection of two \$refs is not reported") {
        assertEquals(emptyList(), merged(ref("EnumAB"), ref("EnumA"), SchemaContext.Write).second)
    }

    // Unsatisfiable or unrepresentable intersections.
    rejected("string and integer", string, """{ "type": "integer" }""", "p", "string", "integer")
    rejected("\$ref object and string", ref("Owner"), string, "p", "Owner")
    rejected("\$ref string and \$ref object", ref("MyString"), ref("Owner"), "p", "MyString", "Owner", "string", "object")
    rejected("disjoint enum \$refs", ref("EnumA"), ref("EnumC"), "p", "EnumA", "EnumC")
    rejected("\$refs to two recursive objects", ref("RecA"), ref("RecB"), "p", "RecA", "RecB", "recursive")
    rejected("\$refs to two unions", ref("UnionA"), ref("UnionB"), "p", "UnionA", "UnionB", "oneOf")
    rejected("disjoint enums", """{ "type": "string", "enum": ["a"] }""", """{ "type": "string", "enum": ["b"] }""", "p")
    rejected(
        "conflicting formats",
        """{ "type": "string", "format": "date" }""",
        """{ "type": "string", "format": "date-time" }""",
        "p", "date", "date-time"
    )
}
