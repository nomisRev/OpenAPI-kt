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

/**
 * An OpenAPI 3.1 type array with a single non-null type, `type: [X, "null"]` (in any order) or `type: [X]`,
 * means exactly `type: X` (with `nullable: true` when `"null"` is listed), like OpenAPI 3.0's `nullable`
 * and `anyOf: [X, {type: null}]`. Rewriting it to that form lets every plugin model it as `X?`, keeping
 * `format`, `enum`, `properties` and `items`, instead of a one-case union of a bare `Schema(type = X)`.
 * Type arrays with several non-null types are left to [TypeArrayPlugin].
 */
internal fun ResolvedSchema.flattenSingleTypeArray(): ResolvedSchema {
    val type = schema.type as? Schema.Type.Array ?: return this
    val nonNull = type.types.filterNot { it == Schema.Type.Basic.Null }
    val single = nonNull.distinct().singleOrNull() ?: return this
    val hasNull = nonNull.size != type.types.size
    val flattened = schema.copy(type = single, nullable = if (hasNull) true else schema.nullable)
    return when (this) {
        is ResolvedSchema.Recursive -> copy(schema = flattened)
        is ResolvedSchema.Reference -> copy(schema = flattened)
        is ResolvedSchema.Value -> copy(schema = flattened)
    }
}

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
