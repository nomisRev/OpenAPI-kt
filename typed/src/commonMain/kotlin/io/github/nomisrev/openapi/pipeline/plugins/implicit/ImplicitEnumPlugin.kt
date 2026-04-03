package io.github.nomisrev.openapi.pipeline.plugins.implicit

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.enumLikeValues
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.pipeline.Phase
import io.github.nomisrev.openapi.pipeline.PluginKey
import io.github.nomisrev.openapi.pipeline.PluginKeys
import io.github.nomisrev.openapi.pipeline.SchemaTransformerEngine
import io.github.nomisrev.openapi.pipeline.SchemaTransformerPlugin
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.transformers.toClosedEnum

private fun Schema.compositeShouldTakePrecedenceOverType(): Boolean =
    listOfNotNull(allOf, oneOf, anyOf)
        .flatten()
        .any { it.addsStructuralCompositeShape() }

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

private fun Schema.isImplicitComposite(): Boolean =
    listOfNotNull(allOf, oneOf, anyOf)
        .flatten()
        .isNotEmpty()

object ImplicitEnumPlugin : SchemaTransformerPlugin {
    override val key: PluginKey = PluginKeys.ImplicitEnum
    override val phase: Phase = Phase.IMPLICIT

    override suspend fun transform(
        ctx: Registry.Scope,
        engine: SchemaTransformerEngine,
        schema: ResolvedSchema,
        context: SchemaContext,
        next: suspend (ResolvedSchema, SchemaContext) -> Model
    ): Model? = with(ctx) {
        val s = schema.schema
        if (s.type != null || s.isImplicitComposite()) return null

        val enumLikeValues = s.enumLikeValues()
        if (enumLikeValues == null) return null

        return schema.toClosedEnum(context, enumLikeValues)
    }
}
