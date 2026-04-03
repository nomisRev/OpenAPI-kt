package io.github.nomisrev.openapi.pipeline.plugins.composite

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.pipeline.Phase
import io.github.nomisrev.openapi.pipeline.PluginKey
import io.github.nomisrev.openapi.pipeline.PluginKeys
import io.github.nomisrev.openapi.pipeline.SchemaTransformerEngine
import io.github.nomisrev.openapi.pipeline.SchemaTransformerPlugin
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.isAnyOfNullableType
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.transformers.buildAnyOf

object AnyOfNullableFlattenPlugin : SchemaTransformerPlugin {
    override val key: PluginKey = PluginKeys.AnyOfNullable
    override val phase: Phase = Phase.COMPOSITE

    override suspend fun transform(
        ctx: Registry.Scope,
        engine: SchemaTransformerEngine,
        schema: ResolvedSchema,
        context: SchemaContext,
        next: suspend (ResolvedSchema, SchemaContext) -> Model
    ): Model? = with(ctx) {
        if (!schema.schema.compositeTakesPrecedence()) return null
        if (!schema.isAnyOfNullableType()) return null

        schema.flattenNull(schema.schema.anyOf!!) { nonNullSchemas ->
            if (nonNullSchemas.size == 1) {
                schema.flattenedSingleNullableBranch(nonNullSchemas.single(), context)
            } else {
                schema.buildAnyOf(context, nonNullSchemas)
            }
        }
    }
}
