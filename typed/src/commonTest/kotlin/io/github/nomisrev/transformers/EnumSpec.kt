package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.Expect
import io.github.nomisrev.all
import io.github.nomisrev.verifyAll
import io.github.nomisrev.default
import io.github.nomisrev.description
import io.github.nomisrev.expect
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.ExampleValue
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.product
import io.github.nomisrev.verifyFails
import kotlin.concurrent.atomics.ExperimentalAtomicApi

private val nulls = listOf("null", "NULL", null)

private fun default(values: List<String>): List<Expect<ExampleValue.Single?, Model.Default<String>?>> =
    values.map { ExampleValue.Single(it) expect Model.Default.Value(it) } + listOf(null expect null)

private fun nullDefault(values: List<String>): List<Expect<ExampleValue.Single?, Model.Default<String>?>> =
    default(values) + listOf(ExampleValue.Single("null") expect Model.Default.Null)

private fun List<Expect<Schema, Model>>.enum(
    name: NamingContext,
    values: List<String>
) = product(description, default(values)) { schema, description, default ->
    schema.actual.copy(
        description = description.actual,
        default = default.actual,
        enum = values.toList()
    ) expect Model.Enum(
        context = name,
        inner = schema.expected.with(description = null, isNullable = false).default(null),
        values = values.toList(),
        default = default.expected,
        isOpen = false,
        description = description.expected,
        title = null,
        isNullable = schema.expected.isNullable
    )
} + product(description, nullDefault(values), nulls) { schema, description, default, NULL ->
    schema.actual.copy(
        description = description.actual,
        default = default.actual,
        enum = (values + NULL).toList()
    ) expect Model.Enum(
        context = name,
        inner = schema.expected.with(description = null, isNullable = false).default(null),
        values = (values + NULL).toList(),
        default = default.expected,
        isOpen = false,
        description = description.expected,
        title = null,
        isNullable = schema.expected.isNullable
    )
}

fun Model.Enum.Companion.ints(name: NamingContext): List<Expect<Schema, Model>> =
    Model.Primitive.Int.all().enum(name, listOf("1", "2", "3"))

fun Model.Enum.Companion.strings(name: NamingContext): List<Expect<Schema, Model>> =
    Model.Primitive.String.all().enum(name, listOf("A", "B", "C"))

fun Model.Enum.Companion.floats(name: NamingContext): List<Expect<Schema, Model>> =
    Model.Primitive.Float.all().enum(name, listOf("1.1", "2.2", "3.3"))

@OptIn(ExperimentalAtomicApi::class)
val closedEnumSpec by testSuite {
    verifyAll("ClosedEnum(Int)", Model.Enum.ints(NamingContext.ObjectProperty("test")))
    verifyAll("ClosedEnum(String)", Model.Enum.strings(NamingContext.ObjectProperty("test")))
    verifyAll("ClosedEnum(Float)", Model.Enum.floats(NamingContext.ObjectProperty("test")))

    verifyFails<IllegalArgumentException>(
        "Empty enum throws IllegalArgumentException",
        Schema(enum = emptyList()),
        "Enum requires at least 1 possible value. {\"enum\":[]}"
    )

    verifyFails<IllegalArgumentException>(
        "multiple default values throws IllegalArgumentException",
        Schema(
            default = ExampleValue.Multiple(listOf("1", "2")),
            enum = listOf("1", "2"),
            type = Schema.Type.Basic.Integer
        ),
        "Multiple default values not supported for enums."
    )

    verifyFails<IllegalArgumentException>(
        "Null default in non-null enum",
        Schema(
            default = ExampleValue.Single("null"),
            enum = listOf("1", "2"),
            type = Schema.Type.Basic.Integer,
            nullable = false
        ),
        "The default value Null is not present in the enum values: [1, 2] & schema is not nullable."
    )

    fun List<Expect<Schema, Model.Primitive.String>>.openEnum(
        name: NamingContext,
        values: List<String>
    ) = product(
        description,
        default(values),
        listOf(false, true, null)
    ) { schema, description, default, isNullable ->
        val enum = schema.actual.copy(enum = values.toList())
        val anyOf =
            Schema(
                anyOf = listOf(ReferenceOr.value(enum), ReferenceOr.value(Schema(type = Schema.Type.Basic.String))),
                default = default.actual,
                description = description.actual,
                nullable = isNullable
            )

        anyOf expect Model.Enum(
            context = name,
            inner = schema.expected.copy(default = null, description = null, isNullable = false),
            values = values.toList(),
            default = default.expected,
            isOpen = true,
            description = description.expected,
            title = null,
            isNullable = isNullable ?: false
        )
    }

    verifyAll(
        "OpenEnum",
        Model.Primitive.String.all().openEnum(NamingContext.ObjectProperty("test"), listOf("A", "B", "C"))
    )

    verifyFails<IllegalArgumentException>(
        "Empty open enum throws IllegalArgumentException",
        Schema(
            anyOf = listOf(
                ReferenceOr.value(Schema(enum = emptyList())),
                ReferenceOr.value(Schema(type = Schema.Type.Basic.String))
            ),
        ),
        "OpenEnum requires at least 1 possible value. {\"anyOf\":[{\"enum\":[]},{\"type\":\"string\"}]}",
    )

    verifyFails<IllegalArgumentException>(
        "OpenEnum multiple default values throws IllegalArgumentException",
        Schema(
            anyOf = listOf(
                ReferenceOr.value(Schema(enum = listOf("1", "2"), type = Schema.Type.Basic.Integer)),
                ReferenceOr.value(Schema(type = Schema.Type.Basic.String))
            ),
            default = ExampleValue.Multiple(listOf("1", "2"))
        ),
        "Multiple default values not supported for enums."
    )

    verifyFails<IllegalArgumentException>(
        "Null default in non-null OpenEnum",
        Schema(
            anyOf = listOf(
                ReferenceOr.value(
                    Schema(
                        enum = listOf("1", "2"),
                        type = Schema.Type.Basic.Integer,
                        nullable = false
                    )
                ),
                ReferenceOr.value(Schema(type = Schema.Type.Basic.String))
            ),
            default = ExampleValue.Single("null")
        ),
        "The default value Null is not present in the enum values: [1, 2] & schema is not nullable.",
    )
}
