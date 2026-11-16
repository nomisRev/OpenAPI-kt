package io.github.nomisrev.openapi.registry

import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.parser.Schema.Type

/**
 * What the keywords next to a schema `$ref` ([io.github.nomisrev.openapi.parser.ReferenceOr.Reference.siblings]) say
 * about that use of the referenced schema. OpenAPI 3.1 evaluates them with the reference, as JSON Schema 2020-12 does;
 * OpenAPI 3.0 says to ignore them, but authors write them to be read, so they apply in both:
 *
 * - [shape]: `required`, `properties`, `items`, `additionalProperties`, `enum` and composites, merged with the
 *   referenced schema as `allOf: [$ref, siblings]`;
 * - [nullable]: `nullable: true` or a `type` listing `"null"` makes that use nullable. Strictly that is an `allOf` of a
 *   non-null schema, which is non-null, but nobody writes it to mean that;
 * - [type]: the other types listed, which can only repeat the referenced schema's type;
 * - annotations (`description`, `deprecated`, `default`, ...) and constraints are not used yet.
 */
internal class ReferenceSiblings(siblings: Schema) {
    val shape: Schema? = Schema(
        required = siblings.required,
        properties = siblings.properties,
        items = siblings.items,
        additionalProperties = siblings.additionalProperties,
        enum = siblings.enum,
        allOf = siblings.allOf,
        oneOf = siblings.oneOf,
        anyOf = siblings.anyOf,
    ).takeUnless { it == Schema() }

    val nullable: Boolean =
        siblings.nullable == true || (siblings.type as? Type.Array)?.types?.contains(Type.Basic.Null) == true

    val type: Set<Type.Basic> = siblings.type.nonNullTypes()

    /** The keywords that [shape] holds, for messages. */
    val shapeKeywords: List<String> = listOfNotNull(
        "required".takeIf { siblings.required.isNotEmpty() },
        "properties".takeIf { siblings.properties != null },
        "items".takeIf { siblings.items != null },
        "additionalProperties".takeIf { siblings.additionalProperties != null },
        "enum".takeIf { siblings.enum != null },
        "allOf".takeIf { siblings.allOf != null },
        "oneOf".takeIf { siblings.oneOf != null },
        "anyOf".takeIf { siblings.anyOf != null },
    )

    /** Whether [type] contradicts the type of [target]: no value has both. A target without a known type has any. */
    fun contradictsTypeOf(target: Schema): Boolean {
        val targetType = target.type.nonNullTypes().ifEmpty {
            when {
                target.properties != null || target.additionalProperties != null -> setOf(Type.Basic.Object)
                target.items != null -> setOf(Type.Basic.Array)
                else -> emptySet()
            }
        }
        return type.isNotEmpty() && targetType.isNotEmpty() && type.none { it in targetType || it.isNumberOf(targetType) }
    }

    private fun Type.Basic.isNumberOf(types: Set<Type.Basic>): Boolean =
        (this == Type.Basic.Number && Type.Basic.Integer in types) || (this == Type.Basic.Integer && Type.Basic.Number in types)
}

private fun Type?.nonNullTypes(): Set<Type.Basic> = when (this) {
    null -> emptySet()
    is Type.Basic -> setOf(this) - Type.Basic.Null
    is Type.Array -> types.toSet() - Type.Basic.Null
}
