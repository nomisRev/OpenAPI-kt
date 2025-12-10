package io.github.nomisrev

import io.github.nomisrev.openapi.Constraints
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.parser.ExampleValue
import io.github.nomisrev.openapi.parser.Schema

fun Model.Default.Companion.string() = listOf(
    ExampleValue.Single("text") expect Model.Default.Value("text"),
    ExampleValue.Multiple(listOf("text1", "text2")) expect Model.Default.Value("text1, text2"),
    null expect null
)

class TextConstraints(val min: Int?, val max: Int?, val pattern: String?)

fun Constraints.Text.Companion.all(): List<Expect<TextConstraints, Constraints.Text?>> = listOf(
    TextConstraints(null, null, null) expect null,
    TextConstraints(10, null, null) expect Constraints.Text(10, null, null),
    TextConstraints(null, 100, null) expect Constraints.Text(0, 100, null),
    TextConstraints(10, 100, null) expect Constraints.Text(10, 100, null),
    TextConstraints(null, null, "pattern") expect Constraints.Text(0, null, "pattern"),
    TextConstraints(10, null, "pattern") expect Constraints.Text(10, null, "pattern"),
    TextConstraints(null, 100, "pattern") expect Constraints.Text(0, 100, "pattern"),
    TextConstraints(10, 100, "pattern") expect Constraints.Text(10, 100, "pattern"),
)

fun Model.Primitive.String.Companion.all(): List<Expect<Schema, Model.Primitive.String>> = listOf(
    Schema.string expect Model.Primitive.String(null, null, null, false),
    Schema.string.nullable() expect Model.Primitive.String(null, null, null, true),
    Schema.string.copy(format = "byte") expect Model.Primitive.String(null, null, null, false),
    Schema.string.nullable().copy(format = "byte") expect Model.Primitive.String(null, null, null, true),
    Schema.string.copy(format = "random") expect Model.Primitive.String(null, null, null, false),
    Schema.string.nullable().copy(format = "random") expect Model.Primitive.String(null, null, null, true),
).product(description, Model.Default.string(), Constraints.Text.all()) { schema, description, default, constraint ->
    schema.actual.copy(
        description = description.actual,
        default = default.actual,
        minLength = constraint.actual.min,
        maxLength = constraint.actual.max,
        pattern = constraint.actual.pattern
    ) expect schema.expected.copy(
        description = description.expected,
        default = default.expected,
        constraint = constraint.expected
    )
} + Expect(
    Schema.string.copy(nullable = true, default = ExampleValue.Single("null")),
    Model.Primitive.String(Model.Default.Null, null, null, true),
)

fun Model.ByteArray.Companion.all(): List<Expect<Schema, Model.ByteArray>> = listOf(
    Schema.string.copy(format = "binary") expect Model.ByteArray(null, false),
    Schema.string.nullable().copy(format = "binary") expect Model.ByteArray(null, true),
).product(description) { schema, description ->
    schema.actual.copy(description = description.actual) expect schema.expected.copy(description = description.expected)
}

fun Model.Uuid.Companion.all() = listOf(
    Schema.string.copy(format = "uuid") expect Model.Uuid(null, false),
    Schema.string.nullable().copy(format = "uuid") expect Model.Uuid(null, true),
).product(description) { schema, description ->
    schema.actual.copy(description = description.actual) expect schema.expected.copy(description = description.expected)
}

fun Model.Date.Companion.all() = listOf(
    Schema.string.copy(format = "date") expect Model.Date(null, false),
    Schema.string.nullable().copy(format = "date") expect Model.Date(null, true),
).product(description) { schema, description ->
    schema.actual.copy(description = description.actual) expect schema.expected.copy(description = description.expected)
}

fun Model.DateTime.Companion.all() = listOf(
    Schema.string.copy(format = "date-time") expect Model.DateTime(null, false),
    Schema.string.nullable().copy(format = "date-time") expect Model.DateTime(null, true),
).product(description) { schema, description ->
    schema.actual.copy(description = description.actual) expect schema.expected.copy(description = description.expected)
}

