package io.github.nomisrev.openapi.registry

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.parser.AdditionalProperties
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.pipeline.SchemaTransformerEngine
import io.github.nomisrev.openapi.transformers.describe
import io.github.nomisrev.openapi.transformers.merge
import io.ktor.client.HttpClient
import kotlinx.coroutines.yield

inline fun <A> registry(
    openAPI: OpenAPI,
    engine: SchemaTransformerEngine = SchemaTransformerEngine.default(),
    block: context(Registry) () -> A
): A = Registry(openAPI, engine).use { block(it) }

class Registry(
    val openAPI: OpenAPI,
    val engine: SchemaTransformerEngine = SchemaTransformerEngine.default(),
) : AutoCloseable {
    private val client: HttpClient = HttpClient()
    private val cache: MutableSet<NamingContext.Reference> = mutableSetOf()

    fun names(): Set<NamingContext.Reference> = cache.toSet()

    private val warnings: MutableSet<String> = linkedSetOf()

    /** Records a warning about a schema that is generated differently from how it is declared. Repeats are kept once. */
    fun warn(message: String) {
        warnings += message
    }

    fun warnings(): List<String> = warnings.toList()

    /** The pairs of `$ref`s being intersected, so the intersection of two recursive schemas fails instead of looping. */
    internal val intersecting: MutableSet<Set<String>> = mutableSetOf()

    @Suppress("RedundantSuspendModifier")
    private suspend fun remoteSchema(url: String): Schema =
        TODO("Remote schemas $url not supported yet.")

    override fun close() = client.close()
    
    fun scope(): Scope = ScopeImpl(null, emptySet())

    /**
     * The names of the component schemas that are context-specific: they, or anything they contain, are `readOnly` or
     * `writeOnly`, following `$ref`s, so they have a request and a response shape. Computed once, in linear time, from
     * the components alone: the answer for a schema depends on that schema only, never on where it is used, so that a
     * use and the definition of one schema agree on its name. Cycles need no special care.
     */
    private val contextSpecificComponents: Set<String> by lazy {
        val prefix = "#/components/schemas/"
        val direct = mutableSetOf<String>()
        val usedBy = mutableMapOf<String, MutableList<String>>()
        openAPI.components.schemas.forEach { [name, declared] ->
            val scan = ContextScan().apply { scan(declared) }
            if (scan.direct) direct += name
            scan.refs.forEach { if (it.startsWith(prefix)) usedBy.getOrPut(it.schemaName()) { mutableListOf() } += name }
        }
        val result = direct.toMutableSet()
        val pending = ArrayDeque(direct)
        while (pending.isNotEmpty()) {
            usedBy[pending.removeLast()]?.forEach { if (result.add(it)) pending.addLast(it) }
        }
        result
    }

    /** What a schema says about its context without following `$ref`s: its own flags, and the components it refers to. */
    private class ContextScan {
        var direct = false
        val refs = mutableSetOf<String>()

        fun scan(refOrSchema: ReferenceOr<Schema>?) {
            when (refOrSchema) {
                null -> Unit
                // Only an annotation on the reference itself counts here; what it refers to is followed through [refs].
                is ReferenceOr.Reference -> {
                    if (refOrSchema.readOnly == true || refOrSchema.writeOnly == true) direct = true
                    if (refOrSchema.ref != "#") refs += refOrSchema.ref
                    refOrSchema.siblings?.let(::scan)
                }
                is ReferenceOr.Value<Schema> -> scan(refOrSchema.value)
            }
        }

        fun scan(schema: Schema) {
            if (schema.readOnly == true || schema.writeOnly == true) direct = true
            schema.allOf?.forEach(::scan)
            schema.oneOf?.forEach(::scan)
            schema.anyOf?.forEach(::scan)
            schema.properties?.values?.forEach(::scan)
            scan(schema.items)
            (schema.additionalProperties as? AdditionalProperties.PSchema)?.let { scan(it.value) }
        }
    }

    /** The name of the component schema [name] as used in [context]: that context when it has two shapes, else none. */
    fun componentReference(name: String, context: SchemaContext): NamingContext.Reference =
        NamingContext.Reference(name, if (name in contextSpecificComponents) context else SchemaContext.Null)

    private fun isContextSpecific(ref: String, peeked: Schema): Boolean =
        if (ref.startsWith("#/components/schemas/")) ref.schemaName() in contextSpecificComponents
        else peeked.isContextSpecific()

    private fun Schema.isContextSpecific(): Boolean {
        val scan = ContextScan().apply { scan(this@isContextSpecific) }
        return scan.direct || scan.refs.any { it.startsWith("#/components/schemas/") && it.schemaName() in contextSpecificComponents }
    }

    suspend fun NamingContext.Reference.toModel(): Model =
        ReferenceOr.schema(name).toModel(NamingContext(this, emptyList()), context)

    suspend fun ReferenceOr<Schema>.toModel(name: NamingContext, context: SchemaContext): Model =
        with(scope()) {
            resolve(name, context) { scope, resolved ->
                engine.transform(scope, this@Registry, resolved, context, true)
            }
        }

    interface Scope {
        /**
         * Resolves a `ReferenceOr<Schema>` taking into account [context] whether it appears as [SchemaContext.Write],
         * or [SchemaContext.Read].
         */
        suspend fun <A> ReferenceOr<Schema>.resolve(
            name: NamingContext,
            context: SchemaContext,
            block: suspend (Scope, ResolvedSchema) -> A
        ): A

        /**
         * Peek a referenced schema. NOT RECURSIVE SAFE!
         * Check readOrWriteOnly inside Registry as an example on how to make stack-safe.
         */
        suspend fun ReferenceOr<Schema>.peek(): Schema
        suspend fun peek(ref: String): Schema

        fun registry(): Registry

        /**
         * A scope in which [source], an inline `allOf` schema that is modelled as an object named [name] by merging
         * its members, resolves to a reference to that object: a member that leads back to it is the same type.
         */
        fun expandingMerge(source: Schema, name: NamingContext): Scope
    }

    /** A use of a referenced schema: [schema] as it is used there, and whether keywords next to the `$ref` [shaped] it. */
    private class Use(val schema: Schema, val shaped: Boolean)

    // Layer of indirection to keep `Registry.Scope` construction private
    private inner class ScopeImpl(
        private val currentAnchor: Pair<NamingContext, Schema>?,
        private val expanding: Set<NamingContext>,
        private val merging: Map<Schema, NamingContext> = emptyMap(),
    ) : Scope {
        override fun registry(): Registry = this@Registry
        override fun expandingMerge(source: Schema, name: NamingContext): Scope =
            ScopeImpl(currentAnchor, expanding, merging + (source to name))
        override suspend fun ReferenceOr<Schema>.peek(): Schema = when (this) {
            is ReferenceOr.Value<Schema> -> value
            is ReferenceOr.Reference if ref == "#" -> currentAnchor?.second
                ?: error("Cannot resolve top-level schema without anchor.")

            // Only what changes the shape is reported here: a peek has no use to name, and the use is resolved too.
            is ReferenceOr.Reference -> useOf(peek(ref), "`\$ref: $ref`", reportUse = false).schema
        }

        /**
         * [target], the schema this reference points to, as this use of it: with the keywords next to the `$ref`
         * applied ([ReferenceSiblings]). Warnings name the use as [use]; [reportUse] reports the ones about that use
         * only (a `type` that is ignored, `nullable`) too.
         */
        private suspend fun ReferenceOr.Reference.useOf(target: Schema, use: String, reportUse: Boolean = true): Use {
            val siblings = siblings?.let(::ReferenceSiblings) ?: return Use(target, shaped = false)
            val ignoredIn30 = " (OpenAPI 3.0 says to ignore keywords next to a `\$ref`)"
                .takeIf { openAPI.openapi.startsWith("3.0") }.orEmpty()
            if (reportUse && siblings.contradictsTypeOf(target)) {
                warn("$use: `type` next to `\$ref: $ref` is ignored: no value is of that type and of the referenced schema's type.")
            }
            val keywords = siblings.shapeKeywords.joinToString { "`$it`" }
            val shaped = siblings.shape?.let { shape ->
                try {
                    target.merge(shape).also {
                        if (ignoredIn30.isNotEmpty()) warn("$use: $keywords next to `\$ref: $ref` is applied to the referenced schema$ignoredIn30.")
                    }
                } catch (e: IllegalArgumentException) {
                    warn("$use: $keywords next to `\$ref: $ref` is ignored: ${e.message}")
                    null
                }
            }
            val schema = shaped ?: target
            return Use(
                if (siblings.nullable && schema.nullable != true) {
                    if (reportUse) warn("$use: `nullable` next to `\$ref: $ref` is read as making this use of it nullable$ignoredIn30.")
                    schema.copy(nullable = true)
                } else schema,
                shaped = shaped != null,
            )
        }

        override suspend fun peek(ref: String): Schema = peek(ref, emptySet())

        private tailrec suspend fun peek(ref: String, visited: Set<String>): Schema {
            check(ref !in visited) {
                "Cyclic schema aliases: ${visited.joinToString(" -> ")} -> $ref"
            }
            if (ref == "#") return currentAnchor?.second
                ?: error("Cannot resolve top-level schema without anchor.")
            if (!ref.startsWith("#")) return remoteSchema(ref)
            val name = ref.schemaName()
            return when (val nested = openAPI.components.schemas[name]) {
                is ReferenceOr.Reference -> peek(nested.ref, visited + ref)
                is ReferenceOr.Value<Schema> -> nested.value
                null -> error("Schema $name not found in ${openAPI.components.schemas.keys}.")
            }
        }

        /**
         * Resolves a `ReferenceOr<Schema>` taking into account [context] whether it appears as [SchemaContext.Write],
         * or [SchemaContext.Read].
         */
        override suspend fun <A> ReferenceOr<Schema>.resolve(
            name: NamingContext,
            context: SchemaContext,
            block: suspend (Scope, ResolvedSchema) -> A
        ): A = when (this) {
            is ReferenceOr.Reference if ref == "#" -> {
                @Suppress("RETURN_VALUE_NOT_USED")
                requireNotNull(currentAnchor) { "Cannot resolve top-level schema without anchor." }
                block(this@ScopeImpl, ResolvedSchema.Recursive(currentAnchor.first, currentAnchor.second))
            }
            is ReferenceOr.Reference -> {
                val use = useOf(peek(ref), name.describe())
                // Keywords next to the `$ref` that shape the value make this use an inline schema: the referenced one
                // merged with them, named where it is used.
                if (use.shaped) {
                    val nextAnchor = if (use.schema.recursiveAnchor == true) Pair(name, use.schema) else currentAnchor
                    merging[use.schema]?.let { return block(this@ScopeImpl, ResolvedSchema.Recursive(it, use.schema)) }
                    val nextMerging = if (use.schema.properties != null) merging + (use.schema to name) else merging
                    return block(ScopeImpl(nextAnchor, expanding, nextMerging), ResolvedSchema.Value(name, use.schema))
                }
                val peeked = use.schema
                // `readOnly` or `writeOnly` next to this `$ref` is about the property that holds it: it is left out of
                // one shape of its parent. Whether the schema referred to has two shapes depends on its own content
                // (and what it refers to) only, so that a use and the definition of one schema agree on its name.
                // The context of the use flows on to what is beneath, which has one shape when this has.
                val contextSpecific = isContextSpecific(ref, peeked)
                val resolvedRef = if (contextSpecific) context else SchemaContext.Null
                val reference = NamingContext.Reference(ref.schemaName(), resolvedRef)
                val namingContext = NamingContext(reference, emptyList())
                val resolved = if (expanding.contains(namingContext)) {
                    ResolvedSchema.Recursive(namingContext, peeked)
                } else {
                    ResolvedSchema.Reference(reference, peeked)
                }
                val nextAnchor = if (peeked.recursiveAnchor == true) Pair(namingContext, peeked) else currentAnchor
                block(ScopeImpl(nextAnchor, expanding + namingContext, merging), resolved)
            }
            is ReferenceOr.Value<Schema> -> {
                // An inline schema is part of the schema that holds it: the context flows through it.
                val nextAnchor = if (value.recursiveAnchor == true) Pair(name, value) else currentAnchor
                val merged = merging[value]
                if (merged != null) return block(this@ScopeImpl, ResolvedSchema.Recursive(merged, value))
                block(ScopeImpl(nextAnchor, expanding, merging), ResolvedSchema.Value(name, value))
            }
        }

        /**
         * When resolving top-level schemas, we need to:
         *   1. Track all top-level schemas we resolved in [cache] so we can generate only the schema's used.
         *   2. Check the schema has only readOnly & writeOnly properties (or nested properties)
         *      - If there are we need to generate a read _or_ write variant of the schema
         *      - If it doesn't, we can use it as a regular schema; this means schemas are [context] dependent
         */
        private suspend fun <A> ReferenceOr.Reference.resolve(
            context: SchemaContext,
            block: suspend (Scope, ResolvedSchema) -> A
        ): A = if (ref == "#") {
            @Suppress("RETURN_VALUE_NOT_USED")
            requireNotNull(currentAnchor) { "Cannot resolve top-level schema without anchor." }
            block(this@ScopeImpl, ResolvedSchema.Recursive(currentAnchor.first, currentAnchor.second))
        } else {
            val name = ref.drop("#/components/schemas/".length)
            val schema = peek(ref)
            // As in the other `resolve`: an annotation next to the `$ref` does not give its target two shapes.
            val contextSpecific = isContextSpecific(ref, schema)
            val schemaContext = if (contextSpecific) context else SchemaContext.Null
            val reference = NamingContext.Reference(name, schemaContext)
            val namingContext = NamingContext(reference, emptyList())
            val resolved = if (expanding.contains(namingContext)) ResolvedSchema.Recursive(namingContext, schema)
            else ResolvedSchema.Reference(reference, schema)

            // if schema isDiscriminatedObjectSubtype

//            cache.add(reference)

            val currentAnchor = if (schema.recursiveAnchor == true) Pair(namingContext, schema) else null
            block(ScopeImpl(currentAnchor, expanding + namingContext, merging), resolved)
        }
    }
}

