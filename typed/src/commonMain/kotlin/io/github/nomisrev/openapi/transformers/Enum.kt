package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.enumLikeValues
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.withoutEnumLikeValues
import kotlin.text.equals

context(ctx: Registry.Scope)
suspend fun ResolvedSchema.toClosedEnum(context: SchemaContext, enum: List<String?>): Model.Enum {
    require(enum.isNotEmpty()) { "Enum requires at least 1 possible value. $schema" }
    val nestedNull = enum.any { it.equals("null", ignoreCase = true) || it == null } || schema.nullable == true
    val inner = ResolvedSchema.Value(
        name,
        schema.withoutEnumLikeValues().copy(description = null, default = null, nullable = false)
    ).let { ctx.registry().engine.transform(ctx, ctx.registry(), it, context, false) }
    val enumValues = enum.map(inner::toEnumValue)
    val enumDefault = enumDefault(inner)
    @Suppress("NullableToStringCall")
    require(!(enumDefault == Model.Default.Null && !nestedNull)) {
        "The default value $enumDefault is not present in the enum values: ${schema.enumLikeValues()} & schema is not nullable."
    }
    @Suppress("UnsafeCallOnNullableType")
    return Model.Enum(name, inner, enumValues, enumDefault, description(), schema.title, isNullable)
}

private fun Model.toEnumValue(rawValue: String?): Model.EnumValue =
    when {
        rawValue == null || rawValue.equals("null", ignoreCase = true) -> Model.EnumValue.Null(rawValue)
        this is Model.Primitive.Boolean ->
            Model.EnumValue.Boolean(
                requireNotNull(rawValue.lowercase().toBooleanStrictOrNull()) { "Enum value $rawValue is not a Boolean." },
                rawValue,
            )

        this is Model.Primitive.Int ->
            Model.EnumValue.Int(
                requireNotNull(rawValue.toIntOrNull()) { "Enum value $rawValue is not a Integer." },
                rawValue,
            )

        this is Model.Primitive.Long ->
            Model.EnumValue.Long(
                requireNotNull(rawValue.toLongOrNull()) { "Enum value $rawValue is not a Integer." },
                rawValue,
            )

        this is Model.Primitive.Float ->
            Model.EnumValue.Float(
                requireNotNull(rawValue.toFloatOrNull()) { "Enum value $rawValue is not a Number." },
                rawValue,
            )

        this is Model.Primitive.Double ->
            Model.EnumValue.Double(
                requireNotNull(rawValue.toDoubleOrNull()) { "Enum value $rawValue is not a Number." },
                rawValue,
            )

        else -> Model.EnumValue.String(rawValue)
    }

private fun ResolvedSchema.enumDefault(inner: Model): Model.Default<Model.EnumValue>? = when (val defaultValue = schema.default) {
    is Single if defaultValue.value.equals("null", ignoreCase = true) -> Model.Default.Null
    is Single -> Model.Default.Value(inner.toEnumValue(defaultValue.value))
    // OpenAI sometimes has empty list for default where inapplicable sometimes
    is Multiple if (defaultValue.values.isEmpty()) -> null
    is Multiple -> throw IllegalArgumentException("Multiple default values not supported for enums. $this")
    null -> null
}

fun ResolvedSchema.default(): Model.Default<String>? = when (val defaultValue = schema.default) {
    is Single if defaultValue.value.equals("null", ignoreCase = true) -> Model.Default.Null
    is Single -> Model.Default.Value(defaultValue.value)
    is Multiple if (defaultValue.values.isEmpty()) -> null
    is Multiple -> throw IllegalArgumentException("Multiple default values not supported for enums. $this")
    null -> null
}
