package io.github.nomisrev.openapi

internal fun Model.needsJsonObjectImport(): Boolean = when (this) {
    is Model.AnyOf -> needsJsonObjectImportForAnyOf()
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

private fun Model.AnyOf.needsJsonObjectImportForAnyOf(): Boolean {
    data class DispatchCase(
        val case: Model.Union.Case,
        val model: Model.Object,
        val uniqueKeys: Set<String>,
    )

    val objectCases = cases.mapNotNull { case ->
        (case.model as? Model.Object)?.let { model ->
            DispatchCase(case, model, emptySet())
        }
    }

    if (objectCases.size != cases.size) return false

    val uniqueKeysByCase = objectCases.map { dispatchCase ->
        val keys = dispatchCase.model.properties.keys
        val otherKeys = objectCases
            .asSequence()
            .filterNot { it.case == dispatchCase.case }
            .flatMapTo(mutableSetOf()) { it.model.properties.keys }
        dispatchCase.copy(uniqueKeys = keys - otherKeys)
    }

    return uniqueKeysByCase.count { it.uniqueKeys.isEmpty() } <= 1
}
