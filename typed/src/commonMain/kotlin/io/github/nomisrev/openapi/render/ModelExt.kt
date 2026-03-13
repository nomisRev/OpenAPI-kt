package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.Object.AdditionalProperties.Allowed
import io.github.nomisrev.openapi.Model.Object.AdditionalProperties.Schema
import io.github.nomisrev.openapi.NamingContext

fun Model.hasDefault(): Boolean = when (this) {
    is Model.Enum -> default != null
    is Model.Collection -> default != null
    is Model.Primitive.Boolean -> default != null
    is Model.Primitive.Double -> default != null
    is Model.Primitive.Float -> default != null
    is Model.Primitive.Int -> default != null
    is Model.Primitive.Long -> default != null
    is Model.Primitive.String -> default != null
    is Model.ByteArray,
    is Model.Date,
    is Model.DateTime,
    is Model.FreeFormJson,
    is Model.Object,
    is Model.Primitive.Unit,
    is Model.Reference,
    is Model.Union,
    is Model.Uuid,
    is Model.DiscriminatedObject -> false
}

context(ctx: Renderer)
fun Model.Object.Property.serializer(): String {
    val serializer = model.serializer()
    return if (!isRequired && !serializer.endsWith(".nullable")) {
        ctx.import(Import.nullable)
        "$serializer.nullable"
    } else serializer
}

context(ctx: Renderer)
fun Model.serializer(): String = when (this) {
    is Model.Primitive -> {
        ctx.import(Import.serializer)
        when (this) {
            is Model.Primitive.Boolean -> "Boolean.serializer()"
            is Model.Primitive.Double -> "Double.serializer()"
            is Model.Primitive.Float -> "Float.serializer()"
            is Model.Primitive.Int -> "Int.serializer()"
            is Model.Primitive.Long -> "Long.serializer()"
            is Model.Primitive.String -> "String.serializer()"
            is Model.Primitive.Unit -> "Unit.serializer()"
        }
    }

    is Model.Uuid -> "Uuid.serializer()"
    is Model.Date -> "LocalDate.serializer()"
    is Model.DateTime -> "LocalDateTime.serializer()"
    is Model.FreeFormJson -> "JsonElement.serializer()"

    is Model.ByteArray -> {
        ctx.import(Import.ByteArraySerializer)
        "ByteArraySerializer()"
    }

    is Model.Collection if inner is Model.FreeFormJson -> "JsonArray.serializer()"
    is Model.Collection -> {
        ctx.import(Import.ListSerializer)
        "ListSerializer(${inner.serializer()})"
    }

    is Model.Object if properties.isEmpty() && additionalProperties is Allowed && additionalProperties.value -> "JsonObject.serializer()"
    is Model.Object if properties.isEmpty() && additionalProperties is Schema -> additionalProperties.value.serializer()

    is Model.ContextHolder -> "${serializerPath()}.serializer()"
}.let { serializer ->
    if (isNullable) {
        ctx.import(Import.nullable)
        "$serializer.nullable"
    } else serializer
}

context(ctx: Renderer)
private fun Model.ContextHolder.serializerPath(): String {
    val names = name().names
    val headSegments = when (val head = context.head) {
        is NamingContext.Path -> head.parts.size
        is NamingContext.Reference -> 1
    }
    val relative = names.drop(headSegments)
    return if (relative.isNotEmpty()) relative.joinToString(".") else name().simpleName
}
