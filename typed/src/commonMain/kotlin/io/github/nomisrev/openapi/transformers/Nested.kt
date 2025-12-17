package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext

internal tailrec fun Model.nestedOrNull(): Model? = when (this) {
    is Model.Collection -> inner.nestedOrNull()
    is Model.ByteArray,
    is Model.Date,
    is Model.FreeFormJson,
    is Model.DateTime,
    is Model.Reference,
    is Model.Uuid,
    is Model.Primitive -> null

    is Model.DiscriminatedObject if context.isTopLevel() -> null
    is Model.Enum if context.isTopLevel() -> null
    is Model.Object if context.isTopLevel() -> null
    is Model.Union if context.isTopLevel() -> null
    is Model.DiscriminatedObject,
    is Model.Enum,
    is Model.Object,
    is Model.Union -> this
}

fun NamingContext.isTopLevel(): Boolean = head is NamingContext.Reference && nested.isEmpty()