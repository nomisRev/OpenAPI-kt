package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.assertEq
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.SchemaContext
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * A named component whose `allOf` has several members that merge to a collection, primitive or map, such as
 * `Tags: {allOf: [{type: array, items: {type: string}}, {minItems: 1}]}`, is the same type as the component written
 * with the merged keywords directly, `Tags: {type: array, items: {type: string}, minItems: 1}`: a value wrapper named
 * after the component, used by every property, array item and map value referencing it. Modelling the merged schema
 * as an inline value instead yields the bare `List<String>`, so the component that properties still reference is
 * never generated.
 */
val allOfMergedAliasSpec by testSuite {
    val aliasName = NamingContext.reference("Alias", SchemaContext.Null)

    fun document(alias: String): String = """
        {
          "openapi": "3.0.3",
          "info": { "title": "test", "version": "1.0.0" },
          "paths": {},
          "components": {
            "schemas": {
              "Alias": $alias,
              "Holder": {
                "type": "object",
                "properties": {
                  "alias": { "${'$'}ref": "#/components/schemas/Alias" },
                  "aliases": { "type": "array", "items": { "${'$'}ref": "#/components/schemas/Alias" } },
                  "aliasMap": { "type": "object", "additionalProperties": { "${'$'}ref": "#/components/schemas/Alias" } }
                },
                "required": ["alias", "aliases", "aliasMap"]
              },
              "Names": { "type": "array", "items": { "type": "string" } },
              "MyString": { "type": "string" },
              "Labels": { "type": "object", "additionalProperties": { "type": "string" } }
            }
          }
        }
    """.trimIndent()

    suspend fun model(alias: String, name: String): Model =
        Registry(OpenAPI.fromJson(document(alias))).use { registry ->
            with(registry) {
                ReferenceOr.schema(name).toModel(NamingContext.reference(name, SchemaContext.Null), SchemaContext.Write)
            }
        }

    data class Case(val label: String, val allOf: String, val direct: String)

    val cases = listOf(
        Case(
            "collection",
            """{ "allOf": [{ "type": "array", "items": { "type": "string" } }, { "minItems": 1 }] }""",
            """{ "type": "array", "items": { "type": "string" }, "minItems": 1 }""",
        ),
        Case(
            "primitive",
            """{ "allOf": [{ "type": "string" }, { "maxLength": 3 }] }""",
            """{ "type": "string", "maxLength": 3 }""",
        ),
        Case(
            "map",
            """{ "allOf": [{ "type": "object", "additionalProperties": { "type": "string" } }, { "minProperties": 1 }] }""",
            """{ "type": "object", "additionalProperties": { "type": "string" }, "minProperties": 1 }""",
        ),
        Case(
            "collection \$ref",
            """{ "allOf": [{ "${'$'}ref": "#/components/schemas/Names" }, { "minItems": 1 }] }""",
            """{ "type": "array", "items": { "type": "string" }, "minItems": 1 }""",
        ),
        Case(
            "primitive \$ref",
            """{ "allOf": [{ "${'$'}ref": "#/components/schemas/MyString" }, { "maxLength": 3 }] }""",
            """{ "type": "string", "maxLength": 3 }""",
        ),
        Case(
            "map \$ref",
            """{ "allOf": [{ "${'$'}ref": "#/components/schemas/Labels" }, { "minProperties": 1 }] }""",
            """{ "type": "object", "additionalProperties": { "type": "string" }, "minProperties": 1 }""",
        ),
    )

    /** `nullable: true` on the component, or on every member: a merged `allOf` is nullable when every member is. */
    fun String.nullableOnComponent(): String = removeSuffix("}").trimEnd() + """, "nullable": true }"""
    fun String.nullableOnEveryMember(): String =
        replace("""{ "type": "array", "items": { "type": "string" } }""", """{ "type": "array", "items": { "type": "string" }, "nullable": true }""")
            .replace("""[{ "type": "string" }""", """[{ "type": "string", "nullable": true }""")
            .replace("""{ "type": "object", "additionalProperties": { "type": "string" } }""", """{ "type": "object", "additionalProperties": { "type": "string" }, "nullable": true }""")
            .replace("""{ "minItems": 1 }""", """{ "minItems": 1, "nullable": true }""")
            .replace("""{ "maxLength": 3 }""", """{ "maxLength": 3, "nullable": true }""")
            .replace("""{ "minProperties": 1 }""", """{ "minProperties": 1, "nullable": true }""")

    cases.forEach { case ->
        test("Component allOf of a ${case.label} and a constraint models like the merged ${case.label} component") {
            assertEq(model(case.direct, "Alias"), model(case.allOf, "Alias"))
        }

        test("Component allOf of a ${case.label} and a constraint is a value wrapper named Alias") {
            val model = model(case.allOf, "Alias")
            assertTrue(
                model is Model.Object && model.isTransparentWrapper && model.context == aliasName && !model.isNullable,
                "should be the non-null Alias value wrapper: $model"
            )
        }

        test("Property, array items and map values referencing a component allOf of a ${case.label} use Alias") {
            val holder = assertIs<Model.Object>(model(case.allOf, "Holder"))
            assertEq(assertIs<Model.Object>(model(case.direct, "Holder")), holder)
            val property = holder.properties.getValue("alias").model
            val items = assertIs<Model.Collection>(holder.properties.getValue("aliases").model).inner
            val values = assertIs<Model.Dictionary>(holder.properties.getValue("aliasMap").model).inner
            assertTrue(
                property is Model.Reference && property.context == aliasName && !property.isNullable,
                "the property should reference Alias: $property"
            )
            listOf(items, values).forEach { inner ->
                assertTrue(
                    inner is Model.Object && inner.isTransparentWrapper && inner.context == aliasName,
                    "should be the Alias wrapper, not the bare ${case.label}: $inner"
                )
            }
        }

        listOfNotNull(
            "nullable: true on the component" to case.allOf.nullableOnComponent(),
            ("nullable: true on every member" to case.allOf.nullableOnEveryMember()).takeUnless { "\$ref" in case.label },
        ).forEach { [label, nullableAllOf] ->
            test("Component allOf of a ${case.label} with $label is a nullable value wrapper named Alias") {
                val model = model(nullableAllOf, "Alias")
                assertTrue(
                    model is Model.Object && model.isTransparentWrapper && model.context == aliasName && model.isNullable,
                    "should be the nullable Alias value wrapper: $model"
                )
            }

            test("A property referencing a component allOf of a ${case.label} with $label is a nullable Alias") {
                val property = assertIs<Model.Object>(model(nullableAllOf, "Holder")).properties.getValue("alias").model
                assertTrue(
                    property is Model.Reference && property.isNullable && property.context == aliasName,
                    "should be a nullable reference to Alias: $property"
                )
            }
        }
    }
}
