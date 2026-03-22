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
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.product
import io.github.nomisrev.zip
import kotlin.test.assertEquals

val anyOfSpec by testSuite {
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
                    Union.Case(Model.Primitive.String(null, null, null, false, null), null),
                    Union.Case(
                        Model.Enum(
                            NamingContext.reference("Model", SchemaContext.Null)
                                .nest(NamingContext.UnionCase("AOrB")),
                            Model.Primitive.String(null, null, null, false, null),
                            listOf("a", "b"),
                            null,
                            null,
                            null,
                            false
                        ),
                        null
                    )
                ),
                null,
                null,
                null,
                null,
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
                    Union.Case(
                        Model.Reference(
                            NamingContext.reference("ModelIdsResponses", SchemaContext.Null),
                            null,
                            false,
                            null
                        ),
                        null
                    ),
                    Union.Case(Model.Primitive.String(null, null, null, false, null), null)
                ),
                null,
                null,
                null,
                null,
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
                Union.Case(s.expected, null),
                Union.Case(l.expected, null),
                Union.Case(d.expected, null),
                Union.Case(b.expected, null),
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
                null,
                isNullable ?: false
            )

            actual expect expected
        }
    )
}