fun String.schemaName() = drop("#/components/schemas/".length)

/**
 * Resolve a `ReferenceOr<Schema>` **this will register** the `Schema` according to the `context` as a consumed schema.
 * Only use this method if you're going to _consume_ the resolved schema.
 * Otherwise, use the predicates available on `ReferenceOr<Schema>` below first to determine if you need this schema.
 */
context(ctx: Registry.Scope)
suspend fun <A> ReferenceOr<Schema>.resolve(
    name: NamingContext,
    context: SchemaContext,
    block: suspend (Registry.Scope, ResolvedSchema) -> A
): A = with(ctx) { resolve(name, context, block) }

context(ctx: Registry.Scope)
suspend fun ReferenceOr<Schema>.peek(): Schema = with(ctx) { peek() }

context(ctx: Registry.Scope)
suspend fun ReferenceOr<Schema>.toModel(name: NamingContext, context: SchemaContext): Model =
    resolve(name, context) { scope, resolved ->
        scope.registry().engine.transform(scope, scope.registry(), resolved, context, true)
    }

context(ctx: Registry)
suspend fun ReferenceOr<Schema>.toModel(name: NamingContext.Head, context: SchemaContext): Model =
    with(ctx) { toModel(NamingContext(name, emptyList()), context) }


context(ctx: Registry)
suspend fun ReferenceOr<Schema>.toModel(name: NamingContext, context: SchemaContext): Model =
    with(ctx) { toModel(name, context) }