fun Model.Default.Companion.integer() = listOf(
    ExampleValue.Single("7") expect Model.Default.Value(7),
//    ExampleValue.Single("null") expect Model.Default.Null,
    null expect null
)

fun Model.Default.Companion.bool() = listOf(
    ExampleValue.Single("true") expect Model.Default.Value(true),
    ExampleValue.Single("false") expect Model.Default.Value(false),
    null expect null
)

fun Model.Primitive.Boolean.Companion.all() = listOf(
    Schema.boolean expect Model.Primitive.Boolean(null, null, false),
    Schema.boolean.nullable() expect Model.Primitive.Boolean(null, null, true),
).product(description, Model.Default.bool()) { schema, description, default ->
    schema.actual.copy(
        description = description.actual,
        default = default.actual
    ) expect schema.expected.copy(
        description = description.expected,
        default = default.expected
    )
} + Expect(
    Schema.boolean.copy(nullable = true, default = ExampleValue.Single("null")),
    Model.Primitive.Boolean(Model.Default.Null, null, true),
)

data class SchemaNumberConstraints(
    val exclusiveMinimum: Boolean?,
    val minimum: Double?,
    val exclusiveMaximum: Boolean?,
    val maximum: Double?,
    val multipleOf: Double?,
)

fun Constraints.Number.Companion.all() = listOf(true, false, null).product(
    listOf(-1000.0, null),
    listOf(true, false, null),
    listOf(1000.0, null),
    listOf(2.0, null)
) { eMin, min, eMax, max, multipleOf ->
    if (eMin == null && min == null && eMax == null && max == null && multipleOf == null) SchemaNumberConstraints(
        eMin,
        min,
        eMax,
        max,
        multipleOf
    ) expect null
    else SchemaNumberConstraints(eMin, min, eMax, max, multipleOf) expect Constraints.Number(
        eMin ?: false,
        min,
        eMax ?: false,
        max,
        multipleOf
    )
}

fun Model.Primitive.Int.Companion.all() = listOf(
    Schema.integer.copy(format = "int32") expect Model.Primitive.Int(null, null, null, false),
    Schema.integer.nullable().copy(format = "int32") expect Model.Primitive.Int(null, null, null, true),
).product(description, Model.Default.integer(), Constraints.Number.all()) { schema, description, default, constraints ->
    schema.actual.copy(
        description = description.actual,
        default = default.actual,
        exclusiveMinimum = constraints.actual.exclusiveMinimum,
        minimum = constraints.actual.minimum,
        exclusiveMaximum = constraints.actual.exclusiveMaximum,
        maximum = constraints.actual.maximum,
        multipleOf = constraints.actual.multipleOf
    ) expect schema.expected.copy(
        description = description.expected,
        default = default.expected,
        constraint = constraints.expected
    )
} + Expect(
    Schema.integer.copy(nullable = true, format = "int32", default = ExampleValue.Single("null")),
    Model.Primitive.Int(Model.Default.Null, null, null, true),
)

fun Model.Default.Companion.number() = listOf(
    ExampleValue.Single("0.7") expect Model.Default.Value(0.7),
//    ExampleValue.Single("null") expect Model.Default.Null,
    null expect null
)

fun Model.Primitive.Long.Companion.all() = listOf(
    Schema.integer expect Model.Primitive.Long(null, null, null, false),
    Schema.integer.nullable() expect Model.Primitive.Long(null, null, null, true),
    Schema.integer.copy(format = "int64") expect Model.Primitive.Long(null, null, null, false),
    Schema.integer.nullable().copy(format = "int64") expect Model.Primitive.Long(null, null, null, true),
).product(description, Model.Default.integer(), Constraints.Number.all()) { schema, description, default, constraints ->
    schema.actual.copy(
        description = description.actual,
        default = default.actual,
        exclusiveMinimum = constraints.actual.exclusiveMinimum,
        minimum = constraints.actual.minimum,
        exclusiveMaximum = constraints.actual.exclusiveMaximum,
        maximum = constraints.actual.maximum,
        multipleOf = constraints.actual.multipleOf
    ) expect schema.expected.copy(
        description = description.expected,
        default = default.expected?.map(Int::toLong),
        constraint = constraints.expected
    )
} + Expect(
    Schema.integer.copy(nullable = true, default = ExampleValue.Single("null")),
    Model.Primitive.Long(Model.Default.Null, null, null, true),
)

