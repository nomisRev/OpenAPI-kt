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
import io.github.nomisrev.openapi.UnionDispatch
import io.github.nomisrev.openapi.parser.ExampleValue
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.product
import io.github.nomisrev.verifyFails
import kotlin.concurrent.atomics.ExperimentalAtomicApi

private val nulls = listOf("null", "NULL", null)

private fun enumString(value: String?): Model.EnumValue = when (value) {
    null -> Model.EnumValue.Null()
    "null", "NULL" -> Model.EnumValue.Null(value)
    else -> Model.EnumValue.String(value)
}

private fun enumInt(value: String?): Model.EnumValue = when (value) {
    null -> Model.EnumValue.Null()
    "null", "NULL" -> Model.EnumValue.Null(value)
    else -> Model.EnumValue.Int(value.toInt(), value)
}

private fun enumLong(value: String?): Model.EnumValue = when (value) {
    null -> Model.EnumValue.Null()
    "null", "NULL" -> Model.EnumValue.Null(value)
    else -> Model.EnumValue.Long(value.toLong(), value)
}

private fun enumFloat(value: String?): Model.EnumValue = when (value) {
    null -> Model.EnumValue.Null()
    "null", "NULL" -> Model.EnumValue.Null(value)
    else -> Model.EnumValue.Float(value.toFloat(), value)
}

private fun enumDouble(value: String?): Model.EnumValue = when (value) {
    null -> Model.EnumValue.Null()
    "null", "NULL" -> Model.EnumValue.Null(value)
    else -> Model.EnumValue.Double(value.toDouble(), value)
}

private fun enumBoolean(value: String?): Model.EnumValue = when (value) {
    null -> Model.EnumValue.Null()
    "null", "NULL" -> Model.EnumValue.Null(value)
    else -> Model.EnumValue.Boolean(value.toBooleanStrict(), value)
}

private fun default(
    values: List<String>,
    parse: (String?) -> Model.EnumValue,
): List<Expect<ExampleValue.Single?, Model.Default<Model.EnumValue>?>> =
    values.map { ExampleValue.Single(it) expect Model.Default.Value(parse(it)) } + listOf(null expect null)

private fun nullDefault(
    values: List<String>,
    parse: (String?) -> Model.EnumValue,
): List<Expect<ExampleValue.Single?, Model.Default<Model.EnumValue>?>> =
    default(values, parse) + listOf(ExampleValue.Single("null") expect Model.Default.Null)

private fun List<Expect<Schema, Model>>.enum(
    name: NamingContext,
    values: List<String>,
    parse: (String?) -> Model.EnumValue,
) = product(description, default(values, parse)) { schema, description, default ->
    schema.actual.copy(
        description = description.actual,
        default = default.actual,
        enum = values.toList()
    ) expect Model.Enum(
        context = name,
        inner = schema.expected.with(description = null, isNullable = false).default(null),
        values = values.map(parse),
        default = default.expected,
        description = description.expected,
        title = null,
        isNullable = schema.expected.isNullable
    )
} + product(description, nullDefault(values, parse), nulls) { schema, description, default, NULL ->
    schema.actual.copy(
        description = description.actual,
        default = default.actual,
        enum = (values + NULL).toList()
    ) expect Model.Enum(
        context = name,
        inner = schema.expected.with(description = null, isNullable = false).default(null),
        values = (values + NULL).map(parse),
        default = default.expected,
        description = description.expected,
        title = null,
        isNullable = schema.expected.isNullable
    )
}

fun Model.Enum.Companion.ints(name: NamingContext): List<Expect<Schema, Model>> =
    Model.Primitive.Int.all().enum(name, listOf("1", "2", "3"), ::enumInt)

fun Model.Enum.Companion.longs(name: NamingContext): List<Expect<Schema, Model>> =
    Model.Primitive.Long.all().enum(name, listOf("1", "2", "3"), ::enumLong)

