package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model

context(ctx: Renderer, builder: StringBuilder)
fun serializable() {
    ctx.import(TypeName.Serializable)
    +"@Serializable"
}

context(ctx: Renderer, builder: StringBuilder)
fun serializable(model: Model.ContextHolder) {
    ctx.import(TypeName.Serializable)
    +"@Serializable(with = ${model.name().simpleName}.Serializer::class)"
}

context(ctx: Renderer, builder: StringBuilder)
fun jvmInline() {
    ctx.import(TypeName.JvmInline)
    +"@JvmInline"
}