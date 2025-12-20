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
    is Model.Collection if inner is Model.ContextHolder && inner.context.isTopLevel() ->
        Model.Object.value(
            inner.context.head as NamingContext.Reference,
            this@render,
            description
        ).render()

    is Model.Reference if context.nested.single() is NamingContext.DiscriminatedObjectCase -> TODO()
    is Model.Date,
    is Model.DateTime,
    is Model.FreeFormJson,
    is Model.ByteArray,
    is Model.Reference,
    is Model.Uuid,
    is Model.Collection,
    is Model.Primitive -> throw IllegalStateException("Cannot render $this")
}
