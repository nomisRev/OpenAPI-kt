package io.github.nomisrev.openapi.registry

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.ApiTree
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.toApiTree
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * Keywords next to a schema `$ref` apply at that use site, in OpenAPI 3.0 and 3.1 documents alike (3.0 says to ignore
 * them, but authors write them to be read):
 *
 * - `required`, `properties`, `items`, `enum` and composites shape the value: the use site is the referenced schema
 *   merged with them, as `allOf: [$ref, siblings]`. A merged property is an inline class of its own.
 * - `nullable: true` and `type: [..., "null"]` make the use site nullable; the referenced class stays as it is.
 * - A `type` other than the referenced schema's is ignored with a warning; no value could have both.
 * - Annotations (`description`, `deprecated`, `default`, ...) and constraints change nothing yet.
 */
val ReferenceSiblingsSpec by testSuite {
    fun ref(name: String, siblings: String = "") =
        """{ "${'$'}ref": "#/components/schemas/$name"${if (siblings.isEmpty()) "" else ", $siblings"} }"""

    fun spec(version: String) = """
        {
          "openapi": "$version",
          "info": { "title": "Api", "version": "1" },
          "paths": {
            "/tax": { "get": { "responses": { "200": { "description": "ok",
              "content": { "application/json": { "schema": ${ref("TaxComponent")} } } } } } },
            "/check": { "post": {
              "requestBody": { "required": true, "content": { "application/json": { "schema": ${ref("Request")} } } },
              "responses": { "204": { "description": "ok" } } } }
          },
          "components": { "schemas": {
            "Dates": { "type": "object", "properties": { "modifiedDate": { "type": "string" }, "name": { "type": "string" } } },
            "Amount": { "type": "object", "properties": { "value": { "type": "integer" } } },
            "TaxComponent": { "allOf": [
              { "type": "object", "properties": { "rate": { "type": "number" } } },
              ${ref("Dates", """"required": ["name"]""")} ] },
            "Request": { "type": "object", "required": ["amount", "strict"], "properties": {
              "amount": ${ref("Amount", """"description": "The amount to check." """)},
              "previous": ${ref("Amount", """"description": "Previous amount", "deprecated": true, "default": { "value": 0 }""")},
              "maybe": ${ref("Amount", """"type": ["object", "null"]""")},
              "nullableRef": ${ref("Amount", """"nullable": true""")},
              "wrongType": ${ref("Amount", """"type": "string"""")},
              "strict": ${ref("Amount", """"required": ["value"]""")},
              "badShape": ${ref("Amount", """"items": { "type": "string" }""")} } }
          } }
        }
    """.trimIndent()

    suspend fun tree(version: String): ApiTree = OpenAPI.fromJson(spec(version)).toApiTree()

    fun ApiTree.model(name: String): Model.Object =
        assertIs(models.single { ((it as Model.ContextHolder).context.head as NamingContext.Reference).name == name })

    fun Model.Object.property(name: String): Model.Object.Property = properties.getValue(name)

    fun Model.Object.Property.referencedName(): String =
        (assertIs<Model.Reference>(model).context.head as NamingContext.Reference).name

    for (version in listOf("3.0.3", "3.1.0")) {
        test("$version: a required sibling of an allOf member makes the property required") {
            val tax = tree(version).model("TaxComponent")
            assertEquals(mapOf("rate" to false, "modifiedDate" to false, "name" to true), tax.properties.mapValues { it.value.isRequired })
        }

        test("$version: a nullable sibling makes the use site nullable and keeps the referenced class") {
            val request = tree(version).model("Request")
            for (name in listOf("maybe", "nullableRef")) {
                val property = request.property(name)
                assertEquals("Amount", property.referencedName(), name)
                assertTrue(property.model.isNullable, name)
            }
            assertFalse(tree(version).model("Amount").isNullable)
        }

        test("$version: annotation siblings keep a non-null reference to the same class") {
            val request = tree(version).model("Request")
            for (name in listOf("amount", "previous")) {
                val property = request.property(name)
                assertEquals("Amount", property.referencedName(), name)
                assertFalse(property.model.isNullable, name)
            }
        }

        test("$version: a type the referenced schema does not have is ignored with a warning") {
            val tree = tree(version)
            val property = tree.model("Request").property("wrongType")
            assertEquals("Amount", property.referencedName())
            assertFalse(property.model.isNullable)
            assertTrue(tree.warnings.any { "wrongType" in it && "type" in it }, tree.warnings.toString())
        }

        test("$version: a required sibling of a property is an inline merge of the referenced schema") {
            val strict = assertIs<Model.Object>(tree(version).model("Request").property("strict").model)
            assertEquals(mapOf("value" to true), strict.properties.mapValues { it.value.isRequired })
        }

        test("$version: a shaping sibling that contradicts the referenced schema is ignored with a warning") {
            val tree = tree(version)
            assertEquals("Amount", tree.model("Request").property("badShape").referencedName())
            assertTrue(tree.warnings.any { "Request.badShape" in it && "ignored" in it }, tree.warnings.toString())
        }

        test("$version: siblings add no component class") {
            assertEquals(
                setOf("Amount", "Request", "TaxComponent"),
                tree(version).models.map { ((it as Model.ContextHolder).context.head as NamingContext.Reference).name }.toSet(),
            )
        }
    }

    test("3.0: siblings that change the model are reported, since OpenAPI 3.0 says to ignore them") {
        val warnings = tree("3.0.3").warnings
        for (name in listOf("#/components/schemas/Dates", "Request.maybe", "Request.nullableRef", "Request.strict")) {
            assertTrue(warnings.any { name in it && "OpenAPI 3.0" in it }, "$name: $warnings")
        }
        assertFalse(warnings.any { "Request.amount" in it || "Request.previous" in it }, warnings.toString())
    }

    test("3.1: a nullable sibling is reported as a lenient reading, merged siblings are not reported") {
        val warnings = tree("3.1.0").warnings
        for (name in listOf("Request.maybe", "Request.nullableRef")) {
            assertTrue(warnings.any { name in it && "nullable" in it }, "$name: $warnings")
        }
        assertFalse(warnings.any { "OpenAPI 3.0" in it || "Request.strict" in it || "Dates" in it }, warnings.toString())
    }
}
