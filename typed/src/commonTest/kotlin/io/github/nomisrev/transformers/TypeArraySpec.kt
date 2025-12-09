package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.description
import io.github.nomisrev.expect
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Schema.Type
import io.github.nomisrev.product
import io.github.nomisrev.verifyAll

val typeArraySpec by testSuite {
    verifyAll(
        "TypeArray",
        description.product(listOf(true, false, null)) { description, isNullable ->
            val actual = Schema(
                type = Type.Array(
                    listOf(
                        Type.Basic.Boolean,
                        Type.Basic.Integer,
                        Type.Basic.String,
                        Type.Basic.Number,
                    )
                ),
                description = description.actual,
                nullable = isNullable
            )
            val expect = Model.Union(
                NamingContext.ObjectProperty("test"),
                listOf(
                    Model.Primitive.Boolean(null, null, false),
                    Model.Primitive.Long(null, null, null, false),
                    Model.Primitive.String(null, null, null, false),
                    Model.Primitive.Double(null, null, null, false),
                ),
                null,
                description.expected,
                emptySet(),
                null,
                isNullable ?: false
            )
            actual expect expect
        }
    )

    verifyAll(
        "TypeArray with explicit Null type",
        description.product(listOf(true, false, null)) { description, isNullable ->
            val actual = Schema(
                type = Type.Array(
                    listOf(
                        Type.Basic.Boolean,
                        Type.Basic.Integer,
                        Type.Basic.String,
                        Type.Basic.Number,
                        Type.Basic.Null
                    )
                ),
                description = description.actual,
                nullable = isNullable
            )
            val expect = Model.Union(
                NamingContext.ObjectProperty("test"),
                listOf(
                    Model.Primitive.Boolean(null, null, false),
                    Model.Primitive.Long(null, null, null, false),
                    Model.Primitive.String(null, null, null, false),
                    Model.Primitive.Double(null, null, null, false),
                ),
                null,
                description.expected,
                emptySet(),
                null,
                true
            )
            actual expect expect
        }
    )
}