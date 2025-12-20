package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext

context(ctx: Renderer)
fun Model.DiscriminatedObject.render(): String = buildString {
    ctx.import(TypeName.SerialName)
    ctx.import(TypeName.JsonClassDiscriminator)

    +"@JsonClassDiscriminator(${discriminator.stringValue()})"
    serializable()
    +"sealed interface ${name().simpleName} {"
    abstractProperties.joinTo(separator = "\n", postfix = "\n\n") {
        it.render(emptySet(), false).prependIndent(ctx.indent)
    }

    subtypes.joinTo(separator = "\n\n", postfix = "\n") {
        val serialName = (it.context.nested.single() as NamingContext.DiscriminatedObjectCase).discriminator
        "@SerialName(${serialName.stringValue()})\n${it.render(name(), abstractProperties.keys)}".prependIndent(ctx.indent)
    }

    append("}")
}
