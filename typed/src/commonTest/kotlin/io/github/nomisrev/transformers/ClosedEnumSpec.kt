package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.Expect
import io.github.nomisrev.all
import io.github.nomisrev.api
import io.github.nomisrev.checkAll
import io.github.nomisrev.default
import io.github.nomisrev.description
import io.github.nomisrev.expect
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.ResolvedSchema.Value
import io.github.nomisrev.openapi.SchemaContext.Input
import io.github.nomisrev.openapi.parser.ExampleValue
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry
import io.github.nomisrev.openapi.toModel
import io.github.nomisrev.product
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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
    ) expect Model.Enum.Closed(
        context = name,
        inner = schema.expected.with(description = null, isNullable = false).default(null),
        values = values.toList(),
        default = default.expected,
        description = description.expected,
        isNullable = schema.expected.isNullable
    )
} + product(description, nullDefault(values), nulls) { schema, description, default, NULL ->
    schema.actual.copy(
        description = description.actual,
        default = default.actual,
        enum = (values + NULL).toList()
    ) expect Model.Enum.Closed(
        context = name,
        inner = schema.expected.with(description = null, isNullable = false).default(null),
        values = (values + NULL).toList(),
        default = default.expected,
        description = description.expected,
        isNullable = schema.expected.isNullable
    )
}

fun Model.Enum.Closed.Companion.ints(name: NamingContext): List<Expect<Schema, Model>> =
    Model.Primitive.Int.all().enum(name, listOf("1", "2", "3"))

fun Model.Enum.Closed.Companion.strings(name: NamingContext): List<Expect<Schema, Model>> =
    Model.Primitive.String.all().enum(name, listOf("A", "B", "C"))

fun Model.Enum.Closed.Companion.floats(name: NamingContext): List<Expect<Schema, Model>> =
    Model.Primitive.Float.all().enum(name, listOf("1.1", "2.2", "3.3"))

@OptIn(ExperimentalAtomicApi::class)
val closedEnumSpec by testSuite {
    checkAll("ClosedEnum(Int)", Model.Enum.Closed.ints(NamingContext.ObjectProperty("value"))) { schema ->
        Value(NamingContext.ObjectProperty("value"), schema).toModel(Input)
    }

    checkAll("ClosedEnum(String)", Model.Enum.Closed.strings(NamingContext.ObjectProperty("value"))) { schema ->
        Value(NamingContext.ObjectProperty("value"), schema).toModel(Input)
    }

    checkAll("ClosedEnum(Float)", Model.Enum.Closed.floats(NamingContext.ObjectProperty("value"))) { schema ->
        Value(NamingContext.ObjectProperty("value"), schema).toModel(Input)
    }

    test("Empty enum throws IllegalArgumentException") {
        val schema = Schema(enum = emptyList())
        val e = assertFailsWith<IllegalArgumentException> {
            registry(api) {
                Value(
                    NamingContext.ObjectProperty("value"),
                    schema
                ).toModel(Input)
            }
        }
        assertEquals("Enum requires at least 1 possible value. {\"enum\":[]}", e.message)
    }

    test("multiple default values throws IllegalArgumentException") {
        val e = assertFailsWith<IllegalArgumentException> {
            registry(api) {
                Value(
                    NamingContext.ObjectProperty("value"),
                    Schema(
                        default = ExampleValue.Multiple(listOf("1", "2")),
                        enum = listOf("1", "2"),
                        type = Schema.Type.Basic.Integer
                    ),
                ).toModel(Input)
            }
        }
        assertEquals("Multiple default values not supported for enums.", e.message)
    }

    test("Null default in non-null enum") {
        val e = assertFailsWith<IllegalArgumentException> {
            registry(api) {
                Value(
                    NamingContext.ObjectProperty("value"),
                    Schema(
                        default = ExampleValue.Single("null"),
                        enum = listOf("1", "2"),
                        type = Schema.Type.Basic.Integer,
                        nullable = false
                    )
                ).toModel(Input)
            }
        }
        assertEquals(
            "The default value null is not present in the enum values: [1, 2] & schema is not nullable.",
            e.message
        )
    }
}