fun Model.Enum.Companion.strings(name: NamingContext): List<Expect<Schema, Model>> =
    Model.Primitive.String.all().enum(name, listOf("A", "B", "C"), ::enumString)

fun Model.Enum.Companion.floats(name: NamingContext): List<Expect<Schema, Model>> =
    Model.Primitive.Float.all().enum(name, listOf("1.1", "2.2", "3.3"), ::enumFloat)

fun Model.Enum.Companion.doubles(name: NamingContext): List<Expect<Schema, Model>> =
    Model.Primitive.Double.all().enum(name, listOf("1.1", "2.2", "3.3"), ::enumDouble)

fun Model.Enum.Companion.booleans(name: NamingContext): List<Expect<Schema, Model>> =
    Model.Primitive.Boolean.all().enum(name, listOf("true", "false"), ::enumBoolean)

@OptIn(ExperimentalAtomicApi::class)
val closedEnumSpec by testSuite {
    verifyAll("ClosedEnum(Int)", Model.Enum.ints(NamingContext.path("test")))
    verifyAll("ClosedEnum(Long)", Model.Enum.longs(NamingContext.path("test")))
    verifyAll("ClosedEnum(String)", Model.Enum.strings(NamingContext.path("test")))
    verifyAll("ClosedEnum(Float)", Model.Enum.floats(NamingContext.path("test")))
    verifyAll("ClosedEnum(Double)", Model.Enum.doubles(NamingContext.path("test")))
    verifyAll("ClosedEnum(Boolean)", Model.Enum.booleans(NamingContext.path("test")))

    verifyFails<IllegalArgumentException>(
        "Empty enum throws IllegalArgumentException",
        Schema(enum = emptyList()),
        "Enum requires at least 1 possible value. {\"enum\":[]}"
    )

//    verifyFails<IllegalArgumentException>(
//        "multiple default values throws IllegalArgumentException",
//        Schema(
//            default = ExampleValue.Multiple(listOf("1", "2")),
//            enum = listOf("1", "2"),
//            type = Schema.Type.Basic.Integer
//        ),
//        "Multiple default values not supported for enums."
//    )

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
        default(values, ::enumString),
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

        val nestedEnum = Model.Enum(
            name.nest(NamingContext.UnionCase(values.joinToString("Or") {
                it.replaceFirstChar { c -> c.uppercase() }
            })),
            Model.Primitive.String(null, null, null, false, null),
            values.map(::enumString),
            null,
            null,
            null,
            false
        )

        anyOf expect Model.AnyOf(
            context = name,
            description = description.expected,
            title = null,
//            default = default.expected,
            default = null, // TODO Union defaults
            cases = listOf(
                Model.Union.Case(nestedEnum, emptySet()),
                Model.Union.Case(Model.Primitive.String(null, null, null, false, null), emptySet()),
            ),
            dispatch = UnionDispatch.Structural,
            isNullable = isNullable ?: false
        )
    }

    // TODO fix:
//    verifyAll(
//        "OpenEnum",
//        Model.Primitive.String.all().openEnum(NamingContext.ObjectProperty("test"), listOf("A", "B", "C"))
//    )

    verifyFails<IllegalArgumentException>(
        "Empty open enum throws IllegalArgumentException",
        Schema(
            anyOf = listOf(
                ReferenceOr.value(Schema(enum = emptyList())),
                ReferenceOr.value(Schema(type = Schema.Type.Basic.String))
            ),
        ),
        "Enum requires at least 1 possible value. {\"enum\":[]}",
    )

//    verifyFails<IllegalArgumentException>(
//        "OpenEnum multiple default values throws IllegalArgumentException",
//        Schema(
//            anyOf = listOf(
//                ReferenceOr.value(Schema(enum = listOf("1", "2"), type = Schema.Type.Basic.Integer)),
//                ReferenceOr.value(Schema(type = Schema.Type.Basic.String))
//            ),
//            default = ExampleValue.Multiple(listOf("1", "2"))
//        ),
//        "Multiple default values not supported for enums."
//    )
}
