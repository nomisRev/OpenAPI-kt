package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.transformers.nestedOrNull

internal fun Model.needsJsonObjectImport(): Boolean = when (this) {
    is Model.AnyOf -> (dispatch == UnionDispatch.Structural && uniqueKeyDispatchAnalysis() != null) ||
        inline.any { it.needsJsonObjectImport() }
    is Model.OneOf -> inline.any { it.needsJsonObjectImport() }
    is Model.Object -> inline.any { it.needsJsonObjectImport() }
    is Model.DiscriminatedObject -> subtypes.any { subtype -> subtype.inline.any { it.needsJsonObjectImport() } }
    is Model.Enum -> inner.nestedOrNull()?.needsJsonObjectImport() == true
    is Model.Collection -> inner.nestedOrNull()?.needsJsonObjectImport() == true

    is Model.ByteArray,
    is Model.Date,
    is Model.DateTime,
    is Model.FreeFormJson,
    is Model.Primitive,
    is Model.Reference,
    is Model.Uuid -> false
}
