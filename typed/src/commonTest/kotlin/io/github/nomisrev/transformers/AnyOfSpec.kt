package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.all
import io.github.nomisrev.api
import io.github.nomisrev.description
import io.github.nomisrev.reference
import io.github.nomisrev.verifyAll
import io.github.nomisrev.expect
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.AnyOf
import io.github.nomisrev.openapi.Model.Union
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.UnionDispatch
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.product
import io.github.nomisrev.zip
import kotlin.test.assertEquals

val anyOfSpec by testSuite {
    fun case(model: Model, vararg discriminatorValues: String): Union.Case =
        Union.Case(model, discriminatorValues.toSet())

    test("anyOf with null among three cases becomes nullable union") {
        val schema = Schema(
            anyOf = listOf(
                ReferenceOr.value(Schema.string),
                ReferenceOr.value(Schema.string.copy(enum = listOf("a", "b"))),
                ReferenceOr.value(Schema.NULL)
            )
        )

        registry(api.reference("Model", schema)) {
            val result = ReferenceOr.schema("Model")
                .toModel(NamingContext.reference("Model", SchemaContext.Null), SchemaContext.Write)

            val expected = AnyOf(
                NamingContext.reference("Model", SchemaContext.Null),
                listOf(
                    case(Model.Primitive.String(null, null, null, false, null)),
                    case(
                        Model.Enum(
                            NamingContext.reference("Model", SchemaContext.Null)
                                .nest(NamingContext.UnionCase("AOrB")),
                            Model.Primitive.String(null, null, null, false, null),
                            listOf(Model.EnumValue.String("a"), Model.EnumValue.String("b")),
                            null,
                            null,
                            null,
                            false
                        )
                    )
                ),
                null,
                null,
                null,
                UnionDispatch.Structural,
                true
            )

            assertEquals(expected, result)
        }
    }

    test("anyOf with ref, string and null becomes nullable union") {
        val modelIdsResponses = Schema(
            anyOf = listOf(
                ReferenceOr.value(Schema.string),
                ReferenceOr.value(Schema.string.copy(enum = listOf("gpt-5", "gpt-4o")))
            )
        )
        val modelIdsCompaction = Schema(
            anyOf = listOf(
                ReferenceOr.schema("ModelIdsResponses"),
                ReferenceOr.value(Schema.string),
                ReferenceOr.value(Schema.NULL)
            )
        )

        val testApi = api
            .reference("ModelIdsResponses", modelIdsResponses)
            .reference("ModelIdsCompaction", modelIdsCompaction)

        registry(testApi) {
            val result = ReferenceOr.schema("ModelIdsCompaction")
                .toModel(NamingContext.reference("ModelIdsCompaction", SchemaContext.Null), SchemaContext.Write)

            val expected = AnyOf(
                NamingContext.reference("ModelIdsCompaction", SchemaContext.Null),
                listOf(
                    case(
                        Model.Reference(
                            NamingContext.reference("ModelIdsResponses", SchemaContext.Null),
                            null,
                            false,
                            null
                        )
                    ),
                    case(Model.Primitive.String(null, null, null, false, null))
                ),
                null,
                null,
                null,
                UnionDispatch.Structural,
                true
            )

            assertEquals(expected, result)
        }
    }

    verifyAll("anyOf[{ type: null }, { type: primitive }", Model.Primitive.all().map { (schema, model) ->
        Schema(
            anyOf = listOf(
                ReferenceOr.value(Schema.NULL),
                ReferenceOr.value(schema)
            )
        ) expect model.with(isNullable = true)
    })

    verifyAll("anyOf[{ nullable: true }, { type: primitive }", Model.Primitive.all().map { (schema, model) ->
        Schema(
            anyOf = listOf(
                ReferenceOr.value(Schema(nullable = true)),
                ReferenceOr.value(schema)
            )
        ) expect model.with(isNullable = true)
    })

    verifyAll("anyOf[{ type: primitive }]", Model.Primitive.all().map { (schema, model) ->
        Schema(anyOf = listOf(ReferenceOr.value(schema))) expect model
    })

    val anyOf =
        Model.Primitive.String.all().zip(
            Model.Primitive.Long.all(),
            Model.Primitive.Double.all(),
            Model.Primitive.Boolean.all()
        ) { s, l, d, b ->
            listOf(
                ReferenceOr.value(s.actual),
                ReferenceOr.value(l.actual),
                ReferenceOr.value(d.actual),
                ReferenceOr.value(b.actual),
            ) expect listOf(
                case(s.expected),
                case(l.expected),
                case(d.expected),
                case(b.expected),
            )
        }

    verifyAll(
        "AnyOf primitives",
        anyOf.product(description, listOf(true, false, null)) { anyOf, description, isNullable ->
            val actual = Schema(
                anyOf = anyOf.actual,
                description = description.actual,
                nullable = isNullable
            )
            val expected = Model.AnyOf(
                NamingContext.path("test"),
                anyOf.expected,
                null,
                description.expected,
                null,
                UnionDispatch.Structural,
                isNullable ?: false
            )

            actual expect expected
        }
    )
}
