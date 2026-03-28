package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.all
import io.github.nomisrev.api
import io.github.nomisrev.assertEq
import io.github.nomisrev.expect
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.AdditionalProperties
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.reference
import io.github.nomisrev.verifyAll
import kotlin.collections.listOf

val allOfSpec by testSuite {
    verifyAll("allOf[{ type: null }, { type: primitive }", Model.Primitive.all().map { (schema, model) ->
        Schema(
            allOf = listOf(
                ReferenceOr.value(Schema.NULL),
                ReferenceOr.value(schema)
            )
        ) expect model.with(isNullable = true)
    })

    verifyAll("allOf[{ nullable: true }, { type: primitive }", Model.Primitive.all().map { (schema, model) ->
        Schema(
            allOf = listOf(
                ReferenceOr.value(Schema(nullable = true)),
                ReferenceOr.value(schema)
            )
        ) expect model.with(isNullable = true)
    })

    val a = Schema(
        type = Schema.Type.Basic.Object,
        properties = mapOf("a" to ReferenceOr.value(Schema(type = Schema.Type.Basic.String))),
    )
    val b = Schema(
        type = Schema.Type.Basic.Object,
        properties = mapOf("b" to ReferenceOr.value(Schema(type = Schema.Type.Basic.String))),
    )
    val ab = Schema(allOf = listOf(ReferenceOr.schema("A"), ReferenceOr.schema("B")))
    val expected = Model.Object(
        NamingContext.reference("AB", SchemaContext.Null), null, null, mapOf(
            "a" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), false),
            "b" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), false)
        ), false, false
    )
    test("AB") {
        val actual = registry(api.reference("A", a).reference("B", b).reference("AB", ab)) {
            ReferenceOr.schema("AB").toModel(NamingContext.reference("AB", SchemaContext.Null), SchemaContext.Write)
        }
        assertEq(expected, actual)
    }

    test("allOf enum intersection remains enum") {
        val actual = registry(api) {
            ReferenceOr.value(
                Schema(
                    allOf = listOf(
                        ReferenceOr.value(Schema(type = Schema.Type.Basic.String, enum = listOf("A", "B"))),
                        ReferenceOr.value(Schema(type = Schema.Type.Basic.String, enum = listOf("B", "C")))
                    )
                )
            ).toModel(NamingContext.path("test"), SchemaContext.Write)
        }

        assertEq(
            Model.Enum(
                NamingContext.path("test"),
                Model.Primitive.String(null, null, null, false, null),
                listOf(Model.EnumValue.String("B")),
                null,
                null,
                null,
                false
            ),
            actual
        )
    }

    test("allOf union intersection keeps compatible branches") {
        val actual = registry(api) {
            ReferenceOr.value(
                Schema(
                    allOf = listOf(
                        ReferenceOr.value(
                            Schema(
                                oneOf = listOf(
                                    ReferenceOr.value(Schema(type = Schema.Type.Basic.String)),
                                    ReferenceOr.value(Schema(type = Schema.Type.Basic.Integer))
                                )
                            )
                        ),
                        ReferenceOr.value(
                            Schema(
                                oneOf = listOf(
                                    ReferenceOr.value(Schema(type = Schema.Type.Basic.String)),
                                    ReferenceOr.value(Schema(type = Schema.Type.Basic.Boolean))
                                )
                            )
                        )
                    )
                )
            ).toModel(NamingContext.path("test"), SchemaContext.Write)
        }

        assertEq(Model.Primitive.String(null, null, null, false, null), actual)
    }

    test("allOf free form object filters non-object union branches") {
        val freeForm = Schema(
            type = Schema.Type.Basic.Object,
            additionalProperties = AdditionalProperties.Allowed(true)
        )

        val actual = registry(api) {
            ReferenceOr.value(
                Schema(
                    allOf = listOf(
                        ReferenceOr.value(freeForm),
                        ReferenceOr.value(
                            Schema(
                                oneOf = listOf(
                                    ReferenceOr.value(Schema(type = Schema.Type.Basic.String)),
                                    ReferenceOr.value(freeForm)
                                )
                            )
                        )
                    )
                )
            ).toModel(NamingContext.path("test"), SchemaContext.Write)
        }

        assertEq(Model.FreeFormJson(null, null, false, null), actual)
    }

    test("allOf object merge preserves right additionalProperties") {
        val actual = registry(api) {
            ReferenceOr.value(
                Schema(
                    allOf = listOf(
                        ReferenceOr.value(
                            Schema(
                                type = Schema.Type.Basic.Object,
                                properties = mapOf(
                                    "fixed" to ReferenceOr.value(Schema(type = Schema.Type.Basic.String))
                                )
                            )
                        ),
                        ReferenceOr.value(
                            Schema(
                                type = Schema.Type.Basic.Object,
                                additionalProperties = AdditionalProperties.PSchema(
                                    ReferenceOr.value(Schema(type = Schema.Type.Basic.String))
                                )
                            )
                        )
                    )
                )
            ).toModel(NamingContext.path("test"), SchemaContext.Write)
        }

        assertEq(
            Model.Object(
                NamingContext.path("test"),
                null,
                null,
                mapOf(
                    "fixed" to Model.Object.Property(
                        Model.Primitive.String(null, null, null, false, null),
                        false
                    )
                ),
                Model.Object.AdditionalProperties.Schema(
                    Model.Primitive.String(null, null, null, false, null)
                ),
                false
            ),
            actual
        )
    }

    test("nested allOf object branches still flatten") {
        val parent = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf("a" to ReferenceOr.value(Schema(type = Schema.Type.Basic.String)))
        )
        val nested = Schema(
            allOf = listOf(
                ReferenceOr.schema("Parent"),
                ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.Object,
                        properties = mapOf("b" to ReferenceOr.value(Schema(type = Schema.Type.Basic.String)))
                    )
                )
            )
        )

        val actual = registry(api.reference("Parent", parent)) {
            ReferenceOr.value(
                Schema(
                    allOf = listOf(
                        ReferenceOr.value(nested),
                        ReferenceOr.value(
                            Schema(
                                type = Schema.Type.Basic.Object,
                                properties = mapOf("c" to ReferenceOr.value(Schema(type = Schema.Type.Basic.String)))
                            )
                        )
                    )
                )
            ).toModel(NamingContext.path("test"), SchemaContext.Write)
        }

        assertEq(
            Model.Object(
                NamingContext.path("test"),
                null,
                null,
                mapOf(
                    "a" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), false),
                    "b" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), false),
                    "c" to Model.Object.Property(Model.Primitive.String(null, null, null, false, null), false),
                ),
                false,
                false
            ),
            actual
        )
    }
}
