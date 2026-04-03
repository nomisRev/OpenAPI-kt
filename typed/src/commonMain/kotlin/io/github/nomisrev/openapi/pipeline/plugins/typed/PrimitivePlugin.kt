package io.github.nomisrev.openapi.pipeline.plugins.typed

import io.github.nomisrev.openapi.Model
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
import io.github.nomisrev.openapi.transformers.primitive
import io.github.nomisrev.openapi.enumLikeValues

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

object PrimitivePlugin : SchemaTransformerPlugin {
    override val key: PluginKey = PluginKeys.Primitive
    override val phase: Phase = Phase.TYPED

    override suspend fun transform(
        ctx: Registry.Scope,
        engine: SchemaTransformerEngine,
        schema: ResolvedSchema,
        context: SchemaContext,
        next: suspend (ResolvedSchema, SchemaContext) -> Model
    ): Model? = with(ctx) {
        val s = schema.schema
        val compositeTakesPrecedence = s.type == null || s.compositeShouldTakePrecedenceOverType()
        if (compositeTakesPrecedence) return null

        val isPrimitiveType = s.type == Schema.Type.Basic.String ||
                s.type == Schema.Type.Basic.Integer ||
                s.type == Schema.Type.Basic.Number ||
                s.type == Schema.Type.Basic.Boolean

        return if (isPrimitiveType && s.enumLikeValues() == null) {
            schema.primitive()
        } else null
    }
}
