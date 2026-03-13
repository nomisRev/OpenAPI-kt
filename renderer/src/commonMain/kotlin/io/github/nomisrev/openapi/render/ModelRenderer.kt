package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.transformers.isTopLevel

context(ctx: Renderer)
fun Model.render(): String = when (this) {
    is Model.Enum -> render()
    is Model.Object -> render()
    is Model.Union -> render()
    is Model.DiscriminatedObject -> render()

    // TODO: Move to transformers
    is Model.Collection -> {
        val innerModel = inner
        if (innerModel is Model.ContextHolder && innerModel.context.isTopLevel()) {
            Model.Object.value(
                innerModel.context.head as NamingContext.Reference,
                this@render,
                description
            ).render()
        } else {
            throw IllegalStateException("Cannot render $this")
        }
    }

    is Model.Reference if context.nested.single() is NamingContext.DiscriminatedObjectCase -> TODO()
    is Model.Date,
    is Model.DateTime,
    is Model.FreeFormJson,
    is Model.ByteArray,
    is Model.Reference,
    is Model.Uuid,
    is Model.Primitive -> throw IllegalStateException("Cannot render $this")
}
