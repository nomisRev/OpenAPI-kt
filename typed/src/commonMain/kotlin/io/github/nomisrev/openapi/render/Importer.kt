package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.transformers.isTopLevel

fun interface Importer {
    fun import(import: Import)
    fun import(vararg names: Import) = names.forEach(::import)
}

context(ctx: Renderer)
fun import(model: Iterable<Model>) = model.forEach { import(it) }

context(ctx: Renderer)
fun import(model: Model) {
    tailrec fun add(model: Model) {
        when (model) {
            is Model.ContextHolder if model.context.isTopLevel() -> ctx.import(model.toTypeName())

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

            // Special case when we need to import subtypes of DiscriminatedObject
            is Model.Reference if model.context.nested.single() is NamingContext.DiscriminatedObjectCase ->
                ctx.import(model.toTypeName())

            is Model.Reference -> throw IllegalStateException("Model.Reference without NamingContext(Reference, []). ${model.context}")
        }
    }
    add(model)
}