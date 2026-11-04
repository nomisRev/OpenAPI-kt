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
 * A discriminated subtype (`allOf: [{$ref: Parent}, {...}]`, listed in Parent's discriminator mapping) that redeclares
 * an inherited property so that no value satisfies both declarations overrides it, like a subclass: the subtype's
 * declaration is its property, and the override is recorded on the [Model.DiscriminatedObject] so generation can warn
 * about it. YouTrack declares `ActivityItem.added: {type: object}` and `CommentActivityItem.added: {type: array}`.
 * A compatible redeclaration is still the intersection of both, and a contradiction outside a discriminated hierarchy
 * is still rejected.
 */
val discriminatedPropertyOverrideSpec by testSuite {
    fun ref(name: String): String = $$"""{ "$ref": "#/components/schemas/$$name" }"""

    fun document(schemas: Map<String, String>): String = """
        {
          "openapi": "3.0.3",
          "info": { "title": "test", "version": "1.0.0" },
          "paths": {},
          "components": { "schemas": { ${schemas.entries.joinToString(",\n") { [name, schema] -> "\"$name\": $schema" }} } }
        }
        """.trimIndent()

    fun parent(properties: String, vararg subtypes: String): String {
        val mapping = (listOf("Item") + subtypes).joinToString(", ") { "\"$it\": \"#/components/schemas/$it\"" }
        return """
        {
          "type": "object",
          "properties": { "type": { "type": "string" }, $properties },
          "discriminator": { "propertyName": "type", "mapping": { $mapping } }
        }
        """.trimIndent()
    }

    fun subtype(parent: String, properties: String, required: String = ""): String =
        """{ "allOf": [${ref(parent)}, { "type": "object", "properties": { $properties }$required }] }"""

    suspend fun model(name: String, schemas: Map<String, String>, context: SchemaContext = SchemaContext.Write): Model =
        Registry(OpenAPI.fromJson(document(schemas))).use { registry ->
            with(registry) {
                ReferenceOr.schema(name).toModel(NamingContext.reference(name, SchemaContext.Null), context)
            }
        }

    suspend fun discriminated(schemas: Map<String, String>, context: SchemaContext = SchemaContext.Write) =
        assertIs<Model.DiscriminatedObject>(model("Item", schemas, context))

    fun Model.DiscriminatedObject.case(name: String): Model.Object = subtypes.single {
        (it.context.nested.last() as NamingContext.DiscriminatedObjectCase).discriminator == name
    }

    fun Model.DiscriminatedObject.property(case: String, property: String): Model =
        case(case).properties.getValue(property).model

    val objectItem = parent(""""added": { "type": "object" }, "name": { "type": "string", "nullable": true }""", "Multi")

    test("a subtype redeclaring an inherited object property as an array overrides it (YouTrack's added)") {
        val model = discriminated(
            mapOf(
                "Item" to objectItem,
                "Multi" to subtype("Item", """"added": { "type": "array", "items": { "type": "string" } }"""),
            )
        )
        val added = assertIs<Model.Collection>(model.property("Multi", "added"))
        assertIs<Model.Primitive.String>(added.inner)
        val override = model.overriddenProperties.single()
        assertEquals("Multi", override.schema)
        assertEquals("Item", override.parent)
        assertEquals("added", override.property)
        assertEquals("""{"type":"object"}""", override.inherited)
        assertEquals("""{"type":"array","items":{"type":"string"}}""", override.declared)
        assertTrue("object" in override.reason && "array" in override.reason, override.reason)
    }

    test("the parent keeps its own declaration of an overridden property") {
        val model = discriminated(
            mapOf(
                "Item" to parent(""""added": { "type": "string" }""", "Multi"),
                "Multi" to subtype("Item", """"added": { "type": "integer" }"""),
            )
        )
        assertIs<Model.Primitive.String>(model.abstractProperties.getValue("added").model)
        assertIs<Model.Primitive.String>(model.property("Default", "added"))
        assertIs<Model.Primitive.Long>(model.property("Multi", "added"))
    }

    test("YouTrack's readOnly placeholder is overridden in the read model") {
        val readOnlyItem = parent(""""added": { "type": "object", "readOnly": true }""", "Comment")
        val model = discriminated(
            mapOf(
                "Item" to readOnlyItem,
                "Comment" to subtype(
                    "Item",
                    """"added": { "type": "array", "readOnly": true, "items": ${ref("Note")} }"""
                ),
                "Note" to """{ "type": "object", "properties": { "text": { "type": "string" } } }""",
            ),
            SchemaContext.Read
        )
        val added = assertIs<Model.Collection>(model.property("Comment", "added"))
        val note = assertIs<Model.ContextHolder>(added.inner).context.head
        assertEquals("Note", assertIs<NamingContext.Reference>(note).name)
        assertEquals(listOf("added"), model.overriddenProperties.map { it.property })
    }

    test("an intermediate subtype's override is inherited, and a deeper subtype can override it again") {
        val model = discriminated(
            mapOf(
                "Item" to parent(""""added": { "type": "object" }""", "Mid", "Leaf"),
                "Mid" to subtype("Item", """"added": { "type": "array", "items": { "type": "string" } }"""),
                "Leaf" to subtype("Mid", """"added": { "type": "string" }"""),
            )
        )
        assertIs<Model.Collection>(model.property("Mid", "added"))
        assertIs<Model.Primitive.String>(model.property("Leaf", "added"))
        // Leaf overrides Mid's declaration, which overrides Item's: each override is recorded once, by the schema
        // that declares it.
        assertEquals(
            listOf("Mid" to "Item", "Leaf" to "Mid"),
            model.overriddenProperties.map { it.schema to it.parent }
        )
    }

    listOf(
        Triple("disjoint enums", """{ "type": "string", "enum": ["a"] }""", """{ "type": "string", "enum": ["b"] }"""),
        Triple("unrelated \$refs", ref("Owner"), ref("Other")),
        Triple(
            "incompatible formats",
            """{ "type": "string", "format": "date" }""",
            """{ "type": "string", "format": "date-time" }"""
        ),
        Triple("a \$ref object and a string", ref("Owner"), """{ "type": "string" }"""),
    ).forEach { [label, inherited, declared] ->
        test("a subtype redeclaring an inherited property with $label overrides it") {
            val model = discriminated(
                mapOf(
                    "Item" to parent(""""p": $inherited""", "Multi"),
                    "Multi" to subtype("Item", """"p": $declared"""),
                    "Owner" to """{ "type": "object", "properties": { "name": { "type": "string" } } }""",
                    "Other" to """{ "type": "object", "properties": { "id": { "type": "integer" } } }""",
                )
            )
            val expected = discriminated(
                mapOf(
                    "Item" to parent(""""p": $declared""", "Multi"),
                    "Multi" to subtype("Item", """"p": $declared"""),
                    "Owner" to """{ "type": "object", "properties": { "name": { "type": "string" } } }""",
                    "Other" to """{ "type": "object", "properties": { "id": { "type": "integer" } } }""",
                )
            )
            assertEquals(expected.property("Multi", "p"), model.property("Multi", "p"))
            assertEquals(listOf("p"), model.overriddenProperties.map { it.property })
        }
    }

    test("a compatible redeclaration is still the intersection, and is no override") {
        val model = discriminated(
            mapOf(
                "Item" to parent(""""name": { "type": "string", "nullable": true }""", "Multi"),
                "Multi" to subtype("Item", """"name": { "type": "string", "maxLength": 3 }""", """, "required": ["name"]"""),
            )
        )
        val name = model.case("Multi").properties.getValue("name")
        val string = assertIs<Model.Primitive.String>(name.model)
        assertFalse(string.isNullable, "$string")
        assertEquals(Constraints.Text(0, 3, null), string.constraint)
        assertTrue(name.isRequired)
        assertEquals(emptyList(), model.overriddenProperties)
    }

    test("a contradictory allOf that is no discriminated subtype is still rejected") {
        val error = assertFails {
            model(
                "Coll",
                mapOf(
                    "Base" to """{ "type": "object", "properties": { "result": { "type": "object" } } }""",
                    "Coll" to subtype("Base", """"result": { "type": "array", "items": { "type": "string" } }"""),
                )
            )
        }
        assertIs<IllegalArgumentException>(error)
        val message = error.message.orEmpty()
        assertTrue("result" in message && "object" in message && "array" in message, message)
    }

    test("a contradictory allOf of a discriminated parent it is not mapped by is still rejected") {
        val error = assertFails {
            model(
                "Unmapped",
                mapOf(
                    "Item" to objectItem,
                    "Multi" to subtype("Item", """"name": { "type": "string" }"""),
                    "Unmapped" to subtype("Item", """"added": { "type": "array", "items": { "type": "string" } }"""),
                )
            )
        }
        assertIs<IllegalArgumentException>(error)
        assertTrue("added" in error.message.orEmpty(), error.message)
    }
}
