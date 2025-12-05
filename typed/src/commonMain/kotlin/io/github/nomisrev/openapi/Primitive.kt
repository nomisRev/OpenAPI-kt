package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.parser.ExampleValue
import io.github.nomisrev.openapi.parser.Schema

context(ctx: ApiCtx)
suspend fun Schema.primitive(): Model =
    when (type) {
        is Schema.Type.Array,
        Schema.Type.Basic.Array,
        Schema.Type.Basic.Object,
        Schema.Type.Basic.Null,
        null -> throw IllegalStateException("$type not allowed for primitive types")

        Schema.Type.Basic.Number -> when (format) {
            "float" -> Model.Primitive.Float(
                default = default("Number", String::toFloatOrNull),
                description = description.get(),
                constraint = Constraints.Number(this),
                isNullable = nullable ?: false
            )

            else -> Model.Primitive.Double(
                default = default("Number", String::toDoubleOrNull),
                description = description.get(),
                constraint = Constraints.Number(this),
                isNullable = nullable ?: false
            )
        }

        Schema.Type.Basic.Boolean -> Model.Primitive.Boolean(
            default = default("Boolean") { it.lowercase().toBooleanStrictOrNull() },
            description = description.get(),
            isNullable = nullable ?: false
        )

        Schema.Type.Basic.Integer -> when (format) {
            "int32" -> Model.Primitive.Int(
                default = default("Integer", String::toIntOrNull),
                description = description.get(),
                constraint = Constraints.Number(this),
                isNullable = nullable ?: false
            )

            else -> Model.Primitive.Long(
                default = default("Integer", String::toLongOrNull),
                description = description.get(),
                constraint = Constraints.Number(this),
                isNullable = nullable ?: false
            )
        }

        Schema.Type.Basic.String -> when (format) {
            "binary" -> Model.ByteArray(description.get(), nullable ?: false)
            "uuid" -> Model.Uuid(description.get(), nullable ?: false)
            "date" -> Model.Date(description.get(), nullable ?: false)
            "date-time" -> Model.DateTime(description.get(), nullable ?: false)
            else -> Model.Primitive.String(
                default = default("String", String::toString) { it.joinToString() },
                description = description.get(),
                constraint = Constraints.Text(this),
                isNullable = nullable ?: false
            )
        }
    }

private inline fun <A : Any> Schema.default(
    label: String,
    onSingle: (String) -> A?,
    onMultiple: (List<String>) -> A,
): Model.Default<A>? =
    when (val default = default) {
        is ExampleValue.Single if default.value.equals("null", ignoreCase = true) -> Model.Default.Null
        is ExampleValue.Single ->
            Model.Default.Value(requireNotNull(onSingle(default.value)) { "Default value ${default.value} is not a $label." })

        is ExampleValue.Multiple -> Model.Default.Value(onMultiple(default.values))
        null -> null
    }

private inline fun <A : Any> Schema.default(label: String, onSingle: (String) -> A?): Model.Default<A>? =
    default(label, onSingle) {
        throw IllegalArgumentException("Multiple default values not supported for ${label}.")
    }
