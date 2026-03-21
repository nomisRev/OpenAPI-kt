package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.all
import io.github.nomisrev.api
import io.github.nomisrev.description
import io.github.nomisrev.expect
import io.github.nomisrev.reference
import io.github.nomisrev.zip
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.AnyOf
import io.github.nomisrev.openapi.Model.OneOf
import io.github.nomisrev.openapi.Model.Union
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.product
import io.github.nomisrev.verifyAll
import kotlin.test.assertEquals

val unionSpec by testSuite {
    val unions =
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
        "Union primitives",
        unions.product(description, listOf(true, false, null)) { oneOf, description, isNullable ->
            val actual = Schema(
                oneOf = oneOf.actual,
                description = description.actual,
                nullable = isNullable
            )
            val expected = Model.OneOf(
                NamingContext.path("test"),
                oneOf.expected,
                null,
                description.expected,
                null,
                null,
                isNullable ?: false
            )

            actual expect expected
        }
    )

    // Test for OpenEnum pattern: anyOf[{ type: String }, { type: String, enum: [a, b] }]
    // The enum case should inherit the outer name: CaseString & CaseModel
    test("OpenEnum pattern - enum case inherits outer name") {
        val openEnumSchema = Schema(
            anyOf = listOf(
                ReferenceOr.value(Schema.string),
                ReferenceOr.value(Schema.string.copy(enum = listOf("a", "b")))
            )
        )
        val testApi = api.reference("Model", openEnumSchema)

        registry(testApi) {
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
                            listOf("a", "b"), null, null, null, false
                        ),
                        null
                    )
                ),
                null,
                null,
                null,
                null,
                false
            )
            assertEquals(expected, result)
        }
    }

    // Test for reference pattern: oneOf[{ $ref:EventA }, { $ref:EventB }, { type: object }]
    // The non-reference case should inherit the outer name: EventA, EventB, CaseEvent
    test("Reference pattern - non-reference case inherits outer name") {
        val eventASchema = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf("value" to ReferenceOr.value(Schema.string))
        )
        val eventBSchema = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf("value" to ReferenceOr.value(Schema.string))
        )
        val eventUnionSchema = Schema(
            oneOf = listOf(
                ReferenceOr.schema("EventA"),
                ReferenceOr.schema("EventB"),
                ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.Object,
                        properties = mapOf("value" to ReferenceOr.value(Schema.string))
                    )
                )
            )
        )
        val testApi = api
            .reference("EventA", eventASchema)
            .reference("EventB", eventBSchema)
            .reference("Event", eventUnionSchema)

        registry(testApi) {
            val result = ReferenceOr.schema("Event")
                .toModel(NamingContext.reference("Event", SchemaContext.Null), SchemaContext.Write)

            val expected = OneOf(
                NamingContext.reference("Event", SchemaContext.Null),
                listOf(
                    Union.Case(
                        Model.Reference(
                            NamingContext.reference("EventA", SchemaContext.Null),
                            null,
                            false,
                            null
                        ), null
                    ),
                    Union.Case(
                        Model.Reference(
                            NamingContext.reference("EventB", SchemaContext.Null),
                            null,
                            false,
                            null
                        ), null
                    ),
                    Union.Case(
                        Model.Object(
                            NamingContext.reference("Event", SchemaContext.Null)
                                .nest(NamingContext.UnionCase("CaseElse")), null, null, mapOf(
                                "value" to Model.Object.Property(
                                    Model.Primitive.String(null, null, null, false, null),
                                    false
                                )
                            ), false, false
                        ), null
                    )
                ),
                null,
                null, null, null, false
            )

            assertEquals(expected, result)
        }
    }

    test("Discriminated union infers implicit mapping from referenced schema names") {
        val directory = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf("type" to ReferenceOr.value(Schema.string))
        )
        val file = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf("type" to ReferenceOr.value(Schema.string))
        )
        val contentSchema = Schema(
            oneOf = listOf(
                ReferenceOr.schema("content-directory"),
                ReferenceOr.schema("content-file")
            ),
            discriminator = Schema.Discriminator(propertyName = "type")
        )
        val testApi = api
            .reference("content-directory", directory)
            .reference("content-file", file)
            .reference("content", contentSchema)

        registry(testApi) {
            val result = ReferenceOr.schema("content")
                .toModel(NamingContext.reference("content", SchemaContext.Null), SchemaContext.Write) as OneOf

            assertEquals("type", result.discriminator)
            assertEquals(
                listOf("content-directory", "content-file"),
                result.cases.map { it.discriminator }
            )
        }
    }

    test("Discriminated union resolves explicit mapping keys from mapping values") {
        val directory = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf("type" to ReferenceOr.value(Schema.string))
        )
        val file = Schema(
            type = Schema.Type.Basic.Object,
            properties = mapOf("type" to ReferenceOr.value(Schema.string))
        )
        val contentSchema = Schema(
            oneOf = listOf(
                ReferenceOr.schema("content-directory"),
                ReferenceOr.schema("content-file")
            ),
            discriminator = Schema.Discriminator(
                propertyName = "type",
                mapping = mapOf(
                    "dir" to "#/components/schemas/content-directory",
                    "file" to "#/components/schemas/content-file"
                )
            )
        )
        val testApi = api
            .reference("content-directory", directory)
            .reference("content-file", file)
            .reference("content", contentSchema)

        registry(testApi) {
            val result = ReferenceOr.schema("content")
                .toModel(NamingContext.reference("content", SchemaContext.Null), SchemaContext.Write) as OneOf

            assertEquals("type", result.discriminator)
            assertEquals(listOf("dir", "file"), result.cases.map { it.discriminator })
        }
    }

    test("Union preserves nullable case models") {
        val unionSchema = Schema(
            oneOf = listOf(
                ReferenceOr.value(Schema.string.copy(nullable = true)),
                ReferenceOr.value(Schema.integer.copy(format = "int32")),
                ReferenceOr.value(
                    Schema(
                        type = Schema.Type.Basic.Array,
                        nullable = true,
                        items = ReferenceOr.value(Schema.string)
                    )
                )
            )
        )
        val testApi = api.reference("Union", unionSchema)

        registry(testApi) {
            val result = ReferenceOr.schema("Union")
                .toModel(NamingContext.reference("Union", SchemaContext.Null), SchemaContext.Write) as Union

            assertEquals(false, result.isNullable)
            assertEquals(true, (result.cases[0].model as Model.Primitive.String).isNullable)
            assertEquals(false, (result.cases[1].model as Model.Primitive.Int).isNullable)

            val nullableCollection = result.cases[2].model as Model.Collection
            assertEquals(true, nullableCollection.isNullable)
            assertEquals(false, nullableCollection.inner.isNullable)
        }
    }
}
