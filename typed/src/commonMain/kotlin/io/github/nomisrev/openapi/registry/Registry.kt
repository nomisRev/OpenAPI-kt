package io.github.nomisrev.openapi.registry

import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.parser.Schema
import io.github.nomisrev.openapi.transformers.toModel
import io.ktor.client.HttpClient

inline fun <A> registry(openAPI: OpenAPI, block: context(Registry) () -> A): A =
    Registry(openAPI).use { block(it) }

class Registry(val openAPI: OpenAPI) : AutoCloseable {
    private val client: HttpClient = HttpClient()
    private val cache: MutableSet<NamingContext.Reference> = mutableSetOf()

    fun names(): Set<NamingContext.Reference> = cache.toSet()

    private suspend fun remoteSchema(url: String): Schema =
        TODO("Remote schemas not supported yet.")

    override fun close() = client.close()

    suspend fun NamingContext.Reference.toModel(): Model =
        ReferenceOr.schema(name).toModel(NamingContext(this, emptyList()), context)

    suspend fun ReferenceOr<Schema>.toModel(name: NamingContext, context: SchemaContext): Model =
        with(ScopeImpl(null, emptySet())) {
            resolve(name, context) { it.toModel(context, true) }
        }

    interface Scope {
        /**
         * Resolves a `ReferenceOr<Schema>` taking into account [context] whether it appears as [SchemaContext.Write],
         * or [SchemaContext.Read].
         */
        suspend fun <A> ReferenceOr<Schema>.resolve(
            name: NamingContext,
            context: SchemaContext,
            block: suspend context(Scope) (ResolvedSchema) -> A
        ): A

        /**
         * Peek a referenced schema. NOT RECURSIVE SAFE!
         * Check readOrWriteOnly inside Registry as an example on how to make stack-safe.
         */
        suspend fun ReferenceOr<Schema>.peek(): Schema
        suspend fun peek(ref: String): Schema
    }

    // Layer of indirection to keep `Registry.Scope` construction private
    private inner class ScopeImpl(
        private val currentAnchor: Pair<NamingContext, Schema>?,
        // TODO: this probably isn't right... ? What about duplicate inline Value Schema's?
        private val expanding: Set<NamingContext>
    ) : Scope {
        override suspend fun ReferenceOr<Schema>.peek(): Schema = when (this) {
            is ReferenceOr.Value<Schema> -> value
            is ReferenceOr.Reference if ref == "#" -> requireNotNull(currentAnchor) {
                "Cannot resolve top-level schema without anchor."
            }.second

            is ReferenceOr.Reference -> peek(ref)
        }

        override suspend fun peek(ref: String): Schema {
            val name = ref.schemaName()
            val schema = when (val nested = openAPI.components.schemas[name]) {
                is ReferenceOr.Reference -> remoteSchema(nested.ref)
                is ReferenceOr.Value<Schema> -> nested.value
                null -> throw IllegalStateException("Schema $name could not be found in ${openAPI.components.schemas.keys}.")
            }
            return schema
        }

        /**
         * Resolves a `ReferenceOr<Schema>` taking into account [context] whether it appears as [SchemaContext.Write],
         * or [SchemaContext.Read].
         */
        override suspend fun <A> ReferenceOr<Schema>.resolve(
            name: NamingContext,
            context: SchemaContext,
            block: suspend context(Scope) (ResolvedSchema) -> A
        ): A = when (this) {
            is ReferenceOr.Reference -> resolve(context, block)
            is ReferenceOr.Value<Schema> -> {
                if (value.recursiveAnchor == true) block(
                    ScopeImpl(Pair(name, value), expanding),
                    ResolvedSchema.Value(name, value)
                )
                else block(this@ScopeImpl, ResolvedSchema.Value(name, value))
            }
        }

        /*
         * Traverse a schema to determine if it has readOnly or writeOnly in any (nested) properties.
         * Checks `allOf`, `oneOf`, `anyOf`, `properties`, and `items`.
         */
        private suspend fun Schema.readOrWriteOnly(visited: MutableSet<String> = mutableSetOf()): Boolean {
            suspend fun ReferenceOr<Schema>.schema(): Schema? = when (this) {
                is ReferenceOr.Value<Schema> -> value
                is ReferenceOr.Reference if (ref == "#" || ref in visited) -> null
                is ReferenceOr.Reference -> {
                    visited.add(ref)
                    peek()
                }
            }

            suspend fun Collection<ReferenceOr<Schema>>?.readOrWriteOnly(): Boolean = orEmpty().any { refOrSchema ->
                val schema = refOrSchema.schema()
                schema != null && (schema.writeOnly == true || schema.readOnly == true || schema.readOrWriteOnly(
                    visited
                ))
            }

            return writeOnly == true || readOnly == true ||
                    allOf.readOrWriteOnly() || oneOf.readOrWriteOnly() ||
                    properties?.values.readOrWriteOnly() ||
                    anyOf.readOrWriteOnly() || listOfNotNull(items).readOrWriteOnly()
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
            block: suspend context(Scope) (ResolvedSchema) -> A
        ): A = if (ref == "#") {
            @Suppress("RETURN_VALUE_NOT_USED")
            requireNotNull(currentAnchor) { "Cannot resolve top-level schema without anchor." }
            block(this@ScopeImpl, ResolvedSchema.Recursive(currentAnchor.first, currentAnchor.second))
        } else {
            val name = ref.drop("#/components/schemas/".length)
            val schema = when (val nested = openAPI.components.schemas[name]) {
                is ReferenceOr.Reference -> remoteSchema(nested.ref)
                is ReferenceOr.Value<Schema> -> nested.value
                null -> throw IllegalStateException("Schema $name could not be found in ${openAPI.components.schemas.keys}.")
            }
            val contextSpecific = schema.readOrWriteOnly()
            val schemaContext = if (contextSpecific) context else SchemaContext.Null
            val reference = NamingContext.Reference(name, schemaContext)
            val context = NamingContext(reference, emptyList())
            val resolved = if (expanding.contains(context)) ResolvedSchema.Recursive(context, schema)
            else ResolvedSchema.Reference(reference, schema)

            // if schema isDiscriminatedObjectSubtype

//            cache.add(reference)

            val currentAnchor = if (schema.recursiveAnchor == true) Pair(context, schema) else null
            block.invoke(ScopeImpl(currentAnchor, expanding + context), resolved)
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
    block: suspend context(Registry.Scope) (ResolvedSchema) -> A
): A = with(ctx) { resolve(name, context, block) }

context(ctx: Registry.Scope)
suspend fun ReferenceOr<Schema>.peek(): Schema = with(ctx) { peek() }

context(ctx: Registry.Scope)
suspend fun ReferenceOr<Schema>.toModel(name: NamingContext, context: SchemaContext): Model =
    resolve(name, context) { it.toModel(context, true) }

context(ctx: Registry)
suspend fun ReferenceOr<Schema>.toModel(name: NamingContext.Head, context: SchemaContext): Model =
    with(ctx) { toModel(NamingContext(name, emptyList()), context) }


context(ctx: Registry)
suspend fun ReferenceOr<Schema>.toModel(name: NamingContext, context: SchemaContext): Model =
    with(ctx) { toModel(name, context) }

