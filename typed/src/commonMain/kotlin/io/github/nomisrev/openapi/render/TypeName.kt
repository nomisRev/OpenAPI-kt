package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.Object.AdditionalProperties.Allowed
import io.github.nomisrev.openapi.Model.Object.AdditionalProperties.Schema
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.render.TypeName.Class
import io.github.nomisrev.openapi.routes.SchemaContext

sealed interface TypeName {
    data class Collection(val type: TypeName) : TypeName
    data class Class(val `package`: String, val names: List<String>) : TypeName {
        constructor(`package`: String, name: String) : this(`package`, listOf(name))

        val simpleName: String get() = names.last()

        fun nest(name: String) = copy(names = names + name)
    }

    fun type(): String = when (this) {
        is Class -> names.joinToString(separator = ".")
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

        val ByteArray = Class("kotlin", "ByteArray")
        val JsonElement = Class("kotlinx.serialization.json", "JsonElement")
        val JsonObject = Class("kotlinx.serialization.json", "JsonObject")
        val JsonArray = Class("kotlinx.serialization.json", "JsonArray")
        val Date = Class("kotlinx.datetime", "LocalDate")
        val DateTime = Class("kotlinx.datetime", "LocalDateTime")
        val Uuid = Class("kotlin.uuid", "Uuid")
    }
}

tailrec fun TypeName.import(): String = when (this) {
    is Class -> "${`package`}.$names"
    is TypeName.Collection -> type.import()
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
    is Model.ContextHolder -> context.name(this)
}

private fun SchemaContext.name(): String = when (this) {
    SchemaContext.Write -> "Request"
    SchemaContext.Read -> "Response"
    SchemaContext.Null -> ""
}

context(builder: MutableList<NamingContext>)
private fun NamingContext.Nested.nested() {
    if (inner !is NamingContext.Nested) builder.add(inner)
    if (outer !is NamingContext.Nested) builder.add(outer)
}

context(ctx: Renderer)
fun Model.ContextHolder.name(): Class = context.name(this)

context(ctx: Renderer)
private fun NamingContext.name(model: Model.ContextHolder): Class = when (val context = this) {
    is NamingContext.Reference -> Class(
        "${ctx.packageName}.model",
        "${context.name}${context.context.name()}"
    )

    is NamingContext.Path -> Class("${ctx.packageName}.api", context.parts)

    NamingContext.AdditionalProperties -> throw IllegalStateException("AdditionalProperties cannot be rendered as a top-level type name")
    is NamingContext.DiscriminatedObjectCase -> throw IllegalStateException("DiscriminatedObjectCase cannot be rendered as a top-level type name")
    is NamingContext.ObjectProperty -> throw IllegalStateException("ObjectProperty cannot be rendered as a top-level type name")
    is NamingContext.Response -> throw IllegalStateException("Response cannot be rendered as a top-level type name")
    is NamingContext.RouteBody -> throw IllegalStateException("RouteBody cannot be rendered as a top-level type name")
    is NamingContext.RouteParam -> throw IllegalStateException("RouteParam cannot be rendered as a top-level type name")
    is NamingContext.UnionCase -> throw IllegalStateException("UnionCase cannot be rendered as a top-level type name")
    is NamingContext.Nested -> {
        val parts = buildList { context.nested() }.reversed()
        val head = parts.first().name(model)
        parts.drop(1).fold(head) { acc, context ->
            val nestedName = when (context) {
                NamingContext.AdditionalProperties -> "Additional"
                is NamingContext.DiscriminatedObjectCase -> context.discriminator.toPascalCase()
                is NamingContext.ObjectProperty -> context.name.toPascalCase()
                is NamingContext.Response -> "${context.operationId.toPascalCase()}Response"
                is NamingContext.RouteBody -> "${context.operationId.toPascalCase()}Body"
                is NamingContext.RouteParam -> context.name.toPascalCase()
                is NamingContext.UnionCase -> context.value.toPascalCase()
                is NamingContext.Nested -> throw IllegalStateException("Nested was filtered out.")
                is NamingContext.Path -> throw IllegalStateException("Path cannot be nested")
                is NamingContext.Reference -> throw IllegalStateException("Reference cannot be nested")
            }
            acc.nest(nestedName)
        }
    }

}
