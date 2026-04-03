package io.github.nomisrev.openapi.pipeline.plugins.fallback

import io.github.nomisrev.openapi.Constraints
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.Model.FreeFormJson
import io.github.nomisrev.openapi.Model.Object
import io.github.nomisrev.openapi.pipeline.Phase
import io.github.nomisrev.openapi.pipeline.PluginKey
import io.github.nomisrev.openapi.pipeline.PluginKeys
import io.github.nomisrev.openapi.pipeline.SchemaTransformerEngine
import io.github.nomisrev.openapi.pipeline.SchemaTransformerPlugin
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.routes.SchemaContext

object FallbackPlugin : SchemaTransformerPlugin {
    override val key: PluginKey = PluginKeys.Fallback
    override val phase: Phase = Phase.FALLBACK

    override suspend fun transform(
        ctx: Registry.Scope,
        engine: SchemaTransformerEngine,
        schema: ResolvedSchema,
        context: SchemaContext,
        next: suspend (ResolvedSchema, SchemaContext) -> Model
    ): Model? = with(ctx) {
        when (schema) {
            is ResolvedSchema.Value ->
                FreeFormJson(
                    schema.description(),
                    Constraints.Object(schema.schema),
                    schema.isNullable,
                    schema.schema.title
                )

            is ResolvedSchema.Recursive ->
                Model.Reference(schema.name, schema.description(), schema.isNullable, schema.schema.title)

            is ResolvedSchema.Reference -> {
                val description = schema.description()
                Object.value(
                    schema.reference,
                    FreeFormJson(
                        description,
                        Constraints.Object(schema.schema),
                        schema.isNullable,
                        schema.schema.title
                    ),
                    title = schema.schema.title,
                    isScalarWrapper = true
                ).with(description = description)
            }
        }
    }
}
