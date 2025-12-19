package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.Object.AdditionalProperties.Allowed
import io.github.nomisrev.openapi.Model.Object.AdditionalProperties.Schema
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.NamingContext.Reference

fun interface Importer {
    fun import(name: TypeName)
}

context(ctx: Renderer)
fun import(model: Iterable<Model>) = model.forEach { import(it) }

context(ctx: Renderer)
fun import(model: Model) {
    tailrec fun add(model: Model) {
        when (model) {
            is Model.ContextHolder if model.context.head is Reference && model.context.nested.isEmpty() ->
                ctx.import(model.toTypeName())

            is Model.Object if model.properties.isEmpty() && model.additionalProperties is Allowed && model.additionalProperties.value ->
                ctx.import(TypeName.JsonObject)

            is Model.Object if model.properties.isEmpty() && model.additionalProperties is Schema -> add(model.additionalProperties.value)

            is Model.Collection if model.inner is Model.FreeFormJson -> ctx.import(TypeName.JsonArray)

            is Model.Collection -> add(model.inner)

            is Model.FreeFormJson -> ctx.import(TypeName.JsonElement)
            is Model.Date -> ctx.import(TypeName.Date)
            is Model.DateTime -> ctx.import(TypeName.DateTime)
            is Model.Uuid -> ctx.import(TypeName.Uuid)

            is Model.ByteArray,
            is Model.DiscriminatedObject,
            is Model.Union,
            is Model.Object,
            is Model.Enum,
            is Model.Primitive -> Unit

            is Model.Reference if model.context.nested.single() is NamingContext.DiscriminatedObjectCase -> ctx.import(model.toTypeName())
            is Model.Reference -> throw IllegalStateException("Model.Reference without NamingContext(Reference, []). ${model.context}")
        }
    }
    add(model)
}

class Renderer(
    val maxLineLength: Int,
    val indentSize: Int,
    val jvm: Boolean,
    val js: Boolean,
    val packageName: String = "io.github.nomisrev",
    import: Importer
) : Importer by import {
    val indent: String get() = " ".repeat(indentSize)
}

fun <A> renderer(block: context(Renderer) () -> A): Pair<A, Set<TypeName>> {
    val buffer = mutableSetOf<TypeName>()
    val ctx = Renderer(120, 4, jvm = true, js = true) { typeName ->
        fun add(typeName: TypeName) {
            when (typeName) {
                TypeName.String,
                TypeName.Int,
                TypeName.Long,
                TypeName.Float,
                TypeName.Double,
                TypeName.ByteArray,
                TypeName.Unit -> Unit

                is TypeName.Collection -> add(typeName.type)
                is TypeName.Class -> buffer.add(typeName)
            }
        }
        add(typeName)
    }
    return Pair(block(ctx), buffer)
}
