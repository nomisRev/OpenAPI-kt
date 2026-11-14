package io.github.nomisrev.openapi.pipeline.plugins.composite

import io.github.nomisrev.openapi.enumLikeValues
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema

internal fun Schema.compositeTakesPrecedence(): Boolean =
    !hasOnlyConstraintBranches() && (type == null || compositeShouldTakePrecedenceOverType())

/**
 * An object that declares its own `properties` next to a `oneOf`/`anyOf` whose branches only constrain it:
 * `anyOf: [{required: [a]}, {required: [b]}, {type: object, maxProperties: 0}]` is the JSON Schema idiom for "at least
 * one of these properties". It describes no alternative shapes, the object is the type, so the composite is no union.
 */
internal fun Schema.hasOnlyConstraintBranches(): Boolean {
    if (type != null && type != Schema.Type.Basic.Object) return false
    if (properties.isNullOrEmpty() || !allOf.isNullOrEmpty() || enumLikeValues() != null) return false
    val branches = listOfNotNull(oneOf, anyOf).flatten()
    return branches.isNotEmpty() &&
            branches.all { it is ReferenceOr.Value<Schema> && it.value.isConstraintOnlyBranch() } &&
            branches.any { it is ReferenceOr.Value<Schema> && it.value.required.isNotEmpty() }
}

/** A branch with nothing but `required` and/or `minProperties`/`maxProperties`, no type of its own (or `object`). */
private fun Schema.isConstraintOnlyBranch(): Boolean =
    (type == null || type == Schema.Type.Basic.Object) &&
            (required.isNotEmpty() || maxProperties != null || minProperties != null) &&
            properties.isNullOrEmpty() &&
            additionalProperties == null &&
            items == null &&
            enumLikeValues() == null &&
            format == null &&
            allOf.isNullOrEmpty() &&
            oneOf.isNullOrEmpty() &&
            anyOf.isNullOrEmpty() &&
            not == null &&
            nullable != true

internal fun Schema.compositeShouldTakePrecedenceOverType(): Boolean =
    listOfNotNull(allOf, oneOf, anyOf)
        .flatten()
        .any(ReferenceOr<Schema>::addsStructuralCompositeShape)

private fun ReferenceOr<Schema>.addsStructuralCompositeShape(): Boolean =
    when (this) {
        is ReferenceOr.Reference -> true
        is ReferenceOr.Value<Schema> -> value.addsStructuralCompositeShape()
    }

private fun Schema.addsStructuralCompositeShape(): Boolean =
    type != null ||
            properties?.isNotEmpty() == true ||
            additionalProperties != null ||
            items != null ||
            enumLikeValues() != null ||
            allOf.orEmpty().isNotEmpty() ||
            oneOf.orEmpty().isNotEmpty() ||
            anyOf.orEmpty().isNotEmpty() ||
            format != null
