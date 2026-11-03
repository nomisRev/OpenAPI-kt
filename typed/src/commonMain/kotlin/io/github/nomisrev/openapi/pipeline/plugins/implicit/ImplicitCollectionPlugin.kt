package io.github.nomisrev.openapi.pipeline.plugins.implicit

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.enumLikeValues
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.pipeline.Phase
import io.github.nomisrev.openapi.pipeline.PluginKey
import io.github.nomisrev.openapi.pipeline.PluginKeys
import io.github.nomisrev.openapi.pipeline.SchemaTransformerEngine
import io.github.nomisrev.openapi.pipeline.SchemaTransformerPlugin
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.transformers.collection

/** A schema without `type` that is modelled as an array: it has `items`, and no object or enum keywords. */
internal fun Schema.isImplicitCollection(): Boolean =
    type == null &&
        items != null &&
        properties.isNullOrEmpty() &&
        additionalProperties == null &&
        enumLikeValues() == null

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
        if (schema.schema.isImplicitCollection()) {
            schema.collection(context)
        } else null
    }
}
