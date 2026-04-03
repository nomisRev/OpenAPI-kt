package io.github.nomisrev.openapi.pipeline.plugins.implicit

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.enumLikeValues
import io.github.nomisrev.openapi.pipeline.Phase
import io.github.nomisrev.openapi.pipeline.PluginKey
import io.github.nomisrev.openapi.pipeline.PluginKeys
import io.github.nomisrev.openapi.pipeline.SchemaTransformerEngine
import io.github.nomisrev.openapi.pipeline.SchemaTransformerPlugin
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.transformers.collection

object ImplicitCollectionPlugin : SchemaTransformerPlugin {
    override val key: PluginKey = PluginKeys.ImplicitCollection
    override val phase: Phase = Phase.IMPLICIT

    override suspend fun transform(
        ctx: Registry.Scope,
        engine: SchemaTransformerEngine,
        schema: ResolvedSchema,
        context: SchemaContext,
        next: suspend (ResolvedSchema, SchemaContext) -> Model
    ): Model? = with(ctx) {
        val s = schema.schema
        val isImplicitCollection = s.type == null &&
            s.items != null &&
            s.properties.isNullOrEmpty() &&
            s.additionalProperties == null &&
            s.enumLikeValues() == null

        if (isImplicitCollection) {
            schema.collection(context)
        } else null
    }
}
