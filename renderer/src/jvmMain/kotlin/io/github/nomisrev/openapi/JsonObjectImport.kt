package io.github.nomisrev.openapi

internal fun Model.needsJsonObjectImport(): Boolean = when (this) {
    is Model.AnyOf -> uniqueKeyDispatchAnalysis() != null
    is Model.Union -> cases.any { it.model.needsJsonObjectImport() }
    is Model.Object -> properties.values.any { it.model.needsJsonObjectImport() } ||
        ((additionalProperties as? Model.Object.AdditionalProperties.Schema)?.value?.needsJsonObjectImport() == true) ||
        inline.any { it.needsJsonObjectImport() }
    is Model.DiscriminatedObject ->
        abstractProperties.values.any { it.model.needsJsonObjectImport() } ||
            subtypes.any { subtype ->
                subtype.properties.values.any { it.model.needsJsonObjectImport() } ||
                    ((subtype.additionalProperties as? Model.Object.AdditionalProperties.Schema)?.value?.needsJsonObjectImport() == true) ||
                    subtype.inline.any { it.needsJsonObjectImport() }
            }

    is Model.Enum -> inner.needsJsonObjectImport()
    is Model.Collection -> inner.needsJsonObjectImport()

    is Model.ByteArray,
    is Model.Date,
    is Model.DateTime,
    is Model.FreeFormJson,
    is Model.Primitive,
    is Model.Reference,
    is Model.Uuid -> false
}
