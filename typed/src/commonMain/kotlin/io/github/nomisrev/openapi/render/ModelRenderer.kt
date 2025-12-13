package io.github.nomisrev.openapi.render

import io.github.nomisrev.openapi.Model

context(ctx: Renderer)
fun Model.render(): String = when (this) {
    is Model.Date,
    is Model.DateTime,
    is Model.FreeFormJson,
    is Model.ByteArray,
    is Model.Collection,
    is Model.Reference,
    is Model.Uuid,
    is Model.Primitive -> throw IllegalStateException("Cannot render $this")

    is Model.Enum -> render()
    is Model.Object -> render()
    is Model.Union -> render()
    is Model.DiscriminatedObject -> TODO()
}
