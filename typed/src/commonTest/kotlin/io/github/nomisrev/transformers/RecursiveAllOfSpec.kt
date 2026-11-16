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
import kotlin.test.assertFails
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * A property declared as `allOf: [{$ref: X}, {description: ...}]` is X: an annotation does not make it another type.
 * So a way back to a schema being generated is a reference to it, as for a plain `$ref` (19 AWS documents, Jira, where
 * the inline copy of X expanded forever), and a non-recursive X is not generated again as a nested class. An `allOf`
 * that adds properties to X is a new type; where it leads back to itself, it is a reference to that type.
 */
val recursiveAllOfSpec by testSuite {
    fun ref(name: String): String = """{ "${'$'}ref": "#/components/schemas/$name" }"""

    suspend fun model(name: String, schemas: String): Model =
        Registry(
            OpenAPI.fromJson(
                """{ "openapi": "3.0.3", "info": { "title": "t", "version": "1" }, "paths": {}, "components": { "schemas": { $schemas } } }"""
            )
        ).use { registry ->
            with(registry) {
                ReferenceOr.schema(name).toModel(NamingContext.reference(name, SchemaContext.Null), SchemaContext.Write)
            }
        }

    fun Model.property(name: String): Model = assertIs<Model.Object>(this).properties.getValue(name).model

    /** The component this model is: a reference to it where it is being generated, else its own model. */
    fun Model.referenceName(): String {
        val context = when (this) {
            is Model.Reference -> context
            is Model.Object -> context
            else -> error("Expected a component's model, got $this")
        }
        return assertIs<NamingContext.Reference>(context.head, "$this").name.also {
            assertTrue(context.nested.isEmpty(), "Expected the component itself, not a type nested in it: $this")
        }
    }

    test("an allOf of a \$ref to the schema itself and a description is a reference to it") {
        val node = model("Node", """"Node": { "type": "object", "properties": { "child": { "allOf": [${ref("Node")}, { "description": "The child." }] } } }""")
        assertEquals("Node", node.property("child").referenceName())
    }

    test("an allOf of a single \$ref to the schema itself is a reference to it") {
        val node = model("Node", """"Node": { "type": "object", "properties": { "child": { "allOf": [${ref("Node")}] } } }""")
        assertEquals("Node", node.property("child").referenceName())
    }

    test("an allOf of a \$ref to a list of the schema is a reference to the list (AWS)") {
        val node = model(
            "Node",
            """
            "Node": { "type": "object", "properties": { "children": { "allOf": [${ref("NodeList")}, { "description": "The children." }] } } },
            "NodeList": { "type": "array", "items": ${ref("Node")} }
            """.trimIndent()
        )
        assertEquals("NodeList", node.property("children").referenceName())
    }

    test("an allOf of a \$ref to a map of the schema is a reference to the map (DynamoDB)") {
        val value = model(
            "AttributeValue",
            """
            "AttributeValue": { "type": "object", "properties": { "S": { "type": "string" }, "M": { "allOf": [${ref("MapAttributeValue")}, { "description": "A map." }] } } },
            "MapAttributeValue": { "type": "object", "additionalProperties": ${ref("AttributeValue")} }
            """.trimIndent()
        )
        assertEquals("MapAttributeValue", value.property("M").referenceName())
    }

    test("mutually recursive allOf properties are references to each other") {
        val a = model(
            "A",
            """
            "A": { "type": "object", "properties": { "b": { "allOf": [${ref("B")}, { "description": "b" }] } } },
            "B": { "type": "object", "properties": { "a": { "allOf": [${ref("A")}, { "description": "a" }] } } }
            """.trimIndent()
        )
        assertEquals("B", a.property("b").referenceName())
    }

    test("a non-recursive allOf of a \$ref and a description is the \$ref, not a nested copy") {
        val node = model(
            "Node",
            """
            "Node": { "type": "object", "properties": { "owner": { "allOf": [${ref("Owner")}, { "description": "x" }] }, "owner2": ${ref("Owner")} } },
            "Owner": { "type": "object", "properties": { "name": { "type": "string" } } }
            """.trimIndent()
        )
        assertEquals("Owner", node.property("owner").referenceName())
        assertEquals("Owner", node.property("owner2").referenceName())
    }

    // readOnly or writeOnly is not about the referenced type, so such an allOf is merged as before, not the $ref.
    test("an allOf of a \$ref and readOnly is not resolved as the \$ref") {
        val node = model(
            "Node",
            """
            "Node": { "type": "object", "properties": { "owner": { "allOf": [${ref("Owner")}, { "readOnly": true }] } } },
            "Owner": { "type": "object", "properties": { "name": { "type": "string" } } }
            """.trimIndent()
        )
        val owner = assertIs<Model.Object>(node).properties["owner"]?.model
        assertTrue(owner !is Model.Reference && (owner as? Model.Object)?.context?.nested?.isNotEmpty() != false, "$owner")
    }

    test("an allOf that adds a property to the schema itself is a new type that refers to itself") {
        val node = model("Node", """"Node": { "type": "object", "properties": { "child": { "allOf": [${ref("Node")}, { "properties": { "extra": { "type": "string" } } }] } } }""")
        val child = assertIs<Model.Object>(node.property("child"))
        assertEquals(setOf("child", "extra"), child.properties.keys)
        assertIs<Model.Reference>(child.properties.getValue("child").model, "the child's child is the child type")
    }

    test("an allOf of two schemas whose additional properties are themselves fails instead of overflowing") {
        val error = assertFails {
            model(
                "Root",
                """
                "A": { "type": "object", "additionalProperties": ${ref("A")} },
                "B": { "type": "object", "additionalProperties": ${ref("B")} },
                "Root": { "allOf": [${ref("A")}, ${ref("B")}] }
                """.trimIndent()
            )
        }
        assertIs<IllegalArgumentException>(error, "${error::class.simpleName}: ${error.message}")
        assertTrue("A" in error.message.orEmpty() && "B" in error.message.orEmpty(), error.message)
    }
}