fun Model.Primitive.Double.Companion.all() = listOf(
    Schema.number expect Model.Primitive.Double(null, null, null, false),
    Schema.number.copy(format = "double") expect Model.Primitive.Double(null, null, null, false),
    Schema.number.nullable() expect Model.Primitive.Double(null, null, null, true),
    Schema.number.nullable().copy(format = "double") expect Model.Primitive.Double(null, null, null, true),
).product(description, Model.Default.number(), Constraints.Number.all()) { schema, description, default, constraints ->
    schema.actual.copy(
        description = description.actual,
        default = default.actual,
        exclusiveMinimum = constraints.actual.exclusiveMinimum,
        minimum = constraints.actual.minimum,
        exclusiveMaximum = constraints.actual.exclusiveMaximum,
        maximum = constraints.actual.maximum,
        multipleOf = constraints.actual.multipleOf
    ) expect schema.expected.copy(
        description = description.expected,
        default = default.expected,
        constraint = constraints.expected
    )
} + Expect(
    Schema.number.copy(nullable = true, default = ExampleValue.Single("null")),
    Model.Primitive.Double(Model.Default.Null, null, null, true),
)

fun Model.Primitive.Float.Companion.all() = listOf(
    Schema.number.copy(format = "float") expect Model.Primitive.Float(null, null, null, false),
    Schema.number.nullable().copy(format = "float") expect Model.Primitive.Float(null, null, null, true),
).product(description, Model.Default.number(), Constraints.Number.all()) { schema, description, default, constraints ->
    schema.actual.copy(
        description = description.actual,
        default = default.actual,
        exclusiveMinimum = constraints.actual.exclusiveMinimum,
        minimum = constraints.actual.minimum,
        exclusiveMaximum = constraints.actual.exclusiveMaximum,
        maximum = constraints.actual.maximum,
        multipleOf = constraints.actual.multipleOf
    ) expect schema.expected.copy(
        description = description.expected,
        default = default.expected?.map(Double::toFloat),
        constraint = constraints.expected
    )
} + Expect(
    Schema.number.copy(format = "float", nullable = true, default = ExampleValue.Single("null")),
    Model.Primitive.Float(Model.Default.Null, null, null, true),
)

fun Model.Primitive.Companion.all(): List<Expect<Schema, Model>> =
    Model.Primitive.String.all() + Model.ByteArray.all() + Model.Uuid.all() + Model.Date.all() + Model.DateTime.all() + Model.Primitive.Boolean.all() + Model.Primitive.Int.all() + Model.Primitive.Long.all() + Model.Primitive.Double.all() + Model.Primitive.Float.all()

data class ObjectConstraint(val minProperties: Int?, val maxProperties: Int?)

fun Constraints.Object.Companion.all() = listOf(
    ObjectConstraint(null, null) expect null,
    ObjectConstraint(10, null) expect Constraints.Object(10, null),
    ObjectConstraint(null, 100) expect Constraints.Object(null, 100),
    ObjectConstraint(10, 100) expect Constraints.Object(10, 100),
)

fun Model.FreeFormJson.Companion.all(): List<Expect<Schema, Model.FreeFormJson>> = listOf(
    Schema() expect Model.FreeFormJson(null, null, false),
    Schema(nullable = true) expect Model.FreeFormJson(null, null, true),
).product(description, Constraints.Object.all()) { schema, description, constraints ->
    schema.actual.copy(
        description = description.actual,
        minProperties = constraints.actual.minProperties,
        maxProperties = constraints.actual.maxProperties
    ) expect schema.expected.copy(description = description.expected, constraint = constraints.expected)
}
