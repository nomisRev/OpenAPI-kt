package io.github.nomisrev.openapi.pipeline.plugins.boolean

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.pipeline.Phase
import io.github.nomisrev.openapi.pipeline.PluginKey
import io.github.nomisrev.openapi.pipeline.PluginKeys
import io.github.nomisrev.openapi.pipeline.SchemaTransformerEngine
import io.github.nomisrev.openapi.pipeline.SchemaTransformerPlugin
import io.github.nomisrev.openapi.pipeline.plugins.fallback.FallbackPlugin
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.booleanSchema
import io.github.nomisrev.openapi.routes.SchemaContext

/**
 * JSON Schema boolean schemas (OpenAPI 3.1): `true` accepts any value, `false` accepts none.
 *
 * - `true` is the empty schema `{}`, so it becomes free-form JSON exactly like `{}` does.
 * - `false` reaching the engine is a value position no value can satisfy (a component, `items`,
 *   a body, a parameter, ...). There is no Kotlin type for it, so it is rejected.
 * - A `oneOf`/`anyOf` branch `false` can never match and is dropped; if every branch is `false`
 *   the schema is unsatisfiable and rejected.
 * - An `allOf` member `false` makes the whole schema unsatisfiable and is rejected.
 *
 * Object properties and `additionalProperties` with a boolean schema are handled where objects
 * are built, see `transformers/Object.kt`.
 */
object BooleanSchemaPlugin : SchemaTransformerPlugin {
    override val key: PluginKey = PluginKeys.BooleanSchema
    override val phase: Phase = Phase.REFERENCE

    override suspend fun transform(
        ctx: Registry.Scope,
        engine: SchemaTransformerEngine,
        schema: ResolvedSchema,
        context: SchemaContext,
        next: suspend (ResolvedSchema, SchemaContext) -> Model
    ): Model? = with(ctx) {
        when (schema.schema.booleanSchema) {
            true -> return FallbackPlugin.transform(ctx, engine, schema, context, next)
            false -> throw IllegalArgumentException(
                "Schema ${schema.describe()} is the boolean schema `false`, which accepts no value " +
                        "and has no Kotlin representation."
            )

            null -> Unit
        }

        val s = schema.schema
        if (s.allOf.orEmpty().any { it.booleanSchema() == false }) {
            throw IllegalArgumentException(
                "allOf of schema ${schema.describe()} contains the boolean schema `false`, so no value can satisfy it."
            )
        }
        val oneOf = s.oneOf?.withoutFalseBranches("oneOf", schema)
        val anyOf = s.anyOf?.withoutFalseBranches("anyOf", schema)
        if (oneOf == s.oneOf && anyOf == s.anyOf) return null
        next(schema.withSchema(s.copy(oneOf = oneOf, anyOf = anyOf)), context)
    }

    context(ctx: Registry.Scope)
    private suspend fun List<ReferenceOr<Schema>>.withoutFalseBranches(
        keyword: String,
        schema: ResolvedSchema,
    ): List<ReferenceOr<Schema>> {
        val branches = filterNot { it.booleanSchema() == false }
        require(isEmpty() || branches.isNotEmpty()) {
            "Every $keyword branch of schema ${schema.describe()} is the boolean schema `false`, so no value can satisfy it."
        }
        return branches
    }

    private fun ResolvedSchema.describe(): String = when (this) {
        is ResolvedSchema.Reference -> "'${reference.name}'"
        is ResolvedSchema.Recursive, is ResolvedSchema.Value -> name.toString()
    }

    private fun ResolvedSchema.withSchema(schema: Schema): ResolvedSchema = when (this) {
        is ResolvedSchema.Value -> copy(schema = schema)
        is ResolvedSchema.Reference -> copy(schema = schema)
        is ResolvedSchema.Recursive -> copy(schema = schema)
    }
}
