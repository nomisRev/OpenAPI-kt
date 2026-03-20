package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.BYTE_ARRAY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.UNIT

private val UuidType = ClassName("kotlin.uuid", "Uuid")
private val LocalDateType = ClassName("kotlinx.datetime", "LocalDate")
private val InstantType = ClassName("kotlinx.datetime", "Instant")
private val JsonElementType = ClassName("kotlinx.serialization.json", "JsonElement")
private val JsonArrayType = ClassName("kotlinx.serialization.json", "JsonArray")
private val ListType = ClassName("kotlin.collections", "List")

fun Model.toTypeName(config: RenderConfig): TypeName {
    val typeName = when (this) {
        is Model.ByteArray -> BYTE_ARRAY
        is Model.Collection -> {
            if (inner is Model.FreeFormJson) JsonArrayType
            else ListType.parameterizedBy(inner.toTypeName(config))
        }

        is Model.Date -> LocalDateType
        is Model.DateTime -> InstantType
        is Model.DiscriminatedObject -> context.toClassName(config)
        is Model.Enum -> context.toClassName(config)
        is Model.FreeFormJson -> JsonElementType
        is Model.Object -> context.toClassName(config)
        is Model.Primitive.Boolean -> BOOLEAN
        is Model.Primitive.Double -> DOUBLE
        is Model.Primitive.Float -> FLOAT
        is Model.Primitive.Int -> INT
        is Model.Primitive.Long -> LONG
        is Model.Primitive.String -> STRING
        is Model.Primitive.Unit -> UNIT
        is Model.Reference -> context.toClassName(config)
        is Model.Union -> context.toClassName(config)
        is Model.Uuid -> UuidType
    }
    return typeName.copy(nullable = isNullable)
}

fun NamingContext.toClassName(config: RenderConfig): ClassName =
    when (val root = head) {
        is NamingContext.Reference -> {
            val simpleNames = listOf(root.name.toPascalCase()) + nested.map { it.toSimpleName() }
            className(config.modelPackage.toPackageName(), simpleNames)
        }

        is NamingContext.Path -> {
            val rootSegmentNames = root.segments.map(PathSegment::routeSegmentSimpleName).ifEmpty { listOf("Root") }
            val methodName = root.method.value.lowercase().replaceFirstChar { it.uppercase() }
            val simpleNames = rootSegmentNames.toMutableList()
            nested.forEach { nestedContext ->
                when (nestedContext) {
                    NamingContext.RouteBody -> {
                        simpleNames.add(methodName)
                        simpleNames.add("Body")
                    }

                    NamingContext.Response -> {
                        simpleNames.add(methodName)
                        simpleNames.add("Response")
                    }

                    else -> simpleNames.add(nestedContext.toSimpleName())
                }
            }
            className(config.apiPackage.toPackageName(), simpleNames)
        }
    }

private fun NamingContext.Nested.toSimpleName(): String =
    when (this) {
        is NamingContext.ObjectProperty -> name.toPascalCase()
        is NamingContext.UnionCase -> value.toPascalCase()
        is NamingContext.DiscriminatedObjectCase -> discriminator.toPascalCase()
        NamingContext.AdditionalProperties -> "AdditionalProperties"
        is NamingContext.RouteParam -> name.toPascalCase()
        NamingContext.RouteBody -> "Body"
        NamingContext.Response -> "Response"
    }

private fun String.toPackageName(): String =
    split('.').filter(String::isNotBlank).joinToString(".") { it.sanitize() }

private fun className(packageName: String, names: List<String>): ClassName {
    val sanitizedNames = names.map(::sanitizeSimpleName)
    return ClassName(packageName, sanitizedNames.first(), *sanitizedNames.drop(1).toTypedArray())
}

private fun sanitizeSimpleName(raw: String): String {
    val stripped = raw.replace(Regex("[^A-Za-z0-9_]"), "").ifBlank { "Unnamed" }
    return if (stripped.first().isDigit()) "_$stripped" else stripped
}
