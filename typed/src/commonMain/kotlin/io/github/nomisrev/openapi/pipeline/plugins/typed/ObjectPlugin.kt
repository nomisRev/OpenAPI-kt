package io.github.nomisrev.openapi.pipeline.plugins.typed

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.parser.Schema
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

object ObjectPlugin : SchemaTransformerPlugin {
    override val key: PluginKey = PluginKeys.Object
    override val phase: Phase = Phase.TYPED

    override suspend fun transform(
        ctx: Registry.Scope,
        engine: SchemaTransformerEngine,
        schema: ResolvedSchema,
        context: SchemaContext,
        next: suspend (ResolvedSchema, SchemaContext) -> Model
    ): Model? = with(ctx) {
        val s = schema.schema
        if (s.type != Schema.Type.Basic.Object) return null

        val properties = s.properties
        return if (properties?.isNotEmpty() == true) {
            schema.toObject(context, properties)
        } else {
            schema.objectWithoutProperties(context)
        }
    }
}
