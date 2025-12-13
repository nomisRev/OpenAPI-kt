package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Constraints
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.parser.ExampleValue
import io.github.nomisrev.openapi.parser.Schema

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.primitive(): Model = when (this) {
    is ResolvedSchema.Recursive -> Model.Reference(name, description(), isNullable)
    is ResolvedSchema.Value -> toPrimitive()
    is ResolvedSchema.Reference -> {
        val inner = toPrimitive()
        Model.Object(
            name,
            inner.description,
            null, // TODO inner.title
            listOf(Model.Object.Property("value", inner.with(description = null, isNullable = false), true)),
            emptySet(),
            false,
            inner.isNullable
        )
    }
}

context(ctx: Registry.Scope)
private suspend fun ResolvedSchema.toPrimitive(): Model =
    when (schema.type) {
        is Schema.Type.Array,
        Schema.Type.Basic.Array,
        Schema.Type.Basic.Object,
        Schema.Type.Basic.Null,
        null -> throw IllegalStateException("${schema.type} not allowed for primitive types")

        Schema.Type.Basic.Number -> when (schema.format) {
            "float" -> Model.Primitive.Float(
                default = default("Number", String::toFloatOrNull),
                description = description(),
                constraint = Constraints.Number(schema),
                isNullable = isNullable
            )

            else -> Model.Primitive.Double(
                default = default("Number", String::toDoubleOrNull),
                description = description(),
                constraint = Constraints.Number(schema),
                isNullable = isNullable
            )
        }

        Schema.Type.Basic.Boolean -> Model.Primitive.Boolean(
            default = default("Boolean") { it.lowercase().toBooleanStrictOrNull() },
            description = description(),
            isNullable = isNullable
        )

        Schema.Type.Basic.Integer -> when (schema.format) {
            "int32" -> Model.Primitive.Int(
                default = default("Integer", String::toIntOrNull),
                description = description(),
                constraint = Constraints.Number(schema),
                isNullable = isNullable
            )

            else -> Model.Primitive.Long(
                default = default("Integer", String::toLongOrNull),
                description = description(),
                constraint = Constraints.Number(schema),
                isNullable = isNullable
            )
        }

        Schema.Type.Basic.String -> when (schema.format) {
            "binary" -> Model.ByteArray(description(), isNullable)
            "uuid" -> Model.Uuid(description(), isNullable)
            "date" -> Model.Date(description(), isNullable)
            "date-time" -> Model.DateTime(description(), isNullable)
            else -> Model.Primitive.String(
                default = default("String", String::toString) { it.joinToString() },
                description = description(),
                constraint = Constraints.Text(schema),
                isNullable = isNullable
            )
        }
    }

private inline fun <A : Any> ResolvedSchema.default(
    label: String,
    onSingle: (String) -> A?,
    onMultiple: (List<String>) -> A,
): Model.Default<A>? =
    when (val default = schema.default) {
        is ExampleValue.Single if default.value.equals("null", ignoreCase = true) -> Model.Default.Null
        is ExampleValue.Single ->
            Model.Default.Value(requireNotNull(onSingle(default.value)) { "Default value ${default.value} is not a $label." })

        is ExampleValue.Multiple -> Model.Default.Value(onMultiple(default.values))
        null -> null
    }

private inline fun <A : Any> ResolvedSchema.default(label: String, onSingle: (String) -> A?): Model.Default<A>? =
    default(label, onSingle) {
        throw IllegalArgumentException("Multiple default values not supported for ${label}.")
    }
