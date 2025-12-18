package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model

context(ctx: Renderer)
fun Model.DiscriminatedObject.render(): String = buildString {
    +"@JsonClassDiscriminator(${discriminator.stringValue()})"
    +"@Serializable"
    +"sealed class ${name().simpleName} {"
    abstractProperties.joinTo(separator = "\n", postfix = "\n") {
        "abstract ${it.render()}".prependIndent(ctx.indent)
    }
    append("}")
}
