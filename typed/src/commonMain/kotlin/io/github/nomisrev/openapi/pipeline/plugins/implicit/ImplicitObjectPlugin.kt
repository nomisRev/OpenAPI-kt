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
import io.github.nomisrev.openapi.transformers.objectWithoutProperties
import io.github.nomisrev.openapi.transformers.toObject

object ImplicitObjectPlugin : SchemaTransformerPlugin {
    override val key: PluginKey = PluginKeys.ImplicitObject
    override val phase: Phase = Phase.IMPLICIT

    override suspend fun transform(
        ctx: Registry.Scope,
        engine: SchemaTransformerEngine,
        schema: ResolvedSchema,
        context: SchemaContext,
        next: suspend (ResolvedSchema, SchemaContext) -> Model
    ): Model? = with(ctx) {
        val s = schema.schema
        if (s.type != null) return null
        if (s.enumLikeValues() != null) return null

        val properties = s.properties
        return if (properties?.isNotEmpty() == true) {
            schema.toObject(context, properties)
        } else if (s.additionalProperties != null) {
            schema.objectWithoutProperties(context)
        } else null
    }
}
