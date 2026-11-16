package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.routes.SchemaContext
import kotlin.test.assertEquals

/**
 * A discriminated base is generated with its cases, so it has a request and a response shape when one of its cases does,
 * though only the case is `readOnly` or `writeOnly` and nothing in the base refers to it (apple.com:sirikit-cloud-media's
 * `IntentResolutionResult`, whose cases refer to the context-specific `MediaItem`). Otherwise the base is named once and
 * generated in both contexts, as two classes of one name. A plain `allOf` parent is not generated with its children, and
 * keeps one shape.
 */
val discriminatedBaseContextSpec by testSuite {
    fun ref(name: String): String = $$"""{ "$ref": "#/components/schemas/$$name" }"""

    fun document(schemas: Map<String, String>): String = """
        {
          "openapi": "3.0.3",
          "info": { "title": "test", "version": "1.0.0" },
          "paths": {},
          "components": { "schemas": { ${schemas.entries.joinToString(",\n") { [name, schema] -> "\"$name\": $schema" }} } }
        }
        """.trimIndent()

    fun base(vararg mapping: Pair<String, String>): String = """
        {
          "type": "object",
          "properties": { "type": { "type": "string" } },
          "discriminator": {
            "propertyName": "type",
            "mapping": { ${mapping.joinToString(", ") { [tag, schema] -> "\"$tag\": \"#/components/schemas/$schema\"" }} }
          }
        }
        """.trimIndent()

    val readOnlyId = """{ "type": "object", "properties": { "id": { "type": "string", "readOnly": true } } }"""

    suspend fun readContext(name: String, schemas: Map<String, String>): SchemaContext =
        Registry(OpenAPI.fromJson(document(schemas))).use { it.componentReference(name, SchemaContext.Read).context }

    test("a base whose mapped case refers to a context-specific schema has a shape per context") {
        assertEquals(
            SchemaContext.Read,
            readContext(
                "Pet",
                mapOf(
                    "Pet" to base("cat" to "Cat"),
                    "Cat" to """{ "allOf": [${ref("Pet")}, { "type": "object", "properties": { "item": ${ref("Item")} } }] }""",
                    "Item" to readOnlyId,
                ),
            ),
        )
    }

    test("a base whose implicit case is readOnly has a shape per context") {
        assertEquals(
            SchemaContext.Read,
            readContext(
                "Pet",
                mapOf(
                    "Pet" to base("cat" to "Cat"),
                    "Cat" to """{ "allOf": [${ref("Pet")}] }""",
                    "Dog" to """{ "allOf": [${ref("Pet")}, $readOnlyId] }""",
                ),
            ),
        )
    }

    test("a plain allOf parent keeps one shape when only a child is readOnly") {
        assertEquals(
            SchemaContext.Null,
            readContext(
                "Named",
                mapOf(
                    "Named" to """{ "type": "object", "properties": { "name": { "type": "string" } } }""",
                    "Dog" to """{ "allOf": [${ref("Named")}, $readOnlyId] }""",
                ),
            ),
        )
    }
}
