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
import io.github.nomisrev.openapi.transformers.union

object TypeArrayPlugin : SchemaTransformerPlugin {
    override val key: PluginKey = PluginKeys.TypeArray
    override val phase: Phase = Phase.TYPED

    override suspend fun transform(
        ctx: Registry.Scope,
        engine: SchemaTransformerEngine,
        schema: ResolvedSchema,
        context: SchemaContext,
        next: suspend (ResolvedSchema, SchemaContext) -> Model
    ): Model? = with(ctx) {
        val s = schema.schema
        val type = s.type
        if (type !is Schema.Type.Array) return null

        val hasNull = type.types.contains(Schema.Type.Basic.Null)
        val resolved = if (hasNull && s.nullable != true) {
            when (schema) {
                is ResolvedSchema.Recursive -> schema.copy(schema = s.copy(nullable = true))
                is ResolvedSchema.Reference -> schema.copy(schema = s.copy(nullable = true))
                is ResolvedSchema.Value -> schema.copy(schema = s.copy(nullable = true))
            }
        } else schema

        val subtypes = (type.types - Schema.Type.Basic.Null).map {
            ReferenceOr.value(Schema(type = it))
        }

        return resolved.union(context, subtypes)
    }
}
