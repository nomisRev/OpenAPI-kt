package io.github.nomisrev.openapi.pipeline

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.routes.SchemaContext
import kotlin.jvm.JvmInline

sealed interface TransformResult {
    data class Handled(val model: Model) : TransformResult
    data object Pass : TransformResult
}

enum class Phase {
    REFERENCE, COMPOSITE, TYPED, IMPLICIT, FALLBACK,
}

@JvmInline
value class PluginKey(val name: String)

interface SchemaTransformerPlugin {
    val key: PluginKey
    val phase: Phase

    suspend fun transform(
        ctx: Registry.Scope,
        engine: SchemaTransformerEngine,
        schema: ResolvedSchema,
        context: SchemaContext,
        next: suspend (ResolvedSchema, SchemaContext) -> Model
    ): Model?
}
