package io.github.nomisrev.openapi.pipeline.plugins.composite

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.pipeline.Phase
import io.github.nomisrev.openapi.pipeline.PluginKey
import io.github.nomisrev.openapi.pipeline.PluginKeys
import io.github.nomisrev.openapi.pipeline.SchemaTransformerEngine
import io.github.nomisrev.openapi.pipeline.SchemaTransformerPlugin
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.SchemaContext

object AnyOfSinglePlugin : SchemaTransformerPlugin {
    override val key: PluginKey = PluginKeys.AnyOfSingle
    override val phase: Phase = Phase.COMPOSITE

    override suspend fun transform(
        ctx: Registry.Scope,
        engine: SchemaTransformerEngine,
        schema: ResolvedSchema,
        context: SchemaContext,
        next: suspend (ResolvedSchema, SchemaContext) -> Model
    ): Model? = with(ctx) {
        if (!schema.schema.compositeTakesPrecedence()) return null
        val anyOf = schema.schema.anyOf ?: return null
        if (anyOf.size != 1) return null

        anyOf.single().toModel(schema.name, context)
    }
}
