package io.github.nomisrev.openapi.pipeline

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.pipeline.plugins.composite.AllOfNullableFlattenPlugin
import io.github.nomisrev.openapi.pipeline.plugins.composite.AllOfPlugin
import io.github.nomisrev.openapi.pipeline.plugins.composite.AnyOfNullableFlattenPlugin
import io.github.nomisrev.openapi.pipeline.plugins.composite.AnyOfPlugin
import io.github.nomisrev.openapi.pipeline.plugins.composite.AnyOfSinglePlugin
import io.github.nomisrev.openapi.pipeline.plugins.composite.OneOfNullableFlattenPlugin
import io.github.nomisrev.openapi.pipeline.plugins.composite.OneOfPlugin
import io.github.nomisrev.openapi.pipeline.plugins.composite.OneOfSinglePlugin
import io.github.nomisrev.openapi.pipeline.plugins.fallback.FallbackPlugin
import io.github.nomisrev.openapi.pipeline.plugins.implicit.ImplicitCollectionPlugin
import io.github.nomisrev.openapi.pipeline.plugins.implicit.ImplicitEnumPlugin
import io.github.nomisrev.openapi.pipeline.plugins.implicit.ImplicitObjectPlugin
import io.github.nomisrev.openapi.pipeline.plugins.typed.CollectionPlugin
import io.github.nomisrev.openapi.pipeline.plugins.typed.EnumPlugin
import io.github.nomisrev.openapi.pipeline.plugins.typed.ObjectPlugin
import io.github.nomisrev.openapi.pipeline.plugins.typed.PrimitivePlugin
import io.github.nomisrev.openapi.pipeline.plugins.typed.TypeArrayPlugin
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.ResolvedSchema
import io.github.nomisrev.openapi.registry.description
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.transformers.discriminatedSubtypeOrNull

class SchemaTransformerEngine internal constructor(
    private val plugins: List<SchemaTransformerPlugin>,
    private val interceptors: List<ModelInterceptor>
) {

    suspend fun transform(
        ctx: Registry.Scope,
        registry: Registry,
        schema: ResolvedSchema,
        context: SchemaContext,
        resolveReference: Boolean = true
    ): Model = with(ctx) {
        // Pre-checks
        if (schema is ResolvedSchema.Reference && !resolveReference) {
            val name = with(ctx) {
                schema.schema.discriminatedSubtypeOrNull(context, schema.reference.name)
            } ?: schema.name
            return Model.Reference(name, with(ctx) { schema.description() }, schema.isNullable, schema.schema.title)
        }
        if (schema is ResolvedSchema.Recursive) {
            return Model.Reference(
                schema.name,
                with(ctx) { schema.description() },
                schema.isNullable,
                schema.schema.title
            )
        }
        if (schema.schema.type == Schema.Type.Basic.Null) {
            error("Null  should always be resolved to result in nullable types. Please report this bug. ${schema.schema}")
        }

        // Plugin chain
        val model = runChain(ctx, registry, schema, context, plugins)

        // Interceptor chain
        return interceptors.fold(model) { acc, interceptor ->
            with(interceptor) {
                with(registry) {
                    intercept(acc, context)
                }
            }
        }
    }

    private suspend fun runChain(
        ctx: Registry.Scope,
        registry: Registry,
        schema: ResolvedSchema,
        context: SchemaContext,
        remainingPlugins: List<SchemaTransformerPlugin>
    ): Model {
        if (remainingPlugins.isEmpty()) {
            throw NoTransformerFoundException("No transformer found for schema: $schema")
        }

        val plugin = remainingPlugins.first()
        val nextPlugins = remainingPlugins.drop(1)

        val next: suspend (ResolvedSchema, SchemaContext) -> Model = { s, c ->
            runChain(ctx, registry, s, c, nextPlugins)
        }

        return plugin.transform(ctx, this, schema, context, next) ?: next(schema, context)
    }

    companion object {
        fun default(): SchemaTransformerEngine = build {
            defaults()
        }

        fun build(block: SchemaTransformerEngineBuilder.() -> Unit): SchemaTransformerEngine {
            val builder = SchemaTransformerEngineBuilder()
            builder.block()
            return builder.build()
        }
    }
}

class SchemaTransformerEngineBuilder {
    private val plugins = mutableListOf<SchemaTransformerPlugin>()
    private val interceptors = mutableListOf<ModelInterceptor>()

    fun defaults() {
        addFirst(Phase.COMPOSITE, AllOfNullableFlattenPlugin)
        addFirst(Phase.COMPOSITE, AllOfPlugin)
        addFirst(Phase.COMPOSITE, OneOfNullableFlattenPlugin)
        addFirst(Phase.COMPOSITE, OneOfSinglePlugin)
        addFirst(Phase.COMPOSITE, OneOfPlugin)
        addFirst(Phase.COMPOSITE, AnyOfNullableFlattenPlugin)
        addFirst(Phase.COMPOSITE, AnyOfSinglePlugin)
        addFirst(Phase.COMPOSITE, AnyOfPlugin)
        addFirst(Phase.TYPED, EnumPlugin)
        addFirst(Phase.TYPED, CollectionPlugin)
        addFirst(Phase.TYPED, TypeArrayPlugin)
        addFirst(Phase.TYPED, ObjectPlugin)
        addFirst(Phase.TYPED, PrimitivePlugin)
        addFirst(Phase.IMPLICIT, ImplicitEnumPlugin)
        addFirst(Phase.IMPLICIT, ImplicitCollectionPlugin)
        addFirst(Phase.IMPLICIT, ImplicitObjectPlugin)
        addFirst(Phase.FALLBACK, FallbackPlugin)
    }

    fun addFirst(phase: Phase, plugin: SchemaTransformerPlugin) {
        checkDuplicateKey(plugin.key)
        val index = plugins.indexOfLast { it.phase.ordinal <= phase.ordinal }
        if (index == -1) {
            plugins.add(0, plugin)
        } else {
            plugins.add(index + 1, plugin)
        }
    }

    fun addAfter(key: PluginKey, plugin: SchemaTransformerPlugin) {
        checkDuplicateKey(plugin.key)
        val index = plugins.indexOfFirst { it.key == key }
        if (index == -1) throw IllegalArgumentException("Key $key not found")
        plugins.add(index + 1, plugin)
    }

    fun addBefore(key: PluginKey, plugin: SchemaTransformerPlugin) {
        checkDuplicateKey(plugin.key)
        val index = plugins.indexOfFirst { it.key == key }
        if (index == -1) throw IllegalArgumentException("Key $key not found")
        plugins.add(index, plugin)
    }

    fun replace(key: PluginKey, plugin: SchemaTransformerPlugin) {
        if (plugin.key != key) checkDuplicateKey(plugin.key)
        val index = plugins.indexOfFirst { it.key == key }
        if (index == -1) throw IllegalArgumentException("Key $key not found")
        plugins[index] = plugin
    }

    fun interceptor(interceptor: ModelInterceptor) {
        interceptors.add(interceptor)
    }

    private fun checkDuplicateKey(key: PluginKey) {
        if (plugins.any { it.key == key }) {
            throw IllegalArgumentException("Duplicate PluginKey: ${key.name}")
        }
    }

    internal fun build(): SchemaTransformerEngine {
        val sortedPlugins = plugins.sortedWith(
            compareBy<SchemaTransformerPlugin> { it.phase.ordinal }
                .thenBy { plugins.indexOf(it) }
        )
        return SchemaTransformerEngine(sortedPlugins, interceptors)
    }
}

class NoTransformerFoundException(message: String) : Exception(message)
