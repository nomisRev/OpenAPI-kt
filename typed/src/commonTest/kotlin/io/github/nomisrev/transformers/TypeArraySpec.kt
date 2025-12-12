package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.Expect
import io.github.nomisrev.description
import io.github.nomisrev.expect
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Schema.Type
import io.github.nomisrev.product
import io.github.nomisrev.verifyAll

val typeArraySpec by testSuite {
    verifyAll("TypeArray", values())
    verifyAll("TypeArray with explicit Null type", values(nullable = true))

    val root = NamingContext.Reference("Root", SchemaContext.Null)
    verifyAll(
        "TypeArray Referenced",
        values(root),
        root
    )
}

private fun values(
    name: NamingContext = NamingContext.ObjectProperty("test"),
    nullable: Boolean? = null
): List<Expect<Schema, Model.Union>> =
    description.product(listOf(true, false, null)) { description, isNullable ->
        val actual = Schema(
            type = Type.Array(
                listOfNotNull(
                    Type.Basic.Boolean,
                    Type.Basic.Integer,
                    Type.Basic.String,
                    Type.Basic.Number,
                    Type.Basic.Null.takeIf { nullable ?: false }
                )
            ),
            description = description.actual,
            nullable = isNullable
        )
        val expect = Model.Union(
            name,
            listOf(
                Model.Union.Case(Model.Primitive.Boolean(null, null, false), null),
                Model.Union.Case(Model.Primitive.Long(null, null, null, false), null),
                Model.Union.Case(Model.Primitive.String(null, null, null, false), null),
                Model.Union.Case(Model.Primitive.Double(null, null, null, false), null),
            ),
            null,
            description.expected,
            emptySet(),
            null,
            nullable ?: isNullable ?: false
        )
        actual expect expect
    }