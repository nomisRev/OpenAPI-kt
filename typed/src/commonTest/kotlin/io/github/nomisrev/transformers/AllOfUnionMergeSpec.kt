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
 * An `allOf` of a union (`oneOf`/`anyOf`) and other members is the union of each branch merged with them. A branch
 * no value of the other members satisfies admits no value, so it is not generated, and a warning says so. When no
 * branch is left, no value satisfies the `allOf`, which is an error, as for an `allOf` without a union. A branch whose
 * intersection exists but no generated type expresses (two unrelated unions) is an error too, not a dropped branch.
 */
val allOfUnionMergeSpec by testSuite {
    fun document(schemas: String): String = """
        {
          "openapi": "3.0.3",
          "info": { "title": "test", "version": "1.0.0" },
          "paths": {},
          "components": { "schemas": { $schemas } }
        }
    """.trimIndent()

    suspend fun model(name: String, schemas: String): Pair<Model, List<String>> =
        Registry(OpenAPI.fromJson(document(schemas))).use { registry ->
            val model = with(registry) {
                ReferenceOr.schema(name).toModel(NamingContext.reference(name, SchemaContext.Null), SchemaContext.Write)
            }
            model to registry.warnings()
        }

    suspend fun property(allOf: String): Pair<Model, List<String>> {
        val [thing, warnings] = model("Thing", """"Thing": { "type": "object", "properties": { "v": { "allOf": $allOf } } }""")
        return assertIs<Model.Object>(thing).properties.getValue("v").model to warnings
    }

    test("allOf of a union and a string drops the branches that are no string, with a warning for each") {
        val [model, warnings] = property(
            """[{ "oneOf": [{ "type": "string" }, { "type": "integer" }, { "type": "object", "properties": { "a": { "type": "string" } } }] },
                { "type": "string", "maxLength": 3 }]"""
        )
        assertIs<Model.Primitive.String>(model)
        assertEquals(2, warnings.size, "$warnings")
        assertTrue(warnings.any { "integer" in it && "not generated" in it }, "$warnings")
        assertTrue(warnings.any { "object" in it && "not generated" in it }, "$warnings")
    }

    test("allOf of a union none of whose branches satisfies the other member is rejected") {
        val error = assertFails {
            property("""[{ "oneOf": [{ "type": "string" }, { "type": "integer" }] }, { "type": "boolean" }]""")
        }
        val message = error.message.orEmpty()
        assertIs<IllegalArgumentException>(error, message)
        assertTrue("string" in message && "integer" in message && "boolean" in message, message)
    }

    test("allOf of a union whose every branch satisfies the other member has no warning") {
        val [model, warnings] = property(
            """[{ "oneOf": [
                  { "type": "object", "properties": { "a": { "type": "string" } }, "required": ["a"] },
                  { "type": "object", "properties": { "b": { "type": "string" } }, "required": ["b"] }] },
                { "type": "object", "properties": { "c": { "type": "string" } } }]"""
        )
        assertEquals(2, assertIs<Model.Union>(model).cases.size, "$model")
        assertEquals(emptyList(), warnings)
    }

    // A branch whose intersection exists but is not generated fails, as the same pair without the union does.
    val cat = """"Cat": { "oneOf": [{ "type": "string", "enum": ["tom"] }, { "type": "integer" }] }"""
    val pet = """"Pet": { "oneOf": [{ "type": "string" }, { "type": "integer" }, { "type": "boolean" }] }"""
    val ref = { name: String -> """{ "${'$'}ref": "#/components/schemas/$name" }""" }

    test("allOf of a union with a branch whose intersection is not generated is rejected, not narrowed") {
        val error = assertFails {
            model(
                "Thing",
                """
                "Thing": { "allOf": [
                  { "oneOf": [
                    { "type": "object", "properties": { "p": ${ref("Cat")} } },
                    { "type": "object", "properties": { "q": { "type": "string" } } }] },
                  { "type": "object", "properties": { "p": ${ref("Pet")} } }] },
                $cat, $pet
                """.trimIndent()
            )
        }
        val message = error.message.orEmpty()
        assertTrue("Cat" in message && "Pet" in message, message)
    }

    test("allOf of the same objects without a union is rejected with the same reason") {
        val error = assertFails {
            model(
                "Thing",
                """
                "Thing": { "allOf": [
                  { "type": "object", "properties": { "p": ${ref("Cat")} } },
                  { "type": "object", "properties": { "p": ${ref("Pet")} } }] },
                $cat, $pet
                """.trimIndent()
            )
        }
        val message = error.message.orEmpty()
        assertTrue("Cat" in message && "Pet" in message, message)
    }
}
