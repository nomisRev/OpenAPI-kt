package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.all
import io.github.nomisrev.api
import io.github.nomisrev.description
import io.github.nomisrev.verifyAll
import io.github.nomisrev.expect
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.Union
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.product
import io.github.nomisrev.reference
import io.github.nomisrev.zip

val anyOfSpec by testSuite {
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
            val expected = Model.Union(
                NamingContext.ObjectProperty("test"),
                anyOf.expected,
                null,
                description.expected,
                null,
                emptySet(),
                null,
                isNullable ?: false
            )

            actual expect expected
        }
    )
}
