package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.all
import io.github.nomisrev.description
import io.github.nomisrev.expect
import io.github.nomisrev.zip
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.Union
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.product
import io.github.nomisrev.verifyAll

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
            val expected = Model.Union(
                NamingContext.ObjectProperty("test"),
                oneOf.expected,
                null,
                description.expected,
                emptySet(),
                null,
                isNullable ?: false
            )

            actual expect expected
        }
    )
}