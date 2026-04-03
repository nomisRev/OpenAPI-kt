package io.github.nomisrev.openapi.pipeline.plugins.composite

import io.github.nomisrev.openapi.enumLikeValues
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema

internal fun Schema.compositeTakesPrecedence(): Boolean =
    type == null || compositeShouldTakePrecedenceOverType()

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
