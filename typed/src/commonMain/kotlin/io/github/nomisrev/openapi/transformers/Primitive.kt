package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Constraints
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.description
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
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

@Suppress("NullableToStringCall")
context(ctx: Registry.Scope)
private suspend fun ResolvedSchema.toPrimitive(): Model =
    when (schema.type) {
        is Schema.Type.Array,
        Schema.Type.Basic.Array,
        Schema.Type.Basic.Object,
        Schema.Type.Basic.Null,
        null -> error("${schema.type} not allowed for primitive types")

        Schema.Type.Basic.Number -> when (schema.format) {
            "float" -> Model.Primitive.Float(
                default = default("Number", onLiteral = String::toFloatOrNull),
                description = description(),
                constraint = Constraints.Number(schema),
                isNullable = isNullable,
                title = schema.title
            )

            else -> Model.Primitive.Double(
                default = default("Number", onLiteral = String::toDoubleOrNull),
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
                default = default("Integer", onLiteral = String::toIntOrNull),
                description = description(),
                constraint = Constraints.Number(schema),
                isNullable = isNullable,
                title = schema.title
            )

            else -> Model.Primitive.Long(
                default = default("Integer", onLiteral = String::toLongOrNull),
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
                default = default("String", acceptsString = true, onLiteral = String::toString) { it.joinToString() },
                description = description(),
                constraint = Constraints.Text(schema),
                isNullable = isNullable,
                title = schema.title
            )
        }
    }

/**
 * The `default` of this schema as the Kotlin value of its declared type, decided from the JSON type of the default:
 * a number or a boolean is parsed by [onLiteral], a string that parses as the declared type is read as it with a
 * warning (`"7"` for an integer), any other string is rejected, and the JSON `null` is [Model.Default.Null], where the string `"null"` is the text `null`.
 *
 * A string schema takes the text of a number or a boolean default as it is, as YAML cannot tell `'1'` from `1`.
 */
context(ctx: Registry.Scope)
private fun <A : Any> ResolvedSchema.default(
    label: String,
    acceptsString: Boolean = false,
    onLiteral: (String) -> A?,
    onMultiple: (List<String>) -> A,
): Model.Default<A>? = when (val default = schema.default) {
    null -> null
    JsonNull -> nullDefault(isNullable)
    is JsonPrimitive -> Model.Default.Value(parseDefault(default, label, acceptsString, onLiteral))
    is JsonArray if default.all { it is JsonPrimitive } ->
        Model.Default.Value(onMultiple(default.map { (it as JsonPrimitive).content }))
    // An object, or an array of structured values: only text can hold it.
    else -> Model.Default.Value(parseDefault(JsonPrimitive(default.toString()), label, acceptsString, onLiteral))
}

context(ctx: Registry.Scope)
private fun <A : Any> ResolvedSchema.parseDefault(
    default: JsonPrimitive,
    label: String,
    acceptsString: Boolean,
    onLiteral: (String) -> A?,
): A {
    val parsed = requireNotNull(onLiteral(default.content)) {
        "Default value $default at ${name.describe()} is not a $label."
    }
    if (default.isString && !acceptsString) {
        ctx.registry().warn(
            "Default value $default at ${name.describe()} is a string; read as the ${label.lowercase()} $parsed " +
                "because the schema declares type ${label.lowercase()}."
        )
    }
    return parsed
}

context(ctx: Registry.Scope)
private fun <A : Any> ResolvedSchema.default(
    label: String,
    acceptsString: Boolean = false,
    onLiteral: (String) -> A?,
): Model.Default<A>? =
    default(label, acceptsString, onLiteral) {
        throw IllegalArgumentException("Multiple default values not supported for ${label} at ${name.describe()}.")
    }
