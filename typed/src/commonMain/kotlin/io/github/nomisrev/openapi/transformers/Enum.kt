package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.enumLikeValues
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.withoutEnumLikeValues
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/**
 * The Kotlin enum for the [enum] members of this schema, decided from the JSON type of each member (`1` is not `"1"`,
 * and the JSON `null` is not the string `"null"`).
 *
 * A schema whose members cannot all be constants of one Kotlin enum is generated as its underlying type, without the
 * `enum` restriction, and the registry records a warning naming the schema:
 * - a member is an array or an object,
 * - the schema declares no `type` and its members are of different JSON types, which one enum could not send as
 *   what they are.
 *
 * A member or `default` of another JSON type than the declared `type` is not valid, and fails with the schema and the
 * value.
 */
context(ctx: Registry.Scope)
suspend fun ResolvedSchema.toClosedEnum(context: SchemaContext, enum: List<JsonElement>): Model {
    require(enum.isNotEmpty()) { "Enum requires at least 1 possible value. $schema" }
    val nestedNull = enum.any { it is JsonNull } || schema.nullable == true
    val declared = schema.type
    val members = enum.filter { it !is JsonNull }

    val unrepresentable = when {
        members.any { it is JsonArray || it is JsonObject } ->
            "has members that are arrays or objects, which a Kotlin enum cannot hold"

        declared == null && members.mixedPrimitiveKinds() ->
            "has no `type` and members of different JSON types (${members.kinds()}), which one Kotlin enum cannot send as what they are"

        else -> null
    }
    if (unrepresentable != null) {
        ctx.registry().warn(
            "Enum of ${name.describe()} $unrepresentable: it is not generated as a Kotlin enum" +
                (declared?.let { "; the declared type is used" } ?: "; its values are kept as JSON") +
                ". Members: ${enum.joinToString(", ")}."
        )
        val underlying = schema.withoutEnumLikeValues().copy(nullable = if (nestedNull) true else schema.nullable)
        return ctx.registry().engine.transform(ctx, ctx.registry(), ResolvedSchema.Value(name, underlying), context, false)
    }

    val innerSchema = schema.withoutEnumLikeValues().copy(description = null, default = null, nullable = false)
    val inner = ResolvedSchema.Value(
        name,
        if (declared == null) innerSchema.copy(type = members.inferredType()) else innerSchema
    ).let { ctx.registry().engine.transform(ctx, ctx.registry(), it, context, false) }
    val enumValues = enum.map { inner.toEnumValue(it, this) }
    val enumDefault = enumDefault(inner)
    @Suppress("NullableToStringCall")
    require(!(enumDefault == Model.Default.Null && !nestedNull)) {
        "The default value $enumDefault is not present in the enum values: ${schema.enumLikeValues()} & schema is not nullable."
    }
    @Suppress("UnsafeCallOnNullableType")
    return Model.Enum(name, inner, enumValues, enumDefault, description(), schema.title, isNullable)
}

/** Integers are numbers, so `[1, 1.5]` is one kind of member. */
private fun List<JsonElement>.mixedPrimitiveKinds(): Boolean =
    map { if (it.kind() == "integer") "number" else it.kind() }.distinct().size > 1

private fun List<JsonElement>.kinds(): String = map { it.kind() }.distinct().joinToString(", ")

private fun JsonElement.kind(): String = when {
    this is JsonPrimitive && isString -> "string"
    this is JsonPrimitive && (content == "true" || content == "false") -> "boolean"
    this is JsonPrimitive && content.toLongOrNull() != null -> "integer"
    this is JsonPrimitive -> "number"
    else -> "structured"
}

/** The type of the members of an enum that declares none: they are all of one JSON type here. */
private fun List<JsonElement>.inferredType(): Schema.Type? = when {
    isEmpty() -> null
    all { it.kind() == "boolean" } -> Schema.Type.Basic.Boolean
    all { it.kind() == "integer" } -> Schema.Type.Basic.Integer
    all { it.kind() == "integer" || it.kind() == "number" } -> Schema.Type.Basic.Number
    else -> null
}

context(ctx: Registry.Scope)
private fun Model.toEnumValue(member: JsonElement, schema: ResolvedSchema): Model.EnumValue {
    if (member is JsonNull) return Model.EnumValue.Null(null)
    val primitive = member as JsonPrimitive
    val raw = primitive.content
    fun mismatch(label: String): Nothing =
        throw IllegalArgumentException("Enum value $member at ${schema.name.describe()} is not a $label.")

    // A string member that parses as the declared number or boolean is read as it, with a warning (`"7"` for 7).
    val literal = raw
    fun <A> coerced(label: String, parsed: A?): A {
        val value = parsed ?: mismatch(label)
        if (primitive.isString) {
            ctx.registry().warn(
                "Enum value $member at ${schema.name.describe()} is a string; read as the ${label.lowercase()} " +
                    "$value because the schema declares type ${label.lowercase()}."
            )
        }
        return value
    }
    return when (this) {
        is Model.Primitive.Boolean ->
            Model.EnumValue.Boolean(coerced("Boolean", literal.lowercase().toBooleanStrictOrNull()), raw)

        is Model.Primitive.Int -> Model.EnumValue.Int(coerced("Integer", literal.toIntOrNull()), raw)
        is Model.Primitive.Long -> Model.EnumValue.Long(coerced("Integer", literal.toLongOrNull()), raw)
        is Model.Primitive.Float -> Model.EnumValue.Float(coerced("Number", literal.toFloatOrNull()), raw)
        is Model.Primitive.Double -> Model.EnumValue.Double(coerced("Number", literal.toDoubleOrNull()), raw)
        // A schema that declares `type: string` takes the text of a number or a boolean member as it is: a YAML
        // document cannot tell `'1'` from `1`, and the member can only be sent as a string.
        else -> Model.EnumValue.String(raw)
    }
}

context(ctx: Registry.Scope)
private fun ResolvedSchema.enumDefault(inner: Model): Model.Default<Model.EnumValue>? =
    when (val defaultValue = schema.default) {
        null -> null
        JsonNull -> Model.Default.Null
        is JsonPrimitive -> Model.Default.Value(inner.toEnumValue(defaultValue, this))
        // OpenAI sometimes has empty list for default where inapplicable sometimes
        is JsonArray if defaultValue.isEmpty() -> null
        else -> throw IllegalArgumentException(
            "Default value $defaultValue at ${name.describe()} is not supported for an enum: it must be one of the members."
        )
    }

/** The default of a schema generated as text, such as a union, where the JSON `null` is no default text. */
fun ResolvedSchema.default(): Model.Default<String>? = when (val defaultValue = schema.default) {
    null -> null
    JsonNull -> Model.Default.Null
    is JsonPrimitive -> Model.Default.Value(defaultValue.content)
    is JsonArray if defaultValue.isEmpty() -> null
    is JsonArray if defaultValue.all { it is JsonPrimitive } ->
        throw IllegalArgumentException("Multiple default values not supported for ${name.describe()}.")
    else -> Model.Default.Value(defaultValue.toString())
}
