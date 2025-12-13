package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.routes.SchemaContext

context(imports: Imports)
operator fun ClassName.not() = imports.import(this)

data class ClassName(val packageName: String, val classNames: List<String>) {
    constructor(packageName: String, className: String) : this(packageName, listOf(className))

    fun render(): String = classNames.joinToString(".")
}

fun Model.className(): ClassName = when (this) {
    is Model.ByteArray -> ClassName("kotlin", "ByteArray")
    is Model.Date -> ClassName("kotlinx.datetime", "LocalDate")
    is Model.DateTime -> ClassName("kotlinx.datetime", "LocalDateTime")
    is Model.FreeFormJson -> ClassName("kotlinx.serialization.json", "JsonElement")
    is Model.Primitive.Boolean -> ClassName("kotlin", "Boolean")
    is Model.Primitive.Double -> ClassName("kotlin", "Double")
    is Model.Primitive.Float -> ClassName("kotlin", "Float")
    is Model.Primitive.Long -> ClassName("kotlin", "Long")
    is Model.Primitive.String -> ClassName("kotlin", "String")
    is Model.Primitive.Int -> ClassName("kotlin", "Int")
    is Model.Primitive.Unit -> ClassName("kotlin", "Unit")
    is Model.Uuid -> ClassName("kotlin", "Uuid")
    is Model.Object if context is NamingContext.Reference -> ClassName("io.github.nomisrev.model", context.name)
    is Model.Union if context is NamingContext.Reference -> ClassName("io.github.nomisrev.model", context.name)
    is Model.Enum if context is NamingContext.Reference -> ClassName("io.github.nomisrev.model", context.name)

    is Model.Union -> TODO()
    is Model.Object -> when (context) {
        NamingContext.AdditionalProperties -> ClassName("ignored-always-nested", "Additional")
        is NamingContext.DiscriminatedObjectCase -> ClassName("ignored-always-nested", context.discriminator)
        is NamingContext.ObjectProperty -> ClassName("ignored-always-nested", context.name)
        is NamingContext.Response -> ClassName("ignored-always-nested", "${context.operationId}Response")
        is NamingContext.RouteBody -> ClassName("ignored-always-nested", "${context.operationId}Body")
        is NamingContext.RouteParam -> ClassName("ignored-always-nested", context.name)
        NamingContext.UnionCase -> TODO()
        is NamingContext.Nested -> TODO()
        is NamingContext.Path -> throw IllegalStateException("Model.Object cannot be rendered as a path part")
        is NamingContext.Reference -> ClassName("io.github.nomisrev.model", context.name)
    }

    is Model.Collection -> TODO()
    is Model.Enum -> TODO()
    is Model.DiscriminatedObject -> TODO()
    is Model.Reference -> TODO()
}

fun NamingContext.className(model: Model): ClassName = when (this) {
    NamingContext.AdditionalProperties -> ClassName("ignored-always-nested", "AdditionalProperties")
    is NamingContext.DiscriminatedObjectCase -> ClassName("ignored-always-nested", discriminator)
    is NamingContext.ObjectProperty -> ClassName("ignored-always-nested", name)
    is NamingContext.Response -> ClassName("ignored-always-nested", "${operationId}Response")
    is NamingContext.RouteBody -> ClassName("ignored-always-nested", "${operationId}Body")
    is NamingContext.RouteParam -> ClassName("ignored-always-nested", name)
    NamingContext.UnionCase -> when (model) {
        is Model.Collection -> TODO()
        is Model.Reference -> TODO()
        is Model.DiscriminatedObject -> TODO()
        is Model.Enum -> TODO()
        is Model.Object -> TODO()
        is Model.Union -> TODO()
        is Model.ByteArray -> ClassName("ignored-always-nested", "CaseByteArray")
        is Model.Date -> ClassName("ignored-always-nested", "CaseDate")
        is Model.DateTime -> ClassName("ignored-always-nested", "CaseDateTime")
        is Model.FreeFormJson -> ClassName("ignored-always-nested", "CaseFreeFormJson")
        is Model.Primitive.Boolean -> ClassName("ignored-always-nested", "CaseBoolean")
        is Model.Primitive.Double -> ClassName("ignored-always-nested", "CaseDouble")
        is Model.Primitive.Float -> ClassName("ignored-always-nested", "CaseFloat")
        is Model.Primitive.Int -> ClassName("ignored-always-nested", "CaseInt")
        is Model.Primitive.Long -> ClassName("ignored-always-nested", "CaseLong")
        is Model.Primitive.String -> ClassName("ignored-always-nested", "CaseString")
        is Model.Primitive.Unit -> ClassName("ignored-always-nested", "Empty")
        is Model.Uuid -> ClassName("ignored-always-nested", "CaseUuid")
    }

    is NamingContext.Nested -> {
        val inner = inner.className(model)
        val outer = outer.className(model)
        ClassName(outer.packageName, outer.classNames + inner.classNames)
    }

    is NamingContext.Path -> ClassName("io.github.nomisrev.api", part)
    is NamingContext.Reference -> {
        val name = when (context) {
            SchemaContext.Write -> "${name}Request"
            SchemaContext.Read -> "${name}Response"
            SchemaContext.Null -> name
        }
        ClassName("io.github.nomisrev.model", name)
    }
}

