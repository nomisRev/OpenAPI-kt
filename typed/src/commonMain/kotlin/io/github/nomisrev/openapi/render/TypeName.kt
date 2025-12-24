package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.Object.AdditionalProperties.Allowed
import io.github.nomisrev.openapi.Model.Object.AdditionalProperties.Schema
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.render.TypeName.Class
import io.github.nomisrev.openapi.routes.SchemaContext

sealed interface Import {

    companion object {
        val serializer = TopLevelFunction("kotlinx.serialization.builtins", "serializer")
        val ListSerializer = TopLevelFunction("kotlinx.serialization.builtins", "ListSerializer")
        val nullable = TopLevelFunction("kotlinx.serialization.builtins", "nullable")
    }
}

data class TopLevelFunction(val packageName: String, val functionName: String) : Import

sealed interface TypeName : Import {
    data class Collection(val type: TypeName) : TypeName

    data class Class(val packageName: String, val names: List<String>) : TypeName {
        constructor(`package`: String, name: String) : this(`package`, listOf(name))

        val simpleName: String get() = names.last().toPascalCase()
        val fqName: String get() = "$packageName.${names.joinToString(".")}"

        fun nest(name: String) = copy(names = names + name)
    }

    fun type(): String = when (this) {
        is Class -> simpleName
        is Collection -> "List<${type.type()}>"
    }

    companion object {
        val Boolean = Class("kotlin", "Boolean")
        val String = Class("kotlin", "String")
        val Int = Class("kotlin", "Int")
        val Long = Class("kotlin", "Long")
        val Float = Class("kotlin", "Float")
        val Double = Class("kotlin", "Double")
        val Unit = Class("kotlin", "Unit")
        val Uuid = Class("kotlin.uuid", "Uuid")
        val ByteArray = Class("kotlin", "ByteArray")

        val JvmInline = Class("kotlin.jvm", "JvmInline")

        val JsName = Class("kotlin.js", "JsName")

        val Date = Class("kotlinx.datetime", "LocalDate")
        val DateTime = Class("kotlinx.datetime", "LocalDateTime")

        val JsonElement = Class("kotlinx.serialization.json", "JsonElement")
        val JsonArray = Class("kotlinx.serialization.json", "JsonArray")
        val JsonObject = Class("kotlinx.serialization.json", "JsonObject")
        val JsonClassDiscriminator = Class("kotlinx.serialization.json", "JsonClassDiscriminator")
        val JsonDecoder = Class("kotlinx.serialization.json", "JsonDecoder")

        val Serializable = Class("kotlinx.serialization", "Serializable")
        val SerialName = Class("kotlinx.serialization", "SerialName")
        val Required = Class("kotlinx.serialization", "Required")
        val ExperimentalSerializationApi = Class("kotlinx.serialization", "ExperimentalSerializationApi")
        val KeepGeneratedSerializer = Class("kotlinx.serialization", "KeepGeneratedSerializer")
        val InternalSerializationApi = Class("kotlinx.serialization", "InternalSerializationApi")
        val PolymorphicKind = Class("kotlinx.serialization.descriptors", "PolymorphicKind")
    }
}

context(ctx: Renderer)
fun Model.toTypeName(): TypeName = when (this) {
    is Model.Date -> TypeName.Date
    is Model.DateTime -> TypeName.DateTime
    is Model.Primitive.Float -> TypeName.Float
    is Model.Primitive.Long -> TypeName.Long
    is Model.Uuid -> TypeName.Uuid
    is Model.Primitive.Boolean -> TypeName.Boolean
    is Model.Primitive.Double -> TypeName.Double
    is Model.Primitive.Int -> TypeName.Int
    is Model.Primitive.String -> TypeName.String
    is Model.Primitive.Unit -> TypeName.Unit
    is Model.Collection if inner is Model.FreeFormJson -> TypeName.JsonArray
    is Model.Collection -> TypeName.Collection(inner.toTypeName())
    is Model.ByteArray -> TypeName.ByteArray
    is Model.FreeFormJson -> TypeName.JsonElement
    is Model.Object if properties.isEmpty() && additionalProperties is Allowed && additionalProperties.value -> TypeName.JsonObject
    is Model.Object if properties.isEmpty() && additionalProperties is Schema -> additionalProperties.value.toTypeName()
    is Model.ContextHolder -> context.name()
}

private fun SchemaContext.name(): String = when (this) {
    SchemaContext.Write -> "Request"
    SchemaContext.Read -> "Response"
    SchemaContext.Null -> ""
}

context(ctx: Renderer)
fun Model.ContextHolder.name(): Class = context.name()

fun Class?.renderAsSuperclass(): String =
    this?.simpleName?.let { " : $it" } ?: ""

context(ctx: Renderer)
fun NamingContext.name(): Class {
    val head = when (head) {
        is NamingContext.Path -> Class("${ctx.packageName}.api", head.parts)
        is NamingContext.Reference -> Class("${ctx.packageName}.model", "${head.name}${head.context.name()}")
    }
    return nested.fold(head) { acc, context ->
        val nestedName = when (context) {
            NamingContext.AdditionalProperties -> "Additional"
            is NamingContext.DiscriminatedObjectCase -> context.discriminator.toPascalCase()
            is NamingContext.ObjectProperty -> context.name.toPascalCase()
            is NamingContext.Response -> "${context.operationId.toPascalCase()}Response"
            is NamingContext.RouteBody -> "${context.operationId.toPascalCase()}Body"
            is NamingContext.RouteParam -> context.name.toPascalCase()
            is NamingContext.UnionCase -> context.value.toPascalCase()
        }
        acc.nest(nestedName)
    }
}
