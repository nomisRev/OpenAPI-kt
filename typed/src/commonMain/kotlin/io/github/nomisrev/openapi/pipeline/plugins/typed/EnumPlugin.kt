package io.github.nomisrev.openapi.pipeline.plugins.typed

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
        .any { it.addsStructuralCompositeShapeForEnum() }

private fun ReferenceOr<Schema>.addsStructuralCompositeShapeForEnum(): Boolean =
    when (this) {
        is ReferenceOr.Reference -> true
        is ReferenceOr.Value<Schema> -> value.addsStructuralCompositeShapeForEnum()
    }

private fun Schema.addsStructuralCompositeShapeForEnum(): Boolean =
    properties?.isNotEmpty() == true ||
            additionalProperties != null ||
            items != null ||
            allOf.orEmpty().isNotEmpty() ||
            oneOf.orEmpty().isNotEmpty() ||
            anyOf.orEmpty().isNotEmpty()

object EnumPlugin : SchemaTransformerPlugin {
    override val key: PluginKey = PluginKeys.Enum
    override val phase: Phase = Phase.TYPED

    override suspend fun transform(
        ctx: Registry.Scope,
        engine: SchemaTransformerEngine,
        schema: ResolvedSchema,
        context: SchemaContext,
        next: suspend (ResolvedSchema, SchemaContext) -> Model
    ): Model? = with(ctx) {
        val s = schema.schema
        if (s.type == null || s.addsStructuralCompositeShapeForEnum()) return null

        val enumLikeValues = s.enumLikeValues()
        if (enumLikeValues == null) return null

        val isTypedEnum = s.type == Schema.Type.Basic.String ||
                s.type == Schema.Type.Basic.Number ||
                s.type == Schema.Type.Basic.Boolean ||
                s.type == Schema.Type.Basic.Integer

        if (!isTypedEnum) return null

        return schema.toClosedEnum(context, enumLikeValues)
    }
}
