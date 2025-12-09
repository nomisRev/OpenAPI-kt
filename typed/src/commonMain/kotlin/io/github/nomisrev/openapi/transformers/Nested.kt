package io.github.nomisrev.openapi.transformers

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext

internal tailrec fun Model.nestedOrNull(): Model? = when (this) {
    is Model.Collection.List -> inner.nestedOrNull()
    is Model.Collection.Map -> inner.nestedOrNull()
    is Model.ByteArray,
    is Model.Date,
    is Model.FreeFormJson,
    is Model.DateTime,
    is Model.Reference,
    is Model.Uuid,
    is Model.Primitive -> null

    is Model.DiscriminatedObject if context is NamingContext.Reference -> null
    is Model.Enum if context is NamingContext.Reference -> null
    is Model.Object if context is NamingContext.Reference -> null
    is Model.Union if context is NamingContext.Reference -> null
    is Model.DiscriminatedObject,
    is Model.Enum,
    is Model.Object,
    is Model.Union -> this
}