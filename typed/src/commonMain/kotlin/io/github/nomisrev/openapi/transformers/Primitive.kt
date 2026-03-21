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
    is ResolvedSchema.Recursive -> Model.Reference(name, description(), isNullable, schema.title)
    is ResolvedSchema.Value -> toPrimitive()
    is ResolvedSchema.Reference -> {
        val inner = toPrimitive()
        Model.Object.value(
            context = reference,
            property = inner,
            title = schema.title,
            isScalarWrapper = true,
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
                isNullable = isNullable,
                title = schema.title
            )

            else -> Model.Primitive.Double(
                default = default("Number", String::toDoubleOrNull),
                description = description(),
                constraint = Constraints.Number(schema),
                isNullable = isNullable,
                title = schema.title
            )
        }

        Schema.Type.Basic.Boolean -> Model.Primitive.Boolean(
            default = default("Boolean") { it.lowercase().toBooleanStrictOrNull() },
            description = description(),
            isNullable = isNullable,
            title = schema.title
        )

        Schema.Type.Basic.Integer -> when (schema.format) {
            "int32" -> Model.Primitive.Int(
                default = default("Integer", String::toIntOrNull),
                description = description(),
                constraint = Constraints.Number(schema),
                isNullable = isNullable,
                title = schema.title
            )

            else -> Model.Primitive.Long(
                default = default("Integer", String::toLongOrNull),
                description = description(),
                constraint = Constraints.Number(schema),
                isNullable = isNullable,
                title = schema.title
            )
        }

        Schema.Type.Basic.String -> when (schema.format) {
            "binary" -> Model.ByteArray(description(), isNullable, schema.title)
            "uuid" -> Model.Uuid(description(), isNullable, schema.title)
            "date" -> Model.Date(description(), isNullable, schema.title)
            "date-time" -> Model.DateTime(description(), isNullable, schema.title)
            else -> Model.Primitive.String(
                default = default("String", String::toString) { it.joinToString() },
                description = description(),
                constraint = Constraints.Text(schema),
                isNullable = isNullable,
                title = schema.title
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
