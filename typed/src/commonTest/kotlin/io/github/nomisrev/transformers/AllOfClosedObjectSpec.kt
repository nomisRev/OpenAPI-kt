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
 * An `allOf` of a closed object (`additionalProperties: false`) and an object with properties the closed one does not
 * declare is, read literally, an object without those properties (`additionalProperties` sees only the properties
 * next to it). Generators for C# and Java close every class and extend it with `allOf`, and the servers send the
 * properties of both (tl-api.azurewebsites.net's `ApiException`), so the merge keeps the properties of both and warns
 * that the closed member forbids them. A closed object next to a typed `additionalProperties` stays closed: `false`
 * forbids every key the closed member does not declare, whatever the other member allows.
 */
val allOfClosedObjectSpec by testSuite {
    fun document(first: String, second: String): String = """
        {
          "openapi": "3.0.3",
          "info": { "title": "test", "version": "1.0.0" },
          "paths": {},
          "components": { "schemas": { "Merged": { "allOf": [$first, $second] } } }
        }
    """.trimIndent()

    suspend fun merged(first: String, second: String): Pair<Model.Object, List<String>> =
        Registry(OpenAPI.fromJson(document(first, second))).use { registry ->
            val model = with(registry) {
                ReferenceOr.schema("Merged").toModel(NamingContext.reference("Merged", SchemaContext.Null), SchemaContext.Write)
            }
            assertIs<Model.Object>(model) to registry.warnings()
        }

    /** Checks [first] and [second] in both member orders. */
    fun bothOrders(label: String, first: String, second: String, check: (Model.Object, List<String>) -> Unit) {
        listOf(first to second, second to first).forEachIndexed { index, [a, b] ->
            test("allOf $label${if (index == 1) ", members reversed" else ""}") {
                val [model, warnings] = merged(a, b)
                check(model, warnings)
            }
        }
    }

    val closedA = """{ "type": "object", "properties": { "a": { "type": "string" } }, "additionalProperties": false }"""
    val openB = """{ "type": "object", "properties": { "b": { "type": "string" } } }"""
    val requiredB = """{ "type": "object", "properties": { "b": { "type": "string" } }, "required": ["b"] }"""

    bothOrders("closed object and an object with another property has both, with a warning", closedA, openB) { model, warnings ->
        assertEquals(setOf("a", "b"), model.properties.keys)
        assertTrue(
            warnings.any { "additionalProperties: false" in it && "[b]" in it && "required" !in it },
            "Expected a warning naming b, got $warnings"
        )
    }

    bothOrders("closed object and an object with a required property names it as required", closedA, requiredB) { model, warnings ->
        assertTrue(model.properties.getValue("b").isRequired)
        assertTrue(warnings.any { "[b]" in it && "required" in it }, "Expected a warning naming required b, got $warnings")
    }

    bothOrders(
        "closed object and a typed additionalProperties stays closed",
        closedA,
        """{ "type": "object", "additionalProperties": { "type": "string" } }"""
    ) { model, warnings ->
        assertEquals(setOf("a"), model.properties.keys)
        assertEquals(Model.Object.AdditionalProperties.Allowed(false), model.additionalProperties)
        assertTrue(
            warnings.any { "additionalProperties: false" in it && "additional properties" in it },
            "Expected a warning about the additional properties, got $warnings"
        )
    }

    // No warning when nothing is forbidden.
    bothOrders(
        "closed object and an object with a property it declares has no warning",
        """{ "type": "object", "properties": { "a": { "type": "string" }, "b": { "type": "string" } }, "additionalProperties": false }""",
        openB
    ) { model, warnings ->
        assertEquals(setOf("a", "b"), model.properties.keys)
        assertEquals(emptyList(), warnings)
    }

    bothOrders("two open objects have no warning", """{ "type": "object", "properties": { "a": { "type": "string" } } }""", openB) { _, warnings ->
        assertEquals(emptyList(), warnings)
    }

    // A component that is a map is generated as a value class wrapping it.
    bothOrders(
        "an open object and a typed additionalProperties is a map",
        """{ "type": "object", "additionalProperties": true }""",
        """{ "type": "object", "additionalProperties": { "type": "string" } }"""
    ) { model, warnings ->
        assertTrue(model.isTransparentWrapper, "$model")
        assertIs<Model.Primitive.String>(assertIs<Model.Dictionary>(model.properties.values.single().model).inner)
        assertEquals(emptyList(), warnings)
    }
}
