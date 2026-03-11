package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext

context(ctx: Renderer)
fun Model.DiscriminatedObject.render(): String = buildString {
    ctx.import(TypeName.SerialName)

    jsonClassDiscriminator(discriminator)
    serializable()
    +"sealed interface ${name().simpleName} {"

    abstractProperties.joinTo(separator = "\n", postfix = "\n\n") {
        it.render(emptySet(), false).prepend()
    }

    subtypes.joinTo(separator = "\n\n", postfix = "\n") {
        val serialName = it.context.nested
            .asReversed()
            .firstNotNullOf { nested -> (nested as? NamingContext.DiscriminatedObjectCase)?.discriminator }
        "@SerialName(${serialName.stringValue()})\n${it.render(name(), abstractProperties.keys)}".prepend()
    }

    append("}")
}
