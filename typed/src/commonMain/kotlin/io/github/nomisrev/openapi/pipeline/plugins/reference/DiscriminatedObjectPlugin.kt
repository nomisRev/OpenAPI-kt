package io.github.nomisrev.openapi.pipeline.plugins.reference

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.pipeline.Phase
import io.github.nomisrev.openapi.pipeline.PluginKey
import io.github.nomisrev.openapi.pipeline.PluginKeys
import io.github.nomisrev.openapi.pipeline.SchemaTransformerEngine
import io.github.nomisrev.openapi.pipeline.SchemaTransformerPlugin
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.isObjectWithDiscriminator
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.transformers.toDiscriminatedObject

object DiscriminatedObjectPlugin : SchemaTransformerPlugin {
    override val key: PluginKey = PluginKeys.DiscriminatedObject
    override val phase: Phase = Phase.REFERENCE

    override suspend fun transform(
        ctx: Registry.Scope,
        engine: SchemaTransformerEngine,
        schema: ResolvedSchema,
        context: SchemaContext,
        next: suspend (ResolvedSchema, SchemaContext) -> Model
    ): Model? = with(ctx) {
        if (schema is ResolvedSchema.Reference && schema.isObjectWithDiscriminator()) {
            schema.toDiscriminatedObject(context)
        } else null
    }
}
