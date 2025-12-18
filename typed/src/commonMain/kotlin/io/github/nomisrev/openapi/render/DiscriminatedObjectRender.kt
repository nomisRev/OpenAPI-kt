package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext

context(ctx: Renderer)
fun Model.DiscriminatedObject.render(): String = buildString {
    +"@JsonClassDiscriminator(${discriminator.stringValue()})"
    +"@Serializable"
    +"sealed class ${name().simpleName} {"
    abstractProperties.joinTo(separator = "\n", postfix = "\n\n") {
        "abstract ${it.render()}".prependIndent(ctx.indent)
    }

    subtypes.joinTo(separator = "\n\n", postfix = "\n") {
        val serialName = (it.context.nested.single() as NamingContext.DiscriminatedObjectCase).discriminator
        "@SerialName(${serialName.stringValue()})\n${it.render(name())}".prependIndent(ctx.indent)
    }

    append("}")
}
