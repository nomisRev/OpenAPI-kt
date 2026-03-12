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

context(ctx: Renderer, builder: StringBuilder)
fun serialName(name: String) = with(ctx) {
    ctx.import(TypeName.SerialName)
    +"@SerialName(${name.stringValue()})"
}

context(ctx: Renderer, builder: StringBuilder)
fun jsonClassDiscriminator(name: String) {
    ctx.import(TypeName.JsonClassDiscriminator, TypeName.ExperimentalSerializationApi)
    +"@OptIn(ExperimentalSerializationApi::class)"
    +"@JsonClassDiscriminator(${name.stringValue()})"
}

context(ctx: Renderer, builder: StringBuilder)
fun experimentalUuidApi() {
    ctx.import(TypeName.ExperimentalUuidApi)
    +"@ExperimentalUuidApi"
}
