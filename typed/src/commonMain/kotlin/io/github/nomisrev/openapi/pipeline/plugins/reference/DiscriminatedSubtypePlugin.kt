package io.github.nomisrev.openapi.pipeline.plugins.reference

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.pipeline.Phase
import io.github.nomisrev.openapi.pipeline.PluginKey
import io.github.nomisrev.openapi.pipeline.PluginKeys
import io.github.nomisrev.openapi.pipeline.SchemaTransformerEngine
import io.github.nomisrev.openapi.pipeline.SchemaTransformerPlugin
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.transformers.discriminatedSubtypeOrNull

object DiscriminatedSubtypePlugin : SchemaTransformerPlugin {
    override val key: PluginKey = PluginKeys.DiscriminatedSubtype
    override val phase: Phase = Phase.REFERENCE

    override suspend fun transform(
        ctx: Registry.Scope,
        engine: SchemaTransformerEngine,
        schema: ResolvedSchema,
        context: SchemaContext,
        next: suspend (ResolvedSchema, SchemaContext) -> Model
    ): Model? = with(ctx) {
        if (schema is ResolvedSchema.Reference) {
            val namingContext = schema.schema.discriminatedSubtypeOrNull(context, schema.reference.name)
            if (namingContext != null) {
                Model.Reference(
                    namingContext,
                    schema.description(),
                    schema.isNullable,
                    schema.schema.title
                )
            } else null
        } else null
    }
}
